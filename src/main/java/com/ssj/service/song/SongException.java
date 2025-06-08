package com.ssj.service.song;

/**
 * User: sam
 * Date: Mar 7, 2007
 * Time: 11:49:14 PM
 * $Id$
 */
public class SongException extends RuntimeException {

  public SongException() {
    super();
  }

  public SongException(Exception e) {
    super(e);
  }

  public SongException(String message) {
    super(message);
  }

}
