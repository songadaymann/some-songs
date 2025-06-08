package com.ssj.web.spring.interceptor;

import com.ssj.service.content.SiteLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SiteLinksHandlerInterceptor implements HandlerInterceptor {
  private SiteLinkService siteLinkService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // load the site links and put them into the request
    request.setAttribute("siteLinks", siteLinkService.getAll());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // not implemented
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    // not implemented
  }

  @Autowired
  public void setSiteLinkService(SiteLinkService siteLinkService) {
    this.siteLinkService = siteLinkService;
  }
}
