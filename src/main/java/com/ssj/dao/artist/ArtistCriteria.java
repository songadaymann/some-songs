package com.ssj.dao.artist;

import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.song.Song;
import com.ssj.model.user.FavoriteArtist;
import com.ssj.dao.SearchCriteria;
import org.hibernate.criterion.*;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class ArtistCriteria extends SearchCriteria<ArtistSearch> {

  protected void populateCriteria(List<Criterion> criteriaList) {
    ArtistSearch search = getSearch();

    if (search.getName() != null && search.getName().trim().length() > 0) {
      if (search.isNameExactMatch()) {
        criteriaList.add(Restrictions.eq("name", search.getName()));
      } else {
        criteriaList.add(Restrictions.ilike("name", search.getName(), MatchMode.ANYWHERE));
      }
    } else if (search.getNameStartsWith() != null && search.getNameStartsWith().trim().length() > 0) {
      criteriaList.add(Restrictions.ilike("name", search.getNameStartsWith(), MatchMode.START));
    }

    if (search.getInfo() != null && search.getInfo().trim().length() > 0) {
      if (search.isInfoExactMatch()) {
        criteriaList.add(Restrictions.eq("info", search.getInfo()));
      } else {
        criteriaList.add(Restrictions.ilike("info", search.getInfo(), MatchMode.ANYWHERE));
      }
    }

    if (search.isHasShownSongs()) {
      DetachedCriteria shownSongs = DetachedCriteria.forClass(Song.class);
      shownSongs.setProjection(Property.forName("artist.id"));
      shownSongs.add(Restrictions.eq("showSong", true));
      criteriaList.add(Subqueries.propertyIn("id", shownSongs));
    }

    if (search.getUser() != null && search.getUser().getId() > 0) {
      criteriaList.add(Restrictions.eq("user", search.getUser()));
    }

    if (search.getInUsersFavorites() != null) {
      DetachedCriteria favoriteArtistIds = DetachedCriteria.forClass(FavoriteArtist.class);
      favoriteArtistIds.setProjection(Property.forName("artist.id"));
      favoriteArtistIds.add(Restrictions.eq("user.id", search.getInUsersFavorites()));
      criteriaList.add(Subqueries.propertyIn("id", favoriteArtistIds));
    }
  }
}
