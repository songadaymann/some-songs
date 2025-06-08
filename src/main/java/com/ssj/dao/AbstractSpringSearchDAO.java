package com.ssj.dao;

import com.ssj.search.SearchBase;
import com.ssj.hibernate.HibernateUtil;

import java.util.List;
import java.lang.reflect.ParameterizedType;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public abstract class AbstractSpringSearchDAO<O, SB extends SearchBase, SCB extends SearchCriteria<SB>> extends AbstractSpringDAO<O> implements SearchDAO<O, SB> {
  public AbstractSpringSearchDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public int doCount(SB search) {
    SCB criteria = instantiateSearchCriteriaBuilder();
    criteria.buildCriteria(search);

    return new HibernateUtil(getCurrentSession()).doCount(getType(), criteria.getAliases(), criteria.getCriteria());
  }

  public List<O> doSearch(SB search) {
    SCB criteria = instantiateSearchCriteriaBuilder();
    criteria.buildCriteria(search);
    Order[] orderBy = getOrderBy(search);

    return new HibernateUtil(getCurrentSession()).createList(getType(),
          criteria.getAliases(), criteria.getFetchModes(), criteria.getCriteria(), orderBy,
          search.getResultsPerPage(), search.getStartResultNum());
  }

  private SCB instantiateSearchCriteriaBuilder() {
    try {
      return ((Class<SCB>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2]).newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract Order[] getOrderBy(SB search);
}
