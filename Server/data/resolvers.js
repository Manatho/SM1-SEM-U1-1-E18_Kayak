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
            const count = await Log.count();
            const log = new Log({
                id: count + 1,
                ...input,
            });
            await log.save();
            return log.toObject();
        },
    },
};

module.exports = resolvers;