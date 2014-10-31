define([
	'underscore',
	'backbone',
], function(_, Backbone) {

	return Backbone.Model.extend({

		initialize: function(attr, options) {
			this.network = options.network;
		},
		
		urlRoot: function() {
			return "api/networks/" + this.network + "/positions";
		}

	});

});
