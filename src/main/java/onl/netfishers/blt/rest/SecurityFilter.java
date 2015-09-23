/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.rest;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import onl.netfishers.blt.aaa.User;

@Priority(Priorities.AUTHORIZATION)
public class SecurityFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest httpRequest;

	@Inject
	javax.inject.Provider<UriInfo> uriInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		User user = (User) httpRequest.getSession().getAttribute("user");
		requestContext.setSecurityContext(new Authorizer(user));
	}

	private class Authorizer implements SecurityContext {

		private User user;
		
		public Authorizer(User user) {
			this.user = user;
		}
		
		@Override
		public boolean isUserInRole(String role) {
			return (user != null &&
					(("admin".equals(role) && user.getLevel() >= User.LEVEL_ADMIN) ||
					("readwrite".equals(role) && user.getLevel() >= User.LEVEL_READWRITE) ||
					("readonly".equals(role) && user.getLevel() >= User.LEVEL_READONLY)));
		}

		@Override
		public boolean isSecure() {
			return "https".equals(uriInfo.get().getRequestUri().getScheme());
		}

		@Override
		public Principal getUserPrincipal() {
			return user;
		}

		@Override
		public String getAuthenticationScheme() {
			return SecurityContext.FORM_AUTH;
		}

	}

}
