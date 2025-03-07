const express = require('express');
const router = express.Router();
const Category = require('../models/Category');
const { StatusCodes } = require('http-status-codes');
const { AppError, asyncHandler } = require('../middleware/errorHandler');

router.get('/', asyncHandler(async (req, res) => {
    const categories = await Category.find();
    if (!categories.length) {
        throw new AppError(
            StatusCodes.NOT_FOUND,
            'No Categories Found',
            'No categories are currently available in the system',
            'https://api.shop.example/errors/not-found',
            req.originalUrl
        );
    }
    res.json(categories);
}));

module.exports = router;
