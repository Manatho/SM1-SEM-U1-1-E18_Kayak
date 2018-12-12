const mongoose = require('mongoose');

mongoose.Promise = global.Promise;

mongoose.connect('mongodb://localhost:27017/kayaklers', {useNewUrlParser: true})
    .then(() => {
        console.log("Succesfully established connection to MongoDB")
    })
    .catch(err => {
        console.error("Problem with connection to MongoDB", err.stack);
        process.exit(1);
    });
