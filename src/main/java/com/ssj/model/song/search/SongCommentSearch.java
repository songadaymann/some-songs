/*
 * Copyright 2002-2007 GeneticMail LLC, all rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
License
 * on the World Wide Web for more details:
 * http://www.fsf.org/licensing/licenses/gpl.txt
 */

package com.ssj.model.song.search;

import com.ssj.search.SearchBase;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;

/**
 * User: sam
 * Date: Sep 12, 2007
 * Time: 9:26:32 AM
 * $Id$
 */
public class SongCommentSearch extends SearchBase {

  public static final int ORDER_BY_CREATE_DATE = 0;

  private static final String DEFAULT_NAME = "Comment Search";

  private static final int DEFAULT_RESULTS_PER_PAGE = 10;

  private int id;

  private String name = DEFAULT_NAME;

  private User user;

  private Song song;

  private boolean onlyVisibleSongs;

  private String commentText;

  private String userDisplayName;

  private Integer notByIgnoredUsers;

  public SongCommentSearch() {
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

  public Song getSong() {
    return song;
  }

  public void setSong(Song song) {
    this.song = song;
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
