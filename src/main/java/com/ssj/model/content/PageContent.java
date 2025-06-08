package com.ssj.model.content;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
public class PageContent {

  public static final int TYPE_FAQ = 1;
  public static final int TYPE_FIRST_TIME = 2;
  public static final int TYPE_CONTACT_ADMIN = 3;

/*
  public static final String LABEL_FAQ = "FAQ";
  public static final String LABEL_FIRST_TIME = "First Time";

  private static final Map<Integer, String> TYPE_LABEL_MAP = new HashMap<Integer, String>();

  static {
    TYPE_LABEL_MAP.put(TYPE_FAQ, LABEL_FAQ);
    TYPE_LABEL_MAP.put(TYPE_FIRST_TIME, LABEL_FIRST_TIME);
  }

  public static String getLabel(int type) {
    return TYPE_LABEL_MAP.get(type);
  }
*/

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "pageContentId")
  private int id;

  @Column(nullable = false)
  private int type;

  @NotEmpty(message = "Content is required")
  @Length(min = 4, max = 8192, message = "Content must be 4 to 8192 characters long")
  @Column(nullable = false)
  @Lob
  private String content;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public PageContent() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
