const mongoose = require('mongoose');
const LogSchema = require('../models/Log');

mongoose.Promise = global.Promise;

mongoose.connect('mongodb://localhost/kayaklers', {useNewUrlParser: true})
    .then(() => {
        console.log("Succesfully established connection to MongoDB")
    })
    .catch(err => {
        console.error("Problem with connection to MongoDB", err.stack);
        process.exit(1);
    });

const Log = mongoose.model('Log', LogSchema);

exports.Log = Log;