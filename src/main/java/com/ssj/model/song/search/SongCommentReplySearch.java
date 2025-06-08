package com.ssj.model.song.search;

import com.ssj.model.song.SongComment;
import com.ssj.search.SearchBase;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class SongCommentReplySearch extends SearchBase {

  public static final int ORDER_BY_CREATE_DATE = 0;

  private static final String DEFAULT_NAME = "Reply Search";

  private static final int DEFAULT_RESULTS_PER_PAGE = 10;

  private int id;

  private String name = DEFAULT_NAME;

  private SongComment originalComment;

  private boolean onlyVisibleSongs;

  private String commentText;

  private String userDisplayName;

  private Integer notByIgnoredUsers;

  public SongCommentReplySearch() {
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

  public SongComment getOriginalComment() {
    return originalComment;
  }

  public void setOriginalComment(SongComment originalComment) {
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

  public boolean isOnlyVisibleSongs() {
    return onlyVisibleSongs;
  }

  public void setOnlyVisibleSongs(boolean onlyVisibleSongs) {
    this.onlyVisibleSongs = onlyVisibleSongs;
  }
}
