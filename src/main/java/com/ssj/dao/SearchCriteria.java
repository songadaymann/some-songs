package com.ssj.dao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.FetchMode;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.ssj.search.SearchBase;


/**
 * Base class for classes that turn searches into arrays of Hibernate Criterion.
 *
 * @author sam
 * @version $Id$
 */
public abstract class SearchCriteria<SB extends SearchBase> {

  private SB search;
  private Criterion[] criteria;
  private Map<String, String> aliases = new HashMap<String, String>();
  private Map<String, FetchMode> fetchModes = new HashMap<String, FetchMode>();

  public SearchCriteria() {
  }

  public void buildCriteria(SB search) {
    this.search = search;

    List<Criterion> criteriaList = new ArrayList<Criterion>();

    populateCriteria(criteriaList);

    criteria = new Criterion[criteriaList.size()];
    criteriaList.toArray(criteria);
   }

  protected abstract void populateCriteria(List<Criterion> criteriaList);

  protected void handleRange(List<Criterion> criteriaList, String fieldName, Object min, Object max) {
    if (min != null) {
      if (max != null && max.equals(min)) {
        // min/max set to same value, use equals criteria
        criteriaList.add(Restrictions.eq(fieldName, min));
      } else {
        criteriaList.add(Restrictions.ge(fieldName, min));
      }
    }
    if (max != null && !max.equals(min)) {
      criteriaList.add(Restrictions.le(fieldName, max));
    }
  }

  protected SB getSearch() {
    return search;
  }

  Criterion[] getCriteria() {
    return criteria;
  }

  protected void addAlias(String field, String alias) {
    aliases.put(field, alias);
  }

  Map getAliases() {
    return aliases;
  }

  protected Map<String, FetchMode> getFetchModes() {
    return fetchModes;
  }

  void setFetchModes(Map<String, FetchMode> fetchModes) {
    this.fetchModes = fetchModes;
  }
}
