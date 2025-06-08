package com.ssj.web.spring.security;

import com.ssj.model.user.User;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class SecurityUtil {

  public static User getUser() {
    User user = null;
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication auth;
      if (securityContext != null) {
        auth = securityContext.getAuthentication();
        if (auth != null) {
          Object principal = auth.getPrincipal();
          if (principal instanceof SpringSecurityUser) {
            SpringSecurityUser authUser = (SpringSecurityUser) principal;
//            int userId = authUser.getUserId();
            user = authUser.getUser();//userService.get(userId);
          }
        }
      }
    return user;
  }

  public static SpringSecurityUser getSecurityUser() {
    SpringSecurityUser user = null;
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication auth;
    if (securityContext != null) {
      auth = securityContext.getAuthentication();
      if (auth != null) {
        Object principal = auth.getPrincipal();
        if (principal instanceof SpringSecurityUser) {
          user = (SpringSecurityUser) principal;
        }
      }
    }
    return user;
  }

  public static Authentication signInUser(User user) {
    // TODO check if user == null?
    List<GrantedAuthority> authorities = SSJUserDetailsServiceImpl.buildRoles(user);
    SpringSecurityUser springSecurityUser = new SpringSecurityUser(user, authorities);
    Authentication authentication = new UsernamePasswordAuthenticationToken(springSecurityUser, user.getPassword(), authorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return authentication;
  }

/*
  public static String getUsername(HttpServletRequest request) {
    String username = null;
    HttpSession session = request.getSession(true);
    if (session != null) {
      SecurityContext securityContext = (SecurityContext) session.getAttribute(HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY);
      Authentication auth;
      if (securityContext != null) {
        auth = securityContext.getAuthentication();
        if (auth != null) {
          username = auth.getName();
        }
      }
    }
    return username;
  }
*/
  public static void updateAuthentication(User user, AuthenticationManager authenticationManager) {
    // get existing authentication, see if it needs to be update
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication auth;
    if (securityContext != null) {
      auth = securityContext.getAuthentication();
      if (auth != null && (!((UserDetails) auth.getPrincipal()).getUsername().equals(user.getUsername()) || !auth.getCredentials().equals(user.getPassword()))) {
        // username and/or password changed, need to update authentication
        auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), auth.getAuthorities());

        securityContext.setAuthentication(authenticationManager.authenticate(auth));
      }
    }
  }
}
