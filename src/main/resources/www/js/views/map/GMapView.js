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
	//'models/network/InterfaceCollection',
	'models/network/Ipv4IgpRouteCollection',
	'text!templates/map/gmap.html',
	'text!templates/map/gmapRouter.html',
	'text!templates/map/routerInterfaces.html',
	'text!templates/map/routerIpv4IgpRoutes.html',
	'text!templates/map/interfaceRow.html',
	'text!templates/map/ipv4IgpRouteRow.html',
	'text!templates/map/link.html',
	//'views/map/RouterView',
	//'views/map/LinkView',
	'bootstrap',
	'infobox',
	'infobubble'
], function($, _, Backbone, NetworkModel, RouterCollection, LinkCollection, Ipv4IgpRouteCollection,  
		gMapTemplate, routerTemplate, interfacesTemplate, ipv4IgpRoutesTemplate, 
		interfaceRowTemplate, ipv4IgpRouteRowTemplate, linkTemplate) {
	
	return Backbone.View.extend({

		el: "#page",

		template: _.template(gMapTemplate),
		routerTemplate: _.template(routerTemplate),
		interfacesTemplate: _.template(interfacesTemplate),
		ipv4IgpRoutesTemplate: _.template(ipv4IgpRoutesTemplate),
		interfaceRowTemplate: _.template(interfaceRowTemplate),
		ipv4IgpRouteRowTemplate: _.template(ipv4IgpRouteRowTemplate),
		linkTemplate: _.template(linkTemplate),
		
		initialize: function(){
			var that = this ;
			
			this.myInfoBoxOptions = {
					disableAutoPan: true,
					maxWidth: 0,
					pixelOffset: new google.maps.Size(0, -20),
					zIndex: null,
					boxStyle: { 
						  background: 'rgb(255,255,255)',
						  color: 'rgb(10,10,10)',
						  'font-size': '13px',
						  opacity: 1,
						  width: "auto"
					},
					closeBoxMargin: "10px 2px 2px 2px",
					closeBoxURL: "",
					infoBoxClearance: new google.maps.Size(1, 1),
					isHidden: false,
					pane: "floatPane",
					enableEventPropagation: false
			};
			
			this.myInfoBubbleOptions = {
			      //maxWidth: 300, 
			      //maxHeight: 300,
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
			      //backgroundClassName: 'phoney',
			      arrowStyle: 2	
			};
			
			this.icon = {
				  	url: '../../../img/router_grey.svg',
					scaledSize: new google.maps.Size(30, 30),
					origin: new google.maps.Point(0,0),
					anchor: new google.maps.Point(20,20)
			};
			
			
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
					/*if (typeof(that.routerView) == "object" && typeof(that.routerView.refresh) == "function") {
						that.routerView.refresh();
					}*/
					
					// TODO: refresh the InfoBubble
				});
			});
		},
		
		autoRefresh: function() {
			var that = this;
			if (this.autoRefreshInt === false || this.autoRefreshInt == null) {
				this.autoRefreshInt = setInterval(function() {
				  that.$("#gmap-toolbar #refresh").prop('disabled', true);
				  that.refresh();
				}, 1000 * 3);
			} else {
			clearInterval(this.autoRefreshInt);
				this.autoRefreshInt = false;
			}
		},
		
		onAddedRouter: function(router, routers) {
			var that = this;
			
			var routerHtml = this.routerTemplate(router.toJSON());
			
			//var interfacesHtml = this.interfacesTemplate(router.toJSON());
          	var interfacesHtml = '<table><thead>'+
				'<tr><th>Interface</th><th>Description</th><th>IP address</th><th></th></tr></thead>';
			          
          	var itfArray = router.get('ipv4Interfaces');
          	$.each(itfArray, function(i,v) {
              		interfacesHtml += that.interfaceRowTemplate(v);
            }); 
          	interfacesHtml += '</table>';
          	
         	var ipv4IgpRoutesHtml = this.ipv4IgpRoutesTemplate(router.toJSON());
          
			this.ipv4IgpRoutes = new Ipv4IgpRouteCollection([], {
				network: this.network.get('id'),
				router: router.get('id')
			});
			//console.log(this.ipv4IgpRoutes);
			this.ipv4IgpRoutes.each(function(routerIpv4IgpRoute) {
				var now = Date.now();
				var data = routerIpv4IgpRoute.toJSON();
				data.intf = routerIpv4IgpRoute;
				
				var ageMonths = typeof(data.date) != 'undefined' ? parseInt(((now - data.date)/(1000*60*60*24*30))%12) : 0;
				var ageDays = typeof(data.date) != 'undefined' ? parseInt(((now - data.date)/(1000*60*60*24))%30) : 0;
				//console.log("data.date: "+data.date+" ageMonths: "+ageMonths+" ageDays: "+ageDays);
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
				
				var bgColor = '';
				if (data.justNew === true) {
					bgColor = 'bgcolor=#a2dfa3';
				}
				else if (data.justLost === true) {
					bgColor = 'bgcolor=#ffaeae';
				}
				data.bgColor = bgColor;
				
				ipv4IgpRoutesHtml.append(that.ipv4IgpRouteRowTemplate(data));
			});

			this.pos = new google.maps.LatLng(router.get('latitude'), router.get('longitude'));
	  		this.bounds.extend(this.pos);
	  		this.map.fitBounds(this.bounds);
	  		
	  		marker = new google.maps.Marker({
	    		position: this.pos,
	    		map: this.map,
	    		animation: google.maps.Animation.DROP,
	    		icon: this.icon
	    	});
          
          			
	  		this.mapsInfoBox2Router(marker,router.get('name'),this.map);
	  		this.mapsInfoBubble2Router(marker, routerHtml, interfacesHtml, ipv4IgpRoutesHtml, this.map);
	        		
		},
      	
		
		mapsInfoBox2Router: function(rtr, content, map) {
		    var infoBox = new InfoBox(this.myInfoBoxOptions);
		  	google.maps.event.addListener(rtr, 'mouseover', function() {
		  		infoBox.setContent(content);
		  		infoBox.open(map, rtr);
			});
			google.maps.event.addListener(rtr, 'mouseout', function() {
				infoBox.close(true);
			});
		},
		
		mapsInfoBubble2Router: function(m, rtr, itf, routes, map) {
			  //console.log("loads the function for:"+rtr);
			  var  infoBubble = new InfoBubble(this.myInfoBubbleOptions);
			  infoBubble.addTab('Router', rtr);
			  infoBubble.addTab('Interfaces', itf);
			  infoBubble.addTab('Prefixes', routes);
			  infoBubble.setTabActive(0);
			  /*infoBubble.updateTab(0,'Router', router_infos);
				infoBubble.updateTab(1,'Interfaces', interfaces);
				infoBubble.updateTab(2,'Prefixes', routes);*/
			  google.maps.event.addListener(m, 'click', function() {
		        infoBubble.open(map, m);
			  });	
		},		
		
		onChangedRouter: function(router, routers) {
		},
		
		onDestroyedRouter: function(router, routers) {
		},
		
		onAddedLink: function(link, links) {
			var that = this;
			var source = null;
			var target = null;
          
          	//console.log(link);
			
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
					strokeOpacity: 0.07,
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
				
				this.mapsInfoBox2Link(
						shadowLine, 
						'<small>'+link.get('protocolId')+" link between: <br>"+
						source.get('name')+" and: "+target.get('name')+'</small>', 
						this.map
				);
              	
				this.mapsInfoBubble2Link(shadowLine,linkContent, this.map);
			}
		},
		
		mapsInfoBox2Link: function(line, content, map) {
		  	var infoBox = new InfoBox(this.myInfoBoxOptions);
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
			  var  infoBubble = new InfoBubble(this.myInfoBubbleOptions);
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