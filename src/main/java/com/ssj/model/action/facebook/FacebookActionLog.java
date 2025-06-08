package com.ssj.model.action.facebook;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * Log of an Open Graph action that was posted to Facebook for a user.
 */
@Entity
public class FacebookActionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "fbActionLogId")
  private int id;

  @Column(nullable = false)
  private String facebookUserId;

  @Column(nullable = false)
  private String facebookGraphId;

  @Column(nullable = false)
  @Index(name = "action_object_index")
  private String action;

  @Column(nullable = false)
  @Index(name = "action_object_index")
  private String object;

  @Column(nullable = false)
  @Index(name = "action_object_index")
  private int objectId;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created = new Date();

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date published;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFacebookUserId() {
    return facebookUserId;
  }

  public void setFacebookUserId(String facebookUserId) {
    this.facebookUserId = facebookUserId;
  }

  public String getFacebookGraphId() {
    return facebookGraphId;
  }

  public void setFacebookGraphId(String facebookGraphId) {
    this.facebookGraphId = facebookGraphId;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public int getObjectId() {
    return objectId;
  }

  public void setObjectId(int objectId) {
    this.objectId = objectId;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getPublished() {
    return published;
  }

  public void setPublished(Date published) {
    this.published = published;
  }
}
