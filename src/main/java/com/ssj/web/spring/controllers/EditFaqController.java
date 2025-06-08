package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.user.User;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.service.messageboard.MessageBoardServiceImpl;
import com.ssj.service.user.UserException;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class EditFaqController {
  private MessageBoardService messageBoardService;

  @RequestMapping("/user/edit_faq")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "id", required = false) Integer postId,
                                               @RequestParam("content") String content) {
    Map modelMap = new HashMap();

    boolean editing = (postId != null);
    try {
      User user = SecurityUtil.getUser();

      MessageBoardPost post = null;
      if (editing) {
        post = messageBoardService.getPost(postId);
        if (post.getUser().getId() != user.getId() && !user.isAdmin()) {
          throw new UserException("You can only edit your own FAQ posts");
        }
      }
      if (post == null) {
        post = new MessageBoardPost();
        MessageBoardTopic faqTopic = new MessageBoardTopic();
        faqTopic.setId(MessageBoardServiceImpl.TOPIC_ID_FAQ);
        post.setTopic(faqTopic);
        post.setUser(user);
      }
      post.setContent(content);
      messageBoardService.savePost(post);
      modelMap.put("success", "true");
    } catch (Exception e) {
      e.printStackTrace();
      modelMap.put("error", e.getMessage());
    }

    return new ModelAndView(new JSONView(), "jsonModel", modelMap);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

}
