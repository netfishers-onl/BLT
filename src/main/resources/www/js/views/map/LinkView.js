define([
	'jquery',
	'underscore',
	'backbone',
	'jsplumb',
	'text!templates/map/link.html',
	'bootstrap'
], function($, _, Backbone, jsPlumb, linkTemplate) {

	return Backbone.View.extend({

		el: "#routerbox",

		template: _.template(linkTemplate),

		initialize: function(options) {
			var that = this;
			this.network = options.network;
			this.link1 = options.link1;
			this.link2 = options.link2;
			this.mapView = options.mapView;
			this.render();
		},
		
		render: function() {
			var that = this;

			$("#editbox").hide();
			this.$el.html("").show();
			var data = {
				link1: this.link1.toJSON(),
				link2: this.link2.toJSON(),
				router1: this.mapView.routers.findRouterByIdentifier(this.link1.get("localRouter")).toJSON(),
				router2: this.mapView.routers.findRouterByIdentifier(this.link1.get("remoteRouter")).toJSON()
			};
			this.$el.html(this.template(data));

			this.$("#remove").off().on("click", function() {
				that.router.destroy().done(function() {
					that.onDelete();
				});
				return false;
			});

			
			return this;
		},
		
		close: function() {
			this.$el.html("");
		}
		

	});
});
