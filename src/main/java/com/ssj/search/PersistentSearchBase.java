package com.ssj.search;

import com.ssj.model.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@MappedSuperclass
public abstract class PersistentSearchBase extends SearchBase {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @Column(nullable = false)
  private String name;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
