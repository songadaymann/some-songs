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

@Controller
public class AdminDeleteCommentController {

  private SongCommentService songCommentService;

  @RequestMapping("/admin/delete_comment")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int commentId) {

    ModelAndView modelAndView = new ModelAndView();

    SongComment comment = songCommentService.getComment(commentId);
    if (comment == null) {
      throw new NotFoundException("Could not find comment with id " + commentId);
    }

    String songRedirect = "redirect:" + PathsUtil.makePath(comment.getSong());
    modelAndView.setViewName(songRedirect);

    songCommentService.deleteComment(comment);
    modelAndView.addObject("commentDeleted", "true");

    return modelAndView;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }
}
