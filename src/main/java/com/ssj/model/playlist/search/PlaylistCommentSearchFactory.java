package com.ssj.model.playlist.search;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class PlaylistCommentSearchFactory {

  public static final int SEARCH_ID_RECENT = -1;

  public static PlaylistCommentSearch getSearch(int searchId) {
    PlaylistCommentSearch search;
    switch(searchId) {
      case SEARCH_ID_RECENT:
      default:
        search = getRecentCommentsSearch();
        break;
    }
    return search;
  }

  public static final String NAME_RECENT_COMMENTS = "Recent Comments";

  private static PlaylistCommentSearch getRecentCommentsSearch() {
    PlaylistCommentSearch search = new PlaylistCommentSearch();

    search.setName(NAME_RECENT_COMMENTS);
    search.setResultsPerPage(10);
    search.setResultsPerNextPage(25);
    search.setOrderBy(PlaylistCommentSearch.ORDER_BY_CREATE_DATE);
    search.setId(SEARCH_ID_RECENT);

    return search;
  }
}
