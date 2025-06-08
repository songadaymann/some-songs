package com.ssj.service.messageboard;

import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.user.User;

import java.util.List;
import java.util.Set;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
public interface MessageBoardService {
  public List<MessageBoardTopic> findTopics();

  public MessageBoardTopic getTopic(int topicId);

  public void save(MessageBoardTopic topic);

  public void deleteTopic(int topicId);

  public List<MessageBoardPost> findPosts(MessageBoardPostSearch search);

  public void savePost(MessageBoardPost post);

  public MessageBoardPost getPost(int postId);

  public int countPosts(User user);

  public int countThreads(User user);

  public void deletePost(MessageBoardPost post);

  public List getPosts(Set multiquotePostIds);

  public List getFaqPosts();

  public List getAdminMessages();

  public void toggleLocked(MessageBoardPost thread);

  public int getThreadPosition(MessageBoardPost post);
}
