package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.user.UserService;
import com.ssj.service.song.SongService;
import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class FavoriteSongController {

  private UserService userService;
  private SongService songService;

  @RequestMapping("/user/favorite_song")
  protected ModelAndView handleRequestInternal(@RequestParam("songId") int songId) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      User user = SecurityUtil.getUser();

      if (user != null) {
        Song song = songService.getSong(songId);

        if (song != null) {
          userService.toggleFavoriteSong(user, song);
          model.put("success", "true");
        } else {
          model.put("error", "Unable to find song with id " + songId);
        }
      } else {
        model.put("error", "You must be logged in to perform this action");
      }
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}
