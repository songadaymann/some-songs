package com.ssj.model.song.search;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class SongCommentSearchFactory {

  public static final int SEARCH_ID_RECENT = -1;

  public static SongCommentSearch getSearch(int searchId) {
    SongCommentSearch search = null;
    switch(searchId) {
      case SEARCH_ID_RECENT:
      default:
        search = getRecentCommentsSearch();
        break;
    }
    return search;
  }

  public static final String NAME_RECENT_COMMENTS = "Recent Comments";

  private static SongCommentSearch getRecentCommentsSearch() {
    SongCommentSearch search = new SongCommentSearch();

    search.setName(NAME_RECENT_COMMENTS);
    search.setOnlyVisibleSongs(true);
    search.setResultsPerPage(10);
    search.setResultsPerNextPage(25);
    search.setOrderBy(SongCommentSearch.ORDER_BY_CREATE_DATE);
    search.setId(SEARCH_ID_RECENT);

    return search;
  }
}
