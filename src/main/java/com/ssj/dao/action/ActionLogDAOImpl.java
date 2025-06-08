package com.ssj.dao.action;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.action.ActionLog;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ActionLogDAOImpl extends AbstractSpringDAO<ActionLog> implements ActionLogDAO {

  @Autowired
  public ActionLogDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
