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

UserController.prototype.signup = function(req, res) {
    console.log('Creating user...');

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

            return res.json({
                status: true,
                user: user
            });

        });
    })(req, res);
};

passport.serializeUser(function(user, done) {
    done(null, user._id);
});

passport.deserializeUser(function(id, done) {
    User.findById(id, function(err, user) {
        done(err, user);
    });
});

passport.use('login', new LocalStrategy({
        usernameField: 'email',
        passReqToCallback: true
    }, function(req, email, password, callback) {
        email = email.toLowerCase();

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

module.exports = UserController;