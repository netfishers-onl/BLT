/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
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
				return this.get('protocolId');
			}
			else {
				name = name.replace(/^Ethernet/, "Eth");
				name = name.replace(/GigabitEthernet/, "Gi");
				name = name.replace(/Bundle-Ether/, "BE");
				name = name.replace(/TenGigE/, "Te");
				return name;
			}
		},
		getMetric: function() {
			var metric = this.get('metric');
			if (typeof(metric) == "undefined") {
				//JunOS implementation does not carry metric TLV
				//thus we try to see if teDefaultMetric can replace a null metric
				var teDefaultMetric = this.get('teDefaultMetric');
				if (typeof(teDefaultMetric) != "undefined") {
					return teDefaultMetric.toString();
				}
				else {
					return "...";
				}
			}
			else {
				return metric.toString();
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
