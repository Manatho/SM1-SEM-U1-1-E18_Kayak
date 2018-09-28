const {makeExecutableSchema} = require(graphql-tools);
const resolvers = require('./resolvers');

const typeDefs = `
type Query {
   log(id: ID) 
}

type Log {
    id: ID!
    duration(unit: ):  
}
`