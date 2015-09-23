/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'text!templates/header/changePassword.html',
	'bootstrap'
], function($, _, Backbone, Dialog, changePasswordTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onChanged = options.onChanged;
			this.render();
		},

		template: _.template(changePasswordTemplate),
		
		onCreate: function() {
			var that = this;
			this.$('#save').click(function() {
				var $button = $(this);
				$button.prop('disabled', true);
				if (that.$("#password").val() != that.$("#cpassword").val()) {
					that.$("#dialogErrorText").text("The passwords don't match.");
					that.$("#dialogError").show();
					$button.prop('disabled', false);
					return false;
				}
				var data = {
					username: that.model.get("name"),
					password: that.$("#opassword").val(),
					newPassword: that.$("#password").val()
				}
				that.model.save(data).done(function() {
					that.close();
					that.onChanged();
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
