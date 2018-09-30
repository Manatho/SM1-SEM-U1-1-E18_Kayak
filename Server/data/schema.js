const {makeExecutableSchema} = require('graphql-tools');
const resolvers = require('./resolvers');

const typeDefs = `
type Query {
   log(id: Int): Log
   allLogs: [Log] 
}

type Log {
    id: Int!
    duration: Float!
    distance: Float! 
    valid: Boolean!
    points: Int!
    gpsPoints: [String!]! 
}

input LogInput {
    duration: Float!
    distance: Float! 
    valid: Boolean!
    points: Int!
    gpsPoints: [String!]! 
}

type Mutation {
    createLog(input: LogInput!): Log
}
`

const schema = makeExecutableSchema({typeDefs, resolvers});

module.exports = schema;