/*jslint node: true */
'use strict';

// Dependencies
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
	username: String,
	password: String,
	email: String
});

module.exports = mongoose.model('UserModel', UserSchema);