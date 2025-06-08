package com.ssj.model.user;

import org.springframework.context.ApplicationEvent;

public class UserEvent extends ApplicationEvent {

  public static final String ACTION_LOGIN = "login";

  private User user;
  private String action;
  private Integer objectId;
  private String objectType;
  private String actionValue;

  /**
   * Create a new ApplicationEvent.
   *
   * @param source the component that published the event (never <code>null</code>)
   */
  public UserEvent(Object source, User user) {
    super(source);
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Integer getObjectId() {
    return objectId;
  }

  public void setObjectId(Integer objectId) {
    this.objectId = objectId;
  }

  public String getObjectType() {
    return objectType;
  }

  public void setActionValue(String actionValue) {
    this.actionValue = actionValue;
  }

  public String getActionValue() {
    return actionValue;
  }

  public void setObjectType(String objectType) {
    this.objectType = objectType;
  }
}
