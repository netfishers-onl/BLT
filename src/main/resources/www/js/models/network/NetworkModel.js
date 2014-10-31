define([
	'underscore',
	'backbone',
	'models/network/SnmpCommunityCollection',
	'models/network/SshAccountCollection'
], function(_, Backbone, SnmpCommunityCollection, SshAccountCollection) {

	return Backbone.Model.extend({

		urlRoot: "api/networks",

		defaults: {
		},
		
		getSnmpCommunities: function() {
			return new SnmpCommunityCollection(this.get('snmpCommunities'), { network: this.get('id') });
		},
		
		getSshAccounts: function() {
			return new SshAccountCollection(this.get('sshAccounts'), { network: this.get('id') });
		}

	});

});
