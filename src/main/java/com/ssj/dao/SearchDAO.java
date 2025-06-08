package com.ssj.dao;

import com.ssj.search.SearchBase;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SearchDAO<T, SB extends SearchBase> extends DAO<T> {

  public int doCount(SB search);

  public List<T> doSearch(SB search);
}
