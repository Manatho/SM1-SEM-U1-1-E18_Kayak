const Log = require("../models/Log");
const { GraphQLScalarType } = require("graphql");
const { Kind } = require("graphql/language");

module.exports = {
  Query: {
    async log(_, args) {
      GPSPoint;
      return await Log.findOne(args);
    },
    async allLogs() {
      return await Log.find();
    }
  },
  GPSPoint: {},

  Mutation: {
    async createLog(_, { logInput }) {
      console.log(logInput);
      let log = new Log({
        ...logInput,
        distance: 0
      });
      
      log.save(function (err) {
        if (err) { 
            console.log(err);
        }
        console.log("test")
    });
      return log.toObject();
    }
  },

  Date: new GraphQLScalarType({
    name: "Date",
    description: "Date custom scalar type",
    parseValue(value) {
      return new Date(value); // value from the client
    },
    serialize(value) {
      return value.getTime(); // value sent to the client
    },
    parseLiteral(ast) {
      if (ast.kind === Kind.INT) {
        return new Date(ast.value); // ast value is always in string format
      }
      return null;
    }
  })
};
