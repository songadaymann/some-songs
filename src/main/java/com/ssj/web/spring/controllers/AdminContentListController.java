package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import com.ssj.service.content.PageContentService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AdminContentListController {

  private String viewName = "admin/content_list";

  private PageContentService contentService;

  @RequestMapping("/admin/content_list")
  protected ModelAndView handleRequestInternal() {
    Map model = new HashMap();

    model.put("faqContent", contentService.getFaqContent());
    model.put("firstTimeContent", contentService.getFirstTimeContent());

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
