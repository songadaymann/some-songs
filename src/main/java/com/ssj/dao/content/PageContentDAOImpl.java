package com.ssj.dao.content;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.content.PageContent;
import org.hibernate.Query;
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
public class PageContentDAOImpl extends AbstractSpringDAO<PageContent> implements PageContentDAO {
  private static final String FIND_BY_TYPE_HQL =
    "from PageContent where type = :type";

  @Autowired
  public PageContentDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List findByType(int type) {
    Query query = getCurrentSession().createQuery(FIND_BY_TYPE_HQL);
    query.setParameter("type", type);
    return query.list();
  }
}
