package com.ssj.service.synch;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class BandcampSynchServiceException extends RuntimeException {
  public BandcampSynchServiceException() {
  }

  public BandcampSynchServiceException(String s) {
    super(s);
  }

  public BandcampSynchServiceException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public BandcampSynchServiceException(Throwable throwable) {
    super(throwable);
  }
}
