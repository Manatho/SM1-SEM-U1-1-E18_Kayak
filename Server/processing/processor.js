const onWaterProcessor = require("./onWater");
const filters = require("./filters");
const logSummarizer = require("./logsummarizer");
const pointProcessing = require("./pointProcessing");

async function process(log) {
	console.log(new Date().getTime());
	console.log(log);

	console.log("Processing starting");

	console.log("Filtering log");
	let filteredData = filters.movingAverage(5, log.gpsPoints).data;

	console.log("Calculating Water Distance");
	await onWaterProcessor.process(filteredData);

	console.log("Calculating stats and speed");
	let summary = logSummarizer.process(filteredData);
	log.startTime = filteredData[0].time;
	log.endTime = filteredData[filteredData.length - 1].time;
	log.duration = summary.duration;

	console.log("Calculating Points");
	pointProcessing.calculatePoints(log, filteredData);
	log.gpsPoints = filteredData;
}

module.exports = process;
