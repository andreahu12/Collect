/*jslint node: true */
'use strict';

// Dependencies
var config = require('../config/secrets').config();

var Post = require('../models/postModel');
var User = require('../models/userModel');

// Constructor
var PostController = function() {};


PostController.prototype.create = function(req, res) {
    var post = new Post(req.body.post);
    post.save(function(err, post) {
        if (err) {
            return res.json({
                status: false,
                message: 'An unknown error occurred'
            });
        }

        User.findById(req.body.id)
            .exec(function(err, user) {
                user.posts.push(post._id);

                user.save(function(err, user) {
                    return res.json({
                        status: true,
                        object: post
                    });
                });

            });
    });
};

module.exports = UserController;