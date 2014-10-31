define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'text!templates/map/deleteSubInterface.html'
], function($, _, Backbone, Edit, deleteSubInterfaceTemplate) {

	return Edit.extend({

		template: _.template(deleteSubInterfaceTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#delete").on("click", function() {
				that.model.set("name", "");
				that.act(that.model.destroy());
				return false;
			});
		}

	});
});
