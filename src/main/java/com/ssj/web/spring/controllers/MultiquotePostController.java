package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.service.messageboard.MessageBoardService;

@Controller
public class MultiquotePostController {
  private MessageBoardService messageBoardService;

  @RequestMapping("/multiquote_post")
  protected ModelAndView get(WebRequest request,
                             @RequestParam("id") int postId) {
    Map<String, String> model = new HashMap<String, String>();

    MessageBoardPost post = messageBoardService.getPost(postId);
    if (post == null) {
      model.put("error", "Unable to find post with id " + postId);
    } else {
      int threadId = (post.getOriginalPost() == null ? post.getId() : post.getOriginalPost().getId());

      Integer multiquoteThreadId = (Integer) request.getAttribute("multiquoteThreadId", WebRequest.SCOPE_SESSION);
      Set multiquotePostIds = (Set) request.getAttribute("multiquotePostIds", WebRequest.SCOPE_SESSION);

      if (multiquotePostIds == null || multiquoteThreadId == null || !multiquoteThreadId.equals(threadId)) {
        multiquoteThreadId = threadId;
        multiquotePostIds = new HashSet();
      }
      if (multiquotePostIds.contains(postId)) {
        multiquotePostIds.remove(postId);
      } else {
        multiquotePostIds.add(postId);
      }

      request.setAttribute("multiquotePostIds", multiquotePostIds, WebRequest.SCOPE_SESSION);
      request.setAttribute("multiquoteThreadId", multiquoteThreadId, WebRequest.SCOPE_SESSION);

      model.put("success", "true");
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }
}
