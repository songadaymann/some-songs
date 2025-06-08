package com.ssj.dao.messageboard;

import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.user.User;
import com.ssj.dao.SearchDAO;

import java.util.Set;
import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface MessageBoardPostDAO extends SearchDAO<MessageBoardPost, MessageBoardPostSearch> {
  public int countPosts(User user);

  public int countThreads(User user);

  public List findPostsById(Set multiquotePostIds);

  public List findPostsByTopicId(int topicId);

  public int getThreadPosition(MessageBoardPost post);
}
