package com.ssj.web.spring.controllers;

import com.ssj.web.spring.form.ContactAdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.apache.commons.lang.StringUtils;

import com.ssj.model.content.PageContent;
import com.ssj.model.user.User;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.service.content.PageContentService;
import com.ssj.service.user.UserService;
import com.ssj.service.messageboard.MessageBoardServiceImpl;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.web.spring.security.SecurityUtil;

@Controller
@SessionAttributes("contactAdminForm")
public class ContactAdminController {

  private String viewName = "contact_admin";

  private PageContentService contentService;
  private UserService userService;
  private MessageBoardService messageBoardService;

  private String adminEmailAddress;

  @ModelAttribute("contactAdminForm")
  public ContactAdminForm getMessage() {
    return new ContactAdminForm();
  }

  @RequestMapping(value = "/contact_admin", method = RequestMethod.GET)
  protected ModelAndView getHandler() {
    return new ModelAndView(getViewName(), "adminEmailAddress", adminEmailAddress);
  }

  @RequestMapping(value = "/contact_admin", method = RequestMethod.POST)
  protected ModelAndView postHandler(@ModelAttribute("contactAdminForm") ContactAdminForm contactForm) {

    String viewName = getViewName();
    ModelMap modelMap = new ModelMap();

    if (StringUtils.isNotBlank(contactForm.getContent())) {
      viewName = "contact_confirm";

    } else {
      modelMap.addAttribute("error", "You must enter your message");
    }

    return new ModelAndView(viewName, modelMap);
  }

  @RequestMapping(value = "/contact_confirm", params = "confirm=true")
  protected ModelAndView confirmHandler(@ModelAttribute("contactAdminForm") ContactAdminForm contactForm,
                                        SessionStatus sessionStatus) {
    ModelMap modelMap = new ModelMap();

    if (StringUtils.isNotBlank(contactForm.getContent())) {
      if (contactForm.getAnonymous() == null || contactForm.getAnonymous()) {
        PageContent content = new PageContent();
        content.setContent(contactForm.getContent());
        content.setType(PageContent.TYPE_CONTACT_ADMIN);
        contentService.save(content);
        userService.sendNewAdminMessageEmail();
        modelMap.addAttribute("success", "true");
        sessionStatus.isComplete();
      } else {
        User user = SecurityUtil.getUser();
        if (user != null) {
          MessageBoardPost post = new MessageBoardPost();
          post.setUser(user);
          post.setContent(contactForm.getContent());
          post.setSubject("Message to admin from " + user.getUsername());
          MessageBoardTopic topic = new MessageBoardTopic();
          topic.setId(MessageBoardServiceImpl.TOPIC_ID_CONTACT_ADMIN);
          post.setTopic(topic);
          messageBoardService.savePost(post);
          userService.sendNewAdminMessageEmail();
          modelMap.addAttribute("success", "true");
          sessionStatus.isComplete();
        } else {
          modelMap.addAttribute("error", "You are not logged in");
        }
      }
    } else {
      modelMap.addAttribute("error", "You must enter your message");
    }

    return new ModelAndView("redirect:/" + getViewName(), modelMap);
  }

  @Autowired
  public void setContentService(PageContentService contentService) {
    this.contentService = contentService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  public String getViewName() {
    return viewName;
  }

  @Value("${mail.smtp.username}")
  public void setAdminEmailAddress(String adminEmailAddress) {
    this.adminEmailAddress = adminEmailAddress;
  }
}
