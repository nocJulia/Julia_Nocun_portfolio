const mongoose = require('mongoose');
const Joi = require('joi');

const productSchema = new mongoose.Schema({
    name: { type: String, required: true },
    description: { type: String, required: true },
    price: { type: Number, required: true, min: 0.01 },
    weight: { type: Number, required: true, min: 0.01 },
    category: { type: mongoose.Schema.Types.ObjectId, ref: 'Category', required: true }
});

const productValidationSchema = Joi.object({
    name: Joi.string().required().messages({
        'string.empty': 'Nazwa produktu nie może być pusta.',
        'any.required': 'Nazwa produktu jest wymagana.'
    }),
    description: Joi.string().required().messages({
        'string.empty': 'Opis produktu nie może być pusty.',
        'any.required': 'Opis produktu jest wymagany.'
    }),
    price: Joi.number().positive().required().messages({
        'number.base': 'Cena musi być liczbą.',
        'number.positive': 'Cena musi być większa niż zero.',
        'any.required': 'Cena jest wymagana.'
    }),
    weight: Joi.number().positive().required().messages({
        'number.base': 'Waga musi być liczbą.',
        'number.positive': 'Waga musi być większa niż zero.',
        'any.required': 'Waga jest wymagana.'
    }),
    category: Joi.string().required().messages({
        'string.empty': 'Kategoria nie może być pusta.',
        'any.required': 'Kategoria jest wymagana.'
    })
});

module.exports = {
    Product: mongoose.model('Product', productSchema),
    validateProduct: (product) => productValidationSchema.validate(product, { abortEarly: false })
};