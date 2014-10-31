define([
	'underscore',
	'backbone',
	'models/network/SnmpCommunityModel'
], function(_, Backbone, SnmpCommunityModel) {

	return Backbone.Collection.extend({

		model: SnmpCommunityModel,
		
	  initialize: function(models, options) {
	  	this.network = options.network;
	  },
	  
	  url: function() {
	  	return "api/networks/" + this.network + "/communities";
	  },

	});

});
