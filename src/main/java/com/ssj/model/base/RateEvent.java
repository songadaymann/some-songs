package com.ssj.model.base;

import com.ssj.model.user.UserEvent;

public abstract class RateEvent extends UserEvent {
  protected RateEvent(Object source, Rating rating) {
    super(source, rating.getUser());
    setAction("rate");
  }
}
