const jwt = require('jsonwebtoken');
const { StatusCodes } = require('http-status-codes');
const User = require('../models/User');

const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';
const JWT_REFRESH_SECRET = process.env.JWT_REFRESH_SECRET || 'your-refresh-secret-key';

const generateTokens = (user) => {
    const accessToken = jwt.sign(
        {
            userId: user._id,
            username: user.username,
            role: user.role
        },
        JWT_SECRET,
        { expiresIn: '1h' }
    );

    const refreshToken = jwt.sign(
        { userId: user._id },
        JWT_REFRESH_SECRET,
        { expiresIn: '7d' }
    );

    return { accessToken, refreshToken };
};

const verifyToken = async (req, res, next) => {
    try {
        const authHeader = req.header('Authorization');
        if (!authHeader) {
            return res.status(StatusCodes.UNAUTHORIZED).json({
                message: 'Brak tokenu uwierzytelniającego'
            });
        }

        const token = authHeader.replace('Bearer ', '');
        const decoded = jwt.verify(token, JWT_SECRET);

        const user = await User.findById(decoded.userId);
        if (!user) {
            return res.status(StatusCodes.UNAUTHORIZED).json({
                message: 'Użytkownik nie istnieje'
            });
        }

        req.user = decoded;
        next();
    } catch (error) {
        if (error.name === 'TokenExpiredError') {
            return res.status(StatusCodes.UNAUTHORIZED).json({
                message: 'Token wygasł'
            });
        }
        res.status(StatusCodes.UNAUTHORIZED).json({
            message: 'Nieprawidłowy token'
        });
    }
};

const authorizeRole = (roles) => {
    return (req, res, next) => {
        if (!roles.includes(req.user.role)) {
            return res.status(StatusCodes.FORBIDDEN).json({
                message: 'Brak uprawnień do wykonania tej operacji'
            });
        }
        next();
    };
};

module.exports = { generateTokens, verifyToken, authorizeRole };
