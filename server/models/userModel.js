/*jslint node: true */
'use strict';

// Dependencies
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
<<<<<<< HEAD
	name: String,
=======
>>>>>>> d48a7356412f0a93f1fb7ff11a0c179facbb8ef5
	username: String,
	password: String,
	email: String,
	followers: [],
	following: []
});

module.exports = mongoose.model('UserModel', UserSchema);