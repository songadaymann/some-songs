package com.ssj.dao.messageboard;

import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;
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
public class MessageBoardPostDAOImpl extends AbstractSpringSearchDAO<MessageBoardPost, MessageBoardPostSearch, MessageBoardPostCriteria> implements MessageBoardPostDAO {
  @Autowired
  public MessageBoardPostDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public void save(MessageBoardPost instance) {
    MessageBoardPost thread = instance.getOriginalPost();
    boolean newReply = instance.getId() == 0 && thread != null;
    super.save(instance);

    // update numReplies of originalPost if inserting a new reply
    // (have to do this here because MySQL won't do this with a trigger)
    // (can't update same table that caused trigger)
    if (newReply) {
      updateNumReplies(thread);
    }
  }

  @Override
  public void delete(MessageBoardPost instance) {
    MessageBoardPost thread = instance.getOriginalPost();

    super.delete(instance);

    if (thread != null) {
      // try to get the delete to actually happen now, before the possible update
      getCurrentSession().flush();

      updateNumReplies(thread);
    }
  }

  private static final String COUNT_REPLIES_HQL =
    "select count(id) from MessageBoardPost where originalPost = :originalPost";

  private void updateNumReplies(MessageBoardPost thread) {
    int numReplies = ((Long) getCurrentSession().createQuery(COUNT_REPLIES_HQL).setParameter("originalPost", thread).iterate().next()).intValue();
    thread.setNumReplies(numReplies);
    save(thread);
  }

  protected Order[] getOrderBy(MessageBoardPostSearch search) {
    Order[] orderByArray = new Order[1];
    String orderByField;
    switch(search.getOrderBy()) {
      case MessageBoardPostSearch.ORDER_BY_LAST_REPLY_DATE:
        orderByField = "lastReplyDate";
        break;
      case MessageBoardPostSearch.ORDER_BY_POST_DATE:
      case MessageBoardPostSearch.ORDER_BY_POST_DATE_ASC:
      default:
        orderByField = "createDate";
        break;
    }
    orderByArray[0] = search.isDescending() ? Order.desc(orderByField) : Order.asc(orderByField);
    return orderByArray;
  }

  private static final String COUNT_POSTS_HQL =
    "select count(id) from MessageBoardPost where originalPost is not null and user = :user";

  public int countPosts(User user) {
    return ((Long) getCurrentSession().createQuery(COUNT_POSTS_HQL).setParameter("user", user).iterate().next()).intValue();
  }

  private static final String COUNT_THREADS_HQL =
    "select count(id) from MessageBoardPost where originalPost is null and user = :user";

  public int countThreads(User user) {
    return ((Long) getCurrentSession().createQuery(COUNT_THREADS_HQL).setParameter("user", user).iterate().next()).intValue();
  }

  private static final String FIND_BY_IDS_HQL =
    "from MessageBoardPost " +
    "where id in (:ids)";

  public List findPostsById(Set multiquotePostIds) {
    return getCurrentSession().createQuery(FIND_BY_IDS_HQL).setParameterList("ids", multiquotePostIds).list();
  }

  private static final String GET_POSTS_BY_TOPIC_HQL =
    "from MessageBoardPost where topic.id = :topicId order by createDate";

  public List findPostsByTopicId(int topicId) {
    return getCurrentSession().createQuery(GET_POSTS_BY_TOPIC_HQL).setParameter("topicId", topicId).list();
  }

  // TODO simplify by not worrying about rows with same create date?
  // or create a new column with the value generated from the create date and id and manage that in java
  private static final String GET_START_NUM_HQL =
    "select count(*) " +
    "from MessageBoardPost " +
    "where (id = :threadId or originalPost.id = :threadId) " +
    "and concat(unix_timestamp(createDate), lpad(id, 5, '0')) < (" +
    " select concat(unix_timestamp(createDate), lpad(id, 5, '0')) " +
    " from MessageBoardPost " +
    " where id = :postId" +
    ")";

  public int getThreadPosition(MessageBoardPost post) {
    int postId = post.getId();
    int threadId = (post.getOriginalPost() == null ? post.getId() : post.getOriginalPost().getId());

    Long postCount = (Long) getCurrentSession().createQuery(GET_START_NUM_HQL)
        .setParameter("threadId", threadId).setParameter("postId", postId)
        .uniqueResult();

    return postCount.intValue();
  }
}
