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
	'models/network/SshAccountCollection',
	'text!templates/admin/community.html',
	'views/admin/AddCommunityView',
	'views/admin/DeleteCommunityView',
	'text!templates/admin/account.html',
	'views/admin/AddAccountView',
	'views/admin/DeleteAccountView',
	'text!templates/admin/user.html',
	'views/admin/AddUserView',
	'views/admin/DeleteUserView',
	'views/admin/EditUserView',
	//'models/LicenseCollection',
	//'text!templates/admin/license.html',
	'text!templates/admin/licensingStatus.html',
	//'views/admin/AddLicenseView',
	//'views/admin/DeleteLicenseView',
	'models/ServerStatusModel',
	'bootstrap'
], function($, _, Backbone, adminTemplate, UserCollection,
		NetworkCollection, networkTemplate,
		AddNetworkView, DeleteNetworkView,
		SnmpCommunityCollection, SshAccountCollection, communityTemplate, AddCommunityView,
		DeleteCommunityView, accountTemplate, AddAccountView, DeleteAccountView,
		userTemplate, AddUserView, DeleteUserView, EditUserView, 
		//LicenseCollection,licenseTemplate, licensingStatusTemplate, AddLicenseView, DeleteLicenseView,
		ServerStatusModel) {

	return Backbone.View.extend({

		el: $("#page"),

		template: _.template(adminTemplate),
		networkTemplate: _.template(networkTemplate),
		communityTemplate: _.template(communityTemplate),
		accountTemplate: _.template(accountTemplate),
		userTemplate: _.template(userTemplate),
		//licenseTemplate: _.template(licenseTemplate),
		//licensingStatusTemplate: _.template(licensingStatusTemplate),
		

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
			
			/*this.$('.license-add').off().on('click', function() {
				new AddLicenseView({
					onAdded: function() {
						that.refreshLicenses();
					}
				});
			});
			*/
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
				var accounts = network.getSshAccounts();
				accounts.each(function(account) {
					var html = that.accountTemplate(account.toJSON());
					$network.find('.network-accounts>tbody').append(html);
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
			this.$('.network-account-add').off('click').on('click', function() {
				var networkId = $(this).closest('.admin-network').data('network');
				var network = that.networks.get(networkId);
				new AddAccountView({
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
			this.$('.account-delete').off('click').on('click', function() {
				var networkId = $(this).closest('.admin-network').data('network');
				var accountId = $(this).closest('.network-account').data('account');
				var network = that.networks.get(networkId);
				var accounts = network.getSshAccounts();
				var account = accounts.get(accountId);
				account.network = networkId;
				new DeleteAccountView({
					model: account,
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
		
		/*refreshLicenses: function() {
			var that = this;
			$("#licenseissue").hide();
			this.$("#licenses>tbody").empty();
			this.$("#licensingStatus").empty();
			this.licenses = new LicenseCollection([]);
			this.globalStatus = new ServerStatusModel();
			this.globalStatus.fetch().done(function() {
				that.renderLicensingStatus();
			});
			this.licenses.fetch().done(function() {
				that.renderLicenses();
			});
		},*/
		
		/*renderLicensingStatus: function() {
			this.$("#licensingStatus").html(this.licensingStatusTemplate(this.globalStatus.toJSON()));
		},
		
		renderLicenses: function() {
			var that = this;
			this.licenses.each(function(license) {
				that.$("#licenses>tbody").append($(that.licenseTemplate(license.toJSON())));
			});
			this.$('#licenses button.destroy').off('click').on('click', function() {
				var id = $(this).closest('tr').data('license');
				var license = that.licenses.get(id);
				new DeleteLicenseView({
					model: license,
					onDeleted: function() {
						that.refreshLicenses();
					}
				});
				return false;
			});
		}*/

	});
});
