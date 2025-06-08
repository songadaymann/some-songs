package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.ssj.model.content.PageContent;
import com.ssj.service.content.PageContentService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AdminDeleteContentController {

  private String viewName = "redirect:/admin/content_list";

  private PageContentService contentService;

  @RequestMapping("/admin/delete_content")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int contentId) {
    ModelMap modelMap = new ModelMap();

    PageContent content = contentService.getContent(contentId);
    if (content != null) {
      contentService.deleteContent(content);
      modelMap.addAttribute("deleted", true);
    } else {
      modelMap.addAttribute("deleteError", "Could not find content with id " + contentId);
    }

    return new ModelAndView(getViewName(), modelMap);
  }

  @Autowired
  public void setContentService(PageContentService contentService) {
    this.contentService = contentService;
  }

  public String getViewName() {
    return viewName;
  }
}
