/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'underscore',
	'backbone',
], function(_, Backbone) {

	return Backbone.Model.extend({
		
		current: false,
		
		initialize: function(options) {
			if (options.current === true) {
				this.current = true;
			}
		},
		
		isAdmin: function() {
			return this.get("level") >= 15;
		},
		
		isReadWrite: function() {
			return this.get("level") >= 3;
		},
		
		isReadOnly: function() {
			return this.get("level") < 3;
		},
		
		toJSON: function() {
			var j = _(this.attributes).clone();
			j.isAdmin = this.isAdmin();
			j.isReadWrite = this.isReadWrite();
			j.isReadOnly = this.isReadOnly();
			if (this.isAdmin()) {
				j.role = "Administrator";
			}
			else if (this.isReadWrite()) {
				j.role = "Read-write user";
			}
			else {
				j.role = "Read-only user";
			}
			return j;
		},
		
		url: function() {
			if (this.current) {
				return "api/user";
			}
			var u = "api/users";
			if (typeof(this.id) == "number") {
				u += "/" + this.id;
			}
			return u;
		}

	});

});
