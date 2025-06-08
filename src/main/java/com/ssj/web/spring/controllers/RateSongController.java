package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

import com.ssj.service.song.SongService;
import com.ssj.service.song.SongRatingService;
import com.ssj.model.user.User;
import com.ssj.model.song.Song;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class RateSongController {
  private SongService songService;
  private SongRatingService songRatingService;

  @RequestMapping("/user/rate_song")
  protected ModelAndView handleRequestInternal(HttpServletRequest request) throws Exception {
    Map<String, Object> model = new HashMap<String, Object>();

    String ratingString = request.getParameter("rating");

    try {
      User user = SecurityUtil.getUser();

      Song song = null;
      String songIdString = request.getParameter("songId");
      if (songIdString != null) {
        int songId = NumberUtils.toInt(songIdString, -1);
        if (songId > -1) {
          song = songService.getSong(songId);
        }
      }

      if (song != null) {
        songRatingService.rateSong(user, song, ratingString);
        model.put("success", "true");
        // update the song
        song = songService.getSong(song.getId());
        int numRatings = song.getNumRatings();
        model.put("numRatings", numRatings);
        if (song.isShowRating()) {
          model.put("rating", song.getRatingString());
        } else {
          model.put("numRatingsNeeded", song.getNumRatingsNeeded());
        }
//        model.put("song", song);
      } else {
        model.put("error", "Unable to find song with id " + songIdString);
      }
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setSongRatingService(SongRatingService songRatingService) {
    this.songRatingService = songRatingService;
  }
}
