const { gql } = require("apollo-server-express");
const mongoose = require("mongoose");

const { ObjectId } = mongoose.Types;

ObjectId.prototype.valueOf = function() {
	return this.toString();
};

module.exports = gql`
	scalar Date

	type Query {
		log(id: ID): Log
		allLogs: [Log]
	}

	type Log {
		_id: ID
		startTime: Date
		endTime: Date
		duration: Float
		distance: Float
		points: Int
		gpsPoints: [GPSPoint]
	}

	type GPSPoint {
		_id: ID
		time: Date
		latitude: Float
		longitude: Float
		altitude: Float
		valid: Boolean
		speed: Float
	}

	input GPSPointInput {
		time: Date
		latitude: Float
		longitude: Float
		altitude: Float
	}

	input LogInput {
		gpsPoints: [GPSPointInput]!
	}

	type Mutation {
		createLog(logInput: LogInput!): Log
		exportAllLogs: String
	}
`;
