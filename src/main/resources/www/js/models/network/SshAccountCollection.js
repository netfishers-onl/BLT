define([
	'underscore',
	'backbone',
	'models/network/SshAccountModel'
], function(_, Backbone, SshAccountModel) {

	return Backbone.Collection.extend({

		model: SshAccountModel,
		
	  initialize: function(models, options) {
	  	this.network = options.network;
	  },
	  
	  url: function() {
	  	return "api/networks/" + this.network + "/accounts";
	  },

	});

});
