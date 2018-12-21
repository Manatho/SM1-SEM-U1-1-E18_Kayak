const fs = require('fs')
const path = require('path')
const Log = require("../models/Log");
const { GraphQLScalarType } = require("graphql");
const { Kind } = require("graphql/language");
const process = require("../processing/processor");


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
			let log = new Log({
				...logInput,
				distance: 0
			});

			await process(log);

			log.save(function(err) {
				if (err) {
					console.log(err);
				}
			});
			return log.toObject();
		},
		async exportAllLogs(){
			var logs = await Log.find()
			const logDir = path.join(__dirname, `../exports/`)
			fs.mkdirSync(logDir, {recursive: true})
			for (const log of logs) {
				const logFile = path.join(logDir, `/${log.startTime.getTime()}.log`)
				for (const p of log.gpsPoints){
					fs.appendFileSync(logFile, `
{
	time: ${p.time.getTime()}
	latitude: ${p.latitude}
	longitude: ${p.longitude}
	altitude: ${p.altitude}
}
					`)
				}
			}
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
				return new Date(new Number(ast.value)); // ast value is always in string format
			} else if (ast.kind === Kind.STRING) {
				return new Date(ast.value);
			}
			return null;
		}
	})
};
