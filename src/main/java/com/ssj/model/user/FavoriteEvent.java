package com.ssj.model.user;

public class FavoriteEvent extends UserEvent {

  public static final String ON_ACTION_VALUE = "on";
  public static final String OFF_ACTION_VALUE = "off";

  public FavoriteEvent(Object source, User user) {
    super(source, user);
    setAction("favorite");
  }
}
