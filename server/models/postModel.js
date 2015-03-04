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
	user: Number
});

module.exports = mongoose.model('PostModel', PostSchema);