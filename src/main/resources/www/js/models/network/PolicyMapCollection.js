define([
	'underscore',
	'backbone',
	'models/network/PolicyMapModel'
], function(_, Backbone, PolicyMapModel) {

	return Backbone.Collection.extend({

		model: function(attrs, options) {
			return new PolicyMapModel(attrs, {
				network: options.collection.network,
				router: options.collection.router
			});
		},
		
	  initialize: function(models, options) {
	  	this.network = options.network;
	  	this.router = options.router;
	  },
	  
	  url: function() {
	  	return "api/networks/" + this.network + "/routers/" + this.router + "/policymaps";
	  },

	});

});
