package com.ssj.dao.playlist;

import com.ssj.model.playlist.PlaylistItem;
import com.ssj.dao.AbstractSpringDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class PlaylistItemDAOImpl extends AbstractSpringDAO<PlaylistItem> implements PlaylistItemDAO {

  @Autowired
  public PlaylistItemDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void save(PlaylistItem item) {
    item.setOrdinal(((Long) getCurrentSession().createFilter(item.getPlaylist().getItems(), "select count(*)").uniqueResult()).intValue());
    super.save(item);
  }

  private static final String GET_BY_PLAYLIST_ID_HQL =
    "from PlaylistItem where playlist.id = :id";

  public List getByPlaylistId(int id) {
    return getCurrentSession().createQuery(GET_BY_PLAYLIST_ID_HQL).setParameter("id", id).list();
  }
}
