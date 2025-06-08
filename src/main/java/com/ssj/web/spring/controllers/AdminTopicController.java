package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.model.messageboard.MessageBoardTopic;

import javax.servlet.ServletException;
import javax.validation.Valid;

/**
 * Place class javadoc here...
 *
 * @version $Id$
 */
@Controller
public class AdminTopicController {

  private String formView = "admin/topic";
  private String successView = "redirect:/admin/topics";

  private MessageBoardService messageBoardService;

  @ModelAttribute("topic")
  protected MessageBoardTopic populateTopic(@RequestParam(value = "id", required = false) Integer topicId) {
    MessageBoardTopic topic = null;
    if (topicId != null) {
      topic = messageBoardService.getTopic(topicId);
    }
    if (topic == null) {
      topic = new MessageBoardTopic();
    }
    return topic;
  }

  @RequestMapping(value = "/admin/topic", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/admin/topic", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute("topic") @Valid MessageBoardTopic topic, BindingResult errors) throws ServletException {
    ModelAndView modelAndView;
    try {
      messageBoardService.save(topic);

      modelAndView = new ModelAndView(getSuccessView(), "saved", "true");
    } catch (Exception e) {
      e.printStackTrace();
      errors.reject("error.song", e.getMessage());
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    }
    return modelAndView;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}
