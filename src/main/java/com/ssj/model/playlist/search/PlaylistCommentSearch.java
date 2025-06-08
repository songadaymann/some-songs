package com.ssj.model.playlist.search;

import com.ssj.search.SearchBase;
import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class PlaylistCommentSearch extends SearchBase {

  public static final int ORDER_BY_CREATE_DATE = 0;

  private static final String DEFAULT_NAME = "Comment Search";

  private static final int DEFAULT_RESULTS_PER_PAGE = 10;

  private int id;

  private String name = DEFAULT_NAME;

  private User user;

  private Playlist playlist;

  private String commentText;

  private String userDisplayName;

  private Integer notByIgnoredUsers;

  public PlaylistCommentSearch() {
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Playlist getPlaylist() {
    return playlist;
  }

  public void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
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
