var express = require("express");
var express_graphql = require("express-graphql");
const schema = require("./data/schema");

const onWaterProcessor = require("./processing/onWater");

const port = 4000;

var app = express();

app.get("/test", async function(req, res) {
	const test = await onWaterProcessor.process([
		{ latitude: 55.3496028, longitude: 10.361572 },
		{ latitude: 55.3496228, longitude: 10.361672 },
		{ latitude: 55.3496228, longitude: 10.361752 },
		{ latitude: 55.3496228, longitude: 10.361832 },
		{ latitude: 55.3496228, longitude: 10.361912 },
		{ latitude: 55.3496228, longitude: 10.362052 },
		{ latitude: 55.3496228, longitude: 10.362132 },
		{ latitude: 55.3496258, longitude: 10.3622 }
	]);
	res.send(`<div style="
  font-family: Monospace;
  letter-spacing: -1px;
  white-space: pre-line;
  ">${test.debuggingOutput}</div>`);
});

app.use(
	"/graphql",
	express_graphql({
		schema: schema,
		graphiql: true
	})
);

app.listen(port, () => console.log("Express GraphQL Server Now Running On https://kayaklers.localtunnel.me/graphql"));
