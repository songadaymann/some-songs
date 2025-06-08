package com.ssj.web.spring.controllers;

import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import java.util.Map;
import java.util.HashMap;

@Controller
public class EditPlaylistCommentController {

  private String viewName = "include/playlist_comment";

  private PlaylistService playlistService;
  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/user/edit_playlist_comment")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "comment", required = false) String comment,
                                               @RequestParam("playlistId") int playlistId) {
//    Map model = new HashMap();
    ModelAndView modelAndView;

    try {
      User user = SecurityUtil.getUser();

      Playlist playlist = playlistService.getPlaylist(playlistId);

      if (playlist != null) {
        playlistCommentService.setPlaylistComment(user, playlist, comment);
        if (comment != null) {
          ModelMap model = new ModelMap();
          model.addAttribute("playlist", playlist);
          model.addAttribute("comment", playlistCommentService.getComment(user, playlist));
          modelAndView = new ModelAndView(getViewName(), model);
        } else {
          Map model = new HashMap();
          model.put("success", "true");
          modelAndView = new ModelAndView(new JSONView(), "jsonModel", model);
        }
      } else {
        throw new IllegalArgumentException("Could not find song with id " + playlistId);
      }
    } catch (Exception e) {
      e.printStackTrace();
      Map model = new HashMap();
      model.put("error", e.getMessage());
      modelAndView = new ModelAndView(new JSONView(), "jsonModel", model);
    }

    return modelAndView;//new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}