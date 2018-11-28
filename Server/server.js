var express = require("express");
var express_graphql = require("express-graphql");
const schema = require("./data/schema");

const onWaterProcessor = require("./processing/onWater");
const filters = require("./processing/filters");
const logSummarizer = require("./processing/logsummarizer");

const port = 4000;

const testData = [
	{ latitude: 55.3496028, longitude: 10.361572, time: 0 },
	{ latitude: 55.3496228, longitude: 10.361672, time: 1 },
	{ latitude: 55.3496228, longitude: 10.361752, time: 2 },
	{ latitude: 55.3496228, longitude: 10.361832, time: 3 },
	{ latitude: 55.3496228, longitude: 10.361912, time: 4 },
	{ latitude: 55.3496228, longitude: 10.362052, time: 5 },
	{ latitude: 55.3496228, longitude: 10.362132, time: 6 },
	{ latitude: 55.3496258, longitude: 10.3622, time: 7 }
];

var app = express();

app.get("/testWater", async function(req, res) {
	const test = await onWaterProcessor.process(testData);
	res.send(`<div style="
  font-family: Monospace;
  letter-spacing: -1px;
  white-space: pre-line;
  ">${test.debuggingOutput}</div>`);
});

app.get("/testFilter", function(req, res) {
	const filteredData = filters.movingMedian(2, testData);
	res.send(`<div style="
  font-family: Monospace;
  letter-spacing: -1px;
  white-space: pre-line;
  ">${filteredData.debuggingOutput}</div>`);
});

app.get("/testSpeed", function(req, res) {
	const summarized = logSummarizer.process(testData);
	res.send(`<div style="
  font-family: Monospace;
  letter-spacing: -1px;
  white-space: pre-line;
  ">${summarized.debuggingOutput}</div>`);
});

app.get("/testFilteredSpeed", function(req, res) {
	const filteredData = filters.movingAverage(2, testData);
	console.log(filteredData);

	const summarized = logSummarizer.process(filteredData.data);
	res.send(`<div style="
  font-family: Monospace;
  letter-spacing: -1px;
  white-space: pre-line;
  ">${summarized.debuggingOutput}</div>`);
});

app.use(
	"/graphql",
	express_graphql({
		schema: schema,
		graphiql: true
	})
);

app.listen(port, () => console.log("Express GraphQL Server Now Running On https://kayaklers.localtunnel.me/graphql"));
