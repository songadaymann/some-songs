package com.ssj.dao.song;

import com.ssj.hibernate.NativeSQLOrder;
import com.ssj.model.song.Song;
import com.ssj.model.song.search.SongSearch;
import com.ssj.model.base.Rateable;
import com.ssj.dao.AbstractSpringSearchDAO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

/**
 * User: sam
 * Date: Mar 1, 2007
 * Time: 7:57:59 PM
 * $Id$
 */
@Repository
public class SongDAOImpl extends AbstractSpringSearchDAO<Song, SongSearch, SongCriteria> implements SongDAO {

  private static final Logger LOGGER = Logger.getLogger(SongDAOImpl.class);

  @Autowired
  public SongDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  // TODO simplify by not handling case when two rows have the same create date?
  private String makeUniqueId(Date createDate, int id) {
    return (createDate.getTime() / 1000) + StringUtils.leftPad(Integer.toString(id), 6, '0');
  }

  private static final String NEWER_SONG_HQL =
    "from Song " +
    "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) > :uniqueId " +
    "and showSong = true " +
    "order by concat(unix_timestamp(createDate), lpad(id, 6, '0'))";

  public Song getNewerSong(int songId) {
    Song song = get(songId);
    String uniqueId = makeUniqueId(song.getCreateDate(), song.getId());
    return getNextSong(NEWER_SONG_HQL, uniqueId);
  }

  private static final String OLDER_SONG_HQL =
    "from Song " +
    "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) < :uniqueId " +
    "and showSong = true " +
    "order by concat(unix_timestamp(createDate), lpad(id, 6, '0')) desc";

  public Song getOlderSong(int songId) {
    Song song = get(songId);
    String uniqueId = makeUniqueId(song.getCreateDate(), song.getId());
    return getNextSong(OLDER_SONG_HQL, uniqueId);
  }

  private String makeRatingUniqueId(float rating, int numRatings) {
    return (StringUtils.leftPad(Integer.toString(Math.round(rating * 100)), 4, '0') + StringUtils.leftPad(Integer.toString(numRatings), 4, '0'));
  }

  private static final String HIGHER_RATED_SONG_HQL =
    "from Song " +
    "where concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0')) > :uniqueId " +
    "and showSong = true " +
    "order by concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0'))";

  public Song getHigherRatedSong(int songId) {
    Song song = get(songId);
    String uniqueId = makeRatingUniqueId(song.getRating(), song.getNumRatings()) + makeUniqueId(song.getCreateDate(), song.getId());
    return getNextSong(HIGHER_RATED_SONG_HQL, uniqueId);
  }

  private static final String LOWER_RATED_SONG_HQL =
    "from Song " +
    "where concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0')) < :uniqueId " +
    "and showSong = true " +
    "order by concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0')) desc";

  public Song getLowerRatedSong(int songId) {
    Song song = get(songId);
    String uniqueId = makeRatingUniqueId(song.getRating(), song.getNumRatings()) + makeUniqueId(song.getCreateDate(), song.getId());
    LOGGER.debug("uniqueId of current song");
    LOGGER.debug(uniqueId);
    Song nextSong = getNextSong(LOWER_RATED_SONG_HQL, uniqueId);
    if (nextSong != null) {
      uniqueId = makeRatingUniqueId(nextSong.getRating(), nextSong.getNumRatings()) + makeUniqueId(nextSong.getCreateDate(), nextSong.getId());
      LOGGER.debug("uniqueId of lower rated song");
      LOGGER.debug(uniqueId);
    }
    return nextSong;
  }

  //TODO get all song related stats with one query:
//  "select count(id), avg(rating), avg(numRatings) from Song where showSong = true";

  private static final String AVERAGE_SONG_RATING_HQL =
    "select avg(rating) from Song where showSong = true";

  public float getAverageSongRating() {
    float averageSongRating = 0;
    Double average = (Double) getCurrentSession().createQuery(AVERAGE_SONG_RATING_HQL).iterate().next();
    if (average != null) {
      averageSongRating = average.floatValue();
    }
    return averageSongRating;
  }

  private static final String AVERAGE_NUM_RATINGS_HQL =
    "select avg(numRatings) from Song where showSong = true";

  public float getAverageNumRatings() {
    float averageNumRatings = 0;
    Double average = (Double) getCurrentSession().createQuery(AVERAGE_NUM_RATINGS_HQL).iterate().next();
    if (average != null) {
      averageNumRatings = average.floatValue();
    }
    return averageNumRatings;
  }

  private static final String GET_RATING_DATA_HQL =
    " select avg(rating), count(*), max(createDate) " +
    " from SongRating " +
    " where rating > -1 " +
    " and disabled = false " +
    " and song = :song ";

  public void updateRatingInfo(Song song) {
    Iterator iterator = getCurrentSession().createQuery(GET_RATING_DATA_HQL).setParameter("song", song).iterate();
    Float rating = null;
    int numRatings = 0;
    Date lastRatedDate = null;
    if (iterator.hasNext()) {
      Object[] results = (Object[]) iterator.next();
      numRatings = ((Long) results[1]).intValue();
      if (numRatings >= Rateable.MIN_RATINGS) {
        rating = ((Double) results[0]).floatValue();
      }
      lastRatedDate = ((Date) results[2]);
    }
    song.setRating(rating);
    song.setNumRatings(numRatings);
    song.setLastRated(lastRatedDate);
    save(song);
  }

  public Song findByBandcmapTrackId(long trackId) {
    return (Song) createCriteria().add(Restrictions.eq("bandcampTrackId", trackId)).uniqueResult();
  }

  private Song getNextSong(String HQL, String uniqueId) {
//    try {
      List songs = getCurrentSession().createQuery(HQL).setParameter("uniqueId", uniqueId).setMaxResults(1).list();
      Song nextSong = null;
      if (songs != null && !songs.isEmpty()) {
        nextSong = (Song) songs.get(0);
      }
      return nextSong;
/*
    } catch (HibernateException e) {
      throw convertHibernateAccessException(e);
    }
*/
  }

  protected Order[] getOrderBy(SongSearch search) {
    Order[] orderByArray;// = new Order[1];
    String orderByField;
    boolean descending;
    switch (Math.abs(search.getOrderBy())) {
      case SongSearch.ORDER_BY_AVG_RATING:
        orderByArray = new Order[3];
        descending = search.getOrderBy() > 0;
        orderByField = "rating";
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "numRatings";
        orderByArray[1] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "createDate";
        orderByArray[2] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
      case SongSearch.ORDER_BY_NUM_RATINGS:
        orderByArray = new Order[3];
        descending = search.getOrderBy() > 0;
        orderByField = "numRatings";
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "rating";
        orderByArray[1] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "createDate";
        orderByArray[2] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
      case SongSearch.ORDER_BY_RANDOM:
        orderByField = "rand()";
        orderByArray = new Order[1];
        orderByArray[0] = new NativeSQLOrder(orderByField, false);
        break;
      case SongSearch.ORDER_BY_LAST_RATED:
        orderByField = "lastRated";
        orderByArray = new Order[1];
        orderByArray[0] = Order.desc(orderByField);
        break;
      case SongSearch.ORDER_BY_LOST_SONGS:
        orderByArray = new Order[2];
        orderByField = "numRatings";
        orderByArray[0] = Order.asc(orderByField);
        orderByField = "createDate";
        orderByArray[1] = Order.asc(orderByField);
        break;
      case SongSearch.ORDER_BY_ALBUM:
        orderByArray = new Order[3];
        orderByField = "album";
        orderByArray[0] = Order.asc(orderByField);
        orderByField = "albumTrackNumber";
        orderByArray[1] = Order.asc(orderByField);
        orderByField = "createDate";
        orderByArray[2] = Order.asc(orderByField);
        break;
      case SongSearch.ORDER_BY_CREATE_DATE:
      default:
        orderByField = "createDate";
        descending = search.getOrderBy() > 0;
        orderByArray = new Order[2];
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "id";
        orderByArray[1] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
    }
//    orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
    return orderByArray;
  }
}
