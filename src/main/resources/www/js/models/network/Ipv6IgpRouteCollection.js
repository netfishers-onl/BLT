/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'underscore',
	'backbone',
	'models/network/Ipv6IgpRouteModel'
], function(_, Backbone, Ipv6IgpRouteModel) {

	return Backbone.Collection.extend({

	  model: function(attrs, options) {
			return new Ipv6IgpRouteModel(attrs, {
				network: options.collection.network,
				router: options.collection.router
			});
	  },
		
	  initialize: function(models, options) {
	  	this.network = options.network;
	  	this.router = options.router;
	  },
	  
	  url: function() {
	  	return "api/networks/" + this.network + "/routers/" + this.router + "/ipv6igproutes";
	  }
	  /*comparator: function(i) {
			var value = 0;
			value += i.getIpv6NetworkAddressToInt();
			value *= 10;
			
			if (i.isIsisL1()) {
				value += 1;
			} else if (i.isIsisL2()) {
				value += 2;
			}
			
			return value;
	  }*/
	  
	});

});
