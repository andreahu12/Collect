/*jslint node: true */
'use strict';

// Dependencies
var _ = require('lodash-node');
var async = require('async');
var config = require('../config/secrets').config();

var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;

var User = require('../models/userModel');
var UserUtils = require('../utils/user');
var userUtils = new UserUtils();

// Constructor
var UserController = function(User) {
    User = User;
};

// Gets list of followers
UserController.prototype.getFollowers = function(req, res) {
    var id = req.query.user_id;

    User.findById(id)
        .exec(function(err, user) {
            if (err) {
                return res.json({
                    status: false,
                    message: 'An unknown error occurred'
                });
            }

            if (!user) {
                return res.json({
                    status: false,
                    message: 'Could not find user'
                });
            }

            return res.json({
                status: true,
                followers: user.followers
            });
        });
};

// Gets list of following
UserController.prototype.getFollowing = function(req, res) {
    var id = req.query.user_id;

    User.findById(id)
        .exec(function(err, user) {
            if (err) {
                return res.json({
                    status: false,
                    message: 'An unknown error occurred'
                });
            }

            if (!user) {
                return res.json({
                    status: false,
                    message: 'Could not find user'
                });
            }

            return res.json({
                status: true,
                following: user.following
            });
        });
};

// Follows user
UserController.prototype.follow = function(req, res) {
    var id = req.body.user_id;
    var followingId = req.body.followingId;

    User.findById(id)
        .exec(function(err, user) {
            if (err) {
                return res.json({
                    status: false,
                    message: 'An unknown error occurred'
                });
            }

            if (!user) {
                return res.json({
                    status: false,
                    message: 'Could not find user'
                });
            }

            user.following.push(followingId);
        });
};

UserController.prototype.unFollow = function(req, res) {
    var id = req.body.user_id;
    var followingId = req.body.followingId;

    User.findById(id)
        .exec(function(err, user) {
            if (err) {
                return res.json({
                    status: false,
                    message: 'An unknown error occurred'
                });
            }

            if (!user) {
                return res.json({
                    status: false,
                    message: 'Could not find user'
                });
            }

            // Add following
            user.following.addToSet(followingId);
            user.save(function(err, user) {
                if (err) {
                    console.log('Error in addFollower():', err);
                }

                return res.json({
                    status: true,
                    object: user
                });
            });
        });
};

UserController.prototype.addFollower = function(req, res) {
    var id = req.body.id;
    var followerId = req.body.followerId;

    User.findById(id)
        .exec(function(err, user) {
            if (err) {
                return res.json({
                    status: false,
                    message: 'An unknown error occurred'
                });
            }

            if (!user) {
                return res.json({
                    status: false,
                    message: 'Could not find user'
                });
            }

            // Adds user to following list
            user.following.push(followerId);

            user.save(function(err, user) {
                if (err) {
                    console.log('Error in addFollower():', err);
                }

                return res.json({
                    status: true,
                    object: user
                });
            });
        });
};

UserController.prototype.removeFollower = function(req, res) {
    var id = req.body.user_id;
    var followerId = req.body.followerId;

    User.findById(id)
        .exec(function(err, user) {
            if (err) {
                return res.json({
                    status: false,
                    message: 'An unknown error occurred'
                });
            }

            if (!user) {
                return res.json({
                    status: false,
                    message: 'Could not find user'
                });
            }

            var userTwoIndex = user.followers.indexOf(followerId);

            if (userTwoIndex === -1) {
                return res.json({
                    status: false,
                    message: 'User not being followed yet'
                });
            }

            // Removes user Two
            user.followers.splice(userTwoIndex, 1);
        });
};

UserController.prototype.signup = function(req, res) {
    passport.authenticate('signup', function(err, user, info) {
        // User not being serialized because no req.login
        // req.login supposed to be called automatically
        // by passport.authenticate
        req.logIn(user, function(err) {
            if (err) {
                console.log('Error in signup:', err);
                return;
            }
            var cookie = {
                userId: user.id,
                subdomain: user.subdomain
            };

            // Add cookie to domain
            res.cookie('LOGIN_INFO', cookie);

            return res.json(user);
        });
    })(req, res);
};

UserController.prototype.login = function(req, res) {
    passport.authenticate('login', function(err, user, info) {
        if (!user) {
            return res.json({
                status: false,
                message: 'Bummer! The email/password combination you entered is not correct.'
            });
        }

        req.logIn(user, function(err) {
            if (err) {
                console.log('Error in login:', err);
                return res.status(401).send(false);
            }

            var cookie = {
                userId: user.id,
                email: user.email
            };

            res.cookie('LOGIN_INFO', cookie, { domain: config.domain });

            console.log('You are logged in', user);

            return res.json({
                status: true,
                user: user
            });

        });
    })(req, res);
};

passport.serializeUser(function(user, done) {
    console.log('User._id', user._id);
    done(null, user._id);
});

passport.deserializeUser(function(id, done) {
    console.log('YO', id);
    User.findById(id, function(err, user) {
        done(err, user);
    });
});

passport.use('login', new LocalStrategy({
        usernameField: 'email',
        passReqToCallback: true
    }, function(req, email, password, callback) {
        email = email.toLowerCase();

        console.log('YOYOY', email);

        User.findOne({ email: email }, function(err, user) {
            if (err) {
                console.log('Error in passport login', err);
                return callback(err);
            }

            if (!user) {
                console.log('User not found');
                return callback(null, false);
            }

            if (!userUtils.isValidPassword(user, password)) {
                console.log('Invalid password');
                return callback(null, false);
            }

            return callback(null, user);
        });
    }
));

passport.use('signup', new LocalStrategy({
        usernameField: 'email',
        passReqToCallback : true
    }, function findOrCreateUser(req, email, password, callback) {
        email = email.toLowerCase();

        User.findOne({ email: email.toLowerCase() }, function(err, user) {
            if (err) {
                console.log('Error in signup', err);
                return callback(err);
            }

            if (user) {
                console.log('User already exists yo');
                console.log('This is the user', user);
                return callback(null, false);
            }

            var newUser = new User();

            newUser.name = req.body.name;
            newUser.username = req.body.username;
            newUser.password = userUtils.createHash(password);
            newUser.email = email;

            newUser.save(function(err) {
                if (err) {
                    console.log('Error in saving user', err);
                    return callback(null, false);
                }

                return callback(null, newUser);
            });
        });
    })
);

// Gets list of all followers
UserController.prototype.getAllUsers = function(req, res) {
    User.find(function(err, users) {
            if (err)
                res.send(err);


            res.json({'users': users});
        });
}

module.exports = UserController;