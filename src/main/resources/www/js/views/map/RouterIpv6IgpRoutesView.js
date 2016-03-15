/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/Ipv6IgpRouteCollection',
	'text!templates/map/routerIpv6IgpRoutes.html',
	'text!templates/map/ipv6IgpRouteRow.html',
	'bootstrap'
], function($, _, Backbone, jsPlumb, Ipv6IgpRouteCollection,
		routerIpv6IgpRoutesTemplate, ipv6IgpRouteRowTemplate
		) {

	return Backbone.View.extend({

		el: "#routerbox #section",

		template: _.template(routerIpv6IgpRoutesTemplate),
		ipv6IgpRouteRowTemplate: _.template(ipv6IgpRouteRowTemplate),

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
			this.refresh();
			return this;
		},
		
		refresh: function() {
			this.refreshIpv6IgpRoutes();
		},
		
		refreshIpv6IgpRoutes: function() {
			var that = this;
			this.ipv6IgpRoutes = new Ipv6IgpRouteCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id'),
				//filter: "ethernet"
			});
			this.$("#prefixes6>tbody").html("");
			this.ipv6IgpRoutes.fetch().done(function() {
				that.renderIpv6IgpRoutes();
			});
		},
		
		renderIpv6IgpRoutes: function() {
			var that = this;
			this.ipv6IgpRoutes.each(function(routerIpv6IgpRoute) {
				var now = Date.now();
				var data = routerIpv6IgpRoute.toJSON();
				data.intf = routerIpv6IgpRoute;
				
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
				
				var bgColor = '';
				if (data.justNew === true) {
					bgColor = 'bgcolor=#a2dfa3';
				}
				else if (data.justLost === true) {
					bgColor = 'bgcolor=#ffaeae';
				}
				data.bgColor = bgColor;
				
				that.$("#prefixes6>tbody").append(that.ipv6IgpRouteRowTemplate(data));
			});
			if (!window.user.isReadWrite()) {
				this.$("#prefixes6 button").remove();
			}
		},	
	});
});
