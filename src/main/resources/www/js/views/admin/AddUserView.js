define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'models/UserModel',
	'text!templates/admin/addUser.html',
	'bootstrap'
], function($, _, Backbone, Dialog, UserModel, addUserTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onAdded = options.onAdded;
			this.render();
		},

		template: _.template(addUserTemplate),
		
		onCreate: function() {
			var that = this;
			this.$('#add').click(function() {
				var $button = $(this);
				$button.prop('disabled', true);
				if (that.$("#password").val() != that.$("#cpassword").val()) {
					that.$("#dialogErrorText").text("The passwords don't match.");
					that.$("#dialogError").show();
					$button.prop('disabled', false);
					return false;
				}
				var user = new UserModel({
					name: that.$("#name").val(),
					password: that.$("#password").val(),
					level: that.$('input[name="role"]:checked').val()
				});
				user.save().done(function() {
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
