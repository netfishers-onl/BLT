define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/InterfaceCollection',
	'text!templates/map/routerTeTunnels.html',
	'text!templates/map/p2pTeTunnelRow.html',
	'text!templates/map/p2mpTeTunnelRow.html',
	'views/map/AddP2pTeTunnelView',
	'views/map/EditP2pTeTunnelView',
	'views/map/DeleteTeTunnelView',
	'views/map/AddP2mpTeTunnelView',
	'views/map/EditP2mpTeTunnelView',
	'bootstrap'
], function($, _, Backbone, jsPlumb, InterfaceCollection,
		routerTeTunnelsTemplate, p2pTeTunnelRowTemplate,
		p2mpTeTunnelRowTemplate, AddP2pTeTunnelView, EditP2pTeTunnelView,
		DeleteTeTunnelView,
		AddP2mpTeTunnelView, EditP2mpTeTunnelView) {

	return Backbone.View.extend({

		el: "#routerbox #section",

		template: _.template(routerTeTunnelsTemplate),
		p2pTeTunnelRowTemplate: _.template(p2pTeTunnelRowTemplate),
		p2mpTeTunnelRowTemplate: _.template(p2mpTeTunnelRowTemplate),

		initialize: function(options) {
			var that = this;
			this.network = options.network;
			this.router = options.router;
			this.mapView = options.mapView;
			this.render();
		},
		
		render: function() {
			var that = this;

			this.$el.html("");
			this.$el.html(this.template(this.router.toJSON()));
			
			if (!window.user.isReadWrite()) {
				this.$("#addp2ptetunnel").remove();
				this.$("#addp2mptetunnel").remove();
			}
			this.$("#addp2ptetunnel").click(function() {
				var addView = new AddP2pTeTunnelView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				return false;
			});
			this.$("#addp2mptetunnel").click(function() {
				var addView = new AddP2mpTeTunnelView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				return false;
			});
			
			this.refresh();
			return this;
		},
		
		refresh: function() {
			this.refreshP2pTeTunnels();
			this.refreshP2mpTeTunnels();
		},
		
		refreshP2pTeTunnels: function() {
			var that = this;
			this.p2ptetunnels = new InterfaceCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id'),
				filter: "p2pte"
			});
			this.$("#p2ptetunnels>tbody").html("");
			this.p2ptetunnels.fetch().done(function() {
				that.renderP2pTeTunnels();
			});
		},
		
		renderP2pTeTunnels: function() {
			var that = this;
			this.p2ptetunnels.each(function(routerInterface) {
				var data = routerInterface.toJSON();
				data.intf = routerInterface;
				that.$("#p2ptetunnels>tbody").append(that.p2pTeTunnelRowTemplate(data));
			});
			if (!window.user.isReadWrite()) {
				this.$("#p2ptetunnels button").remove();
			}
			this.$("#p2ptetunnels>tbody tr").off().on("mouseover", function() {
				that.mapView.clearPath();
				var tunnel = that.p2ptetunnels.get($(this).data("tunnel"));
				var destination = tunnel.get("destination");
				if (typeof(destination) == "object") {
					var path = {};
					var title = "";
					if (destination.paths.length > 0) {
						var sortedPaths = _.sortBy(destination.paths, "key");
						path = sortedPaths[0].value;
					}
					var head = that.router.get("routerId");
					if (typeof(tunnel.get("head")) == "object") {
						head = tunnel.get("head");
					}
					that.mapView.drawPath({
						name: "ShowP2P",
						origin: head,
						destination: destination.destination,
						path: path,
						firstLoose: (tunnel.get("routerRole") != "HEAD")
					});
					that.mapView.showTarget(destination.destination);
				}
			}).on("mouseout", function() {
				that.mapView.clearPath("ShowP2P");
				that.mapView.clearTarget();
			});
			this.$("#p2ptetunnels button.destroy").off().on("click", function() {
				var id = $(this).closest("tr").data("tunnel");
				var deleteView = new DeleteTeTunnelView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					model: that.p2ptetunnels.get(id)
				});
				return false;
			});
			this.$("#p2ptetunnels button.edit").off().on("click", function() {
				var id = $(this).closest("tr").data("tunnel");
				var editView = new EditP2pTeTunnelView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					model: that.p2ptetunnels.get(id)
				});
				return false;
			});
		},
		
		refreshP2mpTeTunnels: function() {
			var that = this;
			this.p2mptetunnels = new InterfaceCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id'),
				filter: "p2mpte"
			});
			this.$("#p2mptetunnels>tbody").html("");
			this.p2mptetunnels.fetch().done(function() {
				that.renderP2mpTeTunnels();
			});
		},
		
		renderP2mpTeTunnels: function() {
			var that = this;
			this.p2mptetunnels.each(function(routerInterface) {
				var data = routerInterface.toJSON();
				data.intf = routerInterface;
				that.$("#p2mptetunnels>tbody").append(that.p2mpTeTunnelRowTemplate(data));
			});
			if (!window.user.isReadWrite()) {
				this.$("#p2mptetunnels button").remove();
			}
			that.$("#p2mptetunnels>tbody tr").off().on("mouseover", function() {
				that.mapView.clearPath();
				var tunnel = that.p2mptetunnels.get($(this).data("tunnel"));
				var destinations = tunnel.get("destinations");
				for (var d = 0; d < destinations.length; d++) {
					var destination = destinations[d];
					if (typeof(destination) == "object") {
						var path = {};
						if (destination.paths.length > 0) {
							var sortedPaths = _.sortBy(destination.paths, "key");
							path = sortedPaths[0].value;
						}
						var head = that.router.get("routerId");
						if (typeof(tunnel.get("head")) == "object") {
							head = tunnel.get("head");
						}
						that.mapView.drawPath({
							name: "ShowP2MP",
							origin: head,
							destination: destination.destination,
							path: path,
							index: d + 1,
							firstLoose: (tunnel.get("routerRole") != "HEAD")
						});
						that.mapView.showTarget(destination.destination);
					}
				}
			}).on("mouseout", function() {
				that.mapView.clearPath("ShowP2MP");
				that.mapView.clearTarget();
			});
			this.$("#p2mptetunnels button.destroy").off().on("click", function() {
				var id = $(this).closest("tr").data("tunnel");
				var deleteView = new DeleteTeTunnelView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					model: that.p2mptetunnels.get(id)
				});
				return false;
			});
			this.$("#p2mptetunnels button.edit").off().on("click", function() {
				var id = $(this).closest("tr").data("tunnel");
				var editView = new EditP2mpTeTunnelView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					model: that.p2mptetunnels.get(id)
				});
				return false;
			});
		}

	});
});
