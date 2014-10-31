define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'text!templates/map/deleteTeTunnel.html'
], function($, _, Backbone, Edit, deleteTeTunnelTemplate) {

	return Edit.extend({

		template: _.template(deleteTeTunnelTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#delete").on("click", function() {
				that.model.set("name", "");
				that.model.set("category", "");
				that.act(that.model.destroy());
				return false;
			});
		}

	});
});
