package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.service.messageboard.MessageBoardException;
import com.ssj.service.user.UserException;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class EditThreadController {

  private static final Logger LOGGER = Logger.getLogger(EditThreadController.class);

  private String formView = "user/edit_thread";
  private String successView = "redirect:/thread";

  private MessageBoardService messageBoardService;

  @ModelAttribute
  protected void formBackingObject(Model model, @RequestParam(value = "id", required = false) Integer threadId) {
    MessageBoardPost post = null;
    if (threadId != null) {
      post = messageBoardService.getPost(threadId);
    }

    if (post == null) {
      post = new MessageBoardPost();
    } else {
      User user = SecurityUtil.getUser();
      if (user == null || (user.getId() != post.getUser().getId() && !user.isAdmin())) {
        throw new MessageBoardException("You do not have access to edit this thread");
      }
    }
    post.mergeContent();
    if (post.getTopic() == null) {
      post.setTopic(new MessageBoardTopic());
    }

    model.addAttribute("thread", post);


    List topics = messageBoardService.findTopics();
    model.addAttribute("topics", topics);
  }

  @RequestMapping(value = "/user/edit_thread", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/edit_thread", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute("thread") @Valid MessageBoardPost post, BindingResult errors) {

    if (!errors.hasErrors()) {
      try {
        User user = SecurityUtil.getUser();

        if (post.getId() == 0) {
          post.setUser(user);
        } else if (user == null || (user.getId() != post.getUser().getId() && !user.isAdmin())) {
          throw new MessageBoardException("You do not have access to edit this post");
        }


        messageBoardService.savePost(post);

      } catch (UserException e) {
        LOGGER.error(e);
        errors.reject("error.thread", e.getMessage());
      }
    }
    ModelAndView modelAndView;
    if (errors.hasErrors()) {
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    } else {
      modelAndView = new ModelAndView(getSuccessView(), "id", post.getId());
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
