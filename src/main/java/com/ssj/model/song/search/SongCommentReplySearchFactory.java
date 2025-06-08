package com.ssj.model.song.search;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class SongCommentReplySearchFactory {

  public static final int SEARCH_ID_RECENT = -1;

  public static SongCommentReplySearch getSearch(int searchId) {
    SongCommentReplySearch search = null;
    switch(searchId) {
      case SEARCH_ID_RECENT:
      default:
        search = getRecentRepliesSearch();
        break;
    }
    return search;
  }

  public static final String NAME_RECENT_REPLIES = "Recent Replies to Comments";

  private static SongCommentReplySearch getRecentRepliesSearch() {
    SongCommentReplySearch search = new SongCommentReplySearch();

    search.setName(NAME_RECENT_REPLIES);
    search.setOnlyVisibleSongs(true);
    search.setResultsPerPage(10);
    search.setResultsPerNextPage(25);
    search.setOrderBy(SongCommentSearch.ORDER_BY_CREATE_DATE);
    search.setId(SEARCH_ID_RECENT);

    return search;
  }
}