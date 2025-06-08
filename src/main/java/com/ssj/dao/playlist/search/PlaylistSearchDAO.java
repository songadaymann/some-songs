package com.ssj.dao.playlist.search;

import com.ssj.model.user.User;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.dao.DAO;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistSearchDAO extends DAO<PlaylistSearch> {

  public int countSearches(User user);

  public List getSearches(User user, int numResults);
}