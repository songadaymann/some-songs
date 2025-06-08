package com.ssj.dao.user;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.user.FavoriteSong;
import com.ssj.model.user.User;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class FavoriteSongsDAOImpl extends AbstractSpringDAO<FavoriteSong> implements FavoriteSongsDAO {
  @Autowired
  public FavoriteSongsDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<FavoriteSong> findByUser(User user) {
    return (List<FavoriteSong>) createCriteria().add(Restrictions.eq("user", user)).list();
  }
}
