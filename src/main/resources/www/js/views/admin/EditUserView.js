/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'views/Dialog',
	'text!templates/admin/editUser.html',
	'bootstrap'
], function($, _, Backbone, Dialog, editUserTemplate) {

	return Dialog.extend({
		
		initialize: function(options) {
			this.onEdited = options.onEdited;
			this.render();
		},

		template: _.template(editUserTemplate),
		
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
					level: that.$('input[name="role"]:checked').val()
				};
				if  (that.$("#password").val() != "--------") {
					data.password = that.$("#password").val();
				}
				that.model.save(data).done(function() {
					that.close();
					that.onEdited();
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
