package com.ssj.web.spring.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor to inject global config for whether or not song posting is open
 * to all users or only to users who have the "canPostSongs" flag set to true.
 *
 * @author sam
 * @version $Id$
 * @deprecated using PropertiesHandler instead, need to inject multiple properties into the request
 */
public class CanPostSongsHandlerInterceptor implements HandlerInterceptor {

  private String canPostSongs;

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    request.setAttribute("canPostSongs", (canPostSongs != null && "true".equals(canPostSongs) ? canPostSongs : "false"));

    return true;
  }

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // do nothing
  }

  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    // do nothing
  }

  @Value("${allowAllUsersToPostSongs:true}")
  public void setCanPostSongs(String canPostSongs) {
    this.canPostSongs = canPostSongs;
  }
}
