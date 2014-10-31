define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'text!templates/map/deleteReverseStaticRoute.html'
], function($, _, Backbone, Edit, deleteReverseStaticRouteTemplate) {

	return Edit.extend({

		template: _.template(deleteReverseStaticRouteTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#delete").on("click", function() {
				that.act(that.model.destroy());
				return false;
			});
		}

	});
});
