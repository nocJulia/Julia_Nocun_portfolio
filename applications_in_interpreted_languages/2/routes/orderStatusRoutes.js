const express = require('express');
const router = express.Router();
const OrderStatus = require('../models/OrderStatus');
const { StatusCodes } = require('http-status-codes');
const { AppError, asyncHandler } = require('../middleware/errorHandler');

router.get('/', asyncHandler(async (req, res) => {
    const statuses = await OrderStatus.find();
    if (!statuses.length) {
        throw new AppError(
            StatusCodes.NOT_FOUND,
            'No Order Statuses Found',
            'No order statuses are currently defined in the system',
            'https://api.shop.example/errors/not-found',
            req.originalUrl
        );
    }
    res.json(statuses);
}));

module.exports = router;
