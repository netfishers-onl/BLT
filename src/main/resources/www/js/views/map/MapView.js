define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/NetworkModel',
	'models/network/RouterCollection',
	'models/network/LinkCollection',
	'models/network/RouterPositions',
	'text!templates/map/map.html',
	'text!templates/map/mapRouter.html',
	'views/map/RouterView',
	'views/map/LinkView',
	'bootstrap'
], function($, _, Backbone, jsPlumb, NetworkModel, RouterCollection, LinkCollection,
		RouterPositions, mapTemplate, routerTemplate, RouterView,
		LinkView) {

	return Backbone.View.extend({

		el: "#page",

		template: _.template(mapTemplate),
		routerTemplate: _.template(routerTemplate),

		initialize: function() {
			var that = this;
			this.pathConnections = [];
			this.network = new NetworkModel({
				id: this.id
			});
			this.routers = new RouterCollection([], {
				network: this.id
			});
			this.routers.on("add", this.onAddedRouter, this);
			this.routers.on("change", this.onChangedRouter, this);
			this.routers.on("destroy", this.onDestroyedRouter, this);
			this.links = new LinkCollection([], {
				network: this.id
			});
			this.links.on("add", this.onAddedLink, this);
			this.links.on("change", this.onChangedLink, this);
			this.links.on("destroy", this.onDestroyedLink, this);
			this.network.fetch().done(function() {
				jsPlumb.ready(function() {
					that.render();
				});
				that.refresh();
			});
		},
		
		refresh: function() {
			var that = this;
			that.routers.fetch().done(function() {
				that.links.fetch().done(function() {
					that.$("#map-toolbar #refresh").prop('disabled', false);
					that.disableDragging();
					if (typeof(that.routerView) == "object" && typeof(that.routerView.refresh) == "function") {
						that.routerView.refresh();
					}
				});
			});
		},
		
		onAddedRouter: function(router, routers) {
			var that = this;
			var item = this.routerTemplate(router.toJSON());
			var i = routers.indexOf(router);
			var x = (typeof(router.get('x')) == "undefined" || router.get('x') == 0 ?
					100 + (i % 2) * 150 : router.get('x'));
			var y = (typeof(router.get('y')) == "undefined" || router.get('y') == 0 ?
					100 + (i / 2) * 150 : router.get('y'));
			item = $(item).css({
				left: x + 'px',
				top: y + 'px'
			});
			this.$("#diagram").append(item);
			this.instance.draggable(this.$('#diagram .router'));
			this.$("#diagram .router").unbind('click').click(function() {
				that.$("#diagram .router").removeClass("selected");
				that.instance.select().removeClass("selected");
				$(this).addClass("selected");
				that.routerView = new RouterView({
					network: that.network,
					router: that.routers.get($(this).data('router')),
					mapView: that,
					onDelete: function() {
						that.routerView.close();
						that.refresh();
					}
				});
				return false;
			});
		},
		
		onChangedRouter: function(router) {
			var item = this.$("#diagram #router" + router.get('id'));
			if (router.get("lost")) {
				item.addClass("lost");
			}
			else {
				item.removeClass("lost");
			}
		},
		
		onDestroyedRouter: function(router) {
			var item = this.$("#diagram #router" + router.get('id'));
			item.remove();
		},
		
		onAddedLink: function(link, links) {
			var that = this;
			var source = null;
			var target = null;
			this.routers.each(function(router) {
				if (link.get('localRouter').identifier == router.get('routerId').identifier) {
					source = router;
				}
				if (link.get('remoteRouter').identifier == router.get('routerId').identifier) {
					target = router;
				}
			});
			if (source != null && target != null) {
				var found = false;
				that.instance.select({
					source: "router" + target.get('id'),
					target: "router" + source.get('id')
				}).each(function(connection) {
					if (_.isEqual(connection.getParameter('srcIp'), link.get('remoteAddress')) &&
							_.isEqual(connection.getParameter('dstIp'), link.get('localAddress'))) {
						found = true;
						connection.addOverlay(
							[ "Label", { id: 11, label: link.getShortName(), location: 0.95, cssClass: "linklabel" } ]
						);
						connection.setParameter("link2Id", link.get("id"));
					}
				});
				
				if (!found) {
					var connection = that.instance.connect({
						id: "link" + link.get('id'),
						source: "router" + source.get('id'),
						target: "router" + target.get('id'),
						anchor: "Continuous",
						connector: [ "StateMachine", { curviness: 10 } ],
						overlays: [
						  [ "Label", { 
							  id: 10,
							  label: link.getShortName(),
							  location: 0.1,
							  cssClass: "linklabel" 
						   } ],
						],
						cssClass: "link" + (link.isLost() ? " lost" : ""),
						paintStyle: { },
						hoverPaintStyle: { },
						parameters: {
							srcIp: link.get('localAddress'),
							dstIp: link.get('remoteAddress'),
							link1Id: link.get('id')
						}
					});
					connection.bind("click", function(c) {
						that.$("#diagram .router").removeClass("selected");
						that.instance.select().removeClass("selected");
						c.addClass("selected");
						that.routerView = new LinkView({
							network: that.network,
							link1: that.links.get(c.getParameter('link1Id')),
							link2: that.links.get(c.getParameter('link2Id')),
							mapView: that,
							onDelete: function() {
								that.routerView.close();
								that.refresh();
							}
						});
						return false;
					});
				}
			}
		},
		
		onChangedLink: function(link) {
			var that = this;
			var source = null;
			var target = null;
			this.routers.each(function(router) {
				if (link.get('localRouter').identifier == router.get('routerId').identifier) {
					source = router;
				}
				if (link.get('remoteRouter').identifier == router.get('routerId').identifier) {
					target = router;
				}
			});
			if (source != null && target != null) {
				that.instance.select({
					source: "router" + source.get('id'),
					target: "router" + target.get('id')
				}).each(function(connection) {
					if (_.isEqual(connection.getParameter('srcIp'), link.get('localAddress')) &&
							_.isEqual(connection.getParameter('dstIp'), link.get('remoteAddress'))) {
						if (link.isLost()) {
							connection.addClass("lost");
						}
						else {
							connection.removeClass("lost");
						}
					}
				});
			}
		},
		
		onDestroyedLink: function(link) {
			var that = this;
			var source = null;
			var target = null;
			this.routers.each(function(router) {
				if (link.get('localRouter').identifier == router.get('routerId').identifier) {
					source = router;
				}
				if (link.get('remoteRouter').identifier == router.get('routerId').identifier) {
					target = router;
				}
			});
			if (source != null && target != null) {
				that.instance.select({
					source: "router" + source.get('id'),
					target: "router" + target.get('id')
				}).each(function(connection) {
					if (_.isEqual(connection.getParameter('srcIp'), link.get('localAddress')) &&
							_.isEqual(connection.getParameter('dstIp'), link.get('remoteAddress'))) {
						that.instance.detach(connection);
					}
				});
			}
		},
		
		render: function() {
			var that = this;

			this.$el.show().html(this.template(this.network.toJSON()));
			this.instance = jsPlumb.getInstance({
				Endpoint: [
					"Dot", { radius: 2 }
				],
				HoverPaintStyle: {
					strokeStyle: "#1e8151",
					lineWidth: 2
				},
				Container: this.$("#diagram")
			});


			this.$("#map-toolbar #refresh").click(function() {
				that.$("#map-toolbar #refresh").prop('disabled', true);
				that.refresh();
				return false;
			}).prop('disabled', false);
			
			this.$("#map-toolbar #zoomin").click(function() {
				that.setZoom(that.zoom * 1.1);
				return false;
			});
			
			this.$("#map-toolbar #zoomout").click(function() {
				that.setZoom(that.zoom * 0.9);
				return false;
			});
			
			if (!window.user.isReadWrite()) {
				this.$("#map-toolbar #editmap").remove();
			}
			this.$("#map-toolbar #editmap").click(function() {
				that.enableDragging();
				that.$("#map-toolbar #group-refresh").hide();
				that.$("#map-toolbar #group-savemap").show();
				that.$("#map-toolbar #group-editmap").hide();
				that.$("#map-toolbar #group-tunnels").hide();
				return false;
			});
			
			this.$("#map-toolbar #savemap").click(function() {
				that.disableDragging();
				that.$("#map-toolbar #group-refresh").show();
				that.$("#map-toolbar #group-savemap").hide();
				that.$("#map-toolbar #group-editmap").show();
				that.$("#map-toolbar #group-tunnels").show();
				var positions = [];
				that.$("#diagram .router").each(function(i, item) {
					var x = $(item).css('left').replace(/px/, "");
					var y = $(item).css('top').replace(/px/, "");
					var id = $(item).data('router');
					positions.push({ router: id, x: x, y: y });
				});
				var routerPositions = new RouterPositions({
					positions: positions
				}, {
					network: that.id
				});
				routerPositions.save();
				return false;
			});
			that.$("#map-toolbar #group-savemap").hide();

			return this;
		},
		
		enableDragging: function() {
			var that = this;
			this.$('#diagram .router').each(function() {
				that.instance.setDraggable(this, true);
			});
			this.$('#diagrambox').addClass("dragging");
		},
		
		disableDragging: function() {
			var that = this;
			this.$('#diagram .router').each(function() {
				that.instance.setDraggable(this, false);
			});
			this.$('#diagrambox').removeClass("dragging");
		},
		
		zoom: 1,
		
		setZoom: function(z) {
			var cssZoom = "scale(" + z + ")";
			this.$("#diagram").css({
				"-webkit-transform": cssZoom,
				"-moz-transform": cssZoom,
				"-ms-transform": cssZoom,
				"-o-transform": cssZoom,
				"transform": cssZoom
			});
			jsPlumb.setZoom(z);
			this.zoom = z;
		},
		
		drawPath: function(params) {
			var that = this;
			var options = _.extend({
				name: "",
				origin: {},
				path: {},
				index: 0,
				firstLoose: false
			}, params);
			var sortedHops = [];
			var previousRouter = null;
			var text = "";
			if (typeof(options.path) == "object") {
				if (typeof(options.path.hops) == "object") {
					sortedHops = _.sortBy(options.path.hops, "key");
				}
				if (typeof(options.path.name) == "string" && !options.path.name.match(/^ *$/)) {
					text += '<span class="title">' + options.path.name + "</span> ";
				}
				else if (options.path.type == "LIVING") {
					text += '<span class="title computed">Computed</span> ';
				}
				else if (options.path.type == "DYNAMIC") {
					text += '<span class="title dynamic">Dynamic</span> ';
				}
			}
			if (typeof(options.origin) == "string") {
				previousRouter = this.routers.findRouterInterfaceByIp(options.origin).router;
			}
			if (typeof(options.origin.ip) == "string") {
				previousRouter = this.routers.findRouterInterfaceByIp(options.origin.ip).router;
			}
			else if (typeof(options.origin.identifier) != "undefined") {
				previousRouter = this.routers.findRouterByIdentifier(options.origin);
			}
			else {
				previousRouter = options.origin;
			}
			if (previousRouter != null) {
				text += "<strong>" + previousRouter.get("name") + "</strong>";
			}
			if (typeof(options.destination) == "object" && options.path.type == "DYNAMIC") {
				sortedHops.push({
					key: "Dyn.",
					value: {
						ipAddress: options.destination,
						type: "LOOSE"
					}
				});
			}
			for (var h in sortedHops) {
				var ip = sortedHops[h].value.ipAddress.ip;
				var router = this.routers.findRouterInterfaceByIp(ip).router;
				text += " ";
				if (sortedHops[h].value.type == "LOOSE" || options.firstLoose) {
					text += "&#151;?";
				}
				text += "&#8594; " + ip + "&nbsp;(<strong>" +
					(router == null ? "?" : router.get("name")) + "</strong>)";
				if (router != null && previousRouter != null) {
					var connection = that.instance.connect({
						id: "path" + sortedHops[h].key,
						source: "router" + previousRouter.get('id'),
						target: "router" + router.get('id'),
						anchor: "Continuous",
						connector: [ "StateMachine", { curviness: 10 } ],
						overlays: [
							[ "Arrow", { id: 0, location: 0.5, direction: 1, width: 12, length: 12, cssClass: "patharrow" } ],
							[ "Label", {
								id: 10,
								label: (options.index == 0 ? "" : options.index.toString() + ".") + sortedHops[h].key.toString(),
								location: 0.2 + (options.index % 6) * 0.1,
								cssClass: "pathlabel pathlabel" + options.index
							} ]
						],
						cssClass: "path path" + options.index +
							(sortedHops[h].value.type == "LOOSE" || options.firstLoose ? " loose" : ""),
						paintStyle: { },
						hoverPaintStyle: { },
						parameters: { }
					});
					that.pathConnections.push(connection);
					options.firstLoose = false;
				}
				previousRouter = router;
			}
			this.$("#diagrambox #legend").html(this.$("#diagrambox #legend").html() +
					'<div class="legend' + options.index + '">' + text + "</div>").show();
			this.drawnPath = options.name;
		},
		
		clearPath: function(name) {
			if (typeof(name) == "string" && name != this.drawnPath) {
				return;
			}
			this.$("#diagrambox #legend").html("").hide();
			for (c in this.pathConnections) {
				this.instance.detach(this.pathConnections[c]);
			}
			this.pathConnections = [];
		},
		
		showTarget: function(target) {
			var router = null;
			if (typeof(target) == "string") {
				router = this.routers.findRouterInterfaceByIp(target).router;
			}
			else if (typeof(target.ip) == "string") {
				router = this.routers.findRouterInterfaceByIp(target.ip).router;
			}
			else if (typeof(target.identifier) != "undefined") {
				router = this.routers.findRouterByIdentifier(target);
			}
			else {
				router = target;
			}
			if (router != null) {
				this.$("#diagrambox #router" + router.get("id")).addClass("target");
			}
		},
		
		clearTarget: function() {
			this.$("#diagrambox .router.target").removeClass("target");
		}

	});
});
