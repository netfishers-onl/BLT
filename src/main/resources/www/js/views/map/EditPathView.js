define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/AddPathView',
	'text!templates/map/editPath.html',
	'bootstrap',
	'typeahead'
], function($, _, Backbone, AddPathView, editPathTemplate) {

	return AddPathView.extend({

		template: _.template(editPathTemplate),
		
		onCreate: function() {
			var that = this;
			var sortedHops = _.sortBy(this.model.get("hops"), "key");
			var max = 0;
			var lastIncrement = 0;
			_.each(sortedHops, function(hop) {
				if (hop.key > max) {
					lastIncrement = hop.key - max;
					max = hop.key;
				}
				that.addHop(hop);
			});
			if (max > 0) {
				this.hopIndex = max + lastIncrement;
				this.hopIncrement = lastIncrement;
			}
			this.mapView.routers.each(function(router) {
				var ipv4Interfaces = router.get("ipv4Interfaces");
				for (var i in ipv4Interfaces) {
					that.routerInterfaces.push(
							ipv4Interfaces[i].ipv4Address.ip + " (" +
							router.get("name") + ", " + ipv4Interfaces[i].name + ")");
				}
			});
			this.$("#addhop").click(function() {
				var hop = _.clone(that.defaultHop);
				hop.key = that.hopIndex;
				that.hopIndex += that.hopIncrement;
				that.addHop(hop);
				return false;
			});
			this.$("#save").on("click", function() {
				var hops = [];
				that.$(".hop").each(function() {
					var hop = $(this).data("hop-index") + " " + $(this).find(".hopip").val();
					if ($(this).find(".hoploose").prop("checked") == true) {
						hop += " loose";
					}
					hops.push(hop);
				});
				that.act(that.model.save({
					hops: hops
				}, { patch: true, type: "PUT" }));
				return false;
			});
			this.drawPath();
		}

	});
});
