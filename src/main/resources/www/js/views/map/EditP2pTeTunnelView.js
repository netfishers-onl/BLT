define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/AddP2pTeTunnelView',
	'models/network/InterfaceModel',
	'models/network/ExplicitPathCollection',
	'text!templates/map/editP2pTeTunnel.html',
	'bootstrap'
], function($, _, Backbone, AddP2pTeTunnelView, InterfaceModel, ExplicitPathCollection,
		editP2pTeTunnelTemplate) {

	return AddP2pTeTunnelView.extend({

		template: _.template(editP2pTeTunnelTemplate),
		
		onCreate: function() {
			var that = this;
			this.addHandlers();
			this.$("#destination").trigger("change");
			var destination = this.model.get("destination");
			if (typeof(destination) == "object") {
				var sortedPaths = _.sortBy(destination.paths, "key");
				for (var p in sortedPaths) {
					this.addPathOption(sortedPaths[p]);
				}
			}
			
			
			this.$("#save").on("click", function() {
				var paths = [];
				that.$("#paths div.pathoption").each(function() {
					paths.push($(this).find("select").val());
				});
				that.act(that.model.save({
					name: that.model.get("name"),
					description: that.$("#description").val(),
					shutdown: that.$("#shutdown").is(":checked"),
					multicastInterface: that.$("#multicast").is(":checked"),
					signalledBandwidth: that.$("#signalledbandwidth").val(),
					setupPriority: that.$("#setuppriority input").val(),
					holdPriority: that.$("#holdpriority input").val(),
					destination: {
						destination: that.$("#destination").val(),
						paths: paths
					}
				}, { patch: true, type: "PUT" }));
				return false;
			});
		}

	});
});
