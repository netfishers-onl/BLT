define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/InterfaceModel',
	'models/network/Ipv4AccessListCollection',
	'models/network/PolicyMapCollection',
	'text!templates/map/addSubInterface.html',
	'bootstrap'
], function($, _, Backbone, Edit, InterfaceModel, Ipv4AccessListCollection,
		PolicyMapCollection, addSubInterfaceTemplate) {

	return Edit.extend({

		template: _.template(addSubInterfaceTemplate),
		
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
				policyMaps: this.policyMaps.toJSON(),
				mother: this.mother.toJSON()
			};
			if (typeof(this.model) == "object") {
				data = _.extend(data, this.model.toJSON());
			}
			return data;
		},
		
		onCreate: function() {
			var that = this;
			this.$("#add").on("click", function() {
				var intf = new InterfaceModel({
					name: that.mother.get("name") + "." + that.$("#tag").val(),
					dot1qTag: that.$("#tag").val(),
					description: that.$("#description").val(),
					ipv4Address: that.$("#ipv4address").val(),
					shutdown: that.$("#shutdown").is(":checked"),
					multicastInterface: that.$("#multicast").is(":checked"),
					inQoS: that.$("#inqos").val(),
					inAcl: that.$("#inacl").val()
				}, {
					network: that.network.get("id"),
					router: that.router.get("id")
				});
				that.act(intf.save());
				return false;
			});
		}

	});
});
