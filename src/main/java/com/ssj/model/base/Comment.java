package com.ssj.model.base;

import com.ssj.model.user.User;

import javax.persistence.*;
import java.util.Set;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@MappedSuperclass
public abstract class Comment<ItemType> {

  public static final int COMMENT_TOTAL_MAX_LENGTH = 4000;
  private static final int COMMENT_TEXT_MAX_LENGTH = 1600;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @Column(nullable = false)
  @Lob
  private String commentText;
  @Lob
  private String moreCommentText;

  @Column(nullable = false)
  private int numReplies;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public Comment() {

  }

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

  public abstract ItemType getItem();
  public abstract void setItem(ItemType item);

  public String getCommentText() {
    return commentText;
  }

  public void setCommentText(String commentText) {
    this.commentText = commentText;
  }

  public String getMoreCommentText() {
    return moreCommentText;
  }

  public void setMoreCommentText(String moreCommentText) {
    this.moreCommentText = moreCommentText;
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

  public void mergeContent() {
    if (commentText != null) {
      if (moreCommentText != null) {
        commentText += moreCommentText;
        moreCommentText = null;
      }
    } else if (moreCommentText != null) {
      commentText = moreCommentText;
      moreCommentText = null;
    }
  }

  public void splitContent() {
    if (commentText == null || commentText.length() <= COMMENT_TEXT_MAX_LENGTH) {
      moreCommentText = null;
    } else {
      moreCommentText = commentText.substring(COMMENT_TEXT_MAX_LENGTH);
      commentText = commentText.substring(0, COMMENT_TEXT_MAX_LENGTH);
    }
  }

  public int getNumReplies() {
    return numReplies;
  }

  public void setNumReplies(int numReplies) {
    this.numReplies = numReplies;
  }
}
