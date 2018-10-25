const {makeExecutableSchema} = require('graphql-tools');
const resolvers = require('./resolvers');

const typeDefs = `
type Query {
   log(id: Int): Log
   GPSPoint(id: Int): GPSPoint
   allLogs: [Log]  
   allGPSPoints: [GPSPoint]
}

type Log {
    id: Int
    startTime: Float
    endTime: Float
    duration: Float
    distance: Float
    valid: Boolean
    points: Int 
    GPSPoints: [GPSPoint]
}

input LogInput {
    startTime: Float
}

type GPSPoint {
    id: Int
    latitude: Float
    longitude: Float
    altitude: Float
    log: Log
}

input GPSPointInput {
    latitude: Float
    longitude: Float
    altitude: Float
    log: Int
}

type Mutation {
    createLog(input: LogInput!): Log
    createGPSPoint(input: GPSPointInput!): GPSPoint
}`;

const schema = makeExecutableSchema({typeDefs, resolvers});

module.exports = schema;