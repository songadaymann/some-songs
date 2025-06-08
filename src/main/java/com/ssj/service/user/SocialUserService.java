package com.ssj.service.user;

import com.ssj.model.user.User;
import org.springframework.social.connect.UsersConnectionRepository;

public interface SocialUserService extends UsersConnectionRepository {

  /**
   * Convenience method to get the API for the user's primary
   * connection of the given type, or null if the user does not
   * have such a connection.
   */
  public <T> T getApi(User user, Class<T> apiClass);

}
