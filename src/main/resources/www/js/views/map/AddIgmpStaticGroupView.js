define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Edit',
	'models/network/IgmpStaticGroupModel',
	'models/network/InterfaceCollection',
	'text!templates/map/addIgmpStaticGroup.html',
	'bootstrap'
], function($, _, Backbone, Edit, IgmpStaticGroupModel,
		InterfaceCollection, addIgmpStaticGroupTemplate) {

	return Edit.extend({

		template: _.template(addIgmpStaticGroupTemplate),
		
		initialize: function(options) {
			var that = this;
			_.extend(this, options);
			this.interfaces = new InterfaceCollection([], {
				network: this.network.get('id'),
				router: this.router.get('id')
			});
			this.interfaces.parse = function(response, options) {
				return _.filter(response, function(i) { return i.type == "CONFIGURED" });
			}
			this.interfaces.fetch().done(function() {
				that.render();
			});
		},
		
		templateData: function() {
			var data = {
				network: this.network.toJSON(),
				router: this.router.toJSON(),
				interfaces: this.interfaces.toJSON()
			};
			return data;
		},
		
		onCreate: function() {
			var that = this;
			this.$("#add").on("click", function() {
				var group = new IgmpStaticGroupModel({
					group: that.$("#group").val(),
					source: that.$("#source").val()
				}, {
					network: that.network.get("id"),
					router: that.router.get("id"),
					intf: that.$("#interface").val()
				});
				that.act(group.save());
				return false;
			});
		}

	});
});
