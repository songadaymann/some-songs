package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.song.SongService;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DeleteSongController {
  private SongService songService;

  @RequestMapping("/user/delete_song")
  protected ModelAndView handleRequestInternal(@RequestParam("songId") int songId) {
    Map<String, Object> model = new HashMap<String, Object>();

    User user = SecurityUtil.getUser();
    if (user != null) {
      Song song = songService.getSong(songId);
      if (song != null) {
        if (song.getArtist().getUser().getId() == user.getId()) {
          // delete stuff ... ? comment replies, comments, ratings, then song?
          songService.deleteSong(song);
          model.put("success", "true");
        } else {
          // error
          model.put("error", "You can only delete songs for artists you created");
        }
      } else {
        // error
        model.put("error", "Can not find song with id " + songId);
      }
    } else {
      // error
      model.put("error", "You must be logged in to delete a song");
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}
