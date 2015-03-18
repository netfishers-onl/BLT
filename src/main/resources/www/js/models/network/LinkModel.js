define([
	'underscore',
	'backbone',
], function(_, Backbone) {

	return Backbone.Model.extend({

		initialize: function(attr, options) {
			this.network = options.network;
		},

		urlRoot: function() {
			return "api/networks/" + this.network + "/links";
		},

		getShortName: function() {
			var name = this.get('localInterfaceName');
			if (typeof(name) == "undefined") {
				//return "?";
				return "n/a";
			}
			else {
				name = name.replace(/^Ethernet/, "Eth");
				name = name.replace(/GigabitEthernet/, "Gi");
				name = name.replace(/Bundle-Ether/, "BE");
				name = name.replace(/TenGigE/, "Te");
				return name;
			}
		},
		
		isLost: function() {
			var lost = this.get('lost');
			if (typeof(lost) != "undefined" && lost === true) {
				return true;
			}
			return false;
		}

	});

});
