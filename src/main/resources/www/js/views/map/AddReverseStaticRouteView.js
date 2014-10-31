define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/Ipv4ReverseStaticRouteModel',
	'text!templates/map/addReverseStaticRoute.html',
	'bootstrap'
], function($, _, Backbone, Edit, Ipv4ReverseStaticRouteModel,
		addReverseStaticRouteTemplate) {

	return Edit.extend({

		template: _.template(addReverseStaticRouteTemplate),
		
		onCreate: function() {
			var that = this;
			this.$("#add").on("click", function() {
				var route = new Ipv4ReverseStaticRouteModel({
					subnet: that.$("#subnet").val(),
					next: that.$("#next").val()
				}, {
					network: that.network.get("id"),
					router: that.router.get("id")
				});
				that.act(route.save());
				return false;
			});
		}
		
	});
});
