package com.ssj.service.content;

import com.ssj.dao.content.SiteLinkDAO;
import com.ssj.model.content.SiteLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SiteLinkServiceImpl implements SiteLinkService {
  
  private SiteLinkDAO siteLinkDAO;
  
  private List<SiteLink> allSiteLinksCache;

  @Override
  public List<SiteLink> getAll() {
    if (allSiteLinksCache == null) {
      allSiteLinksCache = siteLinkDAO.findAll();
    }
    return allSiteLinksCache;
  }

  public void clearCache() {
    allSiteLinksCache = null;
  }

  @Override
  @Transactional
  public void save(SiteLink siteLink) {
    siteLinkDAO.save(siteLink);
    clearCache();
  }

  @Override
  public SiteLink get(int id) {
//    return siteLinkDAO.get(id);
    SiteLink siteLink = null;
    for (SiteLink link : getAll()) {
      if (link.getId() == id) {
        siteLink = link;
        break;
      }
    }
    return siteLink;
  }

  @Override
  @Transactional
  public void delete(int id) {
    SiteLink siteLink = siteLinkDAO.get(id);
    if (siteLink != null) {
      siteLinkDAO.delete(siteLink);
      clearCache();
    }
  }

  @Autowired
  public void setSiteLinkDAO(SiteLinkDAO siteLinkDAO) {
    this.siteLinkDAO = siteLinkDAO;
  }
}
