package com.ssj.dao.user;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.user.IgnoredUser;
import com.ssj.model.user.User;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sam
 * @version $Id$
 */
@Repository
@SuppressWarnings("unchecked")
public class IgnoredUserDAOImpl extends AbstractSpringDAO<IgnoredUser> implements IgnoredUserDAO {
  @Autowired
  public IgnoredUserDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<IgnoredUser> findByUser(User user) {
    return (List<IgnoredUser>) createCriteria().add(Restrictions.eq("user", user)).list();
  }
}
