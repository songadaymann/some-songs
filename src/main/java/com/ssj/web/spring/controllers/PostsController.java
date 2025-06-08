package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.messageboard.MessageBoardPost;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class PostsController {

  private static final Logger LOGGER = Logger.getLogger(PostsController.class);

  private String viewName = "message_board_search";

  private static final int DEFAULT_RESULTS_PER_PAGE = 30;

  private MessageBoardService messageBoardService;

  @RequestMapping("/posts")
  protected ModelAndView handleRequestInternal(WebRequest request,
                                               @RequestParam(value = "start", required = false, defaultValue = "0") int start) {

    MessageBoardPostSearch search = (MessageBoardPostSearch) request.getAttribute("sidebarPostSearch", WebRequest.SCOPE_SESSION);
    if (search == null) {
      search = new MessageBoardPostSearch();
    }

    if (start > 0) {
      // turn 1-based start into 0-based row num
      start--;
      search.setStartResultNum(start);
    }

    search.setResultsPerPage(DEFAULT_RESULTS_PER_PAGE);

    ModelMap model = new ModelMap();
    model.addAttribute("sidebarPostSearch", search);

    List<MessageBoardPost> posts;
    try {
      posts = messageBoardService.findPosts(search);
      model.addAttribute("postSearchResults", posts);
      LOGGER.debug("total post/thread search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  public String getViewName() {
    return viewName;
  }
}
