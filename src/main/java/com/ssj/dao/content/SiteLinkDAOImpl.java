package com.ssj.dao.content;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.content.SiteLink;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SiteLinkDAOImpl extends AbstractSpringDAO<SiteLink> implements SiteLinkDAO {
  @Autowired
  public SiteLinkDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void save(SiteLink instance) {
    getCurrentSession().clear();
    super.save(instance);
  }
}
