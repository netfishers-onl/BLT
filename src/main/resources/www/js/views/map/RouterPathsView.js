define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'models/network/ExplicitPathCollection',
	'text!templates/map/routerPaths.html',
	'text!templates/map/pathRow.html',
	'views/map/AddPathView',
	'views/map/DeletePathView',
	'views/map/EditPathView',
	'bootstrap'
], function($, _, Backbone, jsPlumb, ExplicitPathCollection,
		routerPathsTemplate, pathRowTemplate, AddPathView, DeletePathView,
		EditPathView) {

	return Backbone.View.extend({

		el: "#routerbox #section",

		template: _.template(routerPathsTemplate),
		pathRowTemplate: _.template(pathRowTemplate),

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
				this.$("#addpath").remove();
			}
			this.$("#addpath").on("click", function() {
				var addView = new AddPathView({
					network: that.network,
					router: that.router,
					mapView: that.mapView
				});
				return false;
			});
			return this;
		},
		
		refresh: function() {
			this.refreshPaths();
		},
		
		refreshPaths: function() {
			var that = this;
			this.paths = new ExplicitPathCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			this.$("#paths>tbody").html("");
			this.paths.fetch().done(function() {
				that.renderPaths();
			});
			
		},
		
		renderPaths: function() {
			var that = this;
			this.paths.each(function(path) {
				that.$("#paths>tbody").append(that.pathRowTemplate(path.toJSON()));
			});
			if (!window.user.isReadWrite()) {
				this.$("#paths button").remove();
			}
			that.$("#paths>tbody tr").off().on("mouseover", function() {
				var path = that.paths.get($(this).data("path"));
				that.mapView.drawPath({
					name: "ShowPath",
					origin: that.router.get("routerId"),
					path: path.toJSON()
				});
			}).on("mouseout", function() {
				that.mapView.clearPath("ShowPath");
			});
			this.$("button.destroy").off().on("click", function() {
				var id = $(this).closest("tr").data("path");
				var deleteView = new DeletePathView({
					network: that.network,
					router: that.router,
					model: that.paths.get(id),
					mapView: that.mapView
				});
				return false;
			});
			this.$("button.edit").off().on("click", function() {
				var id = $(this).closest("tr").data("path");
				var editView = new EditPathView({
					network: that.network,
					router: that.router,
					model: that.paths.get(id),
					mapView: that.mapView
				});
				return false;
			});
		}
		

	});
});
