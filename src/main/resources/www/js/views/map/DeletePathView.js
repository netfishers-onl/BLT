define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'text!templates/map/deletePath.html'
], function($, _, Backbone, Edit, deletePathTemplate) {

	return Edit.extend({

		template: _.template(deletePathTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#delete").on("click", function() {
				that.act(that.model.destroy());
				return false;
			});
		}

	});
});
