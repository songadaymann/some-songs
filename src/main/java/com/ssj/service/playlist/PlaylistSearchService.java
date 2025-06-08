package com.ssj.service.playlist;

import com.ssj.model.user.User;
import com.ssj.model.playlist.search.PlaylistSearch;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistSearchService {

  public PlaylistSearch getDefaultSearch();

  public PlaylistSearch getPlaylistSearch(int searchId);

  public void saveSearch(PlaylistSearch search);

  public void deleteSearch(PlaylistSearch search);

  public int countSavedSearches(User user);

  public List getSavedSearches(User user, int numSearches);

}