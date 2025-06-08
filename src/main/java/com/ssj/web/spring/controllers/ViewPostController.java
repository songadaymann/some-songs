package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ViewPostController {

  private MessageBoardService messageBoardService;

  @RequestMapping("/view_post")
  protected ModelAndView get(@RequestParam(value = "id", required = false, defaultValue = "1") int requestPostId) {
    int threadId = 1;
    Integer start = null;
    Integer postId = null;

    MessageBoardPost post = messageBoardService.getPost(requestPostId);

    if (post != null) {
      if (post.getOriginalPost() == null) {
        // post is first in thread
        threadId = requestPostId;
      } else {
        // post is a reply
        threadId = post.getOriginalPost().getId();
        int postStartNum = messageBoardService.getThreadPosition(post);

        MessageBoardPostSearch search = new MessageBoardPostSearch();
        // divide post start by results per page to get page num, multiply by results per page to get page start num
        int pageStartNum = (postStartNum / search.getResultsPerPage()) * search.getResultsPerPage();

        if (pageStartNum > 0) {
          start = pageStartNum;
        }

        if (postStartNum != pageStartNum) {
          postId = post.getId();
        }
      }
    }

    String url = "/thread?id=" + threadId;
    if (start != null) {
      url += "&start=" + start;
    }
    if (postId != null) {
      url += "#post" + postId;
    }

    RedirectView view = new RedirectView(url);
    view.setContextRelative(true);

    return new ModelAndView(view);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }
}
