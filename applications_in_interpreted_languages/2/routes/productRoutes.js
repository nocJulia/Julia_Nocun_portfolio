const express = require('express');
const router = express.Router();
const { Product, validateProduct } = require('../models/Product');
const { StatusCodes } = require('http-status-codes');
const axios = require('axios');
const {asyncHandler, AppError} = require("../middleware/errorHandler");

const GroqApi = process.env.GROQ_API_KEY;

router.get('/', asyncHandler(async (req, res) => {
    const products = await Product.find().populate('category');
    // if (!products.length) {
    //     throw new AppError(
    //         StatusCodes.NOT_FOUND,
    //         'Products Not Found',
    //         'No products available in the database',
    //         'https://api.shop.example/errors/not-found'
    //     );
    // }
    res.json(products);
}));

router.get('/:id', asyncHandler(async (req, res) => {
    const product = await Product.findById(req.params.id).populate('category');
    if (!product) {
        throw new AppError(
            StatusCodes.NOT_FOUND,
            'Product Not Found',
            `Product with ID ${req.params.id} does not exist`,
            'https://api.shop.example/errors/not-found',
            req.originalUrl
        );
    }
    res.json(product);
}));

router.post('/', asyncHandler(async (req, res) => {
    const { error } = validateProduct(req.body);
    if (error) {
        throw new AppError(
            StatusCodes.BAD_REQUEST,
            'Validation Error',
            'The request payload contains validation errors',
            'https://api.shop.example/errors/validation',
            req.originalUrl,
            { errors: error.details.map(detail => ({
                    field: detail.path.join('.'),
                    message: detail.message
                }))}
        );
    }

    const product = new Product(req.body);
    const newProduct = await product.save();
    res.status(StatusCodes.CREATED).json(newProduct);
}));

router.put('/:id', asyncHandler(async (req, res) => {
    const { error } = validateProduct(req.body);
    if (error) {
        throw new AppError(
            StatusCodes.BAD_REQUEST,
            'Validation Error',
            'The product data contains validation errors',
            'https://api.shop.example/errors/validation',
            req.originalUrl,
            {
                errors: error.details.map(detail => ({
                    field: detail.path.join('.'),
                    message: detail.message
                }))
            }
        );
    }

    const updatedProduct = await Product.findByIdAndUpdate(
        req.params.id,
        req.body,
        { new: true, runValidators: true }
    );

    if (!updatedProduct) {
        throw new AppError(
            StatusCodes.NOT_FOUND,
            'Product Not Found',
            `Product with ID ${req.params.id} does not exist`,
            'https://api.shop.example/errors/not-found',
            req.originalUrl
        );
    }

    res.json(updatedProduct);
}));

router.get('/:id/seo-description', asyncHandler(async (req, res) => {
    const product = await Product.findById(req.params.id).populate('category');

    if (!product) {
        throw new AppError(
            StatusCodes.NOT_FOUND,
            'Product Not Found',
            `Product with ID ${req.params.id} does not exist`,
            'https://api.shop.example/errors/not-found',
            req.originalUrl
        );
    }

    try {
        const seoDescription = await generateSEODescription(product);
        res.header('Content-Type', 'text/html');
        res.send(seoDescription);
    } catch (error) {
        throw new AppError(
            StatusCodes.INTERNAL_SERVER_ERROR,
            'SEO Generation Failed',
            'Failed to generate SEO description for the product',
            'https://api.shop.example/errors/seo-generation',
            req.originalUrl,
            {
                productId: product._id,
                productName: product.name,
                technicalDetails: error.message
            }
        );
    }
}));

async function generateSEODescription(product) {
    const prompt = `
        Create an SEO-optimized HTML description for this product.
        Make sure to use appropriate HTML5 semantic tags and include all key product information.

        Product details:
        - Name: ${product.name}
        - Category: ${product.category.name}
        - Price: ${product.price} PLN
        - Weight: ${product.weight} kg
        - Description: ${product.description}

        Requirements:
        1. Use semantic HTML5 tags (article, section, h1, h2, p, etc.)
        2. Include meta description
        3. Add schema.org product markup
        4. Create well-structured content with proper headings
        5. Write only HTML5 code.
    `;

    try {
        const response = await axios.post('https://api.groq.com/openai/v1/chat/completions', {
            model: 'llama3-8b-8192',
            messages: [{
                role: "user",
                content: prompt
            }],
            temperature: 0.7,
            max_tokens: 700,
            top_p: 0.95
        }, {
            headers: {
                'Authorization': `Bearer ${GroqApi}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.data.choices || !response.data.choices[0]) {
            throw new AppError(
                StatusCodes.INTERNAL_SERVER_ERROR,
                'AI Generation Failed',
                'Failed to generate AI response for SEO description',
                'https://api.shop.example/errors/ai-generation',
                null,
                {
                    productId: product._id,
                    modelUsed: 'llama3-8b-8192'
                }
            );
        }

        return response.data.choices[0].message.content.trim();
    } catch (error) {
        if (error instanceof AppError) throw error;

        throw new AppError(
            StatusCodes.INTERNAL_SERVER_ERROR,
            'SEO Generation Failed',
            'External API call failed while generating SEO description',
            'https://api.shop.example/errors/external-api',
            null,
            {
                apiEndpoint: 'groq.com',
                errorDetails: error.message
            }
        );
    }
}

module.exports = router;
