package com.ssj.web.spring.interceptor;

import com.ssj.service.user.UserService;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.web.spring.security.SpringSecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sam
 * @version $Id$
 */
public class FetchLoggedInUserInterceptor implements HandlerInterceptor {

  private UserService userService;

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    SpringSecurityUser user = SecurityUtil.getSecurityUser();
    if (user != null && user.getId() != null) {
      user.setUser(userService.getUser(user.getId()));
    }
    return true;
  }

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // do nothing
  }

  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    // do nothing
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
