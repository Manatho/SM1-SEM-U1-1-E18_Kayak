const {Log} = require('./mongodb');

const resolvers = {
    Query: {
        log(_, args) {
            return Log.findOne(args);
        },
        allLogs() {
            return Log.find();
        },
    },
    Mutation: {
        async createLog(_, {input}) {
            const log = new Log({...input});
            await log.save();
            return log.toObject();
        },
    },
};

module.exports = resolvers;