define([
	'jquery',
	'underscore',
	'backbone',
	'text!templates/login.html',
	'text!templates/startupError.html',
	'models/UserModel',
	'bootstrap'
], function($, _, Backbone, loginTemplate, startupErrorTemplate, UserModel) {

	return Backbone.View.extend({

		el: $("#loginbox"),

		template: _.template(loginTemplate),
		startupErrorTemplate: _.template(startupErrorTemplate),

		initialize: function(options) {
			var that = this;
			this.onSuccess = options.onSuccess;
			window.user = new UserModel({ current: true });
			window.user.fetch().done(function() {
				that.close();
				that.onSuccess();
			}).fail(function(data) {
				if (data.status == 403) {
					that.render();
				}
				else {
					that.renderStartupError();
				}
			});
		},
		
		renderStartupError: function() {
			this.$el.html(this.startupErrorTemplate());
			this.$("#error").show();
		},

		render: function() {
			var that = this;
			this.$el.html(this.template());
			this.$("button").off("click").on("click", function() {
				var $buttons = that.$("button, input").prop("disabled", true);
				window.user.save({
					username: that.$("#username").val(),
					password: that.$("#password").val()
				}).done(function() {
					that.close();
					that.onSuccess();
				}).fail(function(data) {
					that.$("#errorText").text(data.responseJSON.errorMsg);
					that.$("#error").show();
					$buttons.prop("disabled", false);
				});
				return false;
			});
			return this;
		},
		
		close: function() {
		}
		
	});
});
