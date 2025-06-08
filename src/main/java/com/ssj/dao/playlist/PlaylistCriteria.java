package com.ssj.dao.playlist;

import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.playlist.PlaylistRating;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.user.FavoritePlaylist;
import com.ssj.model.user.PreferredUser;
import com.ssj.dao.SearchCriteria;
import org.hibernate.criterion.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class PlaylistCriteria extends SearchCriteria<PlaylistSearch> {
  protected void populateCriteria(List<Criterion> criteriaList) {
    PlaylistSearch search = getSearch();

    if (StringUtils.isNotEmpty(search.getTitle())) {
      if (search.isTitleExactMatch()) {
        criteriaList.add(Restrictions.eq("title", search.getTitle()));
      } else {
        criteriaList.add(Restrictions.ilike("title", search.getTitle(), MatchMode.ANYWHERE));
      }
    }

    if (StringUtils.isNotEmpty(search.getUserDisplayName())) {
      addAlias("user", "u");
      if (search.isUserDisplayNameExactMatch()) {
        criteriaList.add(Restrictions.eq("u.displayName", search.getUserDisplayName()));
      } else {
        criteriaList.add(Restrictions.ilike("u.displayName", search.getUserDisplayName(), MatchMode.ANYWHERE));
      }
    }

    handleRange(criteriaList, "numRatings", search.getNumRatingsMin(), search.getNumRatingsMax());

    handleRange(criteriaList, "numItems", search.getNumItemsMin(), search.getNumItemsMax());

    if (search.getDatePosted() != null) {
      Date createDateMin = null;
      Date createDateMax = null;
      if (search.getDatePosted() > 0) {
        // positive value means playlists since that many days ago
        Date date = new Date();
        date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        if (search.getDatePosted() > 1) {
          date = DateUtils.addDays(date, -search.getDatePosted());
        }
        createDateMin = date;
      } else if (search.getDatePosted() < 0) {
        // negative value means playlists -before- that many days ago
        createDateMin = null;
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
      criteriaList.add(Restrictions.eq("publiclyAvailable", !search.getHidden()));
    }

    if (search.getInUsersFavorites() != null) {
      DetachedCriteria favoritePlaylistIds = DetachedCriteria.forClass(FavoritePlaylist.class);
      favoritePlaylistIds.setProjection(Property.forName("playlist.id"));
      favoritePlaylistIds.add(Restrictions.eq("user.id", search.getInUsersFavorites()));
      criteriaList.add(Subqueries.propertyIn("id", favoritePlaylistIds));
    }

    if (search.getInUsersPreferredUsers() != null) {
      addAlias("user", "u");
      DetachedCriteria preferredUserIds = DetachedCriteria.forClass(PreferredUser.class);
      preferredUserIds.setProjection(Property.forName("preferredUser.id"));
      preferredUserIds.add(Restrictions.eq("user.id", search.getInUsersPreferredUsers()));
      criteriaList.add(Subqueries.propertyIn("u.id", preferredUserIds));
    }

    if (search.getHasCommentByUser() != null) {
      DetachedCriteria commentedPlaylistIds = DetachedCriteria.forClass(PlaylistComment.class);
      commentedPlaylistIds.setProjection(Property.forName("playlist.id"));
      commentedPlaylistIds.add(Restrictions.eq("user.id", search.getHasCommentByUser()));
      criteriaList.add(Subqueries.propertyIn("id", commentedPlaylistIds));
    }

    if (search.getNotRatedByUser() != null) {
      DetachedCriteria ratedPlaylistIds = DetachedCriteria.forClass(PlaylistRating.class);
      ratedPlaylistIds.setProjection( Property.forName("playlist.id"));
      ratedPlaylistIds.add(Restrictions.eq("user.id", search.getNotRatedByUser()));
      criteriaList.add(Subqueries.propertyNotIn("id", ratedPlaylistIds));
    }

    if (search.getInPreferredUsersFavorites() != null) {
      // subselect to get ids of user's preferred users
      DetachedCriteria preferredUserIds = DetachedCriteria.forClass(PreferredUser.class);
      preferredUserIds.setProjection(Property.forName("preferredUser.id"));
      preferredUserIds.add(Restrictions.eq("user.id", search.getInPreferredUsersFavorites()));

      // subselect to get favorite playlist ids of users's preferred users
      DetachedCriteria preferredUsersFavoritePlaylistIds = DetachedCriteria.forClass(FavoritePlaylist.class);
      preferredUsersFavoritePlaylistIds.setProjection(Property.forName("playlist.id"));
      preferredUsersFavoritePlaylistIds.add(Subqueries.propertyIn("user.id", preferredUserIds));

      criteriaList.add(Subqueries.propertyIn("id", preferredUsersFavoritePlaylistIds));
    }

    // TODO other criteria

  }
}
