var express = require('express');
var express_graphql = require('express-graphql');
const schema = require('./data/schema');

const port = parseInt(process.env.PORT, 10) || 4000;

var app = express();
app.use('/graphql', express_graphql({
    schema: schema,
    graphiql: true
}));
app.listen(port, () => console.log('Express GraphQL Server Now Running On localhost:4000/graphql'));
  
