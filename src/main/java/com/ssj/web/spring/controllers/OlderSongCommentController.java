package com.ssj.web.spring.controllers;

import com.ssj.web.util.PathsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.song.SongCommentService;
import com.ssj.model.song.SongComment;

@Controller
public class OlderSongCommentController {

  private SongCommentService songCommentService;

  @RequestMapping("/older_comment")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int commentId,
                                               @RequestParam(value = "songId", required = false) Integer songId) {

    ModelAndView modelAndView = new ModelAndView();
    String songRedirect = "redirect:";
    SongComment songComment;
    if (songId != null) {
      songComment = songCommentService.getOlderCommentForSong(commentId, songId);
    } else {
      songComment = songCommentService.getOlderComment(commentId);
    }
    if (songComment != null) {
      commentId = songComment.getId();
      songRedirect += PathsUtil.makePath(songComment.getSong());
    } else {
      // no older comments, stay on current comment
      SongComment currentComment = songCommentService.getSongComment(commentId);
      songRedirect += PathsUtil.makePath(currentComment.getSong());
    }

    modelAndView.setViewName(songRedirect);
    if (commentId > 0) {
      modelAndView.addObject("comment", commentId);
    }

    return modelAndView;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songService) {
    this.songCommentService = songService;
  }
}