package com.ssj.model.song.search;

import com.ssj.search.SearchBase;
import com.ssj.model.user.User;

/**
 * @version $Id$
 */
public class SongSearchSearch extends SearchBase {
  
  public static final int ORDER_BY_NAME = 0;
  public static final int ORDER_BY_CREATE_DATE = 1;

  public static final String NAME_DEFAULT = "Searches";

  private User user;

  public SongSearchSearch() {
    super();
    setOrderBy(ORDER_BY_CREATE_DATE);
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
