package com.ssj.model.playlist.search;

import com.ssj.search.SearchBase;
import com.ssj.model.playlist.PlaylistComment;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class PlaylistCommentReplySearch extends SearchBase {

  public static final int ORDER_BY_CREATE_DATE = 0;

  private static final String DEFAULT_NAME = "Reply Search";

  private static final int DEFAULT_RESULTS_PER_PAGE = 10;

  private int id;

  private String name = DEFAULT_NAME;

  private PlaylistComment originalComment;

  private String commentText;

  private String userDisplayName;

  private Integer notByIgnoredUsers;

  public PlaylistCommentReplySearch() {
    super();
    setResultsPerPage(DEFAULT_RESULTS_PER_PAGE);
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

  public PlaylistComment getOriginalComment() {
    return originalComment;
  }

  public void setOriginalComment(PlaylistComment originalComment) {
    this.originalComment = originalComment;
  }

  public String getCommentText() {
    return commentText;
  }

  public void setCommentText(String commentText) {
    this.commentText = commentText;
  }

  public String getUserDisplayName() {
    return userDisplayName;
  }

  public void setUserDisplayName(String userDisplayName) {
    this.userDisplayName = userDisplayName;
  }

  public Integer getNotByIgnoredUsers() {
    return notByIgnoredUsers;
  }

  public void setNotByIgnoredUsers(Integer notByIgnoredUsers) {
    this.notByIgnoredUsers = notByIgnoredUsers;
  }
}
