package com.ssj.web.spring.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException() {
    super();
  }

  public NotFoundException(String s) {
    super(s);
  }

  public NotFoundException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public NotFoundException(Throwable throwable) {
    super(throwable);
  }
}
