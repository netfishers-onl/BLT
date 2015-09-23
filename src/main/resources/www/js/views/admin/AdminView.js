/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
define([
	'jquery',
	'underscore',
	'backbone',
	'text!templates/admin/admin.html',
	'models/UserCollection',
	'models/network/NetworkCollection',
	'text!templates/admin/network.html',
	'views/admin/AddNetworkView',
	'views/admin/DeleteNetworkView',
	'models/network/SnmpCommunityCollection',
	'text!templates/admin/community.html',
	'views/admin/AddCommunityView',
	'views/admin/DeleteCommunityView',
	'text!templates/admin/user.html',
	'views/admin/AddUserView',
	'views/admin/DeleteUserView',
	'views/admin/EditUserView',
	'models/ServerStatusModel',
	'bootstrap'
], function($, _, Backbone, adminTemplate, UserCollection,
		NetworkCollection, networkTemplate,
		AddNetworkView, DeleteNetworkView,
		SnmpCommunityCollection, communityTemplate, AddCommunityView, DeleteCommunityView,
		userTemplate, AddUserView, DeleteUserView, EditUserView,ServerStatusModel) { 
	
	return Backbone.View.extend({

		el: $("#page"),

		template: _.template(adminTemplate),
		networkTemplate: _.template(networkTemplate),
		communityTemplate: _.template(communityTemplate),
		userTemplate: _.template(userTemplate),
		

		initialize: function() {
			var that = this;
			this.render();
		},

		render: function() {
			var that = this;
			this.$el.show().html(this.template());

			
			this.$('.network-add').off('click').on('click', function() {
				new AddNetworkView({
					onAdded: function() {
						that.refreshNetworks(true);
					}
				});
				return false;
			});
			
			this.$('.user-add').off('click').on('click', function() {
				new AddUserView({
					onAdded: function() {
						that.refreshUsers();
					}
				});
			});
			
			this.$('#adminMenu').find('a').click(function() {
				that.$('.admin-section').hide();
				that.$('#adminMenu').find('li').removeClass('active');
				$(this).parent().addClass('active');
				var section = $(this).data('section');
				section = section.charAt(0).toUpperCase() + section.slice(1);
				that.$('#admin' + section + "Section").show();
				that['refresh' + section].call(that);
				return false;
			});
			
			this.$('#adminMenu').find('a').first().click();
			
			return this;
		},
		
		refreshNetworks: function(refreshHeader) {
			var that = this;
			this.networks = new NetworkCollection([]);
			that.$('#adminNetworks').empty();
			this.networks.fetch().done(function() {
				that.renderNetworks(refreshHeader);
			});
		},
		
		renderNetworks: function(refreshHeader) {
			var that = this;
			if (refreshHeader === true) {
				window.appRouter.headerView.networks = this.networks;
				window.appRouter.headerView.renderNetworks();
			}
			this.networks.each(function(network) {
				var html = that.networkTemplate(network.toJSON());
				var $network = $(html);
				that.$('#adminNetworks').append($network);
				var communities = network.getSnmpCommunities();
				communities.each(function(community) {
					var html = that.communityTemplate(community.toJSON());
					$network.find('.network-communities>tbody').append(html);
				});
			});
			
			this.$('.network-delete').off('click').on('click', function() {
				var networkId = $(this).closest('.admin-network').data('network');
				var network = that.networks.get(networkId);
				new DeleteNetworkView({
					model: network,
					onDeleted: function() {
						that.refreshNetworks(true);
					}
				});
				return false;
			});
			
			this.$('.network-community-add').off('click').on('click', function() {
				var networkId = $(this).closest('.admin-network').data('network');
				var network = that.networks.get(networkId);
				new AddCommunityView({
					model: network,
					onAdded: function() {
						that.refreshNetworks();
					}
				});
				return false;
			});
			
			this.$('.community-delete').off('click').on('click', function() {
				var networkId = $(this).closest('.admin-network').data('network');
				var communityId = $(this).closest('.network-community').data('community');
				var network = that.networks.get(networkId);
				var communities = network.getSnmpCommunities();
				var community = communities.get(communityId);
				community.network = networkId;
				new DeleteCommunityView({
					model: community,
					onDeleted: function() {
						that.refreshNetworks();
					}
				});
				return false;
			});
		},
		
		refreshUsers: function() {
			var that = this;
			this.$("#users>tbody").empty();
			this.users = new UserCollection([]);
			this.users.fetch().done(function() {
				that.renderUsers();
			});
		},
		
		renderUsers: function() {
			var that = this;
			this.users.each(function(user) {
				that.$("#users>tbody").append($(that.userTemplate(user.toJSON())));
			});
			this.$('#users button.destroy').off('click').on('click', function() {
				var id = $(this).closest('tr').data('user');
				var user = that.users.get(id);
				new DeleteUserView({
					model: user,
					onDeleted: function() {
						that.refreshUsers();
					}
				});
				return false;
			});
			this.$('#users button.edit').off('click').on('click', function() {
				var id = $(this).closest('tr').data('user');
				var user = that.users.get(id);
				new EditUserView({
					model: user,
					onEdited: function() {
						that.refreshUsers();
					}
				});
				return false;
			});
		},
		
	});
});
