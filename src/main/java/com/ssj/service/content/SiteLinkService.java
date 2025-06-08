package com.ssj.service.content;

import com.ssj.model.content.SiteLink;

import java.util.List;

public interface SiteLinkService {
  List<SiteLink> getAll();

  void clearCache();

  void save(SiteLink siteLink);

  SiteLink get(int id);

  void delete(int id);
}
