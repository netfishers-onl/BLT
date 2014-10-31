define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/InterfaceCollection',
	'text!templates/map/routerInterfaces.html',
	'text!templates/map/interfaceRow.html',
	'views/map/AddSubInterfaceView',
	'views/map/DeleteSubInterfaceView',
	'views/map/EditSubInterfaceView',
	'bootstrap'
], function($, _, Backbone, jsPlumb, InterfaceCollection,
		routerInterfacesTemplate, interfaceRowTemplate, AddSubInterfaceView,
		DeleteSubInterfaceView, EditSubInterfaceView) {

	return Backbone.View.extend({

		el: "#routerbox #section",

		template: _.template(routerInterfacesTemplate),
		interfaceRowTemplate: _.template(interfaceRowTemplate),

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
			this.refreshInterfaces();
		},
		
		refreshInterfaces: function() {
			var that = this;
			this.interfaces = new InterfaceCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id'),
				filter: "ethernet"
			});
			this.$("#interfaces>tbody").html("");
			this.interfaces.fetch().done(function() {
				that.renderInterfaces();
			});
		},
		
		renderInterfaces: function() {
			var that = this;
			this.interfaces.each(function(routerInterface) {
				var data = routerInterface.toJSON();
				data.intf = routerInterface;
				that.$("#interfaces>tbody").append(that.interfaceRowTemplate(data));
			});
			if (!window.user.isReadWrite()) {
				this.$("#interfaces button").remove();
			}
			this.$("#interfaces button.add").off().on("click", function() {
				var id = $(this).closest("tr").data("interface");
				var addView = new AddSubInterfaceView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					mother: that.interfaces.get(id)
				});
				return false;
			});
			this.$("#interfaces button.destroy").off().on("click", function() {
				var id = $(this).closest("tr").data("interface");
				var deleteView = new DeleteSubInterfaceView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					model: that.interfaces.get(id)
				});
				return false;
			});
			this.$("#interfaces button.edit").off().on("click", function() {
				var id = $(this).closest("tr").data("interface");
				var editView = new EditSubInterfaceView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					model: that.interfaces.get(id)
				});
				return false;
			});
		}
		

	});
});
