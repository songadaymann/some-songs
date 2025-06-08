package com.ssj.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.util.StringHelper;

/**
 * Taken from:
 *
 * http://opensource.atlassian.com/projects/hibernate/browse/HHH-2381
 *
 * @author sam
 * @version $Id$
 */
public class NativeSQLOrder extends Order {

  private final static String PROPERTY_NAME = "notUsed";

  private boolean ascending;
  private String sql;

  public NativeSQLOrder(String sql, boolean ascending) {
    super(PROPERTY_NAME, ascending);
    this.sql = sql;
    this.ascending = ascending;
  }

  @Override
  public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
    StringBuilder fragment = new StringBuilder();
    fragment.append("(");
    fragment.append(sql);
    fragment.append(")");
    fragment.append(ascending ? " asc" : " desc");
    return StringHelper.replace(fragment.toString(), "{alias}", criteriaQuery.getSQLAlias(criteria));
  }


}