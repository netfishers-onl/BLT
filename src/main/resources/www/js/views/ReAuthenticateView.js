define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'text!templates/reauthenticate.html',
	'bootstrap'
], function($, _, Backbone, Dialog, reauthTemplate) {

	return Dialog.extend({

		template: _.template(reauthTemplate),
		
		dialogOptions: {
			backdrop: "static"
		},
		
		onCreate: function() {
			this.$("#reload, #reloadlink").off().on("click", function() {
				window.location.reload();
			});
		}

	});
});
