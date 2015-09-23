/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'underscore',
	'backbone',
	'models/network/LinkModel'
], function(_, Backbone, LinkModel) {

	return Backbone.Collection.extend({

		model: LinkModel,
		
	  initialize: function(models, options) {
	  	this.network = options.network;
	  },
	  
	  url: function() {
	  	return "api/networks/" + this.network + "/links";
	  },

	});

});
