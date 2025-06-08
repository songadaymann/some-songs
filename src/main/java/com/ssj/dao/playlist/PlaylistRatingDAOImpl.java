package com.ssj.dao.playlist;

import com.ssj.model.playlist.PlaylistRating;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.user.User;
import com.ssj.dao.AbstractSpringDAO;
import com.ssj.hibernate.HibernateUtil;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class PlaylistRatingDAOImpl extends AbstractSpringDAO<PlaylistRating> implements PlaylistRatingDAO {

  @Autowired
  public PlaylistRatingDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void save(PlaylistRating rating) {
    super.save(rating);
    // clear the playlist from the session, since the insert/update of the rating
    // triggers an update of the row in the playlist table, and we need to get that updated data
    getCurrentSession().evict(rating.getPlaylist());
  }

  @Override
  public void delete(PlaylistRating rating) {
    super.delete(rating);
    // clear the playlist from the session, since the delete from the rating table
    // triggers an update of the row in the playlist table. and we need to get that updated data
    getCurrentSession().evict(rating.getPlaylist());
  }

  public List<PlaylistRating> getRatings(User user, List<Playlist> playlists) {
    Criterion[] criteria = new Criterion[] {
      Restrictions.eq("user", user),
      Restrictions.in("playlist", playlists)
    };
    return new HibernateUtil(getCurrentSession()).createList(PlaylistRating.class, criteria);
  }

  public PlaylistRating getRating(User user, Playlist playlist) {
    Criterion[] criteria = new Criterion[] {
      Restrictions.eq("user", user),
      Restrictions.eq("playlist", playlist)
    };
    return (PlaylistRating) new HibernateUtil(getCurrentSession()).find(PlaylistRating.class, criteria);
  }
}
