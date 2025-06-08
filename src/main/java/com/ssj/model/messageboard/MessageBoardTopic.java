package com.ssj.model.messageboard;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * User: sam
 * Date: Feb 27, 2007
 * Time: 11:37:47 PM
 * 
 * @version $Id$
 */
@Entity
public class MessageBoardTopic {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "topicId")
  private int id;

  @NotEmpty(message = "Name is required")
  @Length(min = 4 , max = 64, message = "Name must be 4 to 64 characters")
  @Column(nullable = false, unique = true)
  private String name;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public MessageBoardTopic() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
