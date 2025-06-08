package com.ssj.web.taglib;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class CustomELFunctions {

  public static String concat(String str1, String str2) {
    return str1 + str2;
  }

  public static boolean startsWithConcatResult(String str1, String str2, String str3) {
    return str1.startsWith(str2 + str3);
  }
}
