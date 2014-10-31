define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'text!templates/map/router.html',
	'views/map/RouterInterfacesView',
	'views/map/RouterPathsView',
	'views/map/RouterTeTunnelsView',
	'views/map/RouterRoutingView',
	'bootstrap'
], function($, _, Backbone, jsPlumb, routerTemplate, RouterInterfacesView, RouterPathsView,
		RouterTeTunnelsView, RouterRoutingView) {

	return Backbone.View.extend({

		el: "#routerbox",

		template: _.template(routerTemplate),

		initialize: function(options) {
			var that = this;
			this.network = options.network;
			this.router = options.router;
			this.mapView = options.mapView;
			this.onDelete = options.onDelete;
			this.render();
		},
		
		refresh: function() {
			if (typeof(this.sectionView.refresh) == "function") {
				this.sectionView.refresh();
			}
		},
		
		saveTab: function() {
			this.mapView.routerViewTab = this.$("#menu li.active a").attr("id");
		},
		
		loadTab: function() {
			var tab = "#goto-interfaces";
			if (typeof(this.mapView.routerViewTab) == "string") {
				tab = "#" + this.mapView.routerViewTab;
			}
			this.$(tab).click();
		},
		
		render: function() {
			var that = this;

			$("#editbox").hide();
			this.$el.html("").show();
			this.$el.html(this.template(this.router.toJSON()));
			
			this.$("#goto-interfaces").off('click').on('click', function() {
				that.sectionView = new RouterInterfacesView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				$(this).closest('li').siblings().removeClass("active");
				$(this).closest('li').addClass("active");
				that.saveTab();
				return false;
			});
			this.$("#goto-paths").off('click').on('click', function() {
				that.sectionView = new RouterPathsView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				$(this).closest('li').siblings().removeClass("active");
				$(this).closest('li').addClass("active");
				that.saveTab();
				return false;
			});
			this.$("#goto-tetunnels").off('click').on('click', function() {
				that.sectionView = new RouterTeTunnelsView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				$(this).closest('li').siblings().removeClass("active");
				$(this).closest('li').addClass("active");
				that.saveTab();
				return false;
			});
			this.$("#goto-routing").off('click').on('click', function() {
				that.sectionView = new RouterRoutingView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				$(this).closest('li').siblings().removeClass("active");
				$(this).closest('li').addClass("active");
				that.saveTab();
				return false;
			});
			this.$("#remove").off().on("click", function() {
				that.router.destroy().done(function() {
					that.onDelete();
				});
				return false;
			});
			
			this.loadTab();
			return this;
		},
		
		close: function() {
			this.$el.html("");
		}
		

	});
});
