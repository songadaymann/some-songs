package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.content.PageContent;
import com.ssj.service.content.PageContentService;
import com.ssj.service.messageboard.MessageBoardService;

@Controller
public class AdminDeleteMessageController {
  private PageContentService contentService;
  private MessageBoardService messageBoardService;

  @RequestMapping("/admin/delete_message")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int messageId,
                                               @RequestParam(value = "anonymous") boolean anonymous) {
    Map model = new HashMap();

    if (anonymous) {
      PageContent message = contentService.getContent(messageId);
      if (message != null) {
        contentService.deleteContent(message);
        model.put("success", "true");
      } else {
        model.put("error", "Unable to find anonymous message with id " + messageId);
      }
    } else {
      MessageBoardPost message = messageBoardService.getPost(messageId);
      if (message != null) {
        messageBoardService.deletePost(message);
        model.put("success", "true");
      } else {
        model.put("error", "Unable to find user message with id " + messageId);
      }
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setContentService(PageContentService contentService) {
    this.contentService = contentService;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }
}
