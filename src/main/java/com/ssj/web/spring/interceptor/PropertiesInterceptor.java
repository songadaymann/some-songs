package com.ssj.web.spring.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets configuration properties as request attributes.
 */
public class PropertiesInterceptor implements HandlerInterceptor {

  private String canPostSongs;
  private String siteUrl;
  private String facebookAppId;
  private String facebookAppNamespace;
  private String openGraphActionImageUrl;
  private String twitterAppId;
  private String soundcloudAppId;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    request.setAttribute("canPostSongs", (canPostSongs != null && "true".equals(canPostSongs) ? canPostSongs : "false"));
    request.setAttribute("siteUrl", siteUrl);
    request.setAttribute("facebookAppId", facebookAppId);
    request.setAttribute("facebookAppNamespace", facebookAppNamespace);
    request.setAttribute("openGraphActionImageUrl", openGraphActionImageUrl);
    request.setAttribute("twitterAppId", twitterAppId);
    request.setAttribute("soundcloudAppId", soundcloudAppId);

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    
  }

  @Value("${allowAllUsersToPostSongs:true}")
  public void setCanPostSongs(String canPostSongs) {
    this.canPostSongs = canPostSongs;
  }

  @Value("${site.url:http://somesongs.org}")
  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }

  @Value("${facebook.somesongs.app.clientId:}")
  public void setFacebookAppId(String facebookAppId) {
    this.facebookAppId = facebookAppId;
  }

  @Value("${facebook.somesongs.app.namespace:}")
  public void setFacebookAppNamespace(String facebookAppNamespace) {
    this.facebookAppNamespace = facebookAppNamespace;
  }

  @Value("${facebook.somesongs.app.openGraphActionImageUrl:}")
  public void setOpenGraphActionImageUrl(String openGraphActionImageUrl) {
    this.openGraphActionImageUrl = openGraphActionImageUrl;
  }

  @Value("${twitter.somesongs.app.consumerKey:}")
  public void setTwitterAppId(String twitterAppId) {
    this.twitterAppId = twitterAppId;
  }

  @Value("${soundcloud.somesongs.app.clientId:}")
  public void setSoundcloudAppId(String soundcloudAppId) {
    this.soundcloudAppId = soundcloudAppId;
  }
}
