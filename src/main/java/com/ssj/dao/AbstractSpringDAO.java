package com.ssj.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
public class AbstractSpringDAO<T> implements DAO<T> {

  private Class<T> type;
  private SessionFactory sessionFactory;

  public AbstractSpringDAO(SessionFactory sessionFactory) {
    this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    this.sessionFactory = sessionFactory;
  }

  public void save(T instance) {
    getCurrentSession().saveOrUpdate(instance);
  }

  public T get(Serializable key) {
    return (T) getCurrentSession().get(type, key);
  }

  public void delete(T instance) {
    getCurrentSession().delete(instance);
  }

  public Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }

  public Criteria createCriteria() {
    return getCurrentSession().createCriteria(type);
  }

  public List<T> findAll() {
    return (List<T>) createCriteria().list();
  }

  protected Class<T> getType() {
    return type;
  }
}
