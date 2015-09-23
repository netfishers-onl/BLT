/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'router'
], function($, _, Backbone, Router) {

	var initialize = function() {
		Router.initialize();
	};

	return {
		initialize: initialize
	};

});