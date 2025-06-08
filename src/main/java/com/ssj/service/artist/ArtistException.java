package com.ssj.service.artist;

/**
 * User: sam
 * Date: Mar 9, 2007
 * Time: 9:18:45 AM
 * $Id$
 */
public class ArtistException extends RuntimeException {

  public ArtistException() {
    super();
  }

  public ArtistException(Exception e) {
    super(e);
  }

  public ArtistException(String message) {
    super(message);
  }
}
