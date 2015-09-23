/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Panel',
	'models/network/TaskModel',
	'views/map/TaskMonitorView'
], function($, _, Backbone, Panel, TaskModel, TaskMonitorView) {

	return Panel.extend({
		
		act: function(action) {
			var that = this;
			var $buttons = this.$("button, input").prop("disabled", true);
			action.done(function(data) {
				var task = new TaskModel(data);
				that.close();
				var taskView = new TaskMonitorView({
					network: that.network,
					router: that.router,
					mapView: that.mapView,
					model: task
				});
			}).fail(function(data) {
				that.$("#errorText").text(data.responseJSON.errorMsg);
				that.$("#error").show();
				$buttons.prop("disabled", false);
			});
		}

	});
});
