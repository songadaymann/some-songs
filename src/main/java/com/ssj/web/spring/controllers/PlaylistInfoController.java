package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistException;
import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.web.spring.security.SecurityUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class PlaylistInfoController {

  private String viewName = "include/playlist_info";

  private PlaylistService playlistService;

  @RequestMapping("/playlist_info")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int playlistId) {
    Playlist playlist = playlistService.getPlaylist(playlistId);
    // TODO handle exception/error better (need new view to send people to)
    if (playlist == null) {
      throw new PlaylistException("Unable to find playlist with id " + playlistId);
    }

    ModelAndView modelAndView = new ModelAndView(getViewName(), "playlist", playlist);

    try {
      User user = SecurityUtil.getUser();

      boolean canEdit = playlistService.canEditPlaylist(playlist, user);
      modelAndView.addObject("canEdit", canEdit);
    } catch (Exception e) {
      e.printStackTrace();
    }

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