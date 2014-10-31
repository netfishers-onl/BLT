define([
        'underscore',
        'backbone',
        'models/network/RouterModel'
        ], function(_, Backbone, RouterModel) {

	return Backbone.Collection.extend({

		model: function(attrs, options) {
			return new RouterModel(attrs, {
				network: options.collection.network
			});
		},

		initialize: function(models, options) {
			this.network = options.network;
		},

		url: function() {
			return "api/networks/" + this.network + "/routers";
		},

		findRouterByIdentifier: function(routerId) {
			return _.find(this.models, function(r) {
				return _.isEqual(r.get("routerId"), routerId);
			});
		},
		
		findRouterInterfaceByIp: function(ip) {
			var result = {
				router: null,
				intf: null
			};
			_.find(this.models, function(r) {
				var ipv4Interfaces = r.get("ipv4Interfaces"); 
				for (var i in ipv4Interfaces) {
					if (ipv4Interfaces[i].ipv4Address.ip == ip) {
						result.router = r;
						result.intf = ipv4Interfaces[i];
						return true;
					}
				}
			});
			return result;
		}

	});

});
