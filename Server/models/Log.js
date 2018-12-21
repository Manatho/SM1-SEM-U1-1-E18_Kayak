const mongoose = require("mongoose");

const GPSPointSchema = new mongoose.Schema(
	{
		time: { type: Date, required: true },
		latitude: { type: Number, required: true},
		longitude: { type: Number, required: true },
		altitude: { type: Number, required: true },
		valid: { type: Boolean, default: false },
		speed: { type: Number, default: 0 }
	},
	{ id: false }
);

const logSchema = new mongoose.Schema({
	startTime: { type: Date, default: Date() },
	endTime: { type: Date },
	duration: { type: Number },
	distance: { type: Number },
	points: { type: Number },
	gpsPoints: [GPSPointSchema]
});

module.exports = mongoose.model("Log", logSchema);
