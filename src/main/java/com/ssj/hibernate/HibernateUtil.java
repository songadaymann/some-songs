package com.ssj.hibernate;

import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 * User: sam
 * Date: Mar 6, 2007
 * Time: 9:22:30 AM
 * $Id$
 */
public class HibernateUtil {

  private Session session;

/*
  static {
    try {
      // Create the SessionFactory from hibernate.cfg.xml
      sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      // Make sure you log the exception, as it might be swallowed
      ex.printStackTrace();
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
*/

  public HibernateUtil(Session session) {
    this.session = session;
  }

  /**
   * Retrieves an object using its natural key.  This is really a convenience method around
   * Hibernate's get(Class, Integer) method using Hibernates 2nd level cache by default.
   *
   * @param c  the Class for the object
   * @param id the id
   * @return the persistent Object, if found, null otherwise
   * @see Session#get(Class, java.io.Serializable)
   * @throws org.hibernate.HibernateException
   */
  public Object get(Class c, Serializable id) throws HibernateException {
    return session.get(c, id);
  }

/*
      session.evict(o);
      session.delete(o);
      session.flush();  // delete will not occur without the flush()
*/

  /**
   * Create a list of objects from the given Class and criterion.
   *
   * @param klass
   * @param criterion
   * @return a List of objects
   */
  public List createList(Class klass, Criterion criterion) throws HibernateException {
    return (createList(klass, new Criterion[]{criterion}));
  }

  /**
   * Create a list of objects from the given Class and criterion, sorted
   * according to the given ordering.
   *
   * @param klass
   * @param criterion
   * @param orderBy
   * @return a List of objects
   */
  public List createList(Class klass, Criterion criterion, Order orderBy) throws HibernateException {
    return (createList(klass, new Criterion[]{criterion}, new Order[]{orderBy}, -1, -1));
  }

  /**
   * Create a list of objects from the given Class and criterion.
   *
   * @param klass
   * @param criterion
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param firstResults < 1 for first row, otherwise 0-based row number
   * @return a List of objects
   */
  public List createList(Class klass, Criterion criterion, int pageSize, int firstResults) throws HibernateException {
    return (createList(klass, new Criterion[]{criterion}, null, pageSize, firstResults));
  }

  /**
   * Create a list of objects from the given Class and criterion.
   *
   * @param klass
   * @param criterion
   * @return a List of objects
   */
  public List createList(Class klass, Criterion[] criterion) throws HibernateException {
    return (createList(klass, criterion, null, -1, -1));
  }

  /**
   * Create a list of objects from the given Class and criterion.
   *
   * @param klass
   * @param criterion
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param firstResults < 1 for first result, otherwise 0-based result number
   * @return a List of objects
   */
  public List createList(Class klass, Criterion[] criterion, int pageSize, int firstResults) throws HibernateException {
    return (createList(klass, criterion, null, pageSize, firstResults));
  }

  /**
   * Create a list of objects from the given Class and criterion using the given order
   *
   * @param klass
   * @param criterion
   * @param order
   * @return a List of objects
   */
  public List createList(Class klass, Criterion[] criterion, Order[] order) throws HibernateException {
    return createList(klass, criterion, order, -1, -1);
  }

  /**
   * Create a list of objects from the given Class and criterion using the given order
   *
   * @param klass
   * @param criterion
   * @param order
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param firstResult <=0 for first page, otherwise 0-based page number
   * @return a List of objects
   */
  public List createList(Class klass, Criterion[] criterion, Order[] order, int pageSize, int firstResult) throws HibernateException {
    return createList(klass, null, criterion, order, pageSize, firstResult);//criteria.list();
  }

  /**
   * Create a list of objects from the given Class and criterion using the given order
   *
   * @param klass
   * @param aliases
   * @param criterion
   * @param order
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param firstResult <=0 for first page, otherwise 0-based page number
   * @return a List of objects
   */
  public List createList(Class klass, Map aliases, Criterion[] criterion, Order[] order, int pageSize, int firstResult) throws HibernateException {
    return createList(klass, aliases, null, criterion, order, pageSize, firstResult);
  }

  /**
   * Create a list of objects from the given Class and criterion using the given order
   *
   * @param klass
   * @param aliases
   * @param criterion
   * @param order
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param firstResult <=0 for first page, otherwise 0-based page number
   * @return a List of objects
   */
  public List createList(Class klass, Map aliases, Map fetchModes, Criterion[] criterion, Order[] order, int pageSize, int firstResult) throws HibernateException {
    Criteria criteria = getCriteria(klass, criterion, order);

    if (aliases != null) {
      for (Object o : aliases.keySet()) {
        String key = (String) o;
        criteria.createAlias(key, (String) aliases.get(key));
      }
    }

    if (fetchModes != null) {
      for (Object o : fetchModes.keySet()) {
        String field = (String) o;
        FetchMode fetchMode = (FetchMode) fetchModes.get(field);
        criteria.setFetchMode(field, fetchMode);
      }
    }
/*
    if (criterion != null) {
      for (int i = 0; i < criterion.length; i++) {
        criteria.add(criterion[i]);
      }
    }

    if (order != null) {
      for (int i = 0; i < order.length; i++) {
        criteria.addOrder(order[i]);
      }
    }
*/

    if (pageSize > -1) {
      criteria.setMaxResults(pageSize);
    }

    if (firstResult > 0) {
      criteria.setFirstResult(firstResult);
    }

    return criteria.list();
  }

  private Criteria getCriteria(Class klass, Criterion[] criterion, Order[] order) throws HibernateException {
    Criteria criteria = session.createCriteria(klass);

    if (criterion != null) {
      for (Criterion aCriterion : criterion) {
        criteria.add(aCriterion);
      }
    }

    if (order != null) {
      for (Order anOrder : order) {
        criteria.addOrder(anOrder);
      }
    }

    return criteria;
  }

  /**
   * Executes the given HQL query and returns
   * the List of Objects returned by the Query.
   *
   * @param hql the HQL query
   * @return a List of Objects
   */
  public List createList(String hql) throws HibernateException {
    return createList(hql, null, -1, -1);
  }

  /**
   * Executes the given HQL query using the given single
   * parameter name and value and returns
   * the List of Objects returned by the Query.
   *
   * @param hql the HQL query
   * @param paramName the name of the one parameter of the query
   * @param paramValue the value of the one parameter of the query
   * @return a List of Objects
   */
  public List createList(String hql, String paramName, Object paramValue) throws HibernateException {
    return createList(hql, makeParameterMap(paramName, paramValue), -1, -1);
  }

  /**
   * Executes the given HQL query and returns
   * the List of Objects returned by the Query.
   *
   * @param hql the HQL query
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param startPage < 1 for first page, otherwise 0-based page number
   * @return a List of Objects
   */
  public List createList(String hql, int pageSize, int startPage) throws HibernateException {
    return createList(hql, null, pageSize, startPage);
  }

  /**
   * Executes the given HQL query using the given single
   * parameter name and value and returns
   * the List of Objects returned by the Query.
   *
   * @param hql the HQL query
   * @param paramName the name of the one parameter of the query
   * @param paramValue the value of the one parameter of the query
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param startPage < 1 for first page, otherwise 0-based page number
   * @return a List of Objects
   */
  public List createList(String hql, String paramName, Object paramValue, int pageSize, int startPage) throws HibernateException {
    return createList(hql, makeParameterMap(paramName, paramValue), pageSize, startPage);
  }

  /**
   * Executes the given HQL query with the given parameters
   * and returns the List of Objects returned by the Query.
   *
   * @param hql the HQL query
   * @return a List of Objects
   */
  public List createList(String hql, Map parameters) throws HibernateException {
    return createList(hql, parameters, -1, -1);
  }

  /**
   * Executes the given HQL query with the given parameters
   * and returns the List of Objects returned by the Query.
   *
   * @param hql the HQL query
   * @param pageSize -1 for no pagination, otherwise, the number of rows to return per page
   * @param startPage < 1 for first page, otherwise 0-based page number
   * @return a List of Objects
   */
  public List createList(String hql, Map parameters, int pageSize, int startPage) throws HibernateException {
    List list = null;
    if (StringUtils.isNotBlank(hql)) {
      Query query = getQuery(hql, parameters);
      if (pageSize > -1) {
        query.setMaxResults(pageSize);
      }
      if (startPage > 0) {
        query.setFirstResult(pageSize * startPage);
      }
      list = query.list();
    }
    return list;
  }

  /**
   * Executes the given HQL query and returns
   * the unique result of the query.
   *
   * @param hql the HQL query
   * @return the Object found by the query, or null
   */
  public Object find(String hql) throws HibernateException {
    return find(hql, null);
  }

  /**
   * Executes the given HQL query using the given
   * value for the parameter with the given name.
   * Convenience method for queries with only 1 param.
   *
   * @param hql the HQL query
   * @param paramName the name of the only parameter to the query
   * @param paramValue the value for the only paramter to the query
   * @return the Object found by the query, or null
   */
  public Object find(String hql, String paramName, Object paramValue) throws HibernateException {
    return find(hql, makeParameterMap(paramName, paramValue));
  }

  /**
   * Executes the given HQL query with the given parameters
   * and returns the unique result of the query.
   *
   * @param hql the HQL query
   * @param parameters the optional parameters to the query
   * @return the Object found by the query, or null
   */
  public Object find(String hql, Map parameters) throws HibernateException {
    Object object = null;
    if (StringUtils.isNotBlank(hql)) {
      Query query = getQuery(hql, parameters);
      object = query.uniqueResult();
    }
    return object;
  }

  /**
   * Executes the given update or delete HQL query
   * and returns the number of updated or deleted rows.
   *
   * @param hql the update or delete HQL query
   * @return the number of updated or deleted rows
   */
  public int bulkUpdateOrDelete(String hql) throws HibernateException {
    return bulkUpdateOrDelete(hql, null);
  }

  /**
   * Executes the given update or delete HQL query
   * using the given name and value for the one
   * parameter for the query and returns the number
   * of updated or deleted rows.
   *
   * @param hql the update or delete HQL query
   * @return the number of updated or deleted rows
   */
  public int bulkUpdateOrDelete(String hql, String paramName, Object paramValue) throws HibernateException {
    return bulkUpdateOrDelete(hql, makeParameterMap(paramName, paramValue));
  }

  /**
   * Executes the given update or delete HQL query with
   * the given parameters and returns the number of
   * updated or deleted rows.
   *
   * @param hql the update or delete HQL query
   * @param parameters the option HQL parameters
   * @return the number of updated or deleted rows
   */
  public int bulkUpdateOrDelete(String hql, Map parameters) throws HibernateException {
    int numRowsChanged = 0;
    if (StringUtils.isNotBlank(hql)) {
      Query query = getQuery(hql, parameters);
      numRowsChanged = query.executeUpdate();
    }
    return numRowsChanged;
  }

  /**
   * Gets a Query from the session, and sets its parameters from
   * the given map of parameters if the map is not null and not empty.
   *
   * @param hql
   * @param parameters
   * @return the Query with its parameters set, if there are any
   */
  private Query getQuery(String hql, Map parameters) throws HibernateException {
    Query query = session.createQuery(hql);
    if (parameters != null && !parameters.isEmpty()) {
      for (Object o : parameters.keySet()) {
        String paramName = (String) o;
        Object paramObj = parameters.get(paramName);
        if (paramObj instanceof HQLParameter) {
          // explicitly set the hibernate type
          HQLParameter param = (HQLParameter) paramObj;
          query.setParameter(paramName, param.getValue(), param.getHibernateType());
        } else {
          // have hibernate guess what type to use
          query.setParameter(paramName, paramObj);
        }
      }
    }
    return query;
  }

  /**
   * Find an objects from the given Class and criterion.
   *
   * @param klass
   * @param criterion
   * @return a List of objects
   */
  public Object find(Class klass, Criterion criterion) throws HibernateException {
    return (find(klass, new Criterion[]{criterion}));
  }

  /**
   * Find an objects from the given Class and criterion.
   *
   * @param klass
   * @param criterion
   * @return a List of objects
   */
  public Object find(Class klass, Criterion[] criterion) throws HibernateException {
    Criteria criteria = session.createCriteria(klass);
    if (criterion != null) {
      for (Criterion aCriterion : criterion) {
        criteria.add(aCriterion);
      }
    }
    return criteria.uniqueResult();
  }

  /**
   * Perform the given count HQL statement.
   *
   * @param hql
   * @return the count returned by executing the HQL
   */
  public int doCount(String hql) throws HibernateException {
    return doCount(hql, null);
  }

  /**
   * Perform the given count HQL statement with the
   * given single parameter name and value.
   *
   * @param hql
   * @param paramName the name of the one parameter of the query
   * @param paramValue the value of the one parameter of the query
   * @return the count returned by executing the HQL
   */
  public int doCount(String hql, String paramName, Object paramValue) throws HibernateException {
    return doCount(hql, makeParameterMap(paramName, paramValue));
  }

  public int doCount(Class klass, Criterion[] criterion) {
    return doCount(klass, null, criterion);
  }

  public int doCount(Class klass, Map aliases, Criterion[] criterion) {
    Criteria criteria = getCriteria(klass, criterion, null);

    if (aliases != null) {
      for (Object o : aliases.keySet()) {
        String key = (String) o;
        criteria.createAlias(key, (String) aliases.get(key));
      }
    }

    criteria.setProjection(Projections.rowCount());

    return ((Long) criteria.list().get(0)).intValue();
  }

  /**
   * Perform the given count HQL statement with the given parameters.
   *
   * @param hql
   * @param parameters
   * @return the count returned by executing the HQL with the parameters
   */
  public int doCount(String hql, Map parameters) throws HibernateException {
    int count = 0;
    if (hql != null && hql.trim().length() > 0) {
      Query query = getQuery(hql, parameters);
      count = (Integer) query.iterate().next();
    }
    return count;
  }

  private Map makeParameterMap(String paramName, Object paramValue) {
    Map params = new HashMap();
    params.put(paramName, paramValue);
    return params;
  }
}
