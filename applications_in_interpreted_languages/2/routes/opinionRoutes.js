const express = require('express');
const router = express.Router();
const { Opinion, validateOpinion } = require('../models/Opinion');
const { Order } = require('../models/Order');
const { StatusCodes } = require('http-status-codes');
const { verifyToken } = require('../middleware/auth');
const { AppError, asyncHandler } = require('../middleware/errorHandler');

router.post('/orders/:id/opinions', verifyToken, asyncHandler(async (req, res) => {
    const { error } = validateOpinion(req.body);
    if (error) {
        throw new AppError(
            StatusCodes.BAD_REQUEST,
            'Validation Error',
            'The opinion data contains validation errors',
            'https://api.shop.example/errors/validation',
            req.originalUrl,
            { errors: error.details.map(detail => ({
                    field: detail.path.join('.'),
                    message: detail.message
                }))}
        );
    }

    const order = await Order.findById(req.params.id).populate('status');
    if (!order) {
        throw new AppError(
            StatusCodes.NOT_FOUND,
            'Order Not Found',
            'The specified order does not exist',
            'https://api.shop.example/errors/not-found',
            req.originalUrl
        );
    }

    if (order.username !== req.user.username) {
        throw new AppError(
            StatusCodes.FORBIDDEN,
            'Permission Denied',
            'You do not have permission to add an opinion to this order',
            'https://api.shop.example/errors/forbidden',
            req.originalUrl
        );
    }

    if (!['ZREALIZOWANE', 'ANULOWANE'].includes(order.status.name)) {
        throw new AppError(
            StatusCodes.BAD_REQUEST,
            'Invalid Order Status',
            'Opinions can only be added to completed or cancelled orders',
            'https://api.shop.example/errors/invalid-order-status',
            req.originalUrl
        );
    }

    const existingOpinion = await Opinion.findOne({ order: order._id });
    if (existingOpinion) {
        throw new AppError(
            StatusCodes.CONFLICT,
            'Opinion Already Exists',
            'An opinion for this order already exists',
            'https://api.shop.example/errors/duplicate-opinion',
            req.originalUrl
        );
    }

    const opinion = new Opinion({
        order: order._id,
        rating: req.body.rating,
        content: req.body.content,
        username: req.user.username
    });

    const savedOpinion = await opinion.save();
    res.status(StatusCodes.CREATED).json({
        message: 'Opinion added successfully',
        opinion: savedOpinion
    });
}));

router.get('/orders/:id/opinions', verifyToken, asyncHandler(async (req, res) => {
    const opinions = await Opinion.find({ order: req.params.id });
    if (!opinions.length) {
        throw new AppError(
            StatusCodes.NOT_FOUND,
            'No Opinions Found',
            'No opinions exist for this order',
            'https://api.shop.example/errors/not-found',
            req.originalUrl
        );
    }
    res.json(opinions);
}));

module.exports = router;
