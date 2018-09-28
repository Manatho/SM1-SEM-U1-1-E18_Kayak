const mongoose = require('mongoose');
const LogSchema = require('../models/Log');

mongoose.Promise = global.Promise;

const db = mongoose.createConnection('mongodb://localhost/kayaklers');

const Log = db.model('Log', LogSchema);

exports.Log = Log;