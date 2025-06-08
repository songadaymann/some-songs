package com.ssj.dao.song.search;

import com.ssj.model.song.search.SongSearchSearch;
import com.ssj.dao.SearchCriteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @version $Id$
 */
public class SongSearchCriteria extends SearchCriteria<SongSearchSearch> {
  protected void populateCriteria(List<Criterion> criteriaList) {
    SongSearchSearch search = getSearch();

    if (search.getUser() != null) {
      criteriaList.add(Restrictions.eq("user", search.getUser()));
    }
    
  }
}
