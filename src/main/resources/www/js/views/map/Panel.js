define([
	'jquery',
	'underscore',
	'backbone'
], function($, _, Backbone) {

	return Backbone.View.extend({

		el: "#editbox",

		initialize: function(options) {
			_.extend(this, options);
			this.render();
		},

		templateData: function() {
			var data = {
				network: this.network.toJSON(),
				router: this.router.toJSON()
			};
			if (typeof(this.model) == "object") {
				data = _.extend(data, this.model.toJSON());
			}
			return data;
		},
		
		buttons: function() {
			return {};
		},

		dialogOptions: {},

		onCreate: function() {
		},

		onClose: function() {
		},

		render: function() {
			var that = this;
			var defaultDialogOptions = {
				keyboard: true,
				
			};

			var options = _.extend(defaultDialogOptions, this.dialogOptions);
			$("#routerbox").hide();
			this.$el.empty().off().html(this.template(this.templateData())).show();
			this.$("#cancel").on("click", function() {
				that.close();
			});
			this.onCreate();
			return this;
		},

		close: function() {
			if (this.onClose() === false) {
				return;
			}
			this.$el.hide().empty();
			$("#routerbox").show();
		}

	});
});
