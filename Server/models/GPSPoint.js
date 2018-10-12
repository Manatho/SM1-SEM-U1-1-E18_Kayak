const mongoose = require('mongoose');

const GPSPointSchema = new mongoose.Schema({
    id: {type: Number},
    latitude: {type: Number},
    longitude: {type: Number},
    altitude: {type: Number},
    log: {type: mongoose.Schema.Types.ObjectId, ref: 'Log'},
});

module.exports = GPSPointSchema;