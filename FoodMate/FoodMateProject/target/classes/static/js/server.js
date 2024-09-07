// server.js
const express = require('express');
const bodyParser = require('body-parser');
const Satisfaction = require('./db');

const app = express();
const port = 3000;

app.use(bodyParser.json());
app.use(express.static('public'));

// API endpoint to save satisfaction data
app.post('/api/satisfaction', async (req, res) => {
    const { satisfaction, opinion } = req.body;
    try {
        const newSatisfaction = new Satisfaction({ satisfaction, opinion });
        await newSatisfaction.save();
        res.status(201).send('Satisfaction data saved successfully.');
    } catch (error) {
        res.status(500).send('Error saving satisfaction data.');
    }
});

// API endpoint to get satisfaction data
app.get('/api/satisfaction', async (req, res) => {
    try {
        const data = await Satisfaction.find();
        res.status(200).json(data);
    } catch (error) {
        res.status(500).send('Error fetching satisfaction data.');
    }
});

app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});
