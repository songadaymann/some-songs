package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.service.messageboard.MessageBoardService;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class MessageBoardSearchController {

  private String formView = "message_board_search";
  private String successView = "redirect:/posts";

  private MessageBoardService messageBoardService;

  @ModelAttribute()
  protected void formBackingObject(Model model) {
    MessageBoardPostSearch search = new MessageBoardPostSearch();
    search.setTopic(new MessageBoardTopic());
    model.addAttribute("sidebarPostSearch", search);

    // TODO figure out if I really need to manually do this here
    User user = SecurityUtil.getUser();
    model.addAttribute("user", user);

    List<MessageBoardTopic> topics = messageBoardService.findTopics();
    model.addAttribute("topics", topics);
  }

  @RequestMapping(value = "/search_message_board", params = "!search")
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/search_message_board", params = "search=true")
  public ModelAndView onSubmit(WebRequest request, @ModelAttribute("sidebarPostSearch") MessageBoardPostSearch search) {

    if (search.getOrderBy() == MessageBoardPostSearch.ORDER_BY_POST_DATE_ASC) {
      search.setDescending(false);
    }

/*
    int start = NumberUtils.toInt(request.getParameter("start"), 0);
    start--;
    search.setStartResultNum(start);
*/

    request.setAttribute("sidebarPostSearch", search, WebRequest.SCOPE_SESSION);

    return new ModelAndView(getSuccessView());
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
