/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
require.config({
	paths: {
		jquery: 'libs/jquery/jquery',
		'jquery-ui': 'libs/jquery-ui/jquery-ui',
		underscore: 'libs/underscore/underscore',
		backbone: 'libs/backbone/backbone',
		templates: '../templates',
		bootstrap: 'libs/bootstrap/bootstrap',
		jsplumb: 'libs/jsplumb/jquery.jsPlumb',
		typeahead: 'libs/typeahead/typeahead'
	},
	shim: {
		underscore: {
			exports: '_'
		},
		'jquery-ui': {
			deps: [ 'jquery' ]
		},
		backbone: {
			deps: [ 'underscore', 'jquery' ],
			exports: 'Backbone'
		},
		bootstrap: {
			deps: [ 'jquery' ]
		},
		jsplumb: {
			deps: [ 'jquery', 'jquery-ui' ],
			exports: 'jsPlumb'
		},
		typeahead: {
			deps: [ 'jquery' ]
		}
	}
});

require([
	'app',
], function(App) {
	App.initialize();
});
