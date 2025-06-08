package com.ssj.service.playlist;

import com.ssj.model.user.User;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.playlist.search.PlaylistSearchFactory;
import com.ssj.dao.playlist.search.PlaylistSearchDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
@Transactional(readOnly = true)
public class PlaylistSearchServiceImpl implements PlaylistSearchService {

  private PlaylistSearchDAO playlistSearchDAO;

  public PlaylistSearch getDefaultSearch() {
    return PlaylistSearchFactory.getSearch(PlaylistSearchFactory.SEARCH_ID_NEWEST);
  }

  public PlaylistSearch getPlaylistSearch(int searchId) {
    if (searchId == 0) {
      // zero is not a valid playlist search id
      throw new IllegalArgumentException("Search id must not be zero");
    }
    PlaylistSearch search = null;
    if (searchId > 0) {
      // retrieve from db using dao
      search = playlistSearchDAO.get(searchId);
    } else if (searchId < 0) {
      // retrieve from PlaylistSearchFactory
      search = PlaylistSearchFactory.getSearch(searchId);

    }
    return search;
  }

  @Transactional(readOnly = false)
  public void saveSearch(PlaylistSearch search) {
    playlistSearchDAO.save(search);
  }

  @Transactional(readOnly = false)
  public void deleteSearch(PlaylistSearch search) {
    playlistSearchDAO.delete(search);
  }

  public int countSavedSearches(User user) {
    return playlistSearchDAO.countSearches(user);
  }

  public List getSavedSearches(User user, int numSearches) {
    return playlistSearchDAO.getSearches(user, numSearches);
  }

  @Autowired
  public void setPlaylistSearchDAO(PlaylistSearchDAO playlistSearchDAO) {
    this.playlistSearchDAO = playlistSearchDAO;
  }
}