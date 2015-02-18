/*jslint node: true */
'use strict';

// Dependencies
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
<<<<<<< HEAD
	name: String,
	username: String,
	password: String,
	email: String
=======
	username: String,
	password: String,
	email: String,
	followers: [],
	following: []
>>>>>>> a6d0b71d7cffe3712872232a54bba4af760dc863
});

module.exports = mongoose.model('UserModel', UserSchema);