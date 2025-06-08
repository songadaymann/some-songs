package com.ssj.dao.song.search;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.search.SongSearchSearch;
import com.ssj.model.user.User;
import com.ssj.dao.AbstractSpringSearchDAO;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class SongSearchDAOImpl extends AbstractSpringSearchDAO<SongSearch, SongSearchSearch, SongSearchCriteria> implements SongSearchDAO {

  @Autowired
  public SongSearchDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public int countSearches(User user) {
    return ((Long) getCurrentSession().createCriteria(SongSearch.class)
        .add(Restrictions.eq("user", user))
        .setProjection(Projections.rowCount())
        .list()
        .get(0)).intValue();
  }

  public List getSearches(User user, int numResults) {
    return getCurrentSession().createCriteria(SongSearch.class).add(Restrictions.eq("user", user)).setMaxResults(numResults).list();
  }

  protected Order[] getOrderBy(SongSearchSearch search) {
    Order[] orderByArray = new Order[1];
    String orderByField;
    boolean descending;
    switch (Math.abs(search.getOrderBy())) {
      case SongSearchSearch.ORDER_BY_NAME:
        descending = search.getOrderBy() > 0;
        orderByField = "name";
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
      case SongSearchSearch.ORDER_BY_CREATE_DATE:
      default:
        orderByField = "createDate";
        descending = search.getOrderBy() > 0;
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
    }
    return orderByArray;
  }
}
