package com.ssj.dao.song;

import com.ssj.model.song.SongCommentReply;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.user.User;
import com.ssj.dao.AbstractSpringSearchDAO;
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
public class SongCommentReplyDAOImpl
    extends AbstractSpringSearchDAO<SongCommentReply, SongCommentReplySearch, SongCommentReplyCriteria>
    implements SongCommentReplyDAO {

  @Autowired
  public SongCommentReplyDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  protected Order[] getOrderBy(SongCommentReplySearch search) {
    Order[] orderByArray = new Order[1];
    String orderByField;
    switch(search.getOrderBy()) {
      case SongCommentReplySearch.ORDER_BY_CREATE_DATE:
      default:
        orderByField = "createDate";
        break;
    }
    orderByArray[0] = search.isDescending() ? Order.desc(orderByField) : Order.asc(orderByField);
    return orderByArray;
  }

  // TODO add Song reference to SongCommentReply to avoid one of the joins here?
  private static final String COUNT_REPLIES_BY_USER_HQL =
    "select count(id) from SongCommentReply where originalComment.song.showSong = true and user = :user";

  public int countReplies(User user) {
    return ((Long) getCurrentSession().createQuery(COUNT_REPLIES_BY_USER_HQL).setParameter("user", user).iterate().next()).intValue();
  }

  private static final String GET_SONG_COMMENT_POSITION_HQL =
    "select count(*) " +
    "from SongCommentReply " +
    "where songCommentId = :commentId " +
    "and concat(unix_timestamp(createDate), lpad(id, 5, '0')) < ( " +
    " select concat(unix_timestamp(createDate), lpad(id, 5, '0')) " +
    " from SongCommentReply " +
    " where id = :replyId " +
    ")";

  public int getReplyPosition(SongCommentReply reply) {
    Long commentCount = (Long) getCurrentSession().createQuery(GET_SONG_COMMENT_POSITION_HQL)
        .setParameter("commentId", reply.getOriginalComment().getId()).setParameter("replyId", reply.getId())
        .uniqueResult();

    return commentCount.intValue();
  }

  private static final String FIND_BY_IDS_HQL =
    "from SongCommentReply " +
    "where id in (:ids)";

  public List findRepliesByIds(Set multiquoteReplyIds) {
    return getCurrentSession().createQuery(FIND_BY_IDS_HQL).setParameterList("ids", multiquoteReplyIds).list();
  }

  private static final String COUNT_REPLIES_BY_COMMENT_HQL =
    " select count(*) " +
    " from SongCommentReply " +
    " where originalComment = :comment ";

  public int countReplies(SongComment comment)  {
    return ((Long) getCurrentSession().createQuery(COUNT_REPLIES_BY_COMMENT_HQL).setParameter("comment", comment).list().get(0)).intValue();
  }
}
