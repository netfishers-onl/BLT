define([
	'underscore',
	'backbone',
	'models/network/Ipv4AccessListModel'
], function(_, Backbone, Ipv4AccessListModel) {

	return Backbone.Collection.extend({

		model: function(attrs, options) {
			return new Ipv4AccessListModel(attrs, {
				network: options.collection.network,
				router: options.collection.router
			});
		},
		
	  initialize: function(models, options) {
	  	this.network = options.network;
	  	this.router = options.router;
	  },
	  
	  url: function() {
	  	return "api/networks/" + this.network + "/routers/" + this.router + "/ipv4accesslists";
	  },

	});

});
