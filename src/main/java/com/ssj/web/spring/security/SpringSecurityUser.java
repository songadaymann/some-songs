package com.ssj.web.spring.security;

import com.ssj.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class SpringSecurityUser implements UserDetails {

  private Integer id;
  private String username;
  private String password;
  private transient User user;
  private List<GrantedAuthority> roles;

  public SpringSecurityUser(User user, List<GrantedAuthority> roles) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.roles = roles;
  }

  public Collection<GrantedAuthority> getAuthorities() {
    return roles;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  public boolean isAccountNonExpired() {
    return true;
  }

  public boolean isAccountNonLocked() {
    return true;
  }

  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean isEnabled() {
    return true;
  }

  public Integer getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
