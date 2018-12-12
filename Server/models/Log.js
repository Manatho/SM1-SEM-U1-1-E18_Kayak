const mongoose = require('mongoose');

const GPSPointSchema = new mongoose.Schema({
    time: { type: Date },
    latitude: { type: Number },
    longitude: { type: Number },
    altitude: { type: Number },
  }, {id: false});

const logSchema = new mongoose.Schema({
    startTime: {type: Date},
    endTime: {type: Date},
    duration: {type: Number},
    distance: {type: Number},
    valid: {type: Boolean},
    points: {type: Number},
    gpsPoints: [GPSPointSchema],
});

module.exports = mongoose.model('Log', logSchema);