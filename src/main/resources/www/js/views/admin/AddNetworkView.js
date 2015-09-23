/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'models/network/NetworkModel',
	'text!templates/admin/addNetwork.html',
	'bootstrap'
], function($, _, Backbone, Dialog, NetworkModel, addNetworkTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onAdded = options.onAdded;
			this.render();
		},

		template: _.template(addNetworkTemplate),
		
		onCreate: function() {
			var that = this;
			this.$('#add').click(function() {
				var $button = $(this);
				$button.prop('disabled', true);
				var network = new NetworkModel({
					name: that.$("#name").val(),
					bgpPeerAddress: that.$("#bgpPeerIp").val(),
					bgpAs: that.$("#bgpPeerAs").val()
				});
				network.save().done(function() {
					that.close();
					that.onAdded();
				}).fail(function(data) {
					that.$("#dialogErrorText").text(data.responseJSON.errorMsg);
					that.$("#dialogError").show();
					$button.prop('disabled', false);
				});
				return false;
			});
		}

	});
});
