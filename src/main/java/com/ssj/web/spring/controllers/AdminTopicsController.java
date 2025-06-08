package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.messageboard.MessageBoardService;

import java.util.List;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
@Controller
public class AdminTopicsController {

  private String viewName = "admin/topics";

  private MessageBoardService messageBoardService;

  @RequestMapping("/admin/topics")
  protected ModelAndView handleRequestInternal() {

    List topics = messageBoardService.findTopics();

    return new ModelAndView(getViewName(), "topics", topics);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  public String getViewName() {
    return viewName;
  }
}
