package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import java.util.List;

import com.ssj.service.content.PageContentService;
import com.ssj.service.messageboard.MessageBoardService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AdminMessagesController {

  private String viewName = "admin/messages";

  private PageContentService contentService;
  private MessageBoardService messageBoardService;

  @RequestMapping("/admin/messages")
  protected ModelAndView handleRequestInternal() {
    ModelMap model = new ModelMap();

    List anonymousMessages = contentService.getContactAdminContent();

    model.addAttribute("anonymousMessages", anonymousMessages);

    List userMessages = messageBoardService.getAdminMessages();

    model.addAttribute("userMessages", userMessages);

    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setContentService(PageContentService contentService) {
    this.contentService = contentService;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  public String getViewName() {
    return viewName;
  }
}
