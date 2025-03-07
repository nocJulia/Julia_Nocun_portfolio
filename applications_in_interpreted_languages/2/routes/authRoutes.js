const express = require('express');
const router = express.Router();
const User = require('../models/User');
const { generateTokens } = require('../middleware/auth');
const { StatusCodes } = require('http-status-codes');
const jwt = require('jsonwebtoken');
const { AppError, asyncHandler } = require('../middleware/errorHandler');

const JWT_REFRESH_SECRET = process.env.JWT_REFRESH_SECRET || 'your-refresh-secret-key';

router.post('/register', asyncHandler(async (req, res) => {
    const { username, password, email, role } = req.body;

    const userExists = await User.findOne({
        $or: [{ username }, { email }]
    });

    if (userExists) {
        throw new AppError(
            StatusCodes.CONFLICT,
            'User Already Exists',
            'A user with the provided username or email already exists',
            'https://api.shop.example/errors/duplicate-user',
            req.originalUrl
        );
    }

    const user = new User({ username, password, email, role });
    await user.save();

    const { accessToken, refreshToken } = generateTokens(user);
    user.refreshToken = refreshToken;
    await user.save();

    res.status(StatusCodes.CREATED).json({
        accessToken,
        refreshToken,
        user: {
            id: user._id,
            username: user.username,
            email: user.email,
            role: user.role
        }
    });
}));

router.post('/login', asyncHandler(async (req, res) => {
    const { username, password } = req.body;
    const user = await User.findOne({ username });

    if (!user || !(await user.comparePassword(password))) {
        throw new AppError(
            StatusCodes.UNAUTHORIZED,
            'Authentication Failed',
            'Invalid login credentials',
            'https://api.shop.example/errors/auth',
            req.originalUrl
        );
    }

    const { accessToken, refreshToken } = generateTokens(user);
    user.refreshToken = refreshToken;
    await user.save();

    res.json({
        accessToken,
        refreshToken,
        user: {
            id: user._id,
            username: user.username,
            email: user.email,
            role: user.role
        }
    });
}));

router.post('/refresh-token', asyncHandler(async (req, res) => {
    const { refreshToken } = req.body;

    if (!refreshToken) {
        throw new AppError(
            StatusCodes.BAD_REQUEST,
            'Missing Token',
            'No refresh token provided',
            'https://api.shop.example/errors/missing-token',
            req.originalUrl
        );
    }

    try {
        const decoded = jwt.verify(refreshToken, JWT_REFRESH_SECRET);
        const user = await User.findById(decoded.userId);

        if (!user || user.refreshToken !== refreshToken) {
            throw new AppError(
                StatusCodes.UNAUTHORIZED,
                'Invalid Token',
                'Refresh token is invalid or expired',
                'https://api.shop.example/errors/invalid-token',
                req.originalUrl
            );
        }

        const tokens = generateTokens(user);
        user.refreshToken = tokens.refreshToken;
        await user.save();

        res.json(tokens);
    } catch (error) {
        if (error.name === 'TokenExpiredError') {
            throw new AppError(
                StatusCodes.UNAUTHORIZED,
                'Token Expired',
                'Refresh token has expired',
                'https://api.shop.example/errors/token-expired',
                req.originalUrl
            );
        }
        throw error;
    }
}));

module.exports = router;
