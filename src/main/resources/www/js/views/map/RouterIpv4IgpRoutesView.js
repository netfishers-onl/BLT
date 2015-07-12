define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/Ipv4IgpRouteCollection',
	'text!templates/map/routerIpv4IgpRoutes.html',
	'text!templates/map/ipv4IgpRouteRow.html',
	'bootstrap'
], function($, _, Backbone, jsPlumb, Ipv4IgpRouteCollection,
		routerIpv4IgpRoutesTemplate, ipv4IgpRouteRowTemplate
		) {

	return Backbone.View.extend({

		el: "#routerbox #section",

		template: _.template(routerIpv4IgpRoutesTemplate),
		ipv4IgpRouteRowTemplate: _.template(ipv4IgpRouteRowTemplate),

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
			this.refreshIpv4IgpRoutes();
		},
		
		refreshIpv4IgpRoutes: function() {
			var that = this;
			this.ipv4IgpRoutes = new Ipv4IgpRouteCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id'),
				//filter: "ethernet"
			});
			this.$("#prefixes>tbody").html("");
			this.ipv4IgpRoutes.fetch().done(function() {
				that.renderIpv4IgpRoutes();
			});
		},
		
		renderIpv4IgpRoutes: function() {
			var that = this;
			this.ipv4IgpRoutes.each(function(routerIpv4IgpRoute) {
				var data = routerIpv4IgpRoute.toJSON();
				data.intf = routerIpv4IgpRoute;
				that.$("#prefixes>tbody").append(that.ipv4IgpRouteRowTemplate(data));
			});
			if (!window.user.isReadWrite()) {
				this.$("#prefixes button").remove();
			}
		},
		
		/*getAge: function() {
			var diff = this.get('date') - Date.now();
			function msToTime(duration) {
			    var milliseconds = parseInt((duration%1000)/100)
			        , seconds = parseInt((duration/1000)%60)
			        , minutes = parseInt((duration/(1000*60))%60)
			        , hours = parseInt((duration/(1000*60*60))%24);
	
			    hours = (hours < 10) ? "0" + hours : hours;
			    minutes = (minutes < 10) ? "0" + minutes : minutes;
			    seconds = (seconds < 10) ? "0" + seconds : seconds;
	
			    return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
			}
			return msToTime(diff);
		}*/
	
	});
});
