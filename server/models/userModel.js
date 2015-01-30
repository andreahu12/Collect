// server/models/userModel.js
// Written by Ryan Brooks
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
    email: String,
	username: String,
	password: String
});

module.exports = mongoose.model('User', UserSchema);