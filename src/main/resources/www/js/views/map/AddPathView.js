define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/ExplicitPathModel',
	'text!templates/map/addPath.html',
	'text!templates/map/hop.html',
	'bootstrap',
	'typeahead'
], function($, _, Backbone, Edit, ExplicitPathModel, addPathTemplate, hopTemplate) {

	return Edit.extend({

		template: _.template(addPathTemplate),
		hopTemplate: _.template(hopTemplate),
		
		defaultHop: {
			value: {
				ipAddress: {
					ip: ""
				},
				type: "STRICT"
			}
		},
		
		routerInterfaces: [],
		
		hopIndex: 10,
		
		hopIncrement: 10,
		
		onCreate: function() {
			var that = this;
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
			this.$("#addhop").click();
			this.$("#add").on("click", function() {
				var hops = [];
				that.$(".hop").each(function() {
					var hop = $(this).data("hop-index") + " " + $(this).find(".hopip").val();
					if ($(this).find(".hoploose").prop("checked") == true) {
						hop += " loose";
					}
					hops.push(hop);
				});
				var path = new ExplicitPathModel({
					name: that.$("#pathname").val(),
					hops: hops
				}, {
					network: that.network.get("id"),
					router: that.router.get("id")
				});
				that.act(path.save());
				return false;
			});
		},
		
		addHop: function(data) {
			var that = this;
			var $html = $(that.hopTemplate(data));
			that.$("#hops").append($html);
			if (data.value.ipAddress.ip != "") {
				this.fillHopDescription($html.find("input.hopip"));
			}
			$html.find(".hopremove").on("click", function() {
				$(this).closest("div .form-group").remove();
				var max = 0;
				that.$("div.hop").each(function() {
					if ($(this).data("hop-index") > max) {
						max = $(this).data("hop-index");
					}
				});
				that.hopIndex = max + that.hopIncrement;
				that.drawPath();
				return false;
			});
			$html.find("input.hopip").on("change", function() {
				that.fillHopDescription($(this));
				that.drawPath();
			});
			$html.find("input.hopip").typeahead({
				source: function(query, process) {
					return that.routerInterfaces;
				},
				updater: function(item) {
					return item.replace(/ \(.*/, "");
				}
			});
		},
		
		fillHopDescription: function(item) {
			var that = this;
			var r = that.mapView.routers.findRouterInterfaceByIp(item.val());
			var d = "";
			if (r.router != null) {
				d = r.router.get("name") + ", " + r.intf.name;
			}
			item.closest(".hop").find(".hopdescription").text(d);
		},
		
		drawPath: function() {
			var path = {
				hops: []
			};
			this.$("div.hop").each(function() {
				var hop = {
					key: $(this).data("hop-index"),
					value: {
						ipAddress: {
							ip: $(this).find("input.hopip").val()
						}
					}
				};
				path.hops.push(hop);
			});
			this.mapView.clearPath("AddPath");
			this.mapView.drawPath({
				name: "AddPath",
				origin: this.router,
				path: path
			});
		},
		
		onClose: function() {
			this.mapView.clearPath("AddPath");
		}

	});
});
