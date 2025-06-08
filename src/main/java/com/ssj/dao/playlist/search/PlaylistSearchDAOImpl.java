package com.ssj.dao.playlist.search;

import com.ssj.model.user.User;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.dao.AbstractSpringDAO;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class PlaylistSearchDAOImpl extends AbstractSpringDAO<PlaylistSearch> implements PlaylistSearchDAO {
  @Autowired
  public PlaylistSearchDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public int countSearches(User user) {
    return ((Long) getCurrentSession().createCriteria(PlaylistSearch.class)
        .add(Restrictions.eq("user", user))
        .setProjection(Projections.rowCount())
        .list()
        .get(0)).intValue();
  }

  public List getSearches(User user, int numResults) {
    return getCurrentSession().createCriteria(PlaylistSearch.class).add(Restrictions.eq("user", user)).setMaxResults(numResults).list();
  }
}