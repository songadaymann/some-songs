package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.model.user.User;
import com.ssj.service.playlist.PlaylistService;

@Controller
public class DeletePlaylistItemController {
  private PlaylistService playlistService;

  @RequestMapping("/user/delete_playlist_item")
  protected ModelAndView handleRequestInternal(@RequestParam("playlistItemId") int playlistItemId) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      User user = SecurityUtil.getUser();

      if (user != null) {
        playlistService.deleteItem(user, playlistItemId);
        model.put("success", "true");
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
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

}