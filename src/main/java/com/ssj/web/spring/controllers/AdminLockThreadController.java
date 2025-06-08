package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.model.messageboard.MessageBoardPost;

import java.util.Map;
import java.util.HashMap;

@Controller
public class AdminLockThreadController {

  private MessageBoardService messageBoardService;

  @RequestMapping("/admin/lock_thread")
  protected ModelAndView handleRequestInternal(@RequestParam("threadId") int threadId) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      MessageBoardPost thread = messageBoardService.getPost(threadId);

      if (thread != null) {
        messageBoardService.toggleLocked(thread);
        model.put("success", "true");
      } else {
        model.put("error", "Unable to find thread with id " + threadId);
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
