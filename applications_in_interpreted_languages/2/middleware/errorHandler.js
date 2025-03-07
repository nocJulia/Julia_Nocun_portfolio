const { StatusCodes } = require('http-status-codes');
const { Problem } = require('http-problem-details');

class AppError extends Error {
    constructor(status, title, detail, type = 'about:blank', instance = null, extensions = {}) {
        super(detail);
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.instance = instance;
        this.extensions = extensions;
    }
}

const createProblemDetails = (error, req) => {
    if (error instanceof AppError) {
        return new Problem({
            status: error.status,
            title: error.title,
            detail: error.detail,
            type: error.type,
            instance: error.instance || req.originalUrl,
            ...error.extensions
        });
    }

    if (error.isJoi) {
        return new Problem({
            status: StatusCodes.BAD_REQUEST,
            title: 'Validation Error',
            detail: 'The request payload contains validation errors',
            type: 'https://api.shop.example/errors/validation',
            instance: req.originalUrl,
            errors: error.details.map(detail => ({
                field: detail.path.join('.'),
                message: detail.message
            }))
        });
    }

    if (error.name === 'MongoServerError') {
        if (error.code === 11000) {
            return new Problem({
                status: StatusCodes.CONFLICT,
                title: 'Duplicate Key Error',
                detail: 'A resource with the same unique key already exists',
                type: 'https://api.shop.example/errors/duplicate',
                instance: req.originalUrl,
                field: Object.keys(error.keyPattern)[0]
            });
        }
    }

    return new Problem({
        status: error.status || StatusCodes.INTERNAL_SERVER_ERROR,
        title: 'Internal Server Error',
        detail: process.env.NODE_ENV === 'production' ?
            'An unexpected error occurred' :
            error.message,
        type: 'about:blank',
        instance: req.originalUrl
    });
};

const errorHandler = (err, req, res, next) => {
    console.error(err);

    const problem = createProblemDetails(err, req);

    res.setHeader('Content-Type', 'application/problem+json');
    res.status(problem.status).json(problem);
};

const asyncHandler = (fn) => (req, res, next) => {
    Promise.resolve(fn(req, res, next)).catch(next);
};

module.exports = {
    AppError,
    errorHandler,
    asyncHandler
};
