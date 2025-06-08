package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.model.user.User;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.service.messageboard.MessageBoardService;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DeleteThreadController {
  private MessageBoardService messageBoardService;

  @RequestMapping("/user/delete_thread")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int threadId) {
    Map<String, String> model = new HashMap<String, String>();

    try {
      MessageBoardPost thread = messageBoardService.getPost(threadId);
      if (thread == null) {
        model.put("error", "Could not find thread with id " + threadId);
      } else {
        if (thread.getOriginalPost() != null) {
          model.put("error", "Id " + threadId + " is for a post, not a thread");
        } else {
          User user = SecurityUtil.getUser();
          if (thread.getUser().getId() != user.getId() && !user.isAdmin()) {
            model.put("error", "You cannot delete threads that were made by other users");
          } else {
            messageBoardService.deletePost(thread);
            model.put("success", "true");
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }
}
