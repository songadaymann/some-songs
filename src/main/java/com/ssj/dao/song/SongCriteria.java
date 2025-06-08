package com.ssj.dao.song;

import com.ssj.model.base.Rating;
import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.SongRating;
import com.ssj.model.song.SongComment;
import com.ssj.model.user.FavoriteSong;
import com.ssj.model.user.FavoriteArtist;
import com.ssj.model.user.PreferredUser;
import com.ssj.dao.SearchCriteria;
import org.hibernate.criterion.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * User: sam
 * Date: Mar 7, 2007
 * Time: 9:45:01 PM
 * $Id$
 */
public class SongCriteria extends SearchCriteria<SongSearch> {

  protected void populateCriteria(List<Criterion> criteriaList) {
    SongSearch search = getSearch();

    if (StringUtils.isNotEmpty(search.getTitle())) {
      if (search.isTitleExactMatch()) {
        criteriaList.add(Restrictions.eq("title", search.getTitle()));
      } else {
        criteriaList.add(Restrictions.ilike("title", search.getTitle(), MatchMode.ANYWHERE));
      }
    }

    if (StringUtils.isNotEmpty(search.getArtistName())) {
      addAlias("artist", "a");
      if (search.isArtistNameExactMatch()) {
        criteriaList.add(Restrictions.eq("a.name", search.getArtistName()));
      } else {
        criteriaList.add(Restrictions.ilike("a.name", search.getArtistName(), MatchMode.ANYWHERE));
      }
    }

    if (StringUtils.isNotEmpty(search.getAlbum())) {
      if (search.isAlbumExactMatch()) {
        criteriaList.add(Restrictions.eq("album", search.getAlbum()));
      } else {
        criteriaList.add(Restrictions.ilike("album", search.getAlbum(), MatchMode.ANYWHERE));
      }
    }

    handleRange(criteriaList, "numRatings", search.getNumRatingsMin(), search.getNumRatingsMax());

    if (search.getDatePosted() != null) {
      Date createDateMin = null;
      Date createDateMax = null;
      if (search.getDatePosted() > 0) {
        // positive value means songs since that many days ago
        Date date = new Date();
        date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        if (search.getDatePosted() > 1) {
          date = DateUtils.addDays(date, -search.getDatePosted());
        }
        createDateMin = date;
      } else if (search.getDatePosted() < 0) {
        // negative value means songs -before- that many days ago
        Date date = new Date();
        date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        if (search.getDatePosted() < -1) {
          date = DateUtils.addDays(date, search.getDatePosted());
        }
        createDateMax = date;
      }
      handleRange(criteriaList, "createDate", createDateMin, createDateMax);
    } else {
      // date posted not being used, use date min/max params instead
      handleRange(criteriaList, "createDate", search.getCreateDateMin(), search.getCreateDateMax());
    }

    if (search.getHidden() != null) {
      criteriaList.add(Restrictions.eq("showSong", !search.getHidden()));
    }

    if (search.getInUsersFavorites() != null) {
      DetachedCriteria favoriteSongIds = DetachedCriteria.forClass(FavoriteSong.class);
      favoriteSongIds.setProjection(Property.forName("song.id"));
      favoriteSongIds.add(Restrictions.eq("user.id", search.getInUsersFavorites()));
      criteriaList.add(Subqueries.propertyIn("id", favoriteSongIds));
    }

    if (search.getInUsersFavoriteArtists() != null) {
      addAlias("artist", "a");
      DetachedCriteria favoriteArtistIds = DetachedCriteria.forClass(FavoriteArtist.class);
      favoriteArtistIds.setProjection(Property.forName("artist.id"));
      favoriteArtistIds.add(Restrictions.eq("user.id", search.getInUsersFavoriteArtists()));
      criteriaList.add(Subqueries.propertyIn("a.id", favoriteArtistIds));
    }

    if (search.getHasCommentByUser() != null) {
      DetachedCriteria commentedSongIds = DetachedCriteria.forClass(SongComment.class);
      commentedSongIds.setProjection(Property.forName("song.id"));
      commentedSongIds.add(Restrictions.eq("user.id", search.getHasCommentByUser()));
      criteriaList.add(Subqueries.propertyIn("id", commentedSongIds));
    }

    if (search.getNotRatedByUser() != null) {
      DetachedCriteria ratedSongIds = DetachedCriteria.forClass(SongRating.class);
      ratedSongIds.setProjection( Property.forName("song.id"));
      ratedSongIds.add(Restrictions.eq("user.id", search.getNotRatedByUser()));
      criteriaList.add(Subqueries.propertyNotIn("id", ratedSongIds));
    } else if (search.getRatedGoodByUser() != null) {
      DetachedCriteria ratedSongIds = DetachedCriteria.forClass(SongRating.class);
      ratedSongIds.setProjection( Property.forName("song.id"));
      ratedSongIds.add(Restrictions.eq("user.id", search.getRatedGoodByUser()));
      ratedSongIds.add(Restrictions.eq("rating", Rating.RATING_GOOD));
      criteriaList.add(Subqueries.propertyIn("id", ratedSongIds));
    }

    if (search.getInPreferredUsersFavorites() != null) {
      // subselect to get ids of user's preferred users
      DetachedCriteria preferredUserIds = DetachedCriteria.forClass(PreferredUser.class);
      preferredUserIds.setProjection(Property.forName("preferredUser.id"));
      preferredUserIds.add(Restrictions.eq("user.id", search.getInPreferredUsersFavorites()));

      // subselect to get favorite song ids of users's preferred users
      DetachedCriteria preferredUsersFavoriteSongIds = DetachedCriteria.forClass(FavoriteSong.class);
      preferredUsersFavoriteSongIds.setProjection(Property.forName("song.id"));
      preferredUsersFavoriteSongIds.add(Subqueries.propertyIn("user.id", preferredUserIds));

      criteriaList.add(Subqueries.propertyIn("id", preferredUsersFavoriteSongIds));
    }

    // TODO other criteria
  }

}
