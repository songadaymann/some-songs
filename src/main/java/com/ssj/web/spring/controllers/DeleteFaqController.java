package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.model.user.User;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DeleteFaqController {
  private MessageBoardService messageBoardService;

  @RequestMapping("/user/delete_faq")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int postId) {
    Map model = new HashMap();
    User user = SecurityUtil.getUser();
    MessageBoardPost post = messageBoardService.getPost(postId);
    if (post.getUser().getId() != user.getId() && !user.isAdmin()) {
      model.put("error", "You can only delete your own FAQ posts");
    } else {
      messageBoardService.deletePost(post);
      model.put("success", "true");
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }
}
