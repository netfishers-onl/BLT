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
		typeahead: 'libs/typeahead/typeahead', 
		async: 'libs/require/async',
		infobox: 'libs/gmaps/infobox',
		infobubble: 'libs/gmaps/infobubble'
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
		},
		infobox: {
			deps: [	'async!https://maps.googleapis.com/maps/api/js' ]
		},
		infobubble: {
			deps: [	'async!https://maps.googleapis.com/maps/api/js' ]
		}
	}
});

require([
	'app',
], function(App) {
	App.initialize();
});
