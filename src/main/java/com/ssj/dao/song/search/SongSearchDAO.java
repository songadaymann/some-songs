package com.ssj.dao.song.search;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.search.SongSearchSearch;
import com.ssj.model.user.User;
import com.ssj.dao.SearchDAO;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SongSearchDAO extends SearchDAO<SongSearch, SongSearchSearch> {

  public int countSearches(User user);

  public List getSearches(User user, int numResults);

}
