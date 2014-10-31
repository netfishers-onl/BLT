define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'models/network/SnmpCommunityModel',
	'text!templates/admin/deleteCommunity.html',
	'bootstrap'
], function($, _, Backbone, Dialog, SnmpCommunityModel, deleteCommunityTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onDeleted = options.onDeleted;
			this.render();
		},

		template: _.template(deleteCommunityTemplate),
		
		onCreate: function() {
			var that = this;
			this.$('#delete').click(function() {
				var $button = $(this);
				$button.prop('disabled', true);
				that.model.destroy().done(function() {
					that.close();
					that.onDeleted();
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
