/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
        'underscore',
        'backbone',
        'models/network/DijkstraPathModel'
        ], function(_, Backbone, DijkstraPathModel) {

	return Backbone.Collection.extend({

		model: function(attrs, options) {
			return new DijkstraPathModel(attrs, {
				network: options.collection.network,
				router: options.collection.router
			});
		},

		initialize: function(models, options) {
			this.network = options.network;
			this.router = options.router;
		},

		url: function() {
			return "api/networks/" + this.network + "/routers/" + this.router + "/shortestpathtree";
		}

	});

});
