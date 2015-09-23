/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'underscore',
	'backbone',
	'models/network/NetworkModel'
], function(_, Backbone, NetworkModel) {

	return Backbone.Collection.extend({

		url: "api/networks",

		model: NetworkModel

	});

});
