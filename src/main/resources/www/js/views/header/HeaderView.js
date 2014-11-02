define([
	'jquery',
	'underscore',
	'backbone',
	'text!templates/header/header.html',
	'models/network/NetworkCollection',
	'models/ServerStatusModel',
	'text!templates/header/network.html',
	'views/header/AboutView',
	'views/header/ChangePasswordView',
	'bootstrap'
], function($, _, Backbone, headerTemplate, NetworkCollection, ServerStatusModel,
		networkTemplate, AboutView, ChangePasswordView) {

	return Backbone.View.extend({

		el: $("#header"),

		template: _.template(headerTemplate),
		networkTemplate: _.template(networkTemplate),
		
		initialize: function() {
			this.target = '';
			this.render();
		},

		render: function() {
			var that = this;
			data = {
				user: window.user.toJSON()
			};
			this.$el.html(this.template(data)).show();
			if (!window.user.isAdmin()) {
				this.$("#admin").remove();
			}
			this.$('#about').click(function() {
				var aboutView = new AboutView();
				return false;
			});
			this.refreshNetworks();
			this.$("#disconnect").off().on("click", function() {
				window.user.destroy().always(function() {
					window.location.reload();
					return false;
				});
			});
			this.$("#changepassword").off().on("click", function() {
				new ChangePasswordView({
					model: window.user,
					onChanged: function() {}
				});
				return false;
			});
			this.globalStatus = new ServerStatusModel();
			return this;
		},
		
		refreshNetworks: function() {
			var that = this;
			this.networks = new NetworkCollection([]);
			this.networks.fetch().done(function() {
				that.renderNetworks();
			});
		},
		
		renderNetworks: function() {
			var that = this;
			that.$('#headerNetworks').empty();
			this.networks.each(function(network) {
				var html = that.networkTemplate(network.toJSON());
				that.$("#headerNetworks").append($(html));
			});
			if (this.target == -1) {
				if (this.networks.length == 0) {
					
				}
				else {
					window.appRouter.navigate("map/" + this.networks.at(0).get('id'), { trigger: true });
				}
			}
			this.select(this.target);
		},
		
		select: function(target) {
			this.target = target;
			var tab = '';
			if (target == "admin") {
				tab = "#admin";
			}
			else if (!isNaN(parseInt(target))) {
				tab = '.header-network[data-network="' + parseInt(target) + '"]';
			}
			this.$('li.active').removeClass('active');
			this.$(tab).addClass('active');
		}

	});
});
