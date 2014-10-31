define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'text!templates/map/deleteIpv4SsmRsvpGroup.html'
], function($, _, Backbone, Edit, deleteIpv4SsmRsvpGroupTemplate) {

	return Edit.extend({

		template: _.template(deleteIpv4SsmRsvpGroupTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#delete").on("click", function() {
				that.act(that.model.destroy());
				return false;
			});
		}

	});
});
