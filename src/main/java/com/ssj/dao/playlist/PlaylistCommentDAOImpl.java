package com.ssj.dao.playlist;

import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.model.user.User;
import com.ssj.dao.AbstractSpringSearchDAO;
import com.ssj.hibernate.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
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
public class PlaylistCommentDAOImpl extends AbstractSpringSearchDAO<PlaylistComment, PlaylistCommentSearch, PlaylistCommentCriteria> implements PlaylistCommentDAO {
  @Autowired
  public PlaylistCommentDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public PlaylistComment getComment(User user, Playlist playlist) {
    Criterion[] criteria = new Criterion[] {
      Restrictions.eq("user", user),
      Restrictions.eq("playlist", playlist)
    };
    return (PlaylistComment) new HibernateUtil(getCurrentSession()).find(PlaylistComment.class, criteria);
  }


  private static final String GET_PLAYLIST_COMMENT_POSITION_HQL =
    "select count(*) " +
    "from PlaylistComment " +
    "where playlistId = :playlistId " +
    "and concat(unix_timestamp(createDate), lpad(id, 5, '0')) < ( " +
    " select concat(unix_timestamp(createDate), lpad(id, 5, '0')) " +
    " from PlaylistComment " +
    " where id = :playlistCommentId " +
    ")";

  public int getCommentPosition(PlaylistComment comment) {
    Long commentCount = (Long) getCurrentSession().createQuery(GET_PLAYLIST_COMMENT_POSITION_HQL)
        .setParameter("playlistId", comment.getPlaylist().getId()).setParameter("playlistCommentId", comment.getId())
        .uniqueResult();

    return commentCount.intValue();

  }

  protected Order[] getOrderBy(PlaylistCommentSearch search) {
    Order[] orderByArray = new Order[1];
    String orderByField;
    switch(search.getOrderBy()) {
      case PlaylistCommentSearch.ORDER_BY_CREATE_DATE:
      default:
        orderByField = "createDate";
        break;
    }
    orderByArray[0] = search.isDescending() ? Order.desc(orderByField) : Order.asc(orderByField);
    return orderByArray;
  }
}
