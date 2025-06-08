package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.content.PageContentService;
import com.ssj.model.content.PageContent;

import javax.validation.Valid;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AdminContentController {

  private String formView = "admin/content";
  private String successView = "redirect:/admin/content_list";

  private PageContentService contentService;

  @ModelAttribute("content")
  protected PageContent populateContent(@RequestParam(value = "id", required = false) Integer contentId,
                                        @RequestParam(value = "type", required = false) Integer contentType) {
    PageContent content = null;
    if (contentId != null) {
      content = contentService.getContent(contentId);
    }
    if (content == null) {
      content = new PageContent();
    }
    if (contentType != null) {
      content.setType(contentType);
    }

    return content;
  }

  @RequestMapping(value = "/admin/content", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/admin/content", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute("content") @Valid PageContent content, BindingResult errors) {
    ModelAndView modelAndView;
    try {
      contentService.save(content);

      modelAndView = new ModelAndView(getSuccessView(), "saved", "true");
    } catch (Exception e) {
      e.printStackTrace();
      errors.reject("error.content", e.getMessage());
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    }
    return modelAndView;
  }

  @Autowired
  public void setContentService(PageContentService contentService) {
    this.contentService = contentService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}
