package com.ssj.dao.song;

import com.ssj.model.song.SongRating;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.dao.AbstractSpringDAO;
import com.ssj.hibernate.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class SongRatingDAOImpl extends AbstractSpringDAO<SongRating> implements SongRatingDAO {

  @Autowired
  public SongRatingDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public void save(SongRating rating) {
    super.save(rating);
    // clear the song from the session, since insert/update of rating
    // triggers update of row in song table that we need to get
    getCurrentSession().evict(rating.getSong());
  }

  public void delete(SongRating rating) {
    super.delete(rating);
    // clear the song from the session, since delete from rating
    // triggers update of row in song table that we need to get
    getCurrentSession().evict(rating.getSong());
  }

  public List<SongRating> getRatings(User user, List<Song> songs) {
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("user", user));
    criteria.add(Restrictions.in("song", songs));
    return (List<SongRating>) criteria.list();
  }

  public SongRating getRating(User user, Song song) {
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("user", user));
    criteria.add(Restrictions.eq("song", song));
    return (SongRating) criteria.uniqueResult();
  }

  // TODO get stats with one query
  private static final String COUNT_NON_HIDDEN_RATED_SONGS_HQL =
    "select count(id) from SongRating where song.showSong = true and user = :user";

  public int countNonHiddenRatedSongs(User user) {
    return ((Long) getCurrentSession().createQuery(COUNT_NON_HIDDEN_RATED_SONGS_HQL).setParameter("user", user).iterate().next()).intValue();
  }

  private static final String AVERAGE_SONG_RATING_HQL =
    "select avg(rating) from SongRating where song.showSong = true and user = :user";

  public float getAverageSongRating(User user) {
    float averageSongRating = 0;
    Double average = (Double) getCurrentSession().createQuery(AVERAGE_SONG_RATING_HQL).setParameter("user", user).iterate().next();
    if (average != null) {
      averageSongRating = average.floatValue();
    }
    return averageSongRating;
  }

  private static final String GET_RATINGS_FOR_SAME_SONGS_HQL =
    "select sr1.rating, sr2.rating " +
    "from SongRating sr1, SongRating sr2 " +
    "where sr1.song.id = sr2.song.id " +
    "and sr1.user.id = :userId1 " +
    "and sr2.user.id = :userId2 ";

  public List getRatingsForSameSongs(User user1, User user2) {
    Map<String, Object> params  = new HashMap<String, Object>();
    params.put("userId1", user1.getId());
    params.put("userId2", user2.getId());
    return new HibernateUtil(getCurrentSession()).createList(GET_RATINGS_FOR_SAME_SONGS_HQL, params);
  }

  public List<SongRating> getRecentRatingsWithSongs(int start, int pageSize) {
    Criteria criteria = createCriteria();
    criteria.addOrder(Order.desc("createDate"));
    criteria.setFetchMode("song", FetchMode.JOIN);
    criteria.setFirstResult(start);
    criteria.setMaxResults(pageSize);
    return criteria.list();
  }

}
