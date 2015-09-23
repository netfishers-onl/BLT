/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'models/network/SnmpCommunityModel',
	'text!templates/admin/addCommunity.html',
	'bootstrap'
], function($, _, Backbone, Dialog, SnmpCommunityModel, addCommunityTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onAdded = options.onAdded;
			this.render();
		},

		template: _.template(addCommunityTemplate),
		
		onCreate: function() {
			var that = this;
			this.$('#add').click(function() {
				var $button = $(this);
				$button.prop('disabled', true);
				var community = new SnmpCommunityModel({
					subnet: that.$("#subnet").val(),
					community: that.$("#community").val()
				}, {
					network: that.model.get('id')
				});
				community.save().done(function() {
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
