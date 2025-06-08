package com.ssj.web.spring.security;

import com.ssj.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.dao.DataAccessException;
import com.ssj.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides user details for Spring Security.
 *
 * @version $Id$
 */
public class SSJUserDetailsServiceImpl implements UserDetailsService {

  private UserService userService;

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
    User domainUser = userService.findByLogin(username);
    if (domainUser == null) {
      throw new UsernameNotFoundException("Could not find user with name '" + username + "'");
    }
    List<GrantedAuthority> roles = buildRoles(domainUser);
    return new SpringSecurityUser(domainUser, roles);
  }

  public static List<GrantedAuthority> buildRoles(com.ssj.model.user.User modelUser) {
    List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
    if (modelUser.isAdmin()) {
      roles.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
    }
    if (modelUser.isCanPostSongs()) {
      roles.add(new GrantedAuthorityImpl("ROLE_POSTSONGS"));
    }
    if (modelUser.isCanSynchFromBandcamp()) {
      roles.add(new GrantedAuthorityImpl("ROLE_BANDCAMPSYNCH"));
    }
    roles.add(new GrantedAuthorityImpl("ROLE_USER"));
    return roles;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
