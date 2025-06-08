package com.ssj.model.messageboard.search;

import com.ssj.search.SearchBase;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.messageboard.MessageBoardPost;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class MessageBoardPostSearch extends SearchBase {

  public static final int ORDER_BY_LAST_REPLY_DATE = 1;
  public static final int ORDER_BY_POST_DATE = 2;
  public static final int ORDER_BY_POST_DATE_ASC = 3;

  private static final int DEFAULT_THREADS_PER_PAGE = 15;

  private MessageBoardTopic topic;
  private boolean onlyThreads;
  private MessageBoardPost originalPost;
  private Integer notByIgnoredUsers;
  private String content;
  private String authorName;

  public MessageBoardPostSearch() {
    super();
    setOrderBy(ORDER_BY_LAST_REPLY_DATE);
    setResultsPerPage(DEFAULT_THREADS_PER_PAGE);
  }

  public boolean getOnlyThreads() {
    return onlyThreads;
  }

  public void setOnlyThreads(boolean onlyThreads) {
    this.onlyThreads = onlyThreads;
  }

  public MessageBoardTopic getTopic() {
    return topic;
  }

  public void setTopic(MessageBoardTopic topic) {
    this.topic = topic;
  }

  public MessageBoardPost getOriginalPost() {
    return originalPost;
  }

  public void setOriginalPost(MessageBoardPost originalPost) {
    this.originalPost = originalPost;
  }

  public void setNotByIgnoredUsers(Integer notByIgnoredUsers) {
    this.notByIgnoredUsers = notByIgnoredUsers;
  }

  public Integer getNotByIgnoredUsers() {
    return notByIgnoredUsers;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }
}