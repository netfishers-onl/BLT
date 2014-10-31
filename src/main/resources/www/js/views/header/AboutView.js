define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'text!templates/header/about.html',
	'models/ServerStatusModel',
	'bootstrap'
], function($, _, Backbone, Dialog, aboutTemplate, ServerStatusModel) {

	return Dialog.extend({
		
		initialize: function() {
			var that = this;
			this.model = new ServerStatusModel();
			this.model.fetch().done(function() {
				that.render();
			});
		},

		template: _.template(aboutTemplate),

	});
});
