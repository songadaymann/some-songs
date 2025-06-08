package com.ssj.dao.user;

import com.ssj.dao.DAO;
import com.ssj.model.user.SocialUser;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

public interface SocialUserDAO extends DAO<SocialUser> {
  List<SocialUser> findByProviderId(String providerId);

  List<SocialUser> findByUserId(String userId);

  List<SocialUser> findByUserIdAndProviderId(String userId, String providerId);

  List<SocialUser> findByUserIdAndProviderUserIds(String userId, MultiValueMap<String, String> providerUserIds);

  SocialUser get(String userId, String providerId, String providerUserId);

  List<SocialUser> findPrimaryByUserIdAndProviderId(String userId, String providerId);

  Integer selectMaxRankByUserIdAndProviderId(String userId, String providerId);

  List<String> findUserIdsByProviderIdAndProviderUserId(String providerId, String providerUserId);

  List<String> findUserIdsByProviderIdAndProviderUserIds(String providerId, Set<String> providerUserIds);
}
