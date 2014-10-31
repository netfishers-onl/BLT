define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'text!templates/map/deleteStaticRoute.html'
], function($, _, Backbone, Edit, deleteStaticRouteTemplate) {

	return Edit.extend({

		template: _.template(deleteStaticRouteTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#delete").on("click", function() {
				that.act(that.model.destroy());
				return false;
			});
		}

	});
});
