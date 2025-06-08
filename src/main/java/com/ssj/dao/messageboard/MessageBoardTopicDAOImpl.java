package com.ssj.dao.messageboard;

import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.dao.AbstractSpringDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
@Repository
public class MessageBoardTopicDAOImpl extends AbstractSpringDAO<MessageBoardTopic> implements MessageBoardTopicDAO {

  private static final String FIND_ALL_HQL =
      "from MessageBoardTopic where id > 0 order by name";

  @Autowired
  public MessageBoardTopicDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<MessageBoardTopic> findTopics() {
    return (List<MessageBoardTopic>) getCurrentSession().createQuery(FIND_ALL_HQL).list();
  }
}
