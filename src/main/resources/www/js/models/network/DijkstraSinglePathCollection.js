/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
        'underscore',
        'backbone',
        'models/network/DijkstraSinglePathModel'
        ], function(_, Backbone, DijkstraSinglePathModel) {

	return Backbone.Collection.extend({

		model: function(attrs, options) {
			return new DijkstraSinglePathModel(attrs, {
				network: options.collection.network,
				origin: options.collection.origin,
				target: options.collection.target
			});
		},

		initialize: function(models, options) {
			this.network = options.network;
			this.origin = options.origin;
			this.target = options.target;
		},

		url: function() {
			return "api/networks/" + this.network + "/routers/" + this.origin + "/shortestpathto/"+this.target;
		}

	});

});
