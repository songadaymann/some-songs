package com.ssj.dao.artist;

import com.ssj.model.artist.Artist;
import com.ssj.model.artist.ArtistOtherUser;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.user.User;
import com.ssj.dao.AbstractSpringSearchDAO;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: sam
 * Date: Mar 3, 2007
 * Time: 1:01:34 AM
 * $Id$
 */
@Repository
public class ArtistDAOImpl extends AbstractSpringSearchDAO<Artist, ArtistSearch, ArtistCriteria> implements ArtistDAO {

  @Autowired
  public ArtistDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  protected Order[] getOrderBy(ArtistSearch search) {
    Order[] orderByArray = new Order[1];

    orderByArray[0] = Order.asc("name");

    return orderByArray;
  }

  private static final String SELECT_NAMES_FIRST_LETTERS_HQL =
    "select distinct lower(substring(name, 1, 1)) from Artist order by name";

  public List getFirstCharactersOfArtistNames() {
    return getCurrentSession().createQuery(SELECT_NAMES_FIRST_LETTERS_HQL).list();
  }

  private static final String IS_OTHER_USER_HQL =
    "select count(artist.id) from ArtistOtherUser where artist = :artist and user = :user";

  public boolean isOtherUser(final Artist artist, final User user) {
    Query query = getCurrentSession().createQuery(IS_OTHER_USER_HQL);
    query.setParameter("artist", artist);
    query.setParameter("user", user);

    List count = query.list();

    return (((Long) count.get(0)) > 0);
  }

  public List getArtistsByOtherUser(User user) {
    Criteria criteria = createCriteria();

    final DetachedCriteria otherUserIds = DetachedCriteria.forClass(ArtistOtherUser.class);
    otherUserIds.setProjection(Property.forName("artist.id"));
    otherUserIds.add(Restrictions.eq("user.id", user.getId()));

    criteria.add(Subqueries.propertyIn("id", otherUserIds));

    return criteria.list();
  }

  public Artist findArtistByName(String artistName) {
    return (Artist) createCriteria().add(Restrictions.eq("name", artistName)).uniqueResult();
  }

  public List<Artist> findArtistsForBandcampSynch() {
    return (List<Artist>) createCriteria()
        .add(Restrictions.eq("synchFromBandcamp", true))
        .add(Restrictions.isNotNull("bandcampUrl"))
        .list();
  }
}
