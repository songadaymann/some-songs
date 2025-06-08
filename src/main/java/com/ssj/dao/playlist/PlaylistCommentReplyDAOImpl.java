package com.ssj.dao.playlist;

import com.ssj.dao.AbstractSpringSearchDAO;
import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class PlaylistCommentReplyDAOImpl extends AbstractSpringSearchDAO<PlaylistCommentReply, PlaylistCommentReplySearch, PlaylistCommentReplyCritiera>
    implements PlaylistCommentReplyDAO {

  @Autowired
  public PlaylistCommentReplyDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  protected Order[] getOrderBy(PlaylistCommentReplySearch search) {
    Order[] orderByArray = new Order[1];
    String orderByField;
    switch(search.getOrderBy()) {
      case PlaylistCommentReplySearch.ORDER_BY_CREATE_DATE:
      default:
        orderByField = "createDate";
        break;
    }
    orderByArray[0] = search.isDescending() ? Order.desc(orderByField) : Order.asc(orderByField);
    return orderByArray;
  }

  private static final String GET_PLAYLIST_COMMENT_POSITION_HQL =
    "select count(*) " +
    "from PlaylistCommentReply " +
    "where playlistCommentId = :commentId " +
    "and concat(unix_timestamp(createDate), lpad(id, 5, '0')) < ( " +
    " select concat(unix_timestamp(createDate), lpad(id, 5, '0')) " +
    " from PlaylistCommentReply " +
    " where id = :replyId " +
    ")";

  public int getReplyPosition(PlaylistCommentReply reply) {
    Long commentCount = (Long) getCurrentSession().createQuery(GET_PLAYLIST_COMMENT_POSITION_HQL)
        .setParameter("commentId", reply.getOriginalComment().getId()).setParameter("replyId", reply.getId())
        .uniqueResult();

    return commentCount.intValue();
  }

  private static final String FIND_BY_IDS_HQL =
    "from PlaylistCommentReply " +
    "where id in (:ids)";

  public List findRepliesByIds(Set multiquoteReplyIds) {
    return getCurrentSession().createQuery(FIND_BY_IDS_HQL).setParameterList("ids", multiquoteReplyIds).list();
  }

  private static final String COUNT_REPLIES_BY_COMMENT_HQL =
    "select count(*) " +
    "from PlaylistCommentReply " +
    "where originalComment = :comment";

  public int countReplies(PlaylistComment comment)  {
    return ((Long) getCurrentSession().createQuery(COUNT_REPLIES_BY_COMMENT_HQL).setParameter("comment", comment).list().get(0)).intValue();
  }
}
