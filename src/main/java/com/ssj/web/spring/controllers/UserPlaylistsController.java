package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.service.playlist.PlaylistService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class UserPlaylistsController {

  private String viewName = "user/playlists";
  private String ajaxMenuViewName = "user/playlist_menu";

  private PlaylistService playlistService;

  public ModelMap getModel() {
    User user = SecurityUtil.getUser();

    ModelMap model = new ModelMap();
    if (user != null) {
      int numPlaylists = playlistService.countPlaylists(user);

      List playlists;

      if (numPlaylists > 0) {
        // TODO load a subset
        playlists = playlistService.getPlaylists(user, numPlaylists);
      } else {
        playlists = new ArrayList(numPlaylists);
      }

      model.addAttribute("playlists", playlists);
    }

    return model;
  }

  @RequestMapping("/user/playlists")
  protected ModelAndView handleRequestInternal() {
    return new ModelAndView(getViewName(), getModel());
  }

  @RequestMapping("/user/playlist_menu")
  public ModelAndView playlistMenuAjaxHandler() {
    return new ModelAndView(getAjaxMenuViewName(), getModel());
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  public String getViewName() {
    return viewName;
  }

  public String getAjaxMenuViewName() {
    return ajaxMenuViewName;
  }
}
