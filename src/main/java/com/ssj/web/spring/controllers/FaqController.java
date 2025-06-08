package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.service.content.PageContentService;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class FaqController {

  private String viewName = "faq";

  private MessageBoardService messageBoardService;
  private PageContentService contentService;

  @RequestMapping("/faq")
  public ModelAndView faqHandler() {
    ModelMap model = new ModelMap();

    List faqContent = contentService.getFaqContent();

    model.addAttribute("faqContent", faqContent);

    List posts = messageBoardService.getFaqPosts();

    model.addAttribute("faqPosts", posts);

    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  @Autowired
  public void setContentService(PageContentService contentService) {
    this.contentService = contentService;
  }

  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }
}
