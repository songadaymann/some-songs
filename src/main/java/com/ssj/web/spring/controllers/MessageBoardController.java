package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
@Controller
public class MessageBoardController {

  private String viewName = "message_board";

  private MessageBoardService messageBoardService;

  @RequestMapping("/message_board")
  protected ModelAndView handleRequestInternal(WebRequest request,
                                               @RequestParam(value = "topicId", required = false) Integer topicId,
                                               @RequestParam(value = "threadsStart", required = false) Integer threadsStart,
                                               @RequestParam(value = "postsStart", required = false) Integer postsStart) {
    MessageBoardPostSearch threadSearch;
    MessageBoardPostSearch postSearch;

    if (topicId != null) {
      threadSearch = new MessageBoardPostSearch();
      postSearch = new MessageBoardPostSearch();
      try {
        MessageBoardTopic topic = messageBoardService.getTopic(topicId);
        threadSearch.setTopic(topic);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (threadsStart != null) {
      threadSearch = (MessageBoardPostSearch) request.getAttribute("threadSearch", WebRequest.SCOPE_SESSION);
      postSearch = (MessageBoardPostSearch) request.getAttribute("postSearch", WebRequest.SCOPE_SESSION);
      if (threadSearch == null) {
        threadSearch = new MessageBoardPostSearch();
      }
      threadSearch.setStartResultNum(threadsStart);
    } else if (postsStart != null) {
      threadSearch = (MessageBoardPostSearch) request.getAttribute("threadSearch", WebRequest.SCOPE_SESSION);
      postSearch = (MessageBoardPostSearch) request.getAttribute("postSearch", WebRequest.SCOPE_SESSION);
      if (postSearch == null) {
        postSearch = new MessageBoardPostSearch();
      }
      postSearch.setStartResultNum(postsStart);
    } else {
      threadSearch = (MessageBoardPostSearch) request.getAttribute("threadSearch", WebRequest.SCOPE_SESSION);
      postSearch = (MessageBoardPostSearch) request.getAttribute("postSearch", WebRequest.SCOPE_SESSION);
    }
    if (threadSearch == null) {
      threadSearch = new MessageBoardPostSearch();
      threadSearch.setOnlyThreads(true);
      threadSearch.setOrderBy(MessageBoardPostSearch.ORDER_BY_LAST_REPLY_DATE);

    }
    if (postSearch == null) {
      postSearch = new MessageBoardPostSearch();
      postSearch.setResultsPerPage(10);
      postSearch.setOrderBy(MessageBoardPostSearch.ORDER_BY_POST_DATE);
    }

    User user = SecurityUtil.getUser();
    if (user != null) {
      threadSearch.setNotByIgnoredUsers(user.getId());
      postSearch.setNotByIgnoredUsers(user.getId());
    }

    List<MessageBoardPost> threads = messageBoardService.findPosts(threadSearch);
    List<MessageBoardPost> posts = messageBoardService.findPosts(postSearch);

    request.setAttribute("threadSearch", threadSearch, WebRequest.SCOPE_SESSION);
    request.setAttribute("postSearch", postSearch, WebRequest.SCOPE_SESSION);

    List topics = messageBoardService.findTopics();

    ModelAndView modelAndView = new ModelAndView(getViewName(), "topics", topics);
    modelAndView.addObject("threads", threads);
    modelAndView.addObject("posts", posts);

    return modelAndView;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  public String getViewName() {
    return viewName;
  }
}
