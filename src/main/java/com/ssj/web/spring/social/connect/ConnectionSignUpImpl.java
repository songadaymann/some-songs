package com.ssj.web.spring.social.connect;

import com.ssj.model.user.User;
import com.ssj.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

/**
 * @author sam
 * @version $Id$
 */
@Deprecated
public class ConnectionSignUpImpl implements ConnectionSignUp {
  private UserService userService;

  /**
   * Sign up a new user of the application from the connection.
   * @param connection the connection
   * @return the new user id
   */
  public String execute(Connection<?> connection) {
    UserProfile profile = connection.fetchUserProfile();
    String userId = null;
    if (profile.getEmail() != null) {
      User user = new User();
      user.setUsername(profile.getUsername());
      user.setEmail(profile.getEmail());
      try {
        userService.registerUser(user);
        userId = user.getUsername();
      } catch (Exception e) {
        // hm, what to do here...?
      }
    }
    return userId;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
