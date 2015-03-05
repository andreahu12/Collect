/*jslint node: true */
'use strict';

// Dependencies
var config = require('../config/secrets').config();

var Post = require('../models/postModel');
var User = require('../models/userModel');

// Constructor
var PostController = {};


PostController.create = function(req, res) {

    var post = new Post({
        title: req.body.postTitle,
        price: req.body.postPrice,
        user: req.body.userId,
        description: req.body.postDescription
    });

    post.save(function(err, post) {
        if (err) {
            return res.json({
                status: false,
                message: 'An unknown error occurred'
            });
        }

        User.findById(req.body.userId)
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

PostController.getAllPosts = function(req, res) {
    Post.find(function(err, posts) {
        if (err) {
            return res.json({
                status: false,
                message: 'An unknown error occurred'
            });
        }


        return res.json({
            status: true,
            posts: posts
        });
    });
}


module.exports = PostController;