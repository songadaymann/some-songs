package com.ssj.web.spring.controllers;

import com.ssj.web.spring.exception.NotFoundException;
import com.ssj.web.util.PathsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.song.SongCommentService;
import com.ssj.model.song.SongCommentReply;
import com.ssj.model.song.search.SongCommentReplySearch;

@Controller
public class ViewCommentReplyController {

  private SongCommentService songCommentService;

  @RequestMapping("/view_comment_reply")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "id") int requestReplyId) {
    Integer start = null;
    Integer replyId = null;

    SongCommentReply reply = songCommentService.getSongCommentReply(requestReplyId);

    if (reply == null) {
      throw new NotFoundException("Could not find comment reply with id " + requestReplyId);
    }

    int replyStartNum = songCommentService.getReplyPosition(reply);

    SongCommentReplySearch search = new SongCommentReplySearch();
    // divide post start by results per page to get page num, multiply by results per page to get page start num
    int pageStartNum = (replyStartNum / search.getResultsPerPage()) * search.getResultsPerPage();

    if (pageStartNum > 0) {
      start = pageStartNum;
    }

    if (replyStartNum != pageStartNum) {
      replyId = reply.getId();
    }

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("comment", reply.getOriginalComment().getId());
    String songRedirect = "redirect:" + PathsUtil.makePath(reply.getOriginalComment().getSong());
    if (replyId != null) {
      songRedirect += "#reply" + replyId;
    }
    if (start != null) {
      // convert 0-based start num to 1-based for URL
      modelAndView.addObject("start", (start + 1));
    }
    modelAndView.setViewName(songRedirect);

    return modelAndView;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }
}