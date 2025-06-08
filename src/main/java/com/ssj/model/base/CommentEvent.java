package com.ssj.model.base;

import com.ssj.model.user.UserEvent;

public abstract class CommentEvent extends UserEvent {

  public static final String ADD_ACTION_VALUE = "add";
  public static final String EDIT_ACTION_VALUE = "edit";
  public static final String DELETE_ACTION_VALUE = "delete";

  public CommentEvent(Object source, Comment comment) {
    super(source, comment.getUser());
    setAction("comment");
  }
}
