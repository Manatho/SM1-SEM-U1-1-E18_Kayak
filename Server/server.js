var express = require("express");
var express_graphql = require("express-graphql");
const schema = require("./data/schema");

const onWaterProcessor = require("./processing/onWater");

const port = 4000;

var app = express();

app.get("/test", async function(req, res) {
  const {data} = await onWaterProcessor.process([]);
  console.log("route output: ", data.length);
  res.writeHead(200, {
    "Content-Type": "image/png",
    "Content-Length": data.length
  });
  res.end(data, 'hex');
});

app.use(
  "/graphql",
  express_graphql({
    schema: schema,
    graphiql: true
  })
);

app.listen(port, () =>
  console.log(
    "Express GraphQL Server Now Running On https://kayaklers.localtunnel.me/graphql"
  )
);
