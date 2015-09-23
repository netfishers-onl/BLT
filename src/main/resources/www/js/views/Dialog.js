/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone'
], function($, _, Backbone) {

	return Backbone.View.extend({

		el: "#dialog",

		template: null,

		initialize: function() {
			this.render();
		},

		templateData: function() {
			if (typeof (this.model) == "object") {
				return this.model.toJSON();
			}
			return {};
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
			this.$el.empty().off().html(this.template(this.templateData())).hide();
			this.$el.on('show.bs.modal', function(e) { that.onCreate(e); });
			this.$el.on('hidden.bs.modal', function(e) { that.onClose(e); });
			this.$el.modal(options);
			return this;
		},

		close: function() {
			var r = this.onClose();
			if (r !== false) {
				this.$el.modal('hide').empty();
			}
		}

	});
});
