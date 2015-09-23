/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
        'underscore',
        'backbone',
        'models/network/InterfaceModel'
        ], function(_, Backbone, InterfaceModel) {

	return Backbone.Collection.extend({

		model: function(attrs, options) {
			return new InterfaceModel(attrs, {
				network: options.collection.network,
				router: options.collection.router
			});
		},

		initialize: function(models, options) {
			this.network = options.network;
			this.router = options.router;
			if (typeof(options.filter) == "string") {
				this.filter = options.filter;
			}
		},

		url: function() {
			var u = "api/networks/" + this.network + "/routers/" + this.router + "/interfaces";
			if (typeof(this.filter) == "string") {
				u += "/" + this.filter;
			}
			return u;
		},
		
		comparator: function(i) {
			var value = 0;
			if (i.isP2pTeTunnel()) {
				value += 1000000;
			}
			else if (i.isP2mpTeTunnel()) {
				value += 2000000;
			}
			value += i.getTunnelIndex() * 10;
			if (i.get("type") == "LIVE") {
				value += 1;
			}
			if (typeof(i.get("routerRole")) == "string" && i.get("routerRole") != "HEAD") {
				value += 100000;
			}
			
			if (i.isTeTunnel()) {
				return value
			}
			
			if (i.isEthernet()) {
				function pad(num, size) {
				    var s = "000000000" + num;
				    return s.substr(s.length-size);
				}
				return (i.getBaseName()+pad(i.getDot1qTag(),5));
			}
		}

	});

});
