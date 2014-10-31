/*define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'models/LicenseModel',
	'text!templates/admin/addLicense.html',
	'bootstrap'
], function($, _, Backbone, Dialog, LicenseModel, addLicenseTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onAdded = options.onAdded;
			this.render();
		},

		template: _.template(addLicenseTemplate),
		
		onCreate: function() {
			var that = this;
			this.$('#add').click(function() {
				var $button = $(this);
				$button.prop('disabled', true);
				var license = new LicenseModel({
					text: that.$("#license").val()
				});
				license.save().done(function() {
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
*/