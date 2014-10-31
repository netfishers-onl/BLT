define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/InterfaceModel',
	'models/network/Ipv4AccessListCollection',
	'models/network/PolicyMapCollection',
	'text!templates/map/editSubInterface.html',
	'bootstrap'
], function($, _, Backbone, Edit, InterfaceModel, Ipv4AccessListCollection,
		PolicyMapCollection, editSubInterfaceTemplate) {

	return Edit.extend({

		template: _.template(editSubInterfaceTemplate),
		
		initialize: function(options) {
			var that = this;
			_.extend(this, options);
			this.ipv4AccessLists = new Ipv4AccessListCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			this.policyMaps = new PolicyMapCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			$.when(this.ipv4AccessLists.fetch(), this.policyMaps.fetch()).done(function() {
				that.render();
			});
		},
		
		templateData: function() {
			var data = {
				network: this.network.toJSON(),
				router: this.router.toJSON(),
				ipv4AccessLists: this.ipv4AccessLists.toJSON(),
				policyMaps: this.policyMaps.toJSON()
			};
			if (typeof(this.model) == "object") {
				data = _.extend(data, this.model.toJSON());
			}
			return data;
		},
		
		onCreate: function() {
			var that = this;
			this.$("#save").on("click", function() {
				that.act(that.model.save({
					name: that.model.get("name"),
					description: that.$("#description").val(),
					ipv4Address: that.$("#ipv4address").val(),
					shutdown: that.$("#shutdown").is(":checked"),
					multicastInterface: that.$("#multicast").is(":checked"),
					inQoS: that.$("#inqos").val(),
					inAcl: that.$("#inacl").val()
				}, { patch: true, type: "PUT" }));
				return false;
			});
		}

	});
});
