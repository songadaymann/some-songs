package com.ssj.dao.user;

import com.ssj.dao.AbstractSpringDAO;
import com.ssj.model.user.SocialUser;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

@Repository
@SuppressWarnings("unchecked")
public class SocialUserDAOImpl extends AbstractSpringDAO<SocialUser> implements SocialUserDAO {

  @Autowired
  public SocialUserDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<SocialUser> findByProviderId(String providerId) {
    return (List<SocialUser>) createCriteria().add(Restrictions.eq("providerId", providerId)).list();
  }

  public List<SocialUser> findByUserId(String userId) {
    return (List<SocialUser>) createCriteria().add(Restrictions.eq("userId", userId)).list();
  }

  public List<SocialUser> findByUserIdAndProviderId(String userId, String providerId) {
    return (List<SocialUser>) createCriteria()
        .add(Restrictions.eq("userId", userId))
        .add(Restrictions.eq("providerId", providerId))
        .list();
  }

  public List<SocialUser> findByUserIdAndProviderUserIds(String userId, MultiValueMap<String, String> providerUserIds) {
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("userId", userId));
    Disjunction or = Restrictions.disjunction();
    for (String providerId : providerUserIds.keySet()) {
      or.add(
          Restrictions.and(
              Restrictions.eq("providerId", providerId),
              Restrictions.in("providerUserId", providerUserIds.get(providerId))
          )
      );
    }
    return (List<SocialUser>) criteria.list();
  }

  public SocialUser get(String userId, String providerId, String providerUserId) {
    return (SocialUser) createCriteria()
        .add(Restrictions.eq("userId", userId))
        .add(Restrictions.eq("providerId", providerId))
        .add(Restrictions.eq("providerUserId", providerUserId))
        .uniqueResult();
  }

  public List<SocialUser> findPrimaryByUserIdAndProviderId(String userId, String providerId) {
    return (List<SocialUser>) createCriteria()
        .add(Restrictions.eq("userId", userId))
        .add(Restrictions.eq("providerId", providerId))
//        .add(Restrictions.eq("rank", 1))
        .addOrder(Order.asc("rank"))
        .list();
  }

  private static final String MAX_RANK_QUERY =
      "select max(rank) from SocialUser where userId = :userId and providerId = :providerId";

  public Integer selectMaxRankByUserIdAndProviderId(String userId, String providerId) {
    return (Integer) getCurrentSession().createQuery(MAX_RANK_QUERY)
        .setParameter("userId", userId)
        .setParameter("providerId", providerId)
        .uniqueResult();
  }

  private static final String SELECT_USER_IDS_BY_PROVIDER_ID_AND_PROVIDER_USER_ID =
      "select userId from SocialUser where providerId = :providerId and providerUserId = :providerUserId";

  public List<String> findUserIdsByProviderIdAndProviderUserId(String providerId, String providerUserId) {
    return (List<String>) getCurrentSession().createQuery(SELECT_USER_IDS_BY_PROVIDER_ID_AND_PROVIDER_USER_ID)
        .setParameter("providerId", providerId)
        .setParameter("providerUserId", providerUserId)
        .list();
  }

  private static final String SELECT_USER_IDS_BY_PROVIDER_ID_AND_PROVIDER_USER_IDS =
      "select userId from SocialUser where providerId = :providerId and providerUserId in (:providerUserIds)";

  public List<String> findUserIdsByProviderIdAndProviderUserIds(String providerId, Set<String> providerUserIds) {
    return (List<String>) getCurrentSession().createQuery(SELECT_USER_IDS_BY_PROVIDER_ID_AND_PROVIDER_USER_IDS)
        .setParameter("providerId", providerId)
        .setParameter("providerUserIds", providerUserIds)
        .list();
  }

}
