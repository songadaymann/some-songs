package com.ssj.model.messageboard;

import com.ssj.model.user.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * User: sam
 * Date: Feb 27, 2007
 * Time: 11:36:56 PM
 * $Id$
 */
@Entity
public class MessageBoardPost {

  public static final int CONTENT_TOTAL_MAX_LENGTH = 4000;
  private static final int CONTENT_MAX_LENGTH = 1600;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "postId")
  private int id;

  @ManyToOne
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topicId")
  private MessageBoardTopic topic;

  @ManyToOne
  @JoinColumn(name = "originalPostId")
  private MessageBoardPost originalPost;

//  @NotEmpty(message = "Subject is required")
  @Length(min = 4, max = 64, message = "Subject must be 4 to 64 characters")
  private String subject;

//  @NotEmpty(message = "Content is required")
  @Length(min = 4, max = 4000, message = "Content must be 4 to 4000 characters")
  @Column(nullable = false)
  @Lob
  private String content;
  @Lob
  private String moreContent;

  @OneToMany(mappedBy = "originalPost", cascade = CascadeType.ALL)
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<MessageBoardPost> replies;

  @Column(nullable = false)
  private int numReplies;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastReplyDate;

  @Column(nullable = false, columnDefinition = "BOOLEAN")
  private boolean locked;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public MessageBoardPost() {

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

  public MessageBoardTopic getTopic() {
    return topic;
  }

  public void setTopic(MessageBoardTopic topic) {
    this.topic = topic;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMoreContent() {
    return moreContent;
  }

  public void setMoreContent(String moreContent) {
    this.moreContent = moreContent;
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

  public Set<MessageBoardPost> getReplies() {
    return replies;
  }

  public void setReplies(Set<MessageBoardPost> replies) {
    this.replies = replies;
  }

  public Date getLastReplyDate() {
    return lastReplyDate;
  }

  public void setLastReplyDate(Date lastReplyDate) {
    this.lastReplyDate = lastReplyDate;
  }

  public MessageBoardPost getOriginalPost() {
    return originalPost;
  }

  public void setOriginalPost(MessageBoardPost originalPost) {
    this.originalPost = originalPost;
  }

  public void mergeContent() {
    if (content != null) {
      if (moreContent != null) {
        content += moreContent;
        moreContent = null;
      }
    } else if (moreContent != null) {
      content = moreContent;
      moreContent = null;
    }
  }

  public void splitContent() {
    if (content == null || content.length() <= CONTENT_MAX_LENGTH) {
      moreContent = null;
    } else {
      moreContent = content.substring(CONTENT_MAX_LENGTH);
      content = content.substring(0, CONTENT_MAX_LENGTH);
    }
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public int getNumReplies() {
    return numReplies;
  }

  public void setNumReplies(int numReplies) {
    this.numReplies = numReplies;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MessageBoardPost)) return false;

    MessageBoardPost that = (MessageBoardPost) o;

    if (id != that.id) return false;
    if (content != null ? !content.equals(that.content) : that.content != null) return false;
    if (moreContent != null ? !moreContent.equals(that.moreContent) : that.moreContent != null) return false;
    if (originalPost != null ? !originalPost.equals(that.originalPost) : that.originalPost != null) return false;
    if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
    if (topic != null ? !topic.equals(that.topic) : that.topic != null) return false;
    if (user != null ? !user.equals(that.user) : that.user != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = id;
    result = 31 * result + (user != null ? user.hashCode() : 0);
    result = 31 * result + (topic != null ? topic.hashCode() : 0);
    result = 31 * result + (originalPost != null ? originalPost.hashCode() : 0);
    result = 31 * result + (subject != null ? subject.hashCode() : 0);
    result = 31 * result + (content != null ? content.hashCode() : 0);
    result = 31 * result + (moreContent != null ? moreContent.hashCode() : 0);
    return result;
  }
}