/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'views/header/HeaderView',
	'views/map/MapView',
	'views/admin/AdminView',
	'views/LoginView',
	'views/ReAuthenticateView',
], function($, _, Backbone, HeaderView, MapView, AdminView, LoginView,
		ReAuthenticateView) {

	var initPages = function() {
		
		$(document).ajaxComplete(function(event, jqXHR, ajaxSettings) {
			if (jqXHR.status == 403) {
				new ReAuthenticateView();
			}
			if (typeof(window.reauth) != "undefined") {
				clearTimeout(window.reauth);
			}
			window.reauth = setTimeout(function() {
				new ReAuthenticateView();
			}, (window.user.get("maxIdleTime") - 5) * 1000)
		});
		
		var routes = {
			'map/:id': 'showMap',
			'admin': 'showAdmin',
			// Default
			'*actions': 'showMap'
		};
		if (!window.user.isAdmin()) {
			delete routes.admin;
		}
		
		var AppRouter = Backbone.Router.extend({
			currentView: null,
			routes: routes,
			headerView: new HeaderView()
		});
		window.appRouter = new AppRouter;
		
		appRouter.on('route:showMap', function(id) {
			var id = parseInt(id);
			if (isNaN(id)) {
				this.headerView.select(-1);
			}
			else {
				this.currentView = new MapView({ id: id });
				this.headerView.select(id);
			}
		});
		appRouter.on('route:showAdmin', function() {
			this.currentView = new AdminView();
			this.headerView.select("admin");
		});
		
		Backbone.history.start();

	};

	var initialize = function() {
		var loginView = new LoginView({
			onSuccess: function() {
				$("#splash").hide();
				initPages();
			}
		});
	};
	
	return {
		initialize: initialize
	};
});
