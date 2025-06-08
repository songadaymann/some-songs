package com.ssj.service.messageboard;

import com.ssj.dao.messageboard.MessageBoardTopicDAO;
import com.ssj.dao.messageboard.MessageBoardPostDAO;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
import java.util.Set;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
@Service
@Transactional(readOnly = true)
public class MessageBoardServiceImpl implements MessageBoardService {

  public static final int TOPIC_ID_FAQ = -1;
  public static final int TOPIC_ID_CONTACT_ADMIN = -2;

  private MessageBoardTopicDAO messageBoardTopicDAO;
  private MessageBoardPostDAO messageBoardPostDAO;

  @Autowired
  public void setMessageBoardTopicDAO(MessageBoardTopicDAO messageBoardTopicDAO) {
    this.messageBoardTopicDAO = messageBoardTopicDAO;
  }

  public List<MessageBoardTopic> findTopics() {
    return messageBoardTopicDAO.findTopics();
  }

  public MessageBoardTopic getTopic(int topicId) {
    return messageBoardTopicDAO.get(topicId);
  }

  @Transactional(readOnly = false)
  public void save(MessageBoardTopic topic) {
    messageBoardTopicDAO.save(topic);
  }

  @Transactional(readOnly = false)
  public void deleteTopic(int topicId) {
    MessageBoardTopic topic = messageBoardTopicDAO.get(topicId);
    if (topic == null) {
      throw new IllegalArgumentException("Could not find topic with id " + topicId);
    }
    messageBoardTopicDAO.delete(topic);
  }

  public List<MessageBoardPost> findPosts(MessageBoardPostSearch search) {
    List<MessageBoardPost> posts = null;
    search.setTotalResults(messageBoardPostDAO.doCount(search));
    if (search.getTotalResults() > 0) {
      posts = messageBoardPostDAO.doSearch(search);
    }
    return posts;
  }

  @Transactional(readOnly = false)
  public void savePost(MessageBoardPost post) {
    if (post.isLocked() || (post.getOriginalPost() != null && post.getOriginalPost().isLocked())) {
      throw new ThreadLockedException("This thread has been locked by an administrator");
    }
    savePostNoLockCheck(post);
  }

  @Transactional(readOnly = false)
  private void savePostNoLockCheck(MessageBoardPost post) {
    post.splitContent();
    Date changeDate = new Date();
    post.setChangeDate(changeDate);
    if (post.getId() == 0) {
      // TODO consolidate this to happen along with numReplies update
      // saving a new thread or reply
      post.setCreateDate(changeDate);
      post.setLastReplyDate(changeDate);
      if (post.getOriginalPost() != null) {
        post.getOriginalPost().setLastReplyDate(changeDate);
        messageBoardPostDAO.save(post.getOriginalPost());
      }
    }
    messageBoardPostDAO.save(post);
  }

  public MessageBoardPost getPost(int postId) {
    return messageBoardPostDAO.get(postId);
  }

  public int countPosts(User user) {
    return messageBoardPostDAO.countPosts(user);
  }

  public int countThreads(User user) {
    return messageBoardPostDAO.countThreads(user);
  }

  @Transactional(readOnly = false)
  public void deletePost(MessageBoardPost post) {
    if (post.isLocked() || (post.getOriginalPost() != null && post.getOriginalPost().isLocked())) {
      throw new ThreadLockedException("This thread has been locked by an administrator");
    }
    messageBoardPostDAO.delete(post);
  }

  public List getPosts(Set multiquotePostIds) {
    return messageBoardPostDAO.findPostsById(multiquotePostIds);
  }

  public List getFaqPosts() {
    return messageBoardPostDAO.findPostsByTopicId(TOPIC_ID_FAQ);
  }

  public List getAdminMessages() {
    return messageBoardPostDAO.findPostsByTopicId(TOPIC_ID_CONTACT_ADMIN);
  }

  @Transactional(readOnly = false)
  public void toggleLocked(MessageBoardPost thread) {
    thread.setLocked(!thread.isLocked());
    thread.setChangeDate(new Date());
    savePostNoLockCheck(thread);
  }

  @Transactional(readOnly = false)
  public void saveFaqPost(MessageBoardPost post) {
    MessageBoardTopic faqTopic = new MessageBoardTopic();
    faqTopic.setId(TOPIC_ID_FAQ);
    post.setTopic(faqTopic);
    savePost(post);
  }

  @Autowired
  public void setMessageBoardPostDAO(MessageBoardPostDAO messageBoardPostDAO) {
    this.messageBoardPostDAO = messageBoardPostDAO;
  }

  public int getThreadPosition(MessageBoardPost post) {
    return messageBoardPostDAO.getThreadPosition(post);
  }
}
