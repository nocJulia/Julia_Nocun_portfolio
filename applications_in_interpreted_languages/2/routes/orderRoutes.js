const express = require('express');
const router = express.Router();
const { Order, validateOrder } = require('../models/Order');
const { Product } = require('../models/Product');
const OrderStatus = require('../models/OrderStatus');
const { StatusCodes } = require('http-status-codes');
const { verifyToken, authorizeRole } = require('../middleware/auth');

// Get all orders - for employees only
router.get('/', verifyToken, authorizeRole(['PRACOWNIK']), async (req, res) => {
    try {
        const orders = await Order.find()
            .populate('status')
            .populate('items.product')
            .sort({ createdAt: -1 });
        res.json(orders);
    } catch (error) {
        res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
            message: 'Nie udało się pobrać zamówień.'
        });
    }
});

// Get user's orders
router.get('/user/:username', verifyToken, async (req, res) => {
    try {
        // Check if user is requesting their own orders or is an employee
        if (req.user.username !== req.params.username && req.user.role !== 'PRACOWNIK') {
            return res.status(StatusCodes.FORBIDDEN).json({
                message: 'Brak dostępu do zamówień tego użytkownika'
            });
        }

        const orders = await Order.find({ username: req.params.username })
            .populate('status')
            .populate('items.product')
            .sort({ createdAt: -1 });
        res.json(orders);
    } catch (error) {
        res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
            message: 'Nie udało się pobrać zamówień użytkownika.'
        });
    }
});

// Create new order
router.post('/', verifyToken, async (req, res) => {
    try {
        // Validate the input data first (bez status i createdAt)
        const { error } = validateOrder(req.body);
        if (error) {
            return res.status(StatusCodes.BAD_REQUEST).json({
                message: 'Błąd walidacji.',
                errors: error.details.reduce((acc, err) => {
                    acc[err.path[0]] = err.message;
                    return acc;
                }, {})
            });
        }

        // Find initial status
        const initialStatus = await OrderStatus.findOne({ name: 'NIEZATWIERDZONE' });
        if (!initialStatus) {
            return res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
                message: 'Nie można znaleźć początkowego statusu zamówienia'
            });
        }

        // Validate products
        const productIds = req.body.items.map(item => item.product);
        const products = await Product.find({ _id: { $in: productIds } });

        if (products.length !== productIds.length) {
            return res.status(StatusCodes.BAD_REQUEST).json({
                message: 'Niektóre produkty nie istnieją w bazie danych.'
            });
        }

        // Create order with initial status
        const order = new Order({
            ...req.body,
            status: initialStatus._id
        });

        const savedOrder = await order.save();

        // Return populated order
        const populatedOrder = await Order.findById(savedOrder._id)
            .populate('status')
            .populate('items.product');

        res.status(StatusCodes.CREATED).json(populatedOrder);
    } catch (error) {
        console.error('Order creation error:', error);
        res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
            message: 'Nie udało się utworzyć zamówienia.',
            error: error.message
        });
    }
});

// Update order status
router.patch('/:id', verifyToken, authorizeRole(['PRACOWNIK']), async (req, res) => {
    try {
        const order = await Order.findById(req.params.id).populate('status');
        if (!order) {
            return res.status(StatusCodes.NOT_FOUND).json({
                message: 'Zamówienie nie znalezione.'
            });
        }

        const newStatus = await OrderStatus.findById(req.body.status);
        if (!newStatus) {
            return res.status(StatusCodes.BAD_REQUEST).json({
                message: 'Nieprawidłowy status.'
            });
        }

        if (order.status.name === 'ANULOWANE') {
            return res.status(StatusCodes.BAD_REQUEST).json({
                message: 'Nie można zmienić statusu anulowanego zamówienia.'
            });
        }

        const statusOrder = ['NIEZATWIERDZONE', 'ZATWIERDZONE', 'WYSŁANE', 'ZREALIZOWANE', 'ANULOWANE'];
        const currentIndex = statusOrder.indexOf(order.status.name);
        const newIndex = statusOrder.indexOf(newStatus.name);

        if (newIndex < currentIndex && newStatus.name !== 'ANULOWANE') {
            return res.status(StatusCodes.BAD_REQUEST).json({
                message: 'Nie można zmienić statusu na wcześniejszy.'
            });
        }

        order.status = newStatus._id;
        if (newStatus.name === 'ZATWIERDZONE') {
            order.approvalDate = new Date();
        }

        await order.save();

        // Return populated order
        const updatedOrder = await Order.findById(order._id)
            .populate('status')
            .populate('items.product');

        res.json(updatedOrder);
    } catch (error) {
        res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
            message: 'Nie udało się zaktualizować zamówienia.',
            error: error.message
        });
    }
});

module.exports = router;
