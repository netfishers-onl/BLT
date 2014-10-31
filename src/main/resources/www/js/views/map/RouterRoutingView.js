define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/Ipv4StaticRouteCollection',
	'models/network/InterfaceCollection',
	'models/network/IgmpStaticGroupModel',
	'models/network/Ipv4ReverseStaticRouteCollection',
	'models/network/Ipv4AccessListCollection',
	'models/network/Ipv4SsmRsvpGroupModel',
	'text!templates/map/routerRouting.html',
	'text!templates/map/ipv4StaticRouteRow.html',
	'text!templates/map/ipv4ReverseStaticRouteRow.html',
	'text!templates/map/igmpStaticGroupRow.html',
	'text!templates/map/ipv4SsmRsvpGroupRow.html',
	'views/map/AddStaticRouteView',
	'views/map/DeleteStaticRouteView',
	'views/map/AddReverseStaticRouteView',
	'views/map/DeleteReverseStaticRouteView',
	'views/map/AddIgmpStaticGroupView',
	'views/map/DeleteIgmpStaticGroupView',
	'views/map/AddSsmRsvpGroupView',
	'views/map/DeleteSsmRsvpGroupView',
	'bootstrap'
], function($, _, Backbone, jsPlumb, Ipv4StaticRouteCollection, InterfaceCollection,
		IgmpStaticGroupModel, Ipv4ReverseStaticRouteCollection,
		Ipv4AccessListCollection, Ipv4SsmRsvpGroupModel, routerRoutingTemplate,
		ipv4StaticRouteRowTemplate, ipv4StaticReverseRouteRowTemplate,
		igmpStaticGroupRowTemplate, ipv4SsmRsvpGroupRowTemplate,
		AddStaticRouteView, DeleteStaticRouteView,
		AddReverseStaticRouteView, DeleteReverseStaticRouteView,
		AddIgmpStaticGroupView, DeleteIgmpStaticGroupView,
		AddSsmRsvpGroupView, DeleteSsmRsvpGroupView) {

	return Backbone.View.extend({

		el: "#routerbox #section",

		template: _.template(routerRoutingTemplate),
		ipv4StaticRouteTemplate: _.template(ipv4StaticRouteRowTemplate),
		ipv4StaticReverseRouteTemplate: _.template(ipv4StaticReverseRouteRowTemplate),
		igmpStaticGroupTemplate: _.template(igmpStaticGroupRowTemplate),
		ipv4SsmRsvpGroupRowTemplate: _.template(ipv4SsmRsvpGroupRowTemplate),

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
			

			if (!window.user.isReadWrite()) {
				this.$("#addstaticroute").remove();
				this.$("#addreversestaticroute").remove();
				this.$("#addigmpstaticgroup").remove();
				this.$("#addipv4ssmrsvpgroup").remove();
			}
			this.$("#addstaticroute").off().on("click", function() {
				var addView = new AddStaticRouteView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				return false;
			});this.$("#addreversestaticroute").off().on("click", function() {
				var addView = new AddReverseStaticRouteView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				return false;
			});
			this.$("#addigmpstaticgroup").off().on("click", function() {
				var addView = new AddIgmpStaticGroupView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				return false;
			});
			this.$("#addipv4ssmrsvpgroup").off().on("click", function() {
				var addView = new AddSsmRsvpGroupView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				return false;
			});
			
			return this;
		},
		
		refresh: function() {
			this.refreshIpv4StaticRoutes();
			this.refreshIpv4ReverseStaticRoutes();
			this.refreshIgmpStaticGroups();
			this.refreshSsmRsvpGroups();
		},
		
		refreshIpv4StaticRoutes: function() {
			var that = this;
			this.ipv4StaticRoutes = new Ipv4StaticRouteCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			this.$("#ipv4staticroutes>tbody").html("");
			this.ipv4StaticRoutes.fetch().done(function() {
				that.renderIpv4StaticRoutes();
			});
		},
		
		renderIpv4StaticRoutes: function() {
			var that = this;
			this.ipv4StaticRoutes.each(function(ipv4StaticRoute) {
				var data = ipv4StaticRoute.toJSON();
				that.$("#ipv4staticroutes>tbody").append(that.ipv4StaticRouteTemplate(data));
			});
			if (!window.user.isReadWrite()) {
				this.$("#ipv4staticroutes button").remove();
			}
			this.$("#ipv4staticroutes button.destroy").off().on("click", function() {
				var id = $(this).closest("tr").data("route");
				var deleteView = new DeleteStaticRouteView({
					network: that.network,
					router: that.router,
					model: that.ipv4StaticRoutes.get(id),
					mapView: that.mapView
				});
				return false;
			});
		},
		
		refreshIgmpStaticGroups: function() {
			var that = this;
			this.igmpInterfaces = new InterfaceCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id'),
				filter: "withigmpstaticgroups"
			});
			this.$("#igmpstaticgroups>tbody").html("");
			this.igmpInterfaces.fetch().done(function() {
				that.renderIgmpStaticGroups();
			});
		},
		
		renderIgmpStaticGroups: function() {
			var that = this;
			this.igmpInterfaces.each(function(igmpInterface) {
				_.each(igmpInterface.get("staticGroups"), function(group) {
					var data = group;
					data.intf = igmpInterface.get("routerInterface");
					data.cid = igmpInterface.cid;
					that.$("#igmpstaticgroups>tbody").append(that.igmpStaticGroupTemplate(data));
				});
			});
			if (!window.user.isReadWrite()) {
				this.$("#igmpstaticgroups button").remove();
			}
			this.$("#igmpstaticgroups button.destroy").off().on("click", function() {
				var cid = $(this).closest("tr").data("igmp");
				var id = $(this).closest("tr").data("group");
				var igmpInterface = that.igmpInterfaces.get(cid);
				var data = _.findWhere(igmpInterface.get("staticGroups", { id: id }));
				data.intf = igmpInterface.get("routerInterface");
				var model = new IgmpStaticGroupModel(data, {
					network: that.network.get('id'),
					router: that.router.get('id'),
					intf: data.intf.id
				});
				var deleteView = new DeleteIgmpStaticGroupView({
					network: that.network,
					router: that.router,
					model: model,
					mapView: that.mapView
				});
				return false;
			});
		},
		

		refreshIpv4ReverseStaticRoutes: function() {
			var that = this;
			this.ipv4ReverseStaticRoutes = new Ipv4ReverseStaticRouteCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			this.$("#ipv4reversestaticroutes>tbody").html("");
			this.ipv4ReverseStaticRoutes.fetch().done(function() {
				that.renderIpv4ReverseStaticRoutes();
			});
		},
		
		renderIpv4ReverseStaticRoutes: function() {
			var that = this;
			this.ipv4ReverseStaticRoutes.each(function(ipv4ReverseStaticRoute) {
				var data = ipv4ReverseStaticRoute.toJSON();
				that.$("#ipv4reversestaticroutes>tbody").append(that.ipv4StaticReverseRouteTemplate(data));
			});
			if (!window.user.isReadWrite()) {
				this.$("#ipv4reversestaticroutes button").remove();
			}
			this.$("#ipv4reversestaticroutes button.destroy").off().on("click", function() {
				var id = $(this).closest("tr").data("route");
				var deleteView = new DeleteReverseStaticRouteView({
					network: that.network,
					router: that.router,
					model: that.ipv4ReverseStaticRoutes.get(id),
					mapView: that.mapView
				});
				return false;
			});
		},
		
		refreshSsmRsvpGroups: function() {
			var that = this;
			this.ipv4AccessLists = new Ipv4AccessListCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			this.$("#ipv4ssmrsvpgroups>tbody").html("");
			this.ipv4AccessLists.fetch().done(function() {
				that.renderSsmRsvpGroups();
			});
		},
		
		renderSsmRsvpGroups: function() {
			var that = this;
			var groups = {};
			var rsvpAcl = this.router.get("ipv4MulticastCoreTreeRsvpTeAcl");
			var ssmAcl = this.router.get("ipv4MulticastSsmRangeAcl");
			this.ipv4AccessLists.each(function(acl) {
				var entries = acl.get("entries");
				if (acl.get("name") == rsvpAcl || acl.get("name") == ssmAcl) {
					for (var e in entries) {
						var entry = entries[e];
						if (typeof(entry.destination) == "object" && entry.action == "PERMIT") {
							var ip = entry.destination.ip + "/" + entry.destination.prefixLength;
							if (typeof(groups[ip]) == "undefined") {
								groups[ip] = {
									ip: entry.destination,
									rsvp: false,
									ssm: false
								};
							}
							if (acl.get("name") == rsvpAcl) {
								groups[ip].rsvp = true;
							}
							if (acl.get("name") == ssmAcl) {
								groups[ip].ssm = true;
							}
							
						}
					}
				}
			});
			for (var ip in groups) {
				that.$("#ipv4ssmrsvpgroups>tbody").append(that.ipv4SsmRsvpGroupRowTemplate(groups[ip]));
			}
			if (!window.user.isReadWrite()) {
				this.$("#ipv4ssmrsvpgroups button").remove();
			}
			this.$("#ipv4ssmrsvpgroups button.destroy").off().on("click", function() {
				var ip = $(this).closest("tr").data("ip");
				var pl = $(this).closest("tr").data("pl");
				var group = new Ipv4SsmRsvpGroupModel({
					id: ip + "-" + pl,
					group: ip + "/" + pl
				}, {
					network: that.network.get('id'),
					router: that.router.get('id')
				});
				var deleteView = new DeleteSsmRsvpGroupView({
					network: that.network,
					router: that.router,
					model: group,
					mapView: that.mapView
				});
				return false;
			});
		}
		
	});
});
