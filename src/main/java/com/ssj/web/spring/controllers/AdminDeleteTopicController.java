package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.messageboard.MessageBoardService;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
@Controller
public class AdminDeleteTopicController {

  private String viewName = "redirect:/admin/topics";
  
  private MessageBoardService messageBoardService;

  @RequestMapping("/admin/delete_topic")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int topicId) {
    ModelAndView modelAndView = new ModelAndView(getViewName());
    try {
      messageBoardService.deleteTopic(topicId);
      modelAndView.addObject("deleted", "true");
    } catch (Exception e) {
      modelAndView.addObject("error", e.getMessage());
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
