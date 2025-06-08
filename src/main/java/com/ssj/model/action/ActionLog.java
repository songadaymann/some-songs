package com.ssj.model.action;

import com.ssj.model.user.User;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * A log of an action performed by a user on an object in the system.
 */
@Entity
public class ActionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "actionLogId")
  private int id;

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @Column(nullable = false)
  private String action;

  private Integer objectId;

  private String objectType;

  private String actionValue;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date time;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

  public void setObjectType(String objectType) {
    this.objectType = objectType;
  }

  public String getActionValue() {
    return actionValue;
  }

  public void setActionValue(String actionValue) {
    this.actionValue = actionValue;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }
}
