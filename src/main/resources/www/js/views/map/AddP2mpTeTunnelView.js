define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/InterfaceModel',
	'models/network/ExplicitPathCollection',
	'text!templates/map/addP2mpTeTunnel.html',
	'text!templates/map/pathOption.html',
	'text!templates/map/p2mpDestination.html',
	'bootstrap',
	'typeahead'
], function($, _, Backbone, Edit, InterfaceModel, ExplicitPathCollection, addP2mpTeTunnelTemplate,
		pathOptionTemplate, p2mpDestinationTemplate) {

	return Edit.extend({

		template: _.template(addP2mpTeTunnelTemplate),
		pathOptionTemplate: _.template(pathOptionTemplate),
		destinationTemplate: _.template(p2mpDestinationTemplate),
		
		defaultDestination: {
			destination: {
				ip: ""
			},
			paths: [{
				key: 10,
				value: {
					type: "DYNAMIC"
				}
			}]
		},
		
		defaultPathOption: {
			key: 10,
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
			this.$("#adddestination").on("click", function() {
				var destination = _.clone(that.defaultDestination);
				that.addDestination(destination);
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
		},
		
		onCreate: function() {
			var that = this;
			this.addHandlers();
			this.$("#adddestination").click();
			
			this.$("#add").on("click", function() {
				var destinations = [];
				that.$("#destinations div.destination").each(function() {
					var destination = {
						destination: $(this).find("input.destination").val(),
						paths: []
					};
					$(this).find("select").each(function() {
						destination.paths.push($(this).val());
					});
					destinations.push(destination);
				});
				var tunnel = new InterfaceModel({
					name: "tunnel-mte" + that.$("#tunnelid").val(),
					description: that.$("#description").val(),
					shutdown: that.$("#shutdown").is(":checked"),
					multicastInterface: that.$("#multicast").is(":checked"),
					signalledBandwidth: that.$("#signalledbandwidth").val(),
					setupPriority: that.$("#setuppriority input").val(),
					holdPriority: that.$("#holdpriority input").val(),
					destinations: destinations
				}, {
					network: that.network.get("id"),
					router: that.router.get("id")
				});
				that.act(tunnel.save());
				return false;
			});
			
		},
		
		addDestination: function(destination) {
			var that = this;
			var $html = $(this.destinationTemplate(destination));
			this.$("#destinations").append($html);
			
			$html.find(".destination").typeahead({
				source: function(query, process) {
					return that.routerInterfaces;
				},
				updater: function(item) {
					return item.replace(/ \(.*/, "");
				}
			});
			$html.find(".addpath").on("click", function() {
				var pathOption = _.clone(that.defaultPathOption);
				var $destination = $(this).closest("div.destination");
				that.addPathOption($destination, pathOption);
				return false;
			});
			$html.find(".destination").on("change", function() {
				that.mapView.clearTarget();
				var v = $(this).val();
				if (v.match(/[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+/)) {
					that.mapView.showTarget(v);
				}
			});
			$html.find(".removedestination").on("click", function() {
				$(this).closest("div.destination").remove();
				that.renumberDestinations();
				return false;
			});
			var sortedPaths = _.sortBy(destination.paths, "key");
			for (var p in sortedPaths) {
				this.addPathOption($html, sortedPaths[p]);
			}
			this.renumberDestinations();
		},
		
		addPathOption: function($destination, data) {
			var that = this;
			data.explicitPaths = this.explicitPaths.toJSON();
			var $html = $(that.pathOptionTemplate(data));
			$destination.find(".paths").append($html);
			$html.find(".pathremove").on("click", function() {
				$(this).closest("div .form-group").remove();
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
		
		renumberDestinations: function() {
			this.$("div.destination").each(function(index) {
				$(this).find(".destinationindex").text(index + 1);
			});
		},
		
		onClose: function() {
			this.mapView.clearPath("CreateTunnel");
			this.mapView.clearTarget();
		}

	});
});
