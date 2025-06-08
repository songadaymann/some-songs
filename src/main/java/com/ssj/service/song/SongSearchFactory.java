package com.ssj.service.song;

import com.ssj.model.song.search.SongSearch;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: sam
 * Date: Mar 9, 2007
 * Time: 7:45:59 AM
 * $Id$
 */
@Component
public class SongSearchFactory {

  public static final int SEARCH_ID_NEWEST_SONGS = -1;
  public static final int SEARCH_ID_PAST_WEEK_TOPS = -2;
  public static final int SEARCH_ID_NOT_YET_RATED_BY_USER = -3;
  public static final int SEARCH_ID_TOP_SONGS = -4;
  public static final int SEARCH_ID_RECENTLY_RATED = -5;
  public static final int SEARCH_ID_LOST_SONGS = -6;
  public static final int SEARCH_ID_BY_FAVORITE_ARTISTS = -7;
  public static final int SEARCH_ID_PREFERRED_USERS_PICKS = -8;
  public static final int SEARCH_ID_PAST_DAY_TOPS = -9;
  public static final int SEARCH_ID_PAST_MONTH_TOPS = -11;
  public static final int SEARCH_ID_FAVORITE_SONGS = -12;

  private int pastMonthsTopsRatingsMin;
  private int pastDaysTopsRatingsMin;
  private int pastWeeksTopsRatingsMin;
  private int allTopsRatingsMin;

  public SongSearch getSearch(int searchId) {
    SongSearch search = null;
    switch(searchId) {
      case SEARCH_ID_NEWEST_SONGS:
        search = getNewestSongsSearch();
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
      case SEARCH_ID_TOP_SONGS:
        search = getTopSongsSearch();
        break;
      case SEARCH_ID_LOST_SONGS:
        search = getLostSongsSearch();
        break;
      case SEARCH_ID_BY_FAVORITE_ARTISTS:
        search = getByFavoriteArtistsSearch();
        break;
      case SEARCH_ID_PREFERRED_USERS_PICKS:
        search = getPreferredUsersPicksSearch();
        break;
      case SEARCH_ID_RECENTLY_RATED:
        search = getRecentlyRatedSearch();
        break;
      case SEARCH_ID_FAVORITE_SONGS:
        search = getFavoriteSongsSearch();
        break;
      default:
        break;
    }
    return search;
  }

  public static final String NAME_PREFERRED_USERS_PICKS = "Preferred Users' Picks";

  private SongSearch getPreferredUsersPicksSearch() {
    SongSearch search = new SongSearch(NAME_PREFERRED_USERS_PICKS);

    search.setId(SEARCH_ID_PREFERRED_USERS_PICKS);

    return search;
  }

  public static final String NAME_BY_FAVORITE_ARTISTS = "Favorite Artists' Songs";

  private SongSearch getByFavoriteArtistsSearch() {
    SongSearch search = new SongSearch(NAME_BY_FAVORITE_ARTISTS);

    search.setId(SEARCH_ID_BY_FAVORITE_ARTISTS);

    return search;
  }

  public static final String NAME_LOST_SONGS = "Lost Songs";

  private SongSearch getLostSongsSearch() {
    SongSearch search = new SongSearch(NAME_LOST_SONGS);

    search.setId(SEARCH_ID_LOST_SONGS);
    search.setNumRatingsMin(1);
    search.setDatePosted(-7);
    search.setOrderBy(SongSearch.ORDER_BY_LOST_SONGS);

    return search;
  }

  public static final String NAME_NOT_YET_RATED = "I Haven't Rated";

  private SongSearch getNotYetRatedSearch() {
    SongSearch search = new SongSearch(NAME_NOT_YET_RATED);

    search.setId(SEARCH_ID_NOT_YET_RATED_BY_USER);

    return search;
  }

  public static final String NAME_TOP_SONGS = "Top Songs";

  public SongSearch getTopSongsSearch() {
    SongSearch search = new SongSearch(NAME_TOP_SONGS);

    search.setId(SEARCH_ID_TOP_SONGS);
    search.setNumRatingsMin(allTopsRatingsMin);
    search.setOrderBy(SongSearch.ORDER_BY_AVG_RATING);

    return search;
  }

  public static final String NAME_NEWEST_SONGS = "Newest Songs";

  /**
   * order by create date, no other criteria
   */
  public SongSearch getNewestSongsSearch() {
    SongSearch search = new SongSearch(NAME_NEWEST_SONGS);
    search.setId(SEARCH_ID_NEWEST_SONGS);
    return search;
  }

  public static final String NAME_PAST_WEEKS_TOPS = "Past Week's Top Songs";
  public static final int SONGS_PER_PAGE_TOPS = 10;

  /**
   * created within last week, at least 5 ratings, order by avg rating desc, 5 songs per page
   */
  public SongSearch getPastWeeksTopsSearch() {
    SongSearch search = new SongSearch(NAME_PAST_WEEKS_TOPS);
    search.setNumRatingsMin(pastWeeksTopsRatingsMin);
    search.setDatePosted(7);
    search.setResultsPerPage(SONGS_PER_PAGE_TOPS);
    search.setOrderBy(SongSearch.ORDER_BY_AVG_RATING);
    search.setId(SEARCH_ID_PAST_WEEK_TOPS);
    return search;
  }

  private static final String NAME_PAST_DAYS_TOPS = "Past Day's Tops";

  private SongSearch getPastDaysTopsSearch() {
    SongSearch search = new SongSearch(NAME_PAST_DAYS_TOPS);

    search.setId(SEARCH_ID_PAST_DAY_TOPS);

    search.setNumRatingsMin(pastDaysTopsRatingsMin);

    Date d = new Date();
    d = DateUtils.addDays(d, -1);
    search.setCreateDateMin(d);

    search.setOrderBy(SongSearch.ORDER_BY_AVG_RATING);

    search.setResultsPerPage(10);

    return search;
  }

  private static final String NAME_PAST_MONTHS_TOPS = "Past Month's Tops";

  private SongSearch getPastMonthsTopsSearch() {
    SongSearch search = new SongSearch(NAME_PAST_MONTHS_TOPS);

    search.setId(SEARCH_ID_PAST_MONTH_TOPS);

    search.setNumRatingsMin(pastMonthsTopsRatingsMin);

    Date d = new Date();
    d = DateUtils.addDays(d, -30);
    search.setCreateDateMin(d);

    search.setOrderBy(SongSearch.ORDER_BY_AVG_RATING);

    search.setResultsPerPage(10);

    return search;
  }

  public static final String NAME_RECENTLY_RATED = "Recently Rated";
  public static final int SONGS_PER_PAGE_RECENTLY_RATED = 10;
  public static final int SONGS_PER_NEXT_PAGE_RECENTLY_RATED = 25;

  public SongSearch getRecentlyRatedSearch() {
    SongSearch search = new SongSearch(NAME_RECENTLY_RATED);
    search.setHidden(false);
    search.setResultsPerPage(SONGS_PER_PAGE_RECENTLY_RATED);
    search.setResultsPerNextPage(SONGS_PER_NEXT_PAGE_RECENTLY_RATED);
    search.setNumRatingsMin(1);
    search.setOrderBy(SongSearch.ORDER_BY_LAST_RATED);
    search.setId(SEARCH_ID_RECENTLY_RATED);
    return search;
  }
  
  public static final String NAME_FAVORITE_SONGS = "Favorite Songs";

  public SongSearch getFavoriteSongsSearch() {
    SongSearch search = new SongSearch(NAME_FAVORITE_SONGS);
    search.setHidden(false);
    search.setId(SEARCH_ID_FAVORITE_SONGS);
    return search;
  }

  @Value("${songSearchFactory.pastTopsRatingsMin.month:5}")
  public void setPastMonthsTopsRatingsMin(int pastMonthsTopsRatingsMin) {
    this.pastMonthsTopsRatingsMin = pastMonthsTopsRatingsMin;
  }

  @Value("${songSearchFactory.pastTopsRatingsMin.day:5}")
  public void setPastDaysTopsRatingsMin(int pastDaysTopsRatingsMin) {
    this.pastDaysTopsRatingsMin = pastDaysTopsRatingsMin;
  }

  @Value("${songSearchFactory.pastTopsRatingsMin.week:5}")
  public void setPastWeeksTopsRatingsMin(int pastWeeksTopsRatingsMin) {
    this.pastWeeksTopsRatingsMin = pastWeeksTopsRatingsMin;
  }

  @Value("${songSearchFactory.pastTopsRatingsMin.all:15}")
  public void setAllTopsRatingsMin(int allTopsRatingsMin) {
    this.allTopsRatingsMin = allTopsRatingsMin;
  }
}
