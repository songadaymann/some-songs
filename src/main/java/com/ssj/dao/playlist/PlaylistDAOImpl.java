package com.ssj.dao.playlist;

import com.ssj.hibernate.NativeSQLOrder;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistItem;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.model.base.Rateable;
import com.ssj.dao.AbstractSpringSearchDAO;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class PlaylistDAOImpl extends AbstractSpringSearchDAO<Playlist, PlaylistSearch, PlaylistCriteria> implements PlaylistDAO {

  @Autowired
  public PlaylistDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public int countPlaylists(User user) {
    return ((Long) getCurrentSession().createCriteria(Playlist.class)
        .add(Restrictions.eq("user", user))
        .setProjection(Projections.rowCount())
        .list()
        .get(0)).intValue();
  }

  public List getPlaylists(User user, int numPlaylists) {
    return getCurrentSession().createCriteria(Playlist.class).add(Restrictions.eq("user", user)).setMaxResults(numPlaylists).list();
  }

  public void addToPlaylist(Song song, Playlist playlist) {
    PlaylistItem item = new PlaylistItem();
    item.setSong(song);
    item.setPlaylist(playlist);
//    Set<PlaylistItem> items = ;
    item.setOrdinal(playlist.getItems().size());
    playlist.getItems().add(item);
    // is this necessary?
//    super.save(playlist);
  }

  private static final String WITH_ITEMS_HQL =
    "from Playlist as p join p.items as items where p.id = :id";

  public Playlist getWithItems(int playlistId) {
    return (Playlist) getCurrentSession().createQuery(WITH_ITEMS_HQL).setParameter("id", playlistId).uniqueResult();
  }

  private static final String COUNT_ITEMS_HQL =
    " select count(*) " +
    " from PlaylistItem " +
    " where playlist = :playlist ";

  public void updateItemCount(Playlist playlist) {
    Iterator iterator = getCurrentSession().createQuery(COUNT_ITEMS_HQL).setParameter("playlist", playlist).iterate();
    Long numItems = 0l;
    if (iterator.hasNext()) {
      numItems = (Long) iterator.next();
    }
    playlist.setNumItems(numItems.intValue());
    save(playlist);
  }

  private static final String GET_RATING_DATA_HQL =
    " select avg(rating), count(*), max(createDate) " +
    " from PlaylistRating " +
    " where rating > -1 " +
    " and disabled = false " +
    " and playlist = :playlist ";

  public void updateRatingInfo(Playlist playlist) {
    Iterator iterator = getCurrentSession().createQuery(GET_RATING_DATA_HQL).setParameter("playlist", playlist).iterate();
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
    playlist.setRating(rating);
    playlist.setNumRatings(numRatings);
    playlist.setLastRated(lastRatedDate);
    save(playlist);
  }

  // TODO simplify by not handling case when two rows have the same create date?
  private String makeUniqueId(Date createDate, int id) {
    return (createDate.getTime() / 1000) + StringUtils.leftPad(Integer.toString(id), 6, '0');
  }

  private static final String NEWER_PLAYLIST_HQL =
    "from Playlist " +
    "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) > :uniqueId " +
    "and publiclyAvailable = true " +
    "and numItems > 0 " +
    "order by concat(unix_timestamp(createDate), lpad(id, 6, '0'))";

  public Playlist getNewerPlaylist(int playlistId) {
    Playlist playlist = get(playlistId);
    String uniqueId = makeUniqueId(playlist.getCreateDate(), playlist.getId());
    return getNextPlaylist(NEWER_PLAYLIST_HQL, uniqueId);
  }

  private static final String OLDER_PLAYLIST_HQL =
    "from Playlist " +
    "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) < :uniqueId " +
    "and publiclyAvailable = true " +
    "and numItems > 0 " +
    "order by concat(unix_timestamp(createDate), lpad(id, 6, '0')) desc";

  public Playlist getOlderPlaylist(int playlistId) {
    Playlist playlist = get(playlistId);
    String uniqueId = makeUniqueId(playlist.getCreateDate(), playlist.getId());
    return getNextPlaylist(OLDER_PLAYLIST_HQL, uniqueId);
  }

  private String makeRatingUniqueId(float rating, int numRatings) {
    return (StringUtils.leftPad(Integer.toString(Math.round(rating * 100)), 4, '0') + StringUtils.leftPad(Integer.toString(numRatings), 4, '0'));
  }

  private static final String HIGHER_RATED_PLAYLIST_HQL =
    "from Playlist " +
    "where concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0')) > :uniqueId " +
    "and publiclyAvailable = true " +
    "and numItems > 0 " +
    "order by concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0'))";

  public Playlist getHigherRatedPlaylist(int playlistId) {
    Playlist playlist = get(playlistId);
    String uniqueId = makeRatingUniqueId(playlist.getRating(), playlist.getNumRatings()) + makeUniqueId(playlist.getCreateDate(), playlist.getId());
    return getNextPlaylist(HIGHER_RATED_PLAYLIST_HQL, uniqueId);
  }

  private static final String LOWER_RATED_PLAYLIST_HQL =
    "from Playlist " +
    "where concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0')) < :uniqueId " +
    "and publiclyAvailable = true " +
    "and numItems > 0 " +
    "order by concat(lpad(cast(rating * 100 as integer), 4, '0'), lpad(numRatings, 4, '0'), unix_timestamp(createDate), lpad(id, 6, '0')) desc";

  public Playlist getLowerRatedPlaylist(int playlistId) {
    Playlist playlist = get(playlistId);
    String uniqueId = makeRatingUniqueId(playlist.getRating(), playlist.getNumRatings()) + makeUniqueId(playlist.getCreateDate(), playlist.getId());
    return getNextPlaylist(LOWER_RATED_PLAYLIST_HQL, uniqueId);
  }

  private Playlist getNextPlaylist(String hql, String uniqueId) {
//    try {
      List playlists = getCurrentSession().createQuery(hql).setParameter("uniqueId", uniqueId).setMaxResults(1).list();
      Playlist nextPlaylist = null;
      if (playlists != null && !playlists.isEmpty()) {
        nextPlaylist = (Playlist) playlists.get(0);
      }
      return nextPlaylist;
/*
    } catch (HibernateException e) {
      throw convertHibernateAccessException(e);
    }
*/
  }

  protected Order[] getOrderBy(PlaylistSearch search) {
    Order[] orderByArray;// = new Order[1];
    String orderByField;
    boolean descending;
    switch (Math.abs(search.getOrderBy())) {
      case PlaylistSearch.ORDER_BY_AVG_RATING:
        orderByArray = new Order[3];
        descending = search.getOrderBy() > 0;
        orderByField = "rating";
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "numRatings";
        orderByArray[1] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "createDate";
        orderByArray[2] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
      case PlaylistSearch.ORDER_BY_NUM_RATINGS:
        orderByArray = new Order[3];
        descending = search.getOrderBy() > 0;
        orderByField = "numRatings";
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "rating";
        orderByArray[1] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "createDate";
        orderByArray[2] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
      case PlaylistSearch.ORDER_BY_NUM_SONGS:
        orderByArray = new Order[3];
        descending = search.getOrderBy() > 0;
        orderByField = "numItems";
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "rating";
        orderByArray[1] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "createDate";
        orderByArray[2] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
      case PlaylistSearch.ORDER_BY_RANDOM:
        orderByField = "rand()";
        orderByArray = new Order[1];
        orderByArray[0] = new NativeSQLOrder(orderByField, false);
        break;
      case PlaylistSearch.ORDER_BY_CREATE_DATE:
      default:
        orderByField = "createDate";
        descending = search.getOrderBy() > 0;
        orderByArray = new Order[2];
        orderByArray[0] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        orderByField = "id";
        orderByArray[1] = descending ? Order.desc(orderByField) : Order.asc(orderByField);
        break;
    }
    return orderByArray;
  }
}
