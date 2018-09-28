const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const logSchema = new Schema({
    id: {type: Number, required: true},
    duration: {type: Number, required: true},
    distance: {type: Number, required: true},
    valid: {type: Boolean, required: true},
    points: {type: Number, required: true},
    gpsPoints: {type: [String], required: true},
});

module.exports = logSchema;