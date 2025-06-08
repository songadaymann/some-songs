package com.ssj.model.base;

import com.ssj.model.user.User;
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
@MappedSuperclass
public abstract class CommentReply<CommentType extends Comment<?>> {

//  public static final int COMMENT_TOTAL_MAX_LENGTH = 4000;
  private static final int COMMENT_TEXT_MAX_LENGTH = 1600;

  private int id;

  private User user;

  private String commentText;
  private String moreCommentText;

  private Date createDate = new Date();
  private Date changeDate = createDate;

  public CommentReply() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Transient
  public abstract CommentType getOriginalComment();

  public abstract void setOriginalComment(CommentType originalComment);

  //  @NotEmpty(message = "Body content is required")
  @Length(min = 4, max = 4000, message = "Body content must be 4 to 4000 characters")
  @Column(nullable = false)
  @Lob
  public String getCommentText() {
    return commentText;
  }

  public void setCommentText(String commentText) {
    this.commentText = commentText;
  }

  @Lob
  public String getMoreCommentText() {
    return moreCommentText;
  }

  public void setMoreCommentText(String moreCommentText) {
    this.moreCommentText = moreCommentText;
  }

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Temporal(TemporalType.TIMESTAMP)
  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

}
