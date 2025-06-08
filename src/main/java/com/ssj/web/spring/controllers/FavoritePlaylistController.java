package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.user.UserService;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class FavoritePlaylistController {

  private UserService userService;
  private PlaylistService playlistService;

  @RequestMapping("/user/favorite_playlist")
  protected ModelAndView handleRequestInternal(@RequestParam("playlistId") int playlistId) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      User user = SecurityUtil.getUser();

      if (user != null) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        if (playlist != null) {
          userService.toggleFavoritePlaylist(user, playlist);
          model.put("success", "true");
        } else {
          model.put("error", "Unable to find playlist with id " + playlistId);
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
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }
}
