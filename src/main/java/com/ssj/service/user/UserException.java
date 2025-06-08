package com.ssj.service.user;

/**
 * 
 *
 * @version $Id$
 */
public class UserException extends RuntimeException {

  public UserException() {
    super();
  }

  public UserException(Exception e) {
    super(e);
  }

  public UserException(String message) {
    super(message);
  }
}
