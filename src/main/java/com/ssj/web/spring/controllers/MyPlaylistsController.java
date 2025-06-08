package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class MyPlaylistsController {

  private String viewName = "include/my_playlists";

  private static final int NUM_PLAYLISTS = 8;

  private PlaylistService playlistService;

  @RequestMapping("/include/my_playlists")
  protected ModelAndView handleRequestInternal() {
    User user = SecurityUtil.getUser();

    List playlists = playlistService.getPlaylists(user, NUM_PLAYLISTS);
    
    int numPlaylists = playlistService.countPlaylists(user);

    ModelAndView modelAndView = new ModelAndView(getViewName(), "myPlaylists", playlists);
    modelAndView.addObject("numPlaylists", numPlaylists);

    return modelAndView;
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  public String getViewName() {
    return viewName;
  }
}