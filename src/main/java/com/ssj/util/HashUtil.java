package com.ssj.util;

import java.security.MessageDigest;

public class HashUtil {

  /**
   * From:
   *
   * http://www.mkyong.com/java/java-sha-hashing-example/
   *
   * Turns the given String into its SHA-256 hash in hexadecimal as a String.
   * @param content
   * @return
   */
  public static String getSha256Hash(String content) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(content.getBytes("UTF-8"));

      byte[] bytes = messageDigest.digest();

      //convert the bytes to hex format
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < bytes.length; i++) {
       sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
      }

      return sb.toString();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
