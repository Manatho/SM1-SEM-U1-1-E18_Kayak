var express = require("express");
const { ApolloServer, gql } = require("apollo-server-express");
const mongodb = require("./data/mongodb");
const typeDefs = require("./data/typeDefs");
const resolvers = require("./data/resolvers");

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

const server = new ApolloServer({ typeDefs, resolvers });
server.applyMiddleware({ app });

app.listen(port, () =>
  console.log("🚀 Server ready at https://kayaklers.localtunnel.me/graphql")
);
