/*jslint node: true */
'use strict';

// Dependencies
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var PostSchema = new Schema({
	date: { type: Number, default: Date.now },
	description: String,
	title: String,
	price: String,
	user: String,
	location: {
		lat: String,
		long: String
	}
});

module.exports = mongoose.model('PostModel', PostSchema);