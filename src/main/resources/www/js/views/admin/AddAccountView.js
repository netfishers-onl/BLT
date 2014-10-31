/*define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'models/network/SshAccountModel',
	'text!templates/admin/addAccount.html',
	'bootstrap'
], function($, _, Backbone, Dialog, SshAccountModel, addAccountTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onAdded = options.onAdded;
			this.render();
		},

		template: _.template(addAccountTemplate),
		
		onCreate: function() {
			var that = this;
			this.$('#add').click(function() {
				var $button = $(this);
				$button.prop('disabled', true);
				if (that.$("#sshpassword").val() != that.$("#csshpassword").val()) {
					that.$("#dialogErrorText").text("The passwords don't match.");
					that.$("#dialogError").show();
					$button.prop('disabled', false);
					return false;
				}
				
				var account = new SshAccountModel({
					subnet: that.$("#subnet").val(),
					username: that.$("#sshusername").val(),
					password: that.$("#sshpassword").val()
				}, {
					network: that.model.get('id')
				});
				account.save().done(function() {
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