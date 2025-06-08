package com.ssj.service.song;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.search.SongSearchSearch;
import com.ssj.dao.song.search.SongSearchDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SongSearchServiceImpl implements SongSearchService {

  private SongSearchDAO songSearchDAO;
  private SongSearchFactory songSearchFactory;

  private int defaultSearchResultsPerPage;

  public SongSearch getDefaultSearch() {
    SongSearch defaultSearch = songSearchFactory.getSearch(SongSearchFactory.SEARCH_ID_NEWEST_SONGS);
    defaultSearch.setResultsPerPage(defaultSearchResultsPerPage);
    return defaultSearch;
  }

  public SongSearch getSongSearch(int searchId) {
    if (searchId == 0) {
      // zero is not a valid song search id
      throw new IllegalArgumentException("Search id must not be zero");
    }
    SongSearch search = null;
    if (searchId > 0) {
      // retrieve from db using dao
      search = songSearchDAO.get(searchId);
    } else if (searchId < 0) {
      // retrieve from SongSearchFactory
      search = songSearchFactory.getSearch(searchId);

    }
    return search;
  }

  @Transactional(readOnly = false)
  public void saveSearch(SongSearch search) {
    songSearchDAO.save(search);
  }

  @Transactional(readOnly = false)
  public void deleteSearch(SongSearch search) {
    songSearchDAO.delete(search);
  }

  public int countSavedSearches(SongSearchSearch searchSearch) {
    return songSearchDAO.doCount(searchSearch);
  }

  public List findSavedSearches(SongSearchSearch searchSearch) {
    List<SongSearch> searches = null;
    searchSearch.setTotalResults(songSearchDAO.doCount(searchSearch));
    if (searchSearch.getTotalResults() > 0) {
      searches = songSearchDAO.doSearch(searchSearch);
    }
    return searches;
  }

  @Autowired
  public void setSongSearchDAO(SongSearchDAO songSearchDAO) {
    this.songSearchDAO = songSearchDAO;
  }

  @Autowired
  public void setSongSearchFactory(SongSearchFactory songSearchFactory) {
    this.songSearchFactory = songSearchFactory;
  }

  @Value("${defaultSearchResultsPerPage:10}")
  public void setDefaultSearchResultsPerPage(int defaultSearchResultsPerPage) {
    this.defaultSearchResultsPerPage = defaultSearchResultsPerPage;
  }
}
