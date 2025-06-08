package com.ssj.dao.action.facebook;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.action.facebook.FacebookActionLog;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FacebookActionLogDAOImpl extends AbstractSpringDAO<FacebookActionLog> implements FacebookActionLogDAO {
  @Autowired
  public FacebookActionLogDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public FacebookActionLog find(String action, String object, int objectId) {
    Criteria criteria = createCriteria()
        .add(Restrictions.eq("action", action))
        .add(Restrictions.eq("object", object))
        .add(Restrictions.eq("objectId", objectId));

    return (FacebookActionLog) criteria.uniqueResult();
  }
}
