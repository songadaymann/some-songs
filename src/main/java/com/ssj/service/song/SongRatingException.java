package com.ssj.service.song;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class SongRatingException extends RuntimeException {

  public SongRatingException() {
    super();
  }

  public SongRatingException(String s) {
    super(s);
  }

  public SongRatingException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public SongRatingException(Throwable throwable) {
    super(throwable);
  }
}
