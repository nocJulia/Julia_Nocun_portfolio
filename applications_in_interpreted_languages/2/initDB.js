const mongoose = require('mongoose');
const Category = require('./models/Category');
const OrderStatus = require('./models/OrderStatus');
const { Product } = require('./models/Product');
const User = require('./models/User');
const { Order } = require('./models/Order');

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/shop';

mongoose.connect(MONGODB_URI, { useNewUrlParser: true, useUnifiedTopology: true });

const initCategories = async () => {
    const categories = ['Elektronika', 'Ubrania', 'Uroda', 'Literatura'];
    const categoryDocs = [];
    for (let category of categories) {
        const doc = await Category.findOneAndUpdate(
            { name: category },
            { name: category },
            { upsert: true, new: true }
        );
        categoryDocs.push(doc);
    }
    console.log('Categories initialized');
    return categoryDocs;
};

const initOrderStatuses = async () => {
    const statuses = ['NIEZATWIERDZONE', 'ZATWIERDZONE', 'ANULOWANE', 'ZREALIZOWANE'];
    const statusDocs = [];
    for (let status of statuses) {
        const doc = await OrderStatus.findOneAndUpdate(
            { name: status },
            { name: status },
            { upsert: true, new: true }
        );
        statusDocs.push(doc);
    }
    console.log('Order statuses initialized');
    return statusDocs;
};

const initProducts = async (categories) => {
    const products = [
        {
            name: 'Telefon Samsung',
            description: 'Nowoczesny smartfon z 5G.',
            price: 2999.99,
            weight: 0.5,
            category: categories.find(c => c.name === 'Elektronika')._id
        },
        {
            name: 'T-shirt Biały',
            description: 'Klasyczny biały T-shirt.',
            price: 49.99,
            weight: 0.2,
            category: categories.find(c => c.name === 'Ubrania')._id
        },
        {
            name: 'Krem Nivea',
            description: 'Krem do twarzy.',
            price: 29.99,
            weight: 0.3,
            category: categories.find(c => c.name === 'Uroda')._id
        },
        {
            name: 'Książka "Wzorce projektowe. Elementy oprogramowania obiektowego wielokrotnego użytku"',
            description: 'Naucz się wykorzystywać wzorce projektowe i ułatw sobie pracę!',
            price: 59.99,
            weight: 1.0,
            category: categories.find(c => c.name === 'Literatura')._id
        }
    ];

    for (let product of products) {
        await Product.findOneAndUpdate(
            { name: product.name },
            product,
            { upsert: true }
        );
    }
    console.log('Products initialized');
};

const initUser = async () => {
    const existingUser = await User.findOne({ username: 'pracownik' });
    if (!existingUser) {
        const user = new User({
            username: 'pracownik',
            password: 'password', // Zostanie zaszyfrowane przez pre-save hook
            email: 'pracownik@p.lodz.pl',
            role: 'PRACOWNIK'
        });
        await user.save();
        console.log('User initialized');
    } else {
        console.log('User already exists');
    }
};

const initOrders = async (statuses, products) => {
    const exampleOrders = [
        {
            username: 'pracownik',
            email: 'pracownik@p.lodz.pl',
            phoneNumber: '123456789',
            status: statuses.find(s => s.name === 'NIEZATWIERDZONE')._id,
            items: [
                { product: products.find(p => p.name === 'Telefon Samsung')._id, quantity: 1 },
                { product: products.find(p => p.name === 'T-shirt Biały')._id, quantity: 2 }
            ]
        },
        {
            username: 'pracownik',
            email: 'pracownik@p.lodz.pl',
            phoneNumber: '123456789',
            status: statuses.find(s => s.name === 'ZATWIERDZONE')._id,
            items: [
                { product: products.find(p => p.name === 'Krem Nivea')._id, quantity: 3 },
                { product: products.find(p => p.name === 'Książka "Wzorce projektowe. Elementy oprogramowania obiektowego wielokrotnego użytku"')._id, quantity: 1 }
            ]
        }
    ];

    for (let order of exampleOrders) {
        await Order.findOneAndUpdate(
            { username: order.username, status: order.status },
            order,
            { upsert: true }
        );
    }
    console.log('Orders initialized');
};

const initClient = async () => {
    const existingClient = await User.findOne({ username: 'klient' });
    if (!existingClient) {
        const client = new User({
            username: 'klient',
            password: 'password', // Zostanie zaszyfrowane przez pre-save hook
            email: 'klient@gmail.com',
            role: 'KLIENT'
        });
        await client.save();
        console.log('Client initialized');
    } else {
        console.log('Client already exists');
    }
};

const initClientOrder = async (statuses, products) => {
    const clientOrder = {
        username: 'klient',
        email: 'klient@gmail.com',
        phoneNumber: '987654321',
        status: statuses.find(s => s.name === 'ZREALIZOWANE')._id,
        items: [
            { product: products.find(p => p.name === 'Książka "Wzorce projektowe. Elementy oprogramowania obiektowego wielokrotnego użytku"')._id, quantity: 1 },
            { product: products.find(p => p.name === 'Krem Nivea')._id, quantity: 2 }
        ],
        approvalDate: new Date()
    };

    await Order.findOneAndUpdate(
        { username: clientOrder.username, status: clientOrder.status },
        clientOrder,
        { upsert: true }
    );
    console.log('Client order initialized');
};

const init = async () => {
    const categories = await initCategories();
    const statuses = await initOrderStatuses();
    await initProducts(categories);
    await initUser();
    await initClient();
    const products = await Product.find();
    await initOrders(statuses, products);
    await initClientOrder(statuses, products);
    mongoose.connection.close();
};

init();

