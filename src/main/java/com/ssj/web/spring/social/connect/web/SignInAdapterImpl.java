package com.ssj.web.spring.social.connect.web;

import com.ssj.model.user.User;
import com.ssj.model.user.UserEvent;
import com.ssj.service.user.UserService;
import com.ssj.web.spring.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author sam
 * @version $Id$
 */
public class SignInAdapterImpl
implements   SignInAdapter, ApplicationEventPublisherAware {
  private UserService userService;
  private TokenBasedRememberMeServices tokenBasedRememberMeServices;
  private ApplicationEventPublisher applicationEventPublisher;

  public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
    User user = userService.findByLogin(userId);
    Authentication authentication = SecurityUtil.signInUser(user);
    // set remember-me cookie
    tokenBasedRememberMeServices.onLoginSuccess(
        (HttpServletRequest) request.getNativeRequest(),
        (HttpServletResponse) request.getNativeResponse(),
        authentication);

    UserEvent event = new UserEvent(this, user);
    event.setAction(UserEvent.ACTION_LOGIN);
    applicationEventPublisher.publishEvent(event);

    return null;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setTokenBasedRememberMeServices(TokenBasedRememberMeServices tokenBasedRememberMeServices) {
    this.tokenBasedRememberMeServices = tokenBasedRememberMeServices;
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }
}
