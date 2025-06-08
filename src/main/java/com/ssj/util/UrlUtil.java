package com.ssj.util;

public class UrlUtil {

  public static String trimQueryString(String url) {
    if (url == null || url.indexOf('?') < 0) return url;

    return url.substring(0, url.indexOf('?'));
  }
}
