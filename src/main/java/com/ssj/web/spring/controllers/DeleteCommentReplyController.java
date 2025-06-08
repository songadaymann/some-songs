package com.ssj.web.spring.controllers;

import com.ssj.model.song.Song;
import com.ssj.web.spring.exception.NotFoundException;
import com.ssj.web.util.PathsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.song.SongCommentService;
import com.ssj.service.song.SongException;
import com.ssj.model.user.User;
import com.ssj.model.song.SongCommentReply;
import com.ssj.model.song.SongComment;
import com.ssj.web.spring.security.SecurityUtil;

@Controller
public class DeleteCommentReplyController {

  private SongCommentService songCommentService;

  @RequestMapping("/user/delete_reply")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int replyId) {

    ModelAndView modelAndView = new ModelAndView();

    User user = SecurityUtil.getUser();

    SongCommentReply reply = songCommentService.getSongCommentReply(replyId);

    if (reply == null) {
      throw new NotFoundException("Could not find reply with id " + replyId);
    }
    if (reply.getUser().getId() != user.getId() && !user.isAdmin()) {
      throw new SongException("You cannot delete replies that were made by other users");
    }

    SongComment originalComment = reply.getOriginalComment();
    modelAndView.addObject("comment", originalComment.getId());

    songCommentService.deleteReply(reply);

    Song song = originalComment.getSong();
    String songRedirect = "redirect:" + PathsUtil.makePath(song);
    modelAndView.setViewName(songRedirect);
    modelAndView.addObject("deleted", "true");

    return modelAndView;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }
}
