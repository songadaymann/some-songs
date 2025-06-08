package com.ssj.dao.playlist;

import com.ssj.dao.SearchCriteria;
import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.model.user.IgnoredUser;
import org.hibernate.criterion.*;
import org.hibernate.FetchMode;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class PlaylistCommentCriteria extends SearchCriteria<PlaylistCommentSearch> {
  protected void populateCriteria(List<Criterion> criteriaList) {
    PlaylistCommentSearch search = getSearch();

    if (search.getUser() != null) {
      criteriaList.add(Restrictions.eq("user", search.getUser()));
    }

    if (search.getPlaylist() != null) {
      criteriaList.add(Restrictions.eq("playlist", search.getPlaylist()));
    }

    if (search.getCommentText() != null && search.getCommentText().trim().length() > 0) {
      Disjunction disjunction = Restrictions.disjunction();
      disjunction.add(Restrictions.like("commentText", search.getCommentText(), MatchMode.ANYWHERE));
      disjunction.add(Restrictions.like("moreCommentText", search.getCommentText(), MatchMode.ANYWHERE));
      criteriaList.add(disjunction);
    }

    if (search.getUserDisplayName() != null && search.getUserDisplayName().trim().length() > 0) {
      addAlias("user", "u");
      criteriaList.add(Restrictions.ilike("u.displayName", search.getUserDisplayName(), MatchMode.ANYWHERE));
    }

    if (search.getNotByIgnoredUsers() != null) {
      // subselect to get ids of user's ignored users
      DetachedCriteria ignoredUserIds = DetachedCriteria.forClass(IgnoredUser.class);
      ignoredUserIds.setProjection(Property.forName("ignoredUser.id"));
      ignoredUserIds.add(Restrictions.eq("user.id", search.getNotByIgnoredUsers()));

      criteriaList.add(Subqueries.propertyNotIn("user.id", ignoredUserIds));
    }

    getFetchModes().put("user", FetchMode.JOIN);
//    getFetchModes().put("playlist", FetchMode.JOIN);
  }
}
