const express = require('express');
const router = express.Router();
const { Product } = require('../models/Product');
const Category = require('../models/Category');
const { StatusCodes } = require('http-status-codes');
const { verifyToken, authorizeRole } = require('../middleware/auth');

router.post('/init',
    verifyToken,
    authorizeRole(['PRACOWNIK']),
    async (req, res) => {
        try {
            // Check if products already exist
            const existingProducts = await Product.countDocuments();
            if (existingProducts > 0) {
                return res.status(StatusCodes.CONFLICT).json({
                    message: 'Baza danych już zawiera produkty. Nie można zainicjalizować.'
                });
            }

            if (!Array.isArray(req.body) || req.body.length === 0) {
                return res.status(StatusCodes.BAD_REQUEST).json({
                    message: 'Nieprawidłowy format danych. Oczekiwano tablicy produktów.'
                });
            }

            // Get all categories for validation
            const categories = await Category.find();
            const categoryMap = categories.reduce((acc, cat) => {
                acc[cat.name.toLowerCase()] = cat._id;
                return acc;
            }, {});

            // Validate products and track errors
            const results = {
                total: req.body.length,
                succeeded: 0,
                failed: 0,
                failedProducts: []
            };

            const validProducts = [];

            for (const product of req.body) {
                try {
                    // Validate required fields
                    if (!product.name || !product.description || !product.price || !product.weight || !product.category) {
                        throw new Error('Brak wymaganych pól');
                    }

                    // Validate category
                    const categoryId = categoryMap[product.category.toLowerCase()];
                    if (!categoryId) {
                        throw new Error(`Nieprawidłowa kategoria: ${product.category}`);
                    }

                    // Validate price and weight
                    const price = parseFloat(product.price);
                    const weight = parseFloat(product.weight);

                    if (isNaN(price) || price <= 0) {
                        throw new Error('Nieprawidłowa cena');
                    }

                    if (isNaN(weight) || weight <= 0) {
                        throw new Error('Nieprawidłowa waga');
                    }

                    // If all validations pass, add to valid products
                    validProducts.push({
                        name: product.name,
                        description: product.description,
                        price: price,
                        weight: weight,
                        category: categoryId // Use the category ID from our map
                    });
                    results.succeeded++;
                } catch (error) {
                    results.failed++;
                    results.failedProducts.push({
                        name: product.name || 'Unknown',
                        error: error.message
                    });
                }
            }

            if (validProducts.length === 0) {
                return res.status(StatusCodes.BAD_REQUEST).json({
                    message: 'Żaden produkt nie przeszedł walidacji',
                    error: {
                        status: 400,
                        title: 'Initialization Failed',
                        detail: 'No products were added successfully',
                        type: 'https://api.shop.example/errors/initialization-failed',
                        instance: '/api/init',
                        extensions: {
                            summary: results,
                            failedProducts: results.failedProducts
                        }
                    }
                });
            }

            // Insert all valid products
            await Product.insertMany(validProducts);

            // If some products failed but others succeeded, return partial success
            if (results.failed > 0) {
                return res.status(StatusCodes.PARTIAL_CONTENT).json({
                    message: `Zainicjalizowano ${results.succeeded} z ${results.total} produktów`,
                    summary: results,
                    failedProducts: results.failedProducts
                });
            }

            // All products succeeded
            res.status(StatusCodes.OK).json({
                message: `Pomyślnie zainicjalizowano ${results.succeeded} produktów`,
                summary: results
            });

        } catch (error) {
            console.error('Initialization error:', error);
            res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
                message: 'Wystąpił błąd podczas inicjalizacji bazy danych',
                error: error.message
            });
        }
    }
);

module.exports = router;
