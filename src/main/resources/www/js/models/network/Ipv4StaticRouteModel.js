define([
	'underscore',
	'backbone',
], function(_, Backbone) {

	return Backbone.Model.extend({

		initialize: function(attr, options) {
			this.network = options.network;
			this.router = options.router;
		},
		
		urlRoot: function() {
			return "api/networks/" + this.network + "/routers/" + this.router + "/ipv4staticroutes";
		}

	});

});
