define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/Ipv4SsmRsvpGroupModel',
	'text!templates/map/addIpv4SsmRsvpGroup.html',
	'bootstrap'
], function($, _, Backbone, Edit, Ipv4SsmRsvpGroupModel, addIpv4SsmRsvpGroupTemplate) {

	return Edit.extend({

		template: _.template(addIpv4SsmRsvpGroupTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#add").on("click", function() {
				var group = new Ipv4SsmRsvpGroupModel({
					group: that.$("#group").val(),
					ssm: that.$("#ssm, #rsvpssm").is(".active"),
					rsvp: that.$("#rsvp, #rsvpssm").is(".active")
				}, {
					network: that.network.get("id"),
					router: that.router.get("id")
				});
				that.act(group.save());
				return false;
			});
		}
		
	});
});
