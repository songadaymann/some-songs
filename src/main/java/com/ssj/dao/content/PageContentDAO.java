package com.ssj.dao.content;

import com.ssj.dao.DAO;
import com.ssj.model.content.PageContent;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PageContentDAO extends DAO<PageContent> {
  public List findByType(int type);
}
