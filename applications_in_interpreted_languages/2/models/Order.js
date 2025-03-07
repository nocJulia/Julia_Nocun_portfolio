const mongoose = require('mongoose');
const Joi = require('joi');

const orderSchema = new mongoose.Schema({
    approvalDate: { type: Date, default: null },
    status: { type: mongoose.Schema.Types.ObjectId, ref: 'OrderStatus', required: true },
    username: { type: String, required: true },
    email: { type: String, required: true },
    phoneNumber: { type: String, required: true },
    items: [{
        product: { type: mongoose.Schema.Types.ObjectId, ref: 'Product', required: true },
        quantity: { type: Number, required: true, min: 1 }
    }]
}, {
    timestamps: true // Automatycznie dodaje createdAt i updatedAt
});

const orderValidationSchema = Joi.object({
    username: Joi.string().required().messages({
        'string.empty': 'Nazwa użytkownika nie może być pusta.',
        'any.required': 'Nazwa użytkownika jest wymagana.'
    }),
    email: Joi.string().email().required().messages({
        'string.email': 'Nieprawidłowy format adresu email.',
        'any.required': 'Adres email jest wymagany.'
    }),
    phoneNumber: Joi.string().pattern(/^\d+$/).required().messages({
        'string.pattern.base': 'Numer telefonu może zawierać tylko cyfry.',
        'any.required': 'Numer telefonu jest wymagany.'
    }),
    items: Joi.array().items(
        Joi.object({
            product: Joi.string().required().messages({
                'string.empty': 'ID produktu nie może być puste.',
                'any.required': 'ID produktu jest wymagane.'
            }),
            quantity: Joi.number().integer().positive().required().messages({
                'number.base': 'Ilość musi być liczbą.',
                'number.integer': 'Ilość musi być liczbą całkowitą.',
                'number.positive': 'Ilość musi być większa od zera.',
                'any.required': 'Ilość jest wymagana.'
            })
        })
    ).min(1).required().messages({
        'array.min': 'Zamówienie musi zawierać co najmniej jeden produkt.',
        'any.required': 'Lista produktów jest wymagana.'
    })
    // Usuwamy walidację dla status i createdAt, ponieważ są one dodawane automatycznie
});

module.exports = {
    Order: mongoose.model('Order', orderSchema),
    validateOrder: (order) => orderValidationSchema.validate(order, { abortEarly: false })
};
