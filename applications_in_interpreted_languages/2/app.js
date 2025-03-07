require('dotenv').config();

const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const { StatusCodes } = require('http-status-codes');
const { verifyToken, authorizeRole } = require('./middleware/auth');
const { errorHandler } = require('./middleware/errorHandler');
const authRoutes = require('./routes/authRoutes');
const productRoutes = require('./routes/productRoutes');
const categoryRoutes = require('./routes/categoryRoutes');
const orderRoutes = require('./routes/orderRoutes');
const orderStatusRoutes = require('./routes/orderStatusRoutes');
const initRoutes = require('./routes/initRoutes');
const opinionRoutes = require('./routes/opinionRoutes');
const router = require("./routes/authRoutes");

const app = express();
const PORT = process.env.PORT || 3000;

const corsOptions = {
    origin: process.env.ALLOWED_ORIGINS ? process.env.ALLOWED_ORIGINS.split(',') : ['http://localhost:8080', 'http://127.0.0.1:8080', 'http://192.168.1.2:8080'],
    methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
    allowedHeaders: ['Content-Type', 'Authorization'],
    credentials: true,
    optionsSuccessStatus: 200
};

app.use(cors(corsOptions));

app.use(express.json());

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/shop';
mongoose.connect(MONGODB_URI, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log('Connected to MongoDB'))
    .catch(err => console.error('Could not connect to MongoDB', err));

const apiRouter = express.Router();

apiRouter.use('/auth', authRoutes);
apiRouter.use('/categories', categoryRoutes); // Pozostawione jako publiczne dla przeglądania

// Trasy zabezpieczone - wymagają uwierzytelnienia
// apiRouter.use('/products', verifyToken, productRoutes);
// apiRouter.use('/orders', verifyToken, orderRoutes);
// apiRouter.use('/status', verifyToken, authorizeRole(['PRACOWNIK']), orderStatusRoutes);
// apiRouter.use('/init', initRoutes);
// apiRouter.use('/opinions', opinionRoutes);

apiRouter.use('/products', productRoutes);
apiRouter.use('/orders', verifyToken, orderRoutes);
apiRouter.use('/status', verifyToken, authorizeRole(['PRACOWNIK']), orderStatusRoutes);
apiRouter.use('/', initRoutes);
apiRouter.use('/', opinionRoutes);

// Mount all routes under /api
app.use('/api', apiRouter);

// Middleware obsługi błędów
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({
        message: 'Wystąpił błąd serwera.',
        error: process.env.NODE_ENV === 'production' ? {} : err
    });
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`Server is running on port ${PORT}`);
});

app.use(errorHandler);

module.exports = router;
