define([
	'underscore',
	'backbone',
], function(_, Backbone) {

	return Backbone.Model.extend({

		initialize: function(attr, options) {
			this.network = options.network;
			this.router = options.router;
		},
		
		isIsisL1: function() {
			return typeof (this.get("protocolId") == "ISIS_Level1");
		},
		
		isIsisL2: function() {
			return typeof (this.get("protocolId") == "ISIS_Level2");
		},
		
		isOspf: function() {
			return typeof (this.get("protocolId") == "OSPF");
		},
		
		getIpv4NetworkAddressToInt: function() {
	        var ip = this.get("subnet").ip;
	        var d = ip.split(".");
	        return ((((((+d[0])*256)+(+d[1]))*256)+(+d[2]))*256)+(+d[3]);
		},

		getPrefixLength: function() {
			return this.get('subnet').prefixLength;
		},
		
		getMetric: function() {
			return this.get('metric');
		},
		
		urlRoot: function() {
			return "api/networks/" + this.network + "/routers/" + this.router + "/ipv4igproutes";
		}

	});

});