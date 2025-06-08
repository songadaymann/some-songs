package com.ssj.service.content;

import com.ssj.model.content.PageContent;
import com.ssj.dao.content.PageContentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
@Transactional(readOnly = true)
public class PageContentServiceImpl implements PageContentService {

  private PageContentDAO pageContentDao;

  public List getFaqContent() {
    return getContentByType(PageContent.TYPE_FAQ);
  }

  public List getFirstTimeContent() {
    return getContentByType(PageContent.TYPE_FIRST_TIME);
  }

  public List getContactAdminContent() {
    return getContentByType(PageContent.TYPE_CONTACT_ADMIN);
  }

  private List getContentByType(int type) {
    return pageContentDao.findByType(type);
  }

  @Transactional(readOnly = false)
  public void save(PageContent pageContent) {
    pageContentDao.save(pageContent);
  }

  public PageContent getContent(int contentId) {
    return pageContentDao.get(contentId);
  }

  @Transactional(readOnly = false)
  public void deleteContent(PageContent content) {
    pageContentDao.delete(content);
  }

  @Autowired
  public void setPageContentDao(PageContentDAO pageContentDao) {
    this.pageContentDao = pageContentDao;
  }
}
