//==============================================
//||
//||  jQuery functions for e-terface
//||
//||  All arguments passed to functions are
//||  parsed JSON objects. See documentation
//||  for example responses.
//||
//||  author: Mike Czapik
//==============================================

/*
 * Functions getUserList and selectUser are synchronous
 * calls. This is because we cannot select a user until
 * we get the list of users. Once we get the users, we
 * shouldn't do anything until we get the home directory
 * back from the server.
 */

//Gets the list of users from the server
(function($) {
	$.fn.getUserList = function(callback) {
		var reqURL = "login/userList";
		
		$.ajax({
			url: reqURL,
			success: callback,
			dataType: "json"
		});
	}
}(jQuery));

//Sends the selected user to the server, and gets back the home directory as JSON
(function($) {
	$.fn.selectUser = function(usr, callback) {
		var reqURL = "viz/initUser?user=" + usr;

		$.ajax({
			url: reqURL,
			success: callback,
			dataType: "json"
		});
	}
}(jQuery));

//Retrieves the contents of a directory as JSON for the specified user
(function($) {
	$.fn.getDirectory = function(usr, path, callback) {
		var reqURL = "viz/dir/peek?user=" + usr + "&dir=" + path;

		$.ajax({
			url: reqURL,
			success: callback,
			dataType: "json"
		});
	}
}(jQuery));

//Gets some resource located at a URL from the Web Server
(function($) {
	$.fn.getResource = function(path, callback) {
		var reqURL = "res/getResource?path=" + path;
		
		$.ajax({
			url: reqURL,
			success: callback,
			dataType: "json"
		});
	}
}(jQuery));