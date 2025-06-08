package com.ssj.web.util;

import org.apache.log4j.Logger;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.io.IOException;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class URLChecker {

  private static final Logger LOGGER = Logger.getLogger(URLChecker.class);
  
  private String urlString;

  public URLChecker(String urlString) {
    this.urlString = urlString;
  }

  public boolean isBroken() {
    boolean isBroken = true;
    try {
      LOGGER.debug("Checking url " + urlString);
      URL url = new URL(urlString);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(1000);
      connection.setReadTimeout(1000);
      // don't follow redirects, the link should go right to the mp3
      connection.setInstanceFollowRedirects(false);
      int responseCode = connection.getResponseCode();
      LOGGER.debug("Response code = " + responseCode);

      // let's consider the link broken if the response code isn't 2xx
      isBroken = (responseCode / 100 != 2);

    } catch (MalformedURLException e) {
      LOGGER.error(e);
    } catch (IOException e) {
      LOGGER.error(e);
    }

    return isBroken;
  }
}
