define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/Ipv4StaticRouteModel',
	'text!templates/map/addStaticRoute.html',
	'bootstrap'
], function($, _, Backbone, Edit, Ipv4StaticRouteModel, addStaticRouteTemplate) {

	return Edit.extend({

		template: _.template(addStaticRouteTemplate),
		
		onCreate: function() {
			var that = this;

			this.routerInterfaces = [];
			this.neighborInterfaces = [];
			var ipRgx = /([0-9]+)\.([0-9]+)\.([0-9]+)\.([0-9]+)/;
			this.mapView.routers.each(function(router) {
				if (router.get("id") == that.router.get("id")) {
					return;
				}
				var ipv4Interfaces = router.get("ipv4Interfaces");
				for (var i in ipv4Interfaces) {
					var m1 = ipRgx.exec(ipv4Interfaces[i].ipv4Address.ip);
					var a1 = 0;
					if (m1) {
						a1 = parseInt(m1[1]) << 24 | parseInt(m1[2]) << 16 | parseInt(m1[3]) << 8 | parseInt(m1[4]);
					}
					var otherInterfaces = that.router.get("ipv4Interfaces");
					for (var j in otherInterfaces) {
						var m2 = ipRgx.exec(otherInterfaces[j].ipv4Address.ip);
						var mask = (1 << (32 - otherInterfaces[j].ipv4Address.prefixLength)) - 1;
						mask = ~mask;
						if (m2) {
							var a2 = parseInt(m2[1]) << 24 | parseInt(m2[2]) << 16 | parseInt(m2[3]) << 8 | parseInt(m2[4]);
							if (((a1 & mask) == (a2 & mask)) && a1 != 0) {
								that.neighborInterfaces.push(
										ipv4Interfaces[i].ipv4Address.ip +
										" (" + router.get("name") + ", " + ipv4Interfaces[i].name + ")");
							}
						}
					}
					that.routerInterfaces.push(
							ipv4Interfaces[i].ipv4Address.ip + "/" +
							ipv4Interfaces[i].ipv4Address.prefixLength +
							" (" + router.get("name") + ", " + ipv4Interfaces[i].name + ")");
				}
			});
			this.$("#subnet").typeahead({
				source: function(query, process) {
					return that.routerInterfaces;
				},
				updater: function(item) {
					item = item.replace(/ \(.*/, "");
					var match = /([0-9]+)\.([0-9]+)\.([0-9]+)\.([0-9]+)\/([0-9]+)/.exec(item);
					if (match) {
						var a = parseInt(match[1]) << 24 | parseInt(match[2]) << 16 |
							parseInt(match[3]) << 8 | parseInt(match[4]);
						var l = parseInt(match[5]);
						var m = (1 << (32 - l)) - 1;
						a = a & ~m;
						var i = ((a >> 24) & 255) + "." + ((a >> 16) & 255) + "." + ((a >> 8) & 255) + "." + (a & 255);
						return i + "/" + l;
					}
					return item;
				}
			});
			this.$("#next").typeahead({
				source: function(query, process) {
					return that.neighborInterfaces;
				},
				updater: function(item) {
					return item.replace(/ \(.*/, "");
				}
			});
			
			this.$("#add").on("click", function() {
				var route = new Ipv4StaticRouteModel({
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
