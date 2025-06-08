package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistRatingService;
import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class RatePlaylistController {
  private PlaylistService playlistService;
  private PlaylistRatingService playlistRatingService;

  @RequestMapping("/user/rate_playlist")
  protected ModelAndView handleRequestInternal(@RequestParam("playlistId") int playlistId,
                                               @RequestParam("rating") String ratingString) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      User user = SecurityUtil.getUser();

      Playlist playlist = playlistService.getPlaylist(playlistId);

      if (playlist != null) {
        playlistRatingService.ratePlaylist(user, playlist, ratingString);
        model.put("success", "true");
        // update the playlist
        playlist = playlistService.getPlaylist(playlist.getId());
        int numRatings = playlist.getNumRatings();
        model.put("numRatings", numRatings);
        if (playlist.isShowRating()) {
          model.put("rating", playlist.getRatingString());
        } else {
          model.put("numRatingsNeeded", playlist.getNumRatingsNeeded());
        }
      } else {
        model.put("error", "Unable to find playlist with id " + playlistId);
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

  @Autowired
  public void setPlaylistRatingService(PlaylistRatingService playlistRatingService) {
    this.playlistRatingService = playlistRatingService;
  }
}