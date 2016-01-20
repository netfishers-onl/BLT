/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'models/network/NetworkModel',
	'models/network/RouterCollection',
	'models/network/LinkCollection',
	'models/network/InterfaceCollection',
	'models/network/Ipv4IgpRouteCollection',
	'text!templates/gmap/gmap.html',
	'text!templates/gmap/mapRouter.html',
	'text!templates/gmap/bubbleRouter.html',
	'text!templates/gmap/link.html',
	'text!templates/gmap/bubbleLink.html',
	'text!templates/gmap/bubbleInterfaces.html',
	'text!templates/gmap/bubbleIpv4IgpRoutes.html',
	'text!templates/gmap/bubbleInterfaceRow.html',
	'text!templates/gmap/bubbleIpv4IgpRouteRow.html',
	'bootstrap',
	'infobox',
	'infobubble'
], function($, _, Backbone, NetworkModel, RouterCollection, LinkCollection, InterfaceCollection, Ipv4IgpRouteCollection,  
		gMapTemplate, routerTemplate, bubbleRouterTemplate, linkTemplate, bubbleLinkTemplate, bubbleInterfacesTemplate,
		bubbleIpv4IgpRoutesTemplate, bubbleInterfaceRowTemplate, bubbleIpv4IgpRouteRowTemplate) {
	
	return Backbone.View.extend({

		el: "#page",

		template: _.template(gMapTemplate),
		routerTemplate: _.template(routerTemplate),
		bubbleRouterTemplate: _.template(bubbleRouterTemplate),
		linkTemplate: _.template(linkTemplate),
		bubbleLinkTemplate: _.template(bubbleLinkTemplate),
		bubbleInterfacesTemplate: _.template(bubbleInterfacesTemplate),
		bubbleIpv4IgpRoutesTemplate: _.template(bubbleIpv4IgpRoutesTemplate),
		bubbleInterfaceRowTemplate: _.template(bubbleInterfaceRowTemplate),
		bubbleIpv4IgpRouteRowTemplate: _.template(bubbleIpv4IgpRouteRowTemplate),
		
		defaultBoxOptions: {
			disableAutoPan: true,
			maxWidth: 0,
			pixelOffset: new google.maps.Size(0, -20),
			zIndex: null,
			boxStyle: { 
				background: 'rgb(255,255,255)',
				color: 'rgb(10,10,10)',
				'font-size': '13px',
				opacity: 0.9,
				width: "auto"
			},
			closeBoxMargin: "10px 2px 2px 2px",
			closeBoxURL: "",
			infoBoxClearance: new google.maps.Size(1, 1),
			isHidden: false,
			pane: "floatPane",
			enableEventPropagation: false
		},
		
		defaultRouterBubbleOptions: {
			maxHeight: 170,
			shadowStyle: 1,
			padding: 5,
			backgroundColor: 'rgb(220,220,220)',
			borderRadius: 2,
			arrowSize: 10,
			borderWidth: 1,
			borderColor: '#2c2c2c',
			disableAutoPan: true,
			hideCloseButton: false,
			closeSrc: 'img/fileclose12.png',
			arrowPosition: 20,
			arrowStyle: 2
		},
      	
      	defaultLinkBubbleOptions: {
			maxHeight: 170,
			shadowStyle: 1,
			padding: 5,
			borderRadius: 2,
			arrowSize: 10,
			borderWidth: 1,
			borderColor: '#2c2c2c',
			disableAutoPan: true,
			hideCloseButton: false,
			closeSrc: 'img/fileclose12.png',
			arrowPosition: 20,
			arrowStyle: 2
		},
		
		defaultRouterIcon: {
			url: '../../../img/router_grey.svg',
			scaledSize: new google.maps.Size(30, 30),
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(20, 20),
			animation: google.maps.Animation.DROP
		},
		newRouterIcon: {
			url: '../../../img/router_green.svg',
			scaledSize: new google.maps.Size(46, 46),
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(20, 20),
			animation: google.maps.Animation.DROP
		},
		lostRouterIcon: {
			url: '../../../img/router_red.svg',
			scaledSize: new google.maps.Size(46, 46),
			origin: new google.maps.Point(0, 0),
			anchor: new google.maps.Point(20, 20),
			animation: google.maps.Animation.DROP
		},
		
		
		initialize: function(){
			var that = this ;
			
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
			
			Backbone.history.on("route", function (route, router) {
				if (that.autoRefreshInt !== false && that.autoRefreshInt != null) {
					that.autoRefresh();
				}
			});
			this.mapOptions = {
				mapTypeId: google.maps.MapTypeId.TERRAIN
			};
			this.bounds = new google.maps.LatLngBounds();
			
			this.markersArray = [];
			this.linksArray = [];
			
			this.network.fetch().done(function() {
				that.render();
				that.refresh();
			});
		},
		
		refresh: function() {
			var that = this;
			that.routers.fetch().done(function() {
				that.links.fetch().done(function() {
					that.$("#gmap-toolbar #refresh").prop('disabled', false);
					that.routers.each(function(router) {
						marker = that.markersArray[router.get('id')];
						if (router.get("justAnnouncedAPrefix")) {
							marker.setIcon = this.newRouterIcon;
                          	marker.setAnimation(google.maps.Animation.DROP);
						}
						else if (router.get("justWithdrawnAPrefix")) {
							marker.setIcon = this.lostRouterIcon;
                          	marker.setAnimation(google.maps.Animation.DROP);
						}
						else {
							marker.setIcon = this.defaultRouterIcon;
                          	marker.setAnimation(google.maps.Animation.DROP);
						}
					});
				});
			});
		},
		
		autoRefresh: function() {
			var that = this;
			if (this.autoRefreshInt === false || this.autoRefreshInt == null) {
				this.autoRefreshInt = setInterval(function() {
					that.$("#gmap-toolbar #refresh").prop('disabled', true);
					that.refresh();
				}, 10000 * 2);
			} else {
				clearInterval(this.autoRefreshInt);
				this.autoRefreshInt = false;
			}
		},
		
		onAddedRouter: function(router, routers) {
			var that = this;
			var position = new google.maps.LatLng(router.get('latitude'), router.get('longitude'));
			this.bounds.extend(position);
			this.map.fitBounds(this.bounds);
			
			var marker = new google.maps.Marker({
				position: position,
				map: this.map,
				animation: google.maps.Animation.DROP,
				icon: this.defaultRouterIcon,
				routerId: router.get('id')
			});
			
			if (router.get("justAnnouncedAPrefix")) {
				marker.icon = this.newRouterIcon;
			}
			if (router.get("justWithdrawnAPrefix")) {
	           	marker.icon = this.lostRouterIcon;
			}
			
			this.markersArray[router.get('id')] = marker;
			var bubble = new InfoBubble(that.defaultRouterBubbleOptions);
			
			marker.addListener('click', function(e) {
				var marker = this;
				var router = that.routers.get(this.routerId);
				if (!router) return;
				var ipv4IgpRoutes = new Ipv4IgpRouteCollection([], {
					network: that.network.get('id'),
					router: router.get('id')
				});
				
				var updateBubble = false;
				
				ipv4IgpRoutes.fetch().done(function() {
					if (bubble.isOpen(bubble) || bubble.getContent(bubble)) {updateBubble = true;}
					
					if (!updateBubble) {bubble.addTab("Router", that.bubbleRouterTemplate(router.toJSON()));}
					else {bubble.updateTab(0,"Router", that.bubbleRouterTemplate(router.toJSON()));}
					
					var $interfacesTab = $("<div>").append(that.bubbleInterfacesTemplate(router.toJSON()));
					_.each(router.get("ipv4Interfaces"), function(routerInterface) {
						$interfacesTab.find("tbody").append($(that.bubbleInterfaceRowTemplate(routerInterface)));
					});
					
					if (!updateBubble) {bubble.addTab("Interfaces", $interfacesTab.html());}
					else {bubble.updateTab(1,"Interfaces", $interfacesTab.html());}
					
					var $routesTab = $("<div>").append(that.bubbleIpv4IgpRoutesTemplate(router.toJSON()));
					ipv4IgpRoutes.each(function(route) {
						var now = Date.now();
						var data = route.toJSON();
						
						var ageMonths = typeof(data.date) != 'undefined' ? parseInt(((now - data.date)/(1000*60*60*24*30))%12) : 0;
						var ageDays = typeof(data.date) != 'undefined' ? parseInt(((now - data.date)/(1000*60*60*24))%30) : 0;
						var age = '';
						if (ageMonths > 0) {
							age += ageMonths+"m ";
						}
						if (ageDays > 0) {
							age += ageDays+"d ";
						}
						age +=
							("00"+parseInt(((now - data.date)/(1000*60*60))%24)+":").slice(-3)+
							("00"+parseInt(((now - data.date)/(1000*60))%60)+":").slice(-3)+
							("00"+parseInt((now - data.date)/1000)%60).slice(-2);
						data.age = age;
						
						var rowClass = '';
						if (data.justNew === true) {
							rowClass = 'class=bubbleNew';
						}
						else if (data.justLost === true) {
							rowClass = 'class=bubbleLost';
						}
						data.rowClass = rowClass;
						
						$routesTab.find("tbody").append($(that.bubbleIpv4IgpRouteRowTemplate(data)));
					});
					if (!updateBubble) {bubble.addTab("Prefixes", $routesTab.html());}
					else {bubble.updateTab(2,"Prefixes", $routesTab.html());}
					
					bubble.open(that.map, marker);
				});
				
				
			});
			marker.addListener('mouseover', function() {
				var router = that.routers.get(this.routerId);
				if (!router) return;
				this.box = new InfoBox(that.defaultBoxOptions);
				this.box.setContent(that.routerTemplate(router.toJSON()));
				this.box.open(that.map, this);
			});
			marker.addListener('mouseout', function() {
				if (typeof this.box === "object") {
					this.box.close(true);
					delete(this.box);
				}
			});
		},
		
		onChangedRouter: function(router) {
			var marker = this.markersArray[router.get('id')];
			
			if (router.get("justAnnouncedAPrefix")) {
				marker.icon = this.newRouterIcon;
			}
			else if (router.get("justWithdrawnAPrefix")) {
				marker.icon = this.lostRouterIcon;
			}
			else {
				marker.icon = this.defaultRouterIcon;
			}
		},
		
		onDestroyedRouter: function(router) {
			var marker = this.markersArray[router.get('id')];
			marker.setMap(null);
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
				var path = [
					{lat: source.get('latitude'), lng: source.get('longitude')},
					{lat: target.get('latitude'), lng: target.get('longitude')}
				];
				var line = new google.maps.Polyline({
					path: path,
					strokeColor: "#38692C",
					strokeOpacity: 1,
					strokeWeight: 2,
					map: this.map
				});
				var shadowLine = new google.maps.Polyline({
					path: path,
					strokeColor: "#38692C",
					strokeOpacity: 0.05,
					strokeWeight: 20,
					map: this.map
				});
				
				var linkContent = 
				'<h6>'+link.get('protocolId')+'<small> link between: '+
				source.get('name')+' and: '+target.get('name')+'</small></h6>'+
				'<table style="text-align:left">'+
				'<tr><th></th><th>'+source.get('name')+'</th><th>'+target.get('name')+'</th></tr>'+
				'<tr><td>Interface</td><td>'+link.get('localInterfaceName')+'</td><td>'+link.get('remoteInterfaceName')+'</td></tr>'+
				'<tr><td>Description</td><td>'+link.get('localInterfaceDescription')+'</td><td>'+link.get('remoteInterfaceDescription')+'</td></tr>'+
				'<tr><td>Address</td><td>'+link.get('localAddress').ip+'</td><td>'+link.get('remoteAddress').ip+'</td></tr>'+                    
				'</table>';
				
				/*shadowLine.addListener('click', function(e) {
					this.bubble = new InfoBubble(that.defaultLinkBubbleOptions);
					this.bubble.setContent(that.bubbleLinkTemplate(link.toJSON()));
					this.bubble.setPosition(e.latLng);
					this.bubble.open(that.map);
				});
				shadowLine.addListener('mouseover', function(e) {
					this.box = new InfoBox(that.defaultBoxOptions);
					this.box.setContent(that.linkTemplate(link.toJSON()));
					this.box.setPosition(e.latLng);
					this.box.open(that.map);
				});
				shadowLine.addListener('mouseout', function() {
					if (typeof this.box === "object") {
						this.box.close(true);
						delete(this.box);
					}
				});*/
				
				this.mapsInfoBox2Link(
						shadowLine, 
						'<small>'+link.get('protocolId')+" link between: <br>"+
						source.get('name')+" and: "+target.get('name')+'</small>', 
						this.map
				);
				
				this.mapsInfoBubble2Link(shadowLine,linkContent, this.map);
			};
			
		},
		
		mapsInfoBox2Link: function(line, content, map) {
			var infoBox = new InfoBox(this.defaultBoxOptions);
			google.maps.event.addListener(line, 'mouseover', function(evt) {
				infoBox.setContent(content);
				infoBox.setPosition(evt.latLng);
				infoBox.open(map);
			});
			google.maps.event.addListener(line, 'mouseout', function(evt) {
				infoBox.close(true);
			});
		},
		
		mapsInfoBubble2Link: function(line, content, map) {
			var  infoBubble = new InfoBubble(this.defaultLinkBubbleOptions);
			google.maps.event.addListener(line, 'click', function(evt) {
				infoBubble.setContent(content);
				infoBubble.setPosition(evt.latLng);
				infoBubble.open(map);
			});	
		},
		
		onChangedLink: function(link) {
		},
		
		onDestroyedLink: function(link) {
		},
		
		render: function(){
			var that = this;
			
			this.$el.show().html(this.template());
			
			this.map = new google.maps.Map(document.getElementById('gmap'), this.mapOptions);
			
			this.$("#gmap-toolbar #refresh").click(function() {
				that.$("#gmap-toolbar #refresh").prop('disabled', true);
				that.refresh();
				return false;
			}).prop('disabled', false);
			this.$("#gmap-toolbar #autorefresh").click(function() {
				that.$("#gmap-toolbar #autorefresh").prop('disabled', true);
				that.autoRefresh();
				that.$("#map-toolbar #autorefresh").prop('disabled', false);
				return false;
			}).prop('disabled', false);	
			this.$("#gmap-toolbar #gotoblt").click(function() {
				that.$("#gmap-toolbar #gotoblt").prop('disabled', true);
				window.appRouter.navigate("map/" + that.network.id, { trigger: true });
				return false;
			}).prop('disabled', false);
			
			return this;
		}
	});
});