define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/InterfaceModel',
	'models/network/ExplicitPathCollection',
	'text!templates/map/addP2pTeTunnel.html',
	'text!templates/map/pathOption.html',
	'bootstrap',
	'typeahead'
], function($, _, Backbone, Edit, InterfaceModel, ExplicitPathCollection, addP2pTeTunnelTemplate,
		pathOptionTemplate) {

	return Edit.extend({

		template: _.template(addP2pTeTunnelTemplate),
		pathOptionTemplate: _.template(pathOptionTemplate),
		
		pathIndex: 10,
		pathIncrement: 10,
		
		defaultPathOption: {
			value: {
				type: "DYNAMIC"
			}
		},
		
		initialize: function(options) {
			var that = this;
			_.extend(this, options);
			this.explicitPaths = new ExplicitPathCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			this.explicitPaths.fetch().done(function() {
				that.render();
			});
		},
		
		addHandlers: function() {
			var that = this;
			this.routerInterfaces = [];
			this.mapView.routers.each(function(router) {
				var ipv4Interfaces = router.get("ipv4Interfaces");
				for (var i in ipv4Interfaces) {
					that.routerInterfaces.push(
							ipv4Interfaces[i].ipv4Address.ip + " (" +
							router.get("name") + ", " + ipv4Interfaces[i].name + ")");
				}
			});
			this.$("#destination").typeahead({
				source: function(query, process) {
					return that.routerInterfaces;
				},
				updater: function(item) {
					return item.replace(/ \(.*/, "");
				}
			});
			this.$("#addpath").on("click", function() {
				var pathOption = _.clone(that.defaultPathOption);
				pathOption.key = that.pathIndex;
				that.pathIndex += that.pathIncrement;
				that.addPathOption(pathOption);
				return false;
			});
			this.$(".spinner button.more").on("click", function() {
				var $input = $(this).closest(".spinner").find("input");
				var v = $input.val();
				v = parseInt(v);
				if (!isNaN(v)) {
					v++;
					if (v > 7) v = 7;
					$input.val(v);
				}
				return false;
			});
			this.$(".spinner button.less").on("click", function() {
				var $input = $(this).closest(".spinner").find("input");
				var v = $input.val();
				v = parseInt(v);
				if (!isNaN(v)) {
					v--;
					if (v < 0) v = 0;
					$input.val(v);
				}
				return false;
			});
			this.$(".spinner input").on("change", function() {
				var v = $(this).val();
				v = parseInt(v);
				if (isNaN(v) || v < 0 || v > 7) {
					$(this).val("5");
				}
				return false;
			});
			this.$("#destination").on("change", function() {
				that.mapView.clearTarget();
				var v = $(this).val();
				if (v.match(/[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+/)) {
					that.mapView.showTarget(v);
				}
			});
			
		},
		
		onCreate: function() {
			var that = this;
			this.addHandlers();
			this.$("#addpath").click();
			
			this.$("#add").on("click", function() {
				var paths = [];
				that.$("#paths div.pathoption").each(function() {
					paths.push($(this).find("select").val());
				});
				var tunnel = new InterfaceModel({
					name: "tunnel-te" + that.$("#tunnelid").val(),
					description: that.$("#description").val(),
					shutdown: that.$("#shutdown").is(":checked"),
					multicastInterface: that.$("#multicast").is(":checked"),
					signalledBandwidth: that.$("#signalledbandwidth").val(),
					setupPriority: that.$("#setuppriority input").val(),
					holdPriority: that.$("#holdpriority input").val(),
					destination: {
						destination: that.$("#destination").val(),
						paths: paths
					}
				}, {
					network: that.network.get("id"),
					router: that.router.get("id")
				});
				that.act(tunnel.save());
				return false;
			});
			
		},
		
		addPathOption: function(data) {
			var that = this;
			data.explicitPaths = this.explicitPaths.toJSON();
			var $html = $(that.pathOptionTemplate(data));
			that.$("#paths").append($html);
			$html.find(".pathremove").on("click", function() {
				$(this).closest("div .form-group").remove();
				var max = 0;
				that.$("div.hop").each(function() {
					if ($(this).data("path-index") > max) {
						max = $(this).data("path-index");
					}
				});
				that.pathIndex = max + that.pathIncrement;
				return false;
			});
			$html.on("mouseover", function() {
				var id = $(this).find("option:selected").data("path");
				var path = that.explicitPaths.get(id);
				if (path != null) {
					that.mapView.drawPath({
						name: "CreateTunnel",
						origin: that.router.get("routerId"),
						path: path.toJSON()
					});
				}
			}).on("mouseout", function() {
				that.mapView.clearPath("CreateTunnel");
			});
		},
		
		onClose: function() {
			this.mapView.clearPath("CreateTunnel");
			this.mapView.clearTarget();
		}

	});
});
