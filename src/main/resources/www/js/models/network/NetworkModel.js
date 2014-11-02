define([
	'underscore',
	'backbone',
	'models/network/SnmpCommunityCollection',
], function(_, Backbone, SnmpCommunityCollection) {

	return Backbone.Model.extend({

		urlRoot: "api/networks",

		defaults: {
		},
		
		getSnmpCommunities: function() {
			return new SnmpCommunityCollection(this.get('snmpCommunities'), { network: this.get('id') });
		},
		
	});

});
