define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/AddP2mpTeTunnelView',
	'models/network/InterfaceModel',
	'models/network/ExplicitPathCollection',
	'text!templates/map/editP2mpTeTunnel.html',
	'bootstrap'
], function($, _, Backbone, AddP2mpTeTunnelView, InterfaceModel, ExplicitPathCollection,
		editP2mpTeTunnelTemplate) {

	return AddP2mpTeTunnelView.extend({

		template: _.template(editP2mpTeTunnelTemplate),
		
		onCreate: function() {
			var that = this;
			this.addHandlers();
			this.$("#destination").trigger("change");
			var destinations = this.model.get("destinations");
			for (var d in destinations) {
				this.addDestination(destinations[d]);
			}
			
			this.$("#save").on("click", function() {
				var destinations = [];
				that.$("#destinations div.destination").each(function() {
					var destination = {
						destination: $(this).find("input.destination").val(),
						paths: []
					};
					$(this).find("select").each(function() {
						destination.paths.push($(this).val());
					});
					destinations.push(destination);
				});
				that.act(that.model.save({
					name: that.model.get("name"),
					description: that.$("#description").val(),
					shutdown: that.$("#shutdown").is(":checked"),
					multicastInterface: that.$("#multicast").is(":checked"),
					signalledBandwidth: that.$("#signalledbandwidth").val(),
					setupPriority: that.$("#setuppriority input").val(),
					holdPriority: that.$("#holdpriority input").val(),
					destinations: destinations
				}, { patch: true, type: "PUT" }));
				return false;
			});
		}

	});
});
