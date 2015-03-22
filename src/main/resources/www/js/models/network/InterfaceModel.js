define([
	'underscore',
	'backbone',
], function(_, Backbone) {

	return Backbone.Model.extend({

		initialize: function(attr, options) {
			this.network = options.network;
			this.router = options.router;
		},
		
		isIpv4Address: function() {
			return typeof(this.get('name')) == "string" &&
			this.get('name').match(/^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+/);
		},
		
		isEthernet: function() {
			return typeof(this.get('name')) == "string" &&
					this.get('name').match(/Et(her|)|TenGigE/);
		},
		
		isLoopback: function() {
			return typeof(this.get('name')) == "string" &&
				this.get('name').match(/Loopback/);
		},
		
		isManagement: function() {
			return typeof(this.get('name')) == "string" &&
				this.get('name').match(/MgmtEth/);
		},
		
		isP2pTeTunnel: function() {
			return this.get("category") == "P2PTE" ||
				(typeof(this.get('name')) == "string" && this.get('name').match(/tunnel-te/));
		},
		
		isP2mpTeTunnel: function() {
			return this.get("category") == "P2MPTE" ||
				(typeof(this.get('name')) == "string" && this.get('name').match(/tunnel-mte/));
		},
		
		isTeTunnel: function() {
			return this.isP2pTeTunnel() || this.isP2mpTeTunnel();
		},
		
		isSubinterface: function() {
			return typeof(this.get('name')) == "string" &&
					this.get('name').match(/\.[0-9]+$/);
		},
		
		getDot1qTag: function() {
			if (this.isSubinterface()) {
				return this.get('name').match(/[0-9]+$/);
			}
			return 0;
		},
		
		getBaseName: function() {
			if (this.isEthernet()) {
				if (this.isSubinterface()) {
					var match = /^(.*)\.[0-9]+/.exec(this.get('name'));
					if (match) {
						return match[1];
					}
				} 
				else {return this.get('name');}
			} 
			else if (this.isIpv4Address()) {
				var match = /^(.*)\/[0-9]+/.exec(this.get('name'));
				if (match) {
					return match[1];
				}
			} 
			return 0;
		},
		
		getTunnelIndex: function() {
			if (this.isTeTunnel()) {
				var match = /.*?([0-9]+)$/.exec(this.get('name'));
				if (match) {
					return parseInt(match[1]);
				}
			}
			return 0;
		},
		
		isEditable: function() {
			return this.get('type') == "CONFIGURED" &&
			 	// add edition capabilities for physical Ethernet interfaces
				// ticket #26
				//((this.isEthernet() && this.isSubinterface()) || this.isTeTunnel());
				(this.isEthernet() || this.isTeTunnel());
		},
		
		isDestroyable: function() {
			return this.get('type') == "CONFIGURED" &&
			((this.isEthernet() && this.isSubinterface()) || this.isTeTunnel());
		},
		
		canHaveSubinterface: function() {
			return this.isEthernet() && !this.isSubinterface();
		},
		
		urlRoot: function() {
			var u = "api/networks/" + this.network + "/routers/" + this.router + "/interfaces";
			if (this.isEthernet()) {
				u += "/ethernet";
			}
			else if (this.isP2pTeTunnel()) {
				u += "/p2pte";
			}
			else if (this.isP2mpTeTunnel()) {
				u += "/p2mpte";
			}
			return u;
		}

	});

});
