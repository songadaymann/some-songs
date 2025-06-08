package com.ssj.web.spring.controllers;

import com.ssj.web.spring.exception.NotFoundException;
import com.ssj.web.util.PathsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.song.SongCommentService;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.search.SongCommentSearch;

@Controller
public class ViewCommentController {

  private SongCommentService songCommentService;

  @RequestMapping("/view_comment")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "id") int requestCommentId) {
    Integer start = null;
    Integer commentId = null;

    SongComment comment = songCommentService.getSongComment(requestCommentId);

    if (comment == null) {
      throw new NotFoundException("Could not find comment with id " + requestCommentId);
    }

    int commentStartNum = songCommentService.getCommentPosition(comment);

    SongCommentSearch search = new SongCommentSearch();
    // divide post start by results per page to get page num, multiply by results per page to get page start num
    int pageStartNum = (commentStartNum / search.getResultsPerPage()) * search.getResultsPerPage();

    if (pageStartNum > 0) {
      start = pageStartNum;
    }

    if (commentStartNum != pageStartNum) {
      commentId = comment.getId();
    }

    ModelAndView modelAndView = new ModelAndView();
    String songRedirect = "redirect:" + PathsUtil.makePath(comment.getSong());
    if (commentId != null) {
      songRedirect += "#comment" + commentId;
    }
    modelAndView.setViewName(songRedirect);
    if (start != null) {
      // song comment indexes are 1-based
      modelAndView.addObject("start", (start + 1));
    }

    return modelAndView;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }
}