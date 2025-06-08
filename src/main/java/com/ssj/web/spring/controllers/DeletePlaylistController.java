package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DeletePlaylistController {
  private PlaylistService playlistService;

  @RequestMapping("/user/delete_playlist")
  protected ModelAndView handleRequestInternal(@RequestParam("playlistId") int playlistId) {
    Map<String, Object> model = new HashMap<String, Object>();

    User user = SecurityUtil.getUser();
    if (user != null) {
      Playlist playlist = playlistService.getPlaylist(playlistId);
      if (playlist != null) {
        if (playlist.getUser().getId() == user.getId() || user.isAdmin()) {
          // delete stuff ... ? comment replies, comments, ratings, then playlist?
          playlistService.delete(playlist);
          model.put("success", "true");
        } else {
          // error
          model.put("error", "You do not have permission to delete this playlis");
        }
      } else {
        // error
        model.put("error", "Can not find playlist with id " + playlistId);
      }
    } else {
      // error
      model.put("error", "You must be logged in to delete a playlist");
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }
}