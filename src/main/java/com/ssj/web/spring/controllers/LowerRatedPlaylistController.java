package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class LowerRatedPlaylistController {

  private String viewName = "redirect:/playlist";

  private PlaylistService playlistService;

  @RequestMapping("/lower_rated_playlist")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int id) {

    ModelAndView modelAndView = new ModelAndView(getViewName());

    Playlist newerPlaylist = playlistService.getLowerRatedPlaylist(id);

    if (newerPlaylist != null) {
      id = newerPlaylist.getId();
    }

    modelAndView.addObject("id", id);

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