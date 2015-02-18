// Dependencies
var bCrypt = require('bcrypt-nodejs');

// User
var UserUtils = function() { };

UserUtils.prototype.isValidPassword = function(user, password) {
	return bCrypt.compareSync(password, user.password);
};

UserUtils.prototype.createHash = function(password) {
	return bCrypt.hashSync(password, bCrypt.genSaltSync(10), null);
};

module.exports = UserUtils;