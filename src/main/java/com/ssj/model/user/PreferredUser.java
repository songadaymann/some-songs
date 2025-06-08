package com.ssj.model.user;

import javax.persistence.*;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
public class PreferredUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private User user;
  @ManyToOne
  @JoinColumn(name = "preferredUserId", nullable = false)
  private User preferredUser;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;


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

  public User getPreferredUser() {
    return preferredUser;
  }

  public void setPreferredUser(User preferredUser) {
    this.preferredUser = preferredUser;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
