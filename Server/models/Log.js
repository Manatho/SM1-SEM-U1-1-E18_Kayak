const mongoose = require('mongoose');

const logSchema = new mongoose.Schema({
    id: {type: Number},
    startTime: {type: Number},
    endTime: {type: Number},
    duration: {type: Number},
    distance: {type: Number},
    valid: {type: Boolean},
    points: {type: Number},
    GPSPoints: [{type: mongoose.Schema.Types.ObjectId, ref: 'GPSPoint'}],
});

module.exports = logSchema;