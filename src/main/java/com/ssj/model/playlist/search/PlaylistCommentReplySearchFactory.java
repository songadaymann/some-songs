package com.ssj.model.playlist.search;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class PlaylistCommentReplySearchFactory {

  public static final int SEARCH_ID_RECENT = -1;

  public static PlaylistCommentReplySearch getSearch(int searchId) {
    PlaylistCommentReplySearch search;
    switch(searchId) {
      case SEARCH_ID_RECENT:
      default:
        search = getRecentRepliesSearch();
        break;
    }
    return search;
  }

  public static final String NAME_RECENT_REPLIES = "Recent Replies to Comments";

  private static PlaylistCommentReplySearch getRecentRepliesSearch() {
    PlaylistCommentReplySearch search = new PlaylistCommentReplySearch();

    search.setName(NAME_RECENT_REPLIES);
    search.setResultsPerPage(10);
    search.setResultsPerNextPage(25);
    search.setOrderBy(PlaylistCommentSearch.ORDER_BY_CREATE_DATE);
    search.setId(SEARCH_ID_RECENT);

    return search;
  }
}