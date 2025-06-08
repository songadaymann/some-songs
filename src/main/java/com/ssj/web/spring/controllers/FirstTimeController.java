package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.ssj.service.content.PageContentService;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class FirstTimeController {

  private String viewName = "first_time";

  private PageContentService contentService;

  @RequestMapping("/first_time")
  protected ModelAndView handleRequestInternal() {
    ModelMap model = new ModelMap();

    List faqContent = contentService.getFirstTimeContent();

    model.addAttribute("firstTimeContent", faqContent);

    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setContentService(PageContentService contentService) {
    this.contentService = contentService;
  }

  public String getViewName() {
    return viewName;
  }
}