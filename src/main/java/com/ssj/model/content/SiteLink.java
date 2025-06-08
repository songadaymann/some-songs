package com.ssj.model.content;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class SiteLink {
 
  public static final int HEADER_LEFT = 1;
  public static final int HEADER_RIGHT = 2;
  public static final int FOOTER_LEFT = 3;
  public static final int FOOTER_RIGHT = 4;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "linkId")
  private int id;

  @NotEmpty(message = "URL is required")
  @Length(max = 1024, message = "URL cannot be longer than 1024 characters")
  @Pattern(regexp = "https?://([a-zA-Z0-9\\-]+\\.)+([a-zA-Z][a-zA-Z0-9\\-]+)(:\\d+)?/?.*",
      message = "Please enter a valid URL for the link")
  @Column(nullable = false)
  private String url;

  @NotEmpty(message = "Label is required")
  @Length(max = 256, message = "Label cannot be longer than 256 characters")
  @Column(nullable = false)
  private String label;

  @Length(max = 512, message = "Label cannot be longer than 512 characters")
  private String hover;

  private int position;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public SiteLink() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getHover() {
    return hover;
  }

  public void setHover(String hover) {
    this.hover = hover;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
  
  public String getPositionText() {
    String positionText = null;
    switch (position) {
      case HEADER_LEFT:
        positionText = "Header Left";
        break;
      case HEADER_RIGHT:
        positionText = "Header Right";
        break;
      case FOOTER_LEFT:
        positionText = "Footer Left";
        break;
      case FOOTER_RIGHT:
        positionText = "Footer Right";
    }
    return positionText;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }
}
