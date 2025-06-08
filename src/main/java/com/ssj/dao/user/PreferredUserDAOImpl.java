package com.ssj.dao.user;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.user.PreferredUser;
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
public class PreferredUserDAOImpl extends AbstractSpringDAO<PreferredUser> implements PreferredUserDAO {
  @Autowired
  public PreferredUserDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<PreferredUser> findByUser(User user) {
    return (List<PreferredUser>) createCriteria().add(Restrictions.eq("user", user)).list();
  }
}
