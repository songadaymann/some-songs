package com.ssj.dao.messageboard;

import com.ssj.dao.DAO;
import com.ssj.model.messageboard.MessageBoardTopic;

import java.util.List;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
public interface MessageBoardTopicDAO extends DAO<MessageBoardTopic> {
  public List<MessageBoardTopic> findTopics();
}
