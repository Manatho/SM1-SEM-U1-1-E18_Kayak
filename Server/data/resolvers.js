const {Log, GPSPoint} = require('./mongodb');

const resolvers = {
    Query: {
        log(_, args) {
            return Log.findOne(args);
        },
        allLogs() {
            return Log.find();
        },
        GPSPoint(_, args) {
            return Log.find(args);
        },
        allGPSPoints() {
            return GPSPoint.find();
        },
    },
    Log: {
       GPSPoints(log) {
           return GPSPoint.find({log});
       },  
    },
    GPSPoint: {
        log(GPSPoint) {
            return Log.findById(GPSPoint.log);
        },
    },

    Mutation: {
        async createLog(_, {input}) {
            const count = await Log.countDocuments();
            const log = new Log({
                id: count + 1,
                ...input,
            });
            await log.save();
            return log.toObject();
        },
        async createGPSPoint(_, {input}) {
            const log = await Log.findOne({id: input.log});
            const count = await GPSPoint.countDocuments();
            const gpsPoint = new GPSPoint({
                ...input,
                log: log._id,
                id: count + 1     
            });
            await gpsPoint.save();
            return gpsPoint.toObject();
        }
    },
};

module.exports = resolvers;