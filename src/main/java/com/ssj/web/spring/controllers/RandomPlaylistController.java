package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.model.playlist.Playlist;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class RandomPlaylistController {

  private String viewName = "redirect:/playlist";

  private PlaylistService playlistService;

  @RequestMapping("/random_playlist")
  protected ModelAndView handleRequestInternal() {
    ModelAndView modelAndView = new ModelAndView(getViewName());

    Playlist random = playlistService.getRandomPlaylist();

    modelAndView.addObject("id", random.getId());

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