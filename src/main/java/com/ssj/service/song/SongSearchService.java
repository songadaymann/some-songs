package com.ssj.service.song;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.search.SongSearchSearch;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SongSearchService {

  public SongSearch getDefaultSearch();

  public SongSearch getSongSearch(int searchId);

  public void saveSearch(SongSearch search);

  public void deleteSearch(SongSearch search);

  public int countSavedSearches(SongSearchSearch searchSearch);

  public List findSavedSearches(SongSearchSearch searchSearch);

}
