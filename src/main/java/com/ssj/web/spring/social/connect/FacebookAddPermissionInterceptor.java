package com.ssj.web.spring.social.connect;

import com.ssj.model.user.User;
import com.ssj.service.user.UserService;
import com.ssj.web.spring.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Detects when user is just adding the "publish_actions" (aka publish to Timeline)
 * permission, and when they grant that we turn on the "publish to timeline" feature.
 * Not sure if we can safely assume they've really granted the permission, though...?
 */
public class FacebookAddPermissionInterceptor implements ConnectInterceptor<Facebook> {

  public static final String ENABLE_TIMELINE = "enableTimeline";
  public static final String TIMELINE_ENABLED = "timelineEnabled";

  private UserService userService;

  @Override
  public void preConnect(ConnectionFactory<Facebook> facebookConnectionFactory,
                         MultiValueMap<String, String> parameters, WebRequest request) {
    if ("publish_actions".equals(request.getParameter("scope"))) {
      request.setAttribute(ENABLE_TIMELINE, "true", RequestAttributes.SCOPE_SESSION);
    }
  }

  @Override
  public void postConnect(Connection<Facebook> facebookConnection, WebRequest request) {
    if ("true".equals(request.getAttribute(ENABLE_TIMELINE, RequestAttributes.SCOPE_SESSION))) {
      request.setAttribute(ENABLE_TIMELINE, null, RequestAttributes.SCOPE_SESSION);
      User user = SecurityUtil.getUser();
      user.setPublishToTimeline(true);
      userService.save(user);
      request.setAttribute(TIMELINE_ENABLED, "true", RequestAttributes.SCOPE_SESSION);
    }
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
