define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'text!templates/map/deleteIgmpStaticGroup.html'
], function($, _, Backbone, Edit, deleteIgmpStaticGroupTemplate) {

	return Edit.extend({

		template: _.template(deleteIgmpStaticGroupTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#delete").on("click", function() {
				that.act(that.model.destroy());
				return false;
			});
		}

	});
});
