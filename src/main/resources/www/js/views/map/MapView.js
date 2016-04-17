/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/DijkstraSinglePathCollection',
	'models/network/NetworkModel',
	'models/network/RouterCollection',
	'models/network/LinkCollection',
	'models/network/RouterPositions',
	'text!templates/map/map.html',
	'text!templates/map/mapRouter.html',
	'views/map/RouterView',
	'views/map/LinkView',
	'bootstrap'
], function($, _, Backbone, jsPlumb, DijkstraSinglePathCollection, NetworkModel, RouterCollection, LinkCollection,
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
			this.routers.on("remove", this.onDestroyedRouter, this);
			this.links = new LinkCollection([], {
				network: this.id
			});
			this.links.on("add", this.onAddedLink, this);
			this.links.on("change", this.onChangedLink, this);
			this.links.on("remove", this.onDestroyedLink, this);
			this.network.fetch().done(function() {
				jsPlumb.ready(function() {
					that.render();
				});
				that.refresh();
			});
			
			this.gmapsEnable = true;
			
			Backbone.history.on("route", function (route, router) {
				if (that.autoRefreshInt !== false && that.autoRefreshInt != null) {
					that.autoRefresh();
				}
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
			that.$("#diagrambox #legend").html("").hide();
			for (c in that.pathConnections) {
				that.instance.detach(that.pathConnections[c]);
			}
			that.pathConnections = [];
		},
		
		autoRefresh: function() {
			var that = this;
			if (this.autoRefreshInt === false || this.autoRefreshInt == null) {
				this.autoRefreshInt = setInterval(function() {
				  that.$("#map-toolbar #refresh").prop('disabled', true);
				  that.refresh();
				}, 1000 * 3);
			} else {
			clearInterval(this.autoRefreshInt);
				this.autoRefreshInt = false;
			}
		},
		
		onAddedRouter: function(router, routers) {
			var that = this;
			var item = this.routerTemplate(router.toJSON());
			var i = routers.indexOf(router);
			var step = 2*Math.PI / routers.length;
			var x = (typeof(router.get('x')) == "undefined" || router.get('x') == 0 ?
					600 + 500 * Math.cos(step*i) : router.get('x'));
			var y = (typeof(router.get('y')) == "undefined" || router.get('y') == 0 ?
					300 - 0.25 * 500 * Math.sin(step*i) : router.get('y'));
			item = $(item).css({
				left: x + 'px',
				top: y + 'px'
			});
			
			if (router.get('latitude') == 0 && router.get('longitude') == 0) {
				this.gmapsEnable = false ;
              	that.$("#map-toolbar #gotogmaps").hide();
			}
			
			this.$("#diagram").append(item);
			this.instance.draggable(this.$('#diagram .router'));
			this.$("#diagram .router").unbind('click').click(function() {
				that.$("#diagram .router").removeClass("selected");
				that.instance.select().removeClass("selected");
				that.$('#routerbox').hide();
				that.$("#diagrambox #legend").html("").hide();
				for (c in that.pathConnections) {
					that.instance.detach(that.pathConnections[c]);
				}
				that.pathConnections = [];
				$(this).addClass("selected");
				that.routerView = new RouterView({
					network: that.network,
					router: that.routers.get($(this).data('router')),
					mapView: that,
					onDelete: function() {
						that.routerView.close();
						delete that.routerView;
						that.refresh();
					}
				});
				return false;
			});
			this.$("#diagram .router").unbind('contextmenu').contextmenu(function() {
				that.$('#routerbox').hide();
              	var target = null;
              	$.each(that.$("#diagram .router"),function(item,div){
                  	var id = '#router'+ parseInt(item+1);
                  	if ($(id).hasClass("selected")) {target = parseInt(item+1);}
                });
              	if (target == null || target == that.routers.get($(this).data('router')).id) { 
                  return false;
                }
              	var dijkstraPath = new DijkstraSinglePathCollection([], {
					network: that.network.get('id'),
					origin: that.routers.get($(this).data('router')).id,
					target: target
				});
				
				dijkstraPath.fetch().done(function() {
					var randColor = 'rgb(' + (Math.floor(Math.random() * 155 + 100)) + 
      				',' + (Math.floor(Math.random() * 155 + 100)) + 
                    ',' + (Math.floor(Math.random() * 155 + 100)) + ')';
					var previousVertex = null;
                  	var path = dijkstraPath.toJSON();
                  	$.each(path[0].vertices,function(index,vertex) {
						if (vertex != null && previousVertex != null) {
							var common = {
								id: "dijkstraPath",
								source: "router" + previousVertex.id,
								target: "router" + vertex.id,
								anchor: "Continuous",
								connector: [ "StateMachine", { curviness: 10 } ],
								paintStyle:{ 
	                                  outlineColor:"#000000", outlineWidth:1,
	                                  strokeStyle:randColor, lineWidth:2 
	                            },
	                            hoverPaintStyle:{},
	                            overlays: [
	                                 		[ "Label", {
	   											id: "metric",
	   											label: ''+path[0].weight,
	   											location: 0.2 + (1 % 6) * 0.1,
	                                            cssClass: "linklabel",
	                                            labelStyle: {
	                                                 fillStyle: randColor,
	                                                 color: "#000000",
	                                                 padding: '0.3',
	                                                 borderWidth: 1
	                                            },
	   										}]
	                                   ],
	                            parameters: { }
							}
							if (index == path[0].vertices.length - 1 ) {
                              	var connection = that.instance.connect(
                                  	common,
                                  	{ overlays: [
										[ "Arrow", { id: 0, location: 0.5, direction: 1, width: 18, length: 18, 
											paintStyle: {lineWidth:1,strokeStyle: "#000000", fillStyle: randColor},
                                         } ],
                                         [ "Label", {
	   											id: "metric",
	   											label: ''+path[0].weight,
	   											location: 0.2 + (1 % 6) * 0.1,
	                                            cssClass: "linklabel",
	                                            labelStyle: {
	                                                 fillStyle: randColor,
	                                                 color: "#000000",
	                                                 padding: '0.3',
	                                                 borderWidth: 1
	                                            },
	   									}] 
									]}
                                );
							}
							else {
								var connection = that.instance.connect(common);
							}
                          	that.pathConnections.push(connection);
                        }
						previousVertex = vertex;	
                    });
					that.$("#diagrambox #legend").html(that.$("#diagrambox #legend").html() +
							'<div class="legend" style="color:' + randColor + 
							'"> Dijkstra shortest path from ' + 
                            path[0].vertices[0].name + ' to ' + path[0].targetname +
							" (" + path[0].weight + ")" + "</div>").show();
				});
				return false ;
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
			if (router.get("justAnnouncedAPrefix") && router.get("justWithdrawnAPrefix")) {
				item.addClass("haslossandnewpfx");
			}
			else {
				item.removeClass("haslossandnewpfx");
			}
			if (router.get("justAnnouncedAPrefix")) {
				item.addClass("hasanewprefix");
			}
			else {
				item.removeClass("hasanewprefix");
			}
			if (router.get("justWithdrawnAPrefix")) {
				item.addClass("haslostaprefix");
			}
			else {
				item.removeClass("haslostaprefix");
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
							_.isEqual(connection.getParameter('dstIp'), link.get('localAddress')) &&
							_.isEqual(connection.getParameter('protocolId'), link.get('protocolId'))) {
						found = true;
						connection.addOverlay(
							[ "Label", { id: 11, label: link.getMetric(), location: 0.90, cssClass: "linklabel" } ]
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
							  label: link.getMetric(),
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
							link1Id: link.get('id'),
							protocolId: link.get('protocolId')
						}
					});
					connection.bind("click", function(c, e) {
						that.$("#diagram .router").removeClass("selected");
						that.instance.select().removeClass("selected");
						that.$('#routerbox').hide();
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
						e.stopPropagation();
						e.preventDefault();
						return false;
					}, true);
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
							_.isEqual(connection.getParameter('dstIp'), link.get('remoteAddress')) &&
							_.isEqual(connection.getParameter('protocolId'), link.get('protocolId'))) {
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
			if (source != null || target != null) {
				var criteria = {};
				if (source != null) {
					criteria.source = "router" + source.get('id'); 
				}
				if (target != null) {
					criteria.target = "router" + target.get('id');
				}
				that.instance.select(criteria).each(function(connection) {
					if (_.isEqual(connection.getParameter('srcIp'), link.get('localAddress')) &&
							_.isEqual(connection.getParameter('dstIp'), link.get('remoteAddress')) &&
							_.isEqual(connection.getParameter('protocolId'), link.get('protocolId'))) {
						that.instance.detach(connection);
					}
				});
			}
		},
		
		render: function() {
			var that = this;
			
			this.$el.show().html(this.template(this.network.toJSON()));
			this.$el.on('click', function() {
				that.$("#diagram .router").removeClass("selected");
				that.instance.select().removeClass("selected");
				that.$('#routerbox').hide();
				return false;
			})
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
			
			this.$("#map-toolbar #autorefresh").click(function() {
				that.$("#map-toolbar #autorefresh").prop('disabled', true);
				that.autoRefresh();
				that.$("#map-toolbar #autorefresh").prop('disabled', false);
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
				if (that.autoRefreshInt !== false && that.autoRefreshInt != null) {
					that.autoRefresh();
				}
				
				that.$("#map-toolbar #group-refresh").hide();
				that.$("#map-toolbar #group-savemap").show();
				that.$("#map-toolbar #group-editmap").hide();
				return false;
			});
			
			this.$("#map-toolbar #savemap").click(function() {
				that.disableDragging();
				that.$("#map-toolbar #group-refresh").show();
				that.$("#map-toolbar #group-savemap").hide();
				that.$("#map-toolbar #group-editmap").show();
				var positions = [];
				that.$("#diagram .router").each(function(i, item) {
					var x = $(item).css('left').replace(/(\.\d+|)px/, "");
					var y = $(item).css('top').replace(/(\.\d+|)px/, "");
					var r = $(item).data('router');
					positions.push({ router: r, x: x, y: y });
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
			
			this.$("#map-toolbar #gotogmaps").click(function() {
				that.$("#map-toolbar #gotogmaps").prop('disabled', true);
				window.appRouter.navigate("gmaps/" + that.network.id, { trigger: true });
				return false;
			}).prop('disabled', false);
			
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
				
	});
});