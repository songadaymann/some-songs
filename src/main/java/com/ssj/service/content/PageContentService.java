package com.ssj.service.content;

import com.ssj.model.content.PageContent;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PageContentService {

  public List getFaqContent();

  public List getFirstTimeContent();

  public List getContactAdminContent();

  public void save(PageContent pageContent);

  public PageContent getContent(int contentId);

  public void deleteContent(PageContent content);
}
