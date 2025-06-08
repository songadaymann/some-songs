package com.ssj.web.spring.controllers;

import com.ssj.service.song.SongService;
import com.ssj.service.song.SongCommentService;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

@Controller
public class EditCommentController {

  private SongService songService;
  private SongCommentService songCommentService;

  @RequestMapping("/user/edit_comment")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "comment", required = false) String comment,
                                               @RequestParam("songId") int songId) {
    Map model = new HashMap();

    try {
      User user = SecurityUtil.getUser();

      Song song = songService.getSong(songId);

      if (song != null) {
        songCommentService.setSongComment(user, song, comment);
        model.put("success", "true");
      } else {
        throw new IllegalArgumentException("Could not find song with id " + songId);
      }
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }
}
