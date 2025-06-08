package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.service.messageboard.MessageBoardException;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ThreadController {

  private String viewName = "thread";

  private MessageBoardService messageBoardService;

  @RequestMapping("/thread")
  protected ModelAndView handleRequestInternal(WebRequest request,
                                               @RequestParam("id") int threadId,
                                               @RequestParam(value = "start", required = false, defaultValue = "0") int start) {

    MessageBoardPost thread = messageBoardService.getPost(threadId);
    if (thread == null) {
      throw new MessageBoardException("Unable to find thread with id " + threadId);
    }

    // remove multiquote post ids from the session if user is viewing a new thread
    Integer multiquoteThreadId = (Integer) request.getAttribute("multiquoteThreadId", WebRequest.SCOPE_SESSION);
    if (multiquoteThreadId != null && !multiquoteThreadId.equals(thread.getId())) {
      request.removeAttribute("multiquoteThreadId", WebRequest.SCOPE_SESSION);
      request.removeAttribute("multiquotePostIds", WebRequest.SCOPE_SESSION);
    }

    MessageBoardPostSearch search = new MessageBoardPostSearch();
    search.setOriginalPost(thread);
    search.setAlwaysShowFullPage(false);

    search.setStartResultNum(start);
    search.setOrderBy(MessageBoardPostSearch.ORDER_BY_POST_DATE);
    search.setDescending(false);
    List<MessageBoardPost> posts = messageBoardService.findPosts(search);

    // get topic list for right nav
    List<MessageBoardTopic> topics = messageBoardService.findTopics();

    ModelAndView modelAndView = new ModelAndView(getViewName(), "thread", thread);
    modelAndView.addObject("search", search);
    modelAndView.addObject("searchResults", posts);
    User user = SecurityUtil.getUser();
    modelAndView.addObject("user", user);
    modelAndView.addObject("topics", topics);

    // get current topic here to keep this logic out of the JSP
    MessageBoardTopic currentTopic;
    if (posts != null && !posts.isEmpty()) {
      MessageBoardPost post = posts.get(0);
      if (post.getOriginalPost() != null) {
        currentTopic = post.getOriginalPost().getTopic();
      } else {
        currentTopic = post.getTopic();
      }
      modelAndView.addObject("currentTopic", currentTopic);
    }

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
