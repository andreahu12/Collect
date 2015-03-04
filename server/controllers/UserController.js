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
var UserController = {};

// Gets list of followers
UserController.getFollowers = function(req, res) {
    var id = req.query.user_id;
    var followers = [];

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

            // Loops through all followers and pushes user object into
            // followers
            async.each(user.followers, function(follower, callback) {
                User.findById(follower)
                    .exec(function(err, user) {
                        followers.push(user);
                        callback();
                    });
            }, function(err) {
                if (err) {
                    return res.json({
                        status: false,
                        message: 'An unknown error occurred'
                    });
                }

                return res.json({
                    status: true,
                    followers: followers
                });
            });
        });
};


// Gets list of following
UserController.getFollowing = function(req, res) {
    var id = req.query.user_id;
    var following = [];

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

            // Loops through all following and pushes user object into
            // followers
            async.each(user.following, function(follow, callback) {
                User.findById(follow) // Err I don't know what else to name it >.>
                    .exec(function(err, user) {
                        following.push(user);
                        callback();
                    });
            }, function(err) {
                if (err) {
                    return res.json({
                        status: false,
                        message: 'An unknown error occurred'
                    });
                }

                return res.json({
                    status: true,
                    following: following
                });
            });
        });
};

UserController.get = function(req, res) {
    User.findById(req.query.id)
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
                user: user
            });
        });
};

// Follows user
UserController.follow = function(req, res) {
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

            user.save(function(err, user) {
                if (err) {
                    console.log('Error in follow():', err);
                }

                return res.json({
                    status: true,
                    user: user
                });
            });
        });
};

UserController.unFollow = function(req, res) {
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

            var userTwoIndex = user.following.indexOf(followingId);

            if (userTwoIndex === -1) {
                return res.json({
                    status: false,
                    message: 'User not following this account'
                });
            }

            // Removes user Two
            user.following.splice(userTwoIndex, 1);

            user.save(function(err, user) {
                if (err) {
                    console.log('Error in unFollow():', err);
                }

                return res.json({
                    status: true,
                    user: user
                });
            });
        });
};

UserController.addFollower = function(req, res) {
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
            user.followers.push(followerId);

            user.save(function(err, user) {
                if (err) {
                    console.log('Error in addFollower():', err);
                }

                return res.json({
                    status: true,
                    user: user
                });
            });
        });
};

UserController.removeFollower = function(req, res) {
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
            user.save(function(err, user) {
                if (err) {
                    console.log('Error in removeFollower():', err);
                }

                return res.json({
                    status: true,
                    user: user
                });
            });
        });
};

UserController.signup = function(req, res) {
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

UserController.login = function(req, res) {
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
UserController.getAllUsers = function(req, res) {
    User.find(function(err, users) {
            if (err)
                res.send(err);


            res.json({'users': users});
        });
}

module.exports = UserController;