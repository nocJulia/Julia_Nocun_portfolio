const mongoose = require('mongoose');
const Joi = require('joi');

const opinionSchema = new mongoose.Schema({
    order: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Order',
        required: true
    },
    rating: {
        type: Number,
        required: true,
        min: 1,
        max: 5
    },
    content: {
        type: String,
        required: true,
        minlength: 1,
        maxlength: 1000
    },
    createdAt: {
        type: Date,
        default: Date.now
    },
    username: {
        type: String,
        required: true
    }
});

// Ensure one opinion per order
opinionSchema.index({ order: 1 }, { unique: true });

const opinionValidationSchema = Joi.object({
    rating: Joi.number().integer().min(1).max(5).required().messages({
        'number.base': 'Ocena musi być liczbą.',
        'number.integer': 'Ocena musi być liczbą całkowitą.',
        'number.min': 'Ocena nie może być mniejsza niż 1.',
        'number.max': 'Ocena nie może być większa niż 5.',
        'any.required': 'Ocena jest wymagana.'
    }),
    content: Joi.string().min(1).max(1000).required().messages({
        'string.empty': 'Treść opinii nie może być pusta.',
        'string.min': 'Treść opinii musi zawierać co najmniej 1 znak.',
        'string.max': 'Treść opinii nie może przekraczać 1000 znaków.',
        'any.required': 'Treść opinii jest wymagana.'
    })
});

const Opinion = mongoose.model('Opinion', opinionSchema);

module.exports = {
    Opinion,
    validateOpinion: (opinion) => opinionValidationSchema.validate(opinion, { abortEarly: false })
};
