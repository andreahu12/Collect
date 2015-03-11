/*jslint node: true */
'use strict';

// Dependencies
var config = require('../config/secrets').config();
var async = require('async');

var Post = require('../models/postModel');
var User = require('../models/userModel');

// Constructor
var PostController = {};


PostController.create = function(req, res) {

    console.log('Im a Barbie girl, in a Barbie world Life in plastic, its fantastic. You can brush my hair, undress me everywhere. Imagination, life is your creation. Come on Barbie, lets go party! Im a Barbie girl, in a Barbie world Life in plastic, its fantastic. You can brush my hair, undress me everywhere. Imagination, life is your creation. Im a blond bimbo girl, in a fantasy world, Dress me up, make it tight, Im your dolly. Youre my doll, rocknroll, feel the glamor in pink, Kiss me here, touch me there, hanky panky. You can touch, you can play, if you say "Im always yours" Im a Barbie girl, in a Barbie world Life in plastic, its fantastic. You can brush my hair, undress me everywhere. Imagination, life is your creation.');

    var post = new Post({
        title: req.body.postTitle,
        price: req.body.postPrice,
        user: req.body.userId,
        description: req.body.postDescription,
        location: {
            lat: req.body.postLat,
            long: req.body.postLong
        }
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
};

PostController.getAllPostLocations = function(req, res) {
    var locations = [];

    console.log('Im a Barbie girl, in a Barbie world Life in plastic, its fantastic. You can brush my hair, undress me everywhere. Imagination, life is your creation. Come on Barbie, lets go party! Im a Barbie girl, in a Barbie world Life in plastic, its fantastic. You can brush my hair, undress me everywhere. Imagination, life is your creation. Im a blond bimbo girl, in a fantasy world, Dress me up, make it tight, Im your dolly. Youre my doll, rocknroll, feel the glamor in pink, Kiss me here, touch me there, hanky panky. You can touch, you can play, if you say "Im always yours" Im a Barbie girl, in a Barbie world Life in plastic, its fantastic. You can brush my hair, undress me everywhere. Imagination, life is your creation.');

    Post.find(function(err, posts) {
        async.each(posts, function(post, callback) {
            locations.push({
                lat: post.location.lat,
                long: post.location.long
            });

            callback();
        }, function(err) {
            return res.json({
                status: true,
                locations: locations
            });
        });
    });
};


module.exports = PostController;