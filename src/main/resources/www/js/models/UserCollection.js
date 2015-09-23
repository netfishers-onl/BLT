/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'underscore',
	'backbone',
	'models/UserModel'
], function(_, Backbone, UserModel) {

	return Backbone.Collection.extend({

		model: UserModel,
	  
	  url: function() {
	  	return "api/users";
	  },

	});

});
