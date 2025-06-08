package com.ssj.web.spring.security;

import com.ssj.model.user.User;
import com.ssj.model.user.UserEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import com.ssj.service.user.UserService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class ApplicationSecurityListener
implements   ApplicationListener<InteractiveAuthenticationSuccessEvent>,
             ApplicationEventPublisherAware {

  private UserService userService;
  private ApplicationEventPublisher applicationEventPublisher;

  public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
    Object principal = event.getAuthentication().getPrincipal();

    if (principal != null && principal instanceof SpringSecurityUser) {
      SpringSecurityUser securityUser = (SpringSecurityUser) principal;

      User user = userService.findByLogin(securityUser.getUsername());

      UserEvent userEvent = new UserEvent(this, user);
      userEvent.setAction(UserEvent.ACTION_LOGIN);

      applicationEventPublisher.publishEvent(userEvent);
    }
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }
}
