// db.js
const mongoose = require('mongoose');

mongoose.connect('mongodb://localhost:27017/satisfaction', { useNewUrlParser: true, useUnifiedTopology: true });

const satisfactionSchema = new mongoose.Schema({
    satisfaction: String,
    opinion: String,
    date: { type: Date, default: Date.now }
});

const Satisfaction = mongoose.model('Satisfaction', satisfactionSchema);

module.exports = Satisfaction;
