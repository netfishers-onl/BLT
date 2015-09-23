/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'views/map/Panel',
	'models/network/TaskModel',
	'text!templates/map/taskMonitor.html',
	'text!templates/map/task.html'
], function($, _, Backbone, Panel, TaskModel, taskMonitorTemplate, taskTemplate) {

	return Panel.extend({

		template: _.template(taskMonitorTemplate),
		taskTemplate: _.template(taskTemplate),
		
		onCreate: function() {
			var that = this;
			this.refresh();
		},
		
		onClose: function() {
			this.closed = true;
			this.mapView.refresh();
		},
		
		refresh: function() {
			var that = this;
			this.model.fetch().done(function() {
				that.renderTask();
			});
		},
		
		renderTask: function() {
			var that = this;
			if (this.closed === true) {
				return;
			}
			var html = this.taskTemplate(this.model.toJSON());
			var $item = this.$('#tasks div[data-task="' + this.model.get("id") + '"]');
			if ($item.length === 0) {
				var $html = $('<div data-task="' + this.model.get("id") + '"/>').html(html);
				this.$("#tasks").append($html);
			}
			else {
				$item.html(html);
			}
			if (_.contains(["RUNNING", "NEW", "WAITING"], this.model.get("status"))) {
				setTimeout(_.bind(this.refresh, this), 5000);
			}
			else {
				if (this.model.get("relatedTasks").length > 0) {
					this.model = new TaskModel({
						id: this.model.get("relatedTasks")[0]
					});
					this.refresh();
				}
				else {
					this.$(".buttonbar").show();
				}
			}
		}

	});
});
