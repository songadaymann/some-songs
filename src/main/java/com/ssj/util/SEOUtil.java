package com.ssj.util;

/**
 * Search engine optimization related static utility methods.
 */
public class SEOUtil {

  public static final String NOT_ALPHA_NUMERIC_SPACE_REGEX = "[^a-zA-Z0-9 ]";

  public static String cleanForUrl(String title) {
    title = title.toLowerCase();
    title = title.replaceAll(NOT_ALPHA_NUMERIC_SPACE_REGEX,"");
    title = title.replace(' ', '-');
    if (title.length() > 35) {
      int indexOfDashPastThirtyFive = title.indexOf("-", 35);
      if (indexOfDashPastThirtyFive > -1) {
        title = title.substring(0, indexOfDashPastThirtyFive);
      }
    }
    return title;
  } 
}
