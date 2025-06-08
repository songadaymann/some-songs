package com.ssj.model.playlist.search;

import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class PlaylistSearchFactory {

  public static final int SEARCH_ID_NEWEST = -1;
  public static final int SEARCH_ID_PAST_WEEK_TOPS = -2;
  public static final int SEARCH_ID_NOT_YET_RATED_BY_USER = -3;
  public static final int SEARCH_ID_TOP_PLAYLISTS = -4;
  public static final int SEARCH_ID_RECENTLY_RATED = -5;
  public static final int SEARCH_ID_LOST_PLAYLISTS = -6;
  public static final int SEARCH_ID_BY_PREFERRED_USERS = -7;
  public static final int SEARCH_ID_PREFERRED_USERS_PICKS = -8;
  public static final int SEARCH_ID_PAST_DAY_TOPS = -9;
  public static final int SEARCH_ID_PAST_MONTH_TOPS = -11;

  public static PlaylistSearch getSearch(int searchId) {
    PlaylistSearch search = null;
    switch(searchId) {
      case SEARCH_ID_NEWEST:
        search = getNewestSearch();
        break;
      case SEARCH_ID_PAST_WEEK_TOPS:
        search = getPastWeeksTopsSearch();
        break;
      case SEARCH_ID_PAST_DAY_TOPS:
        search = getPastDaysTopsSearch();
        break;
      case SEARCH_ID_PAST_MONTH_TOPS:
        search = getPastMonthsTopsSearch();
        break;
      case SEARCH_ID_NOT_YET_RATED_BY_USER:
        search = getNotYetRatedSearch();
        break;
      case SEARCH_ID_TOP_PLAYLISTS:
        search = getTopSongsSearch();
        break;
      case SEARCH_ID_LOST_PLAYLISTS:
        search = getLostSongsSearch();
        break;
      case SEARCH_ID_BY_PREFERRED_USERS:
        search = getByFavoriteArtistsSearch();
        break;
      case SEARCH_ID_PREFERRED_USERS_PICKS:
        search = getPreferredUsersPicksSearch();
        break;
      case SEARCH_ID_RECENTLY_RATED:
        search = getRecentlyRatedSearch();
      default:
        break;
    }
    return search;
  }

  public static final String NAME_PREFERRED_USERS_PICKS = "Preferred Users' Picks";

  private static PlaylistSearch getPreferredUsersPicksSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_PREFERRED_USERS_PICKS);

    search.setId(SEARCH_ID_PREFERRED_USERS_PICKS);

    return search;
  }

  public static final String NAME_BY_FAVORITE_ARTISTS = "Favorite Artists' Songs";

  private static PlaylistSearch getByFavoriteArtistsSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_BY_FAVORITE_ARTISTS);

    search.setId(SEARCH_ID_BY_PREFERRED_USERS);

    return search;
  }

  public static final String NAME_LOST_SONGS = "Lost Songs";

  private static PlaylistSearch getLostSongsSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_LOST_SONGS);

    search.setId(SEARCH_ID_LOST_PLAYLISTS);
    search.setNumRatingsMin(1);
    search.setDatePosted(-7);
    search.setOrderBy(PlaylistSearch.ORDER_BY_LOST_SONGS);

    return search;
  }

  public static final String NAME_NOT_YET_RATED = "I Haven't Rated";

  private static PlaylistSearch getNotYetRatedSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_NOT_YET_RATED);

    search.setId(SEARCH_ID_NOT_YET_RATED_BY_USER);

    return search;
  }

  public static final String NAME_TOP_SONGS = "Top Songs";

  public static PlaylistSearch getTopSongsSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_TOP_SONGS);

    search.setId(SEARCH_ID_TOP_PLAYLISTS);
    search.setNumRatingsMin(15);
    search.setOrderBy(PlaylistSearch.ORDER_BY_AVG_RATING);

    return search;
  }

  public static final String NAME_NEWEST = "Newest Playlists";

  /**
   * order by create date, no other criteria
   */
  public static PlaylistSearch getNewestSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_NEWEST);
    search.setId(SEARCH_ID_NEWEST);
    return search;
  }

  public static final String NAME_PAST_WEEKS_TOPS = "Past Week's Top Songs";
  public static final int SONGS_PER_PAGE_TOPS = 10;

  /**
   * created within last week, at least 5 ratings, order by avg rating desc, 5 songs per page
   */
  public static PlaylistSearch getPastWeeksTopsSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_PAST_WEEKS_TOPS);
    search.setNumRatingsMin(5);
    search.setDatePosted(7);
    search.setResultsPerPage(SONGS_PER_PAGE_TOPS);
    search.setOrderBy(PlaylistSearch.ORDER_BY_AVG_RATING);
    search.setId(SEARCH_ID_PAST_WEEK_TOPS);
    return search;
  }

  private static final String NAME_PAST_DAYS_TOPS = "Past Day's Tops";

  private static PlaylistSearch getPastDaysTopsSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_PAST_DAYS_TOPS);

    search.setId(SEARCH_ID_PAST_DAY_TOPS);

    search.setNumRatingsMin(5);

    Date d = new Date();
    d = DateUtils.addDays(d, -1);
    search.setCreateDateMin(d);

    search.setOrderBy(PlaylistSearch.ORDER_BY_AVG_RATING);

    search.setResultsPerPage(10);

    return search;
  }

  private static final String NAME_PAST_MONTHS_TOPS = "Past Month's Tops";

  private static PlaylistSearch getPastMonthsTopsSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_PAST_MONTHS_TOPS);

    search.setId(SEARCH_ID_PAST_MONTH_TOPS);

    search.setNumRatingsMin(10);

    Date d = new Date();
    d = DateUtils.addDays(d, -1);
    search.setCreateDateMin(d);

    search.setOrderBy(PlaylistSearch.ORDER_BY_AVG_RATING);

    search.setResultsPerPage(10);

    return search;
  }

  public static final String NAME_RECENTLY_RATED = "Recently Rated";
  public static final int SONGS_PER_PAGE_RECENTLY_RATED = 10;
  public static final int SONGS_PER_NEXT_PAGE_RECENTLY_RATED = 25;

  public static PlaylistSearch getRecentlyRatedSearch() {
    PlaylistSearch search = new PlaylistSearch(NAME_RECENTLY_RATED);
    search.setResultsPerPage(SONGS_PER_PAGE_RECENTLY_RATED);
    search.setResultsPerNextPage(SONGS_PER_NEXT_PAGE_RECENTLY_RATED);
    search.setNumRatingsMin(1);
    search.setOrderBy(PlaylistSearch.ORDER_BY_LAST_RATED);
    search.setId(SEARCH_ID_RECENTLY_RATED);
    return search;
  }
}
