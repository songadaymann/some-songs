package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongRatingService;
import com.ssj.service.song.SongSearchService;
import com.ssj.model.song.search.SongSearch;
import com.ssj.service.song.SongSearchFactory;
import com.ssj.model.song.SongRating;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SongsController {

  private static final Logger LOGGER = Logger.getLogger(SongsController.class);

  private String viewName = "songs";

  private SongService songService;
  private SongSearchService songSearchService;
  private SongRatingService songRatingService;
  private SongSearchFactory songSearchFactory;

  @RequestMapping("/songs")
  protected ModelAndView songsHandler(@RequestParam(value = "songSearchId", required = false) Integer songSearchId,
                                      WebRequest webRequest,
                                      @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                      @RequestParam(value = "resultsPerPage", required = false, defaultValue = "0") int resultsPerPage) {
//    boolean hidePaging = "true".equals(request.getParameter("songListHidePaging"));

    SongSearch search;
    if (songSearchId != null) {
      search = songSearchService.getSongSearch(songSearchId);
    } else {
      search = (SongSearch) webRequest.getAttribute("songSearch", WebRequest.SCOPE_REQUEST);
      if (search == null) {
        search = (SongSearch) webRequest.getAttribute("songSearch", WebRequest.SCOPE_SESSION);
      }
    }
    if (search == null) {
      // default to showing newest songs
      search = songSearchFactory.getNewestSongsSearch();
    }

    if (start > 0) {
      // turn 1-based start into 0-based row num
      start--;
      search.setStartResultNum(start);
    }

    if (resultsPerPage > 0) {
      search.setResultsPerPage(resultsPerPage);
      // put user back on first page
      search.setStartResultNum(0);
    }

    // handle sorting also?

    ModelMap model = new ModelMap();
    model.addAttribute("songSearch", search);

    List<Song> songs = null;
    try {
      songs = songService.findSongs(search);
      model.addAttribute("songSearchResults", songs);
      LOGGER.debug("total song search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    User user = SecurityUtil.getUser();
    if (user != null && songs != null && !songs.isEmpty()) {
      List<SongRating> ratings = songRatingService.getRatings(user, songs);
      Map<Integer, SongRating> ratingsMap = new HashMap<Integer, SongRating>();
      for (SongRating songRating : ratings) {
        ratingsMap.put(songRating.getSong().getId(), songRating);
      }

      model.addAttribute("ratingsMap", ratingsMap);
    }

//    boolean foundSongs = search.getTotalResults() > 0;
    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setSongRatingService(SongRatingService songRatingService) {
    this.songRatingService = songRatingService;
  }

  @Autowired
  public void setSongSearchService(SongSearchService songSearchService) {
    this.songSearchService = songSearchService;
  }

  @Autowired
  public void setSongSearchFactory(SongSearchFactory songSearchFactory) {
    this.songSearchFactory = songSearchFactory;
  }

  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }
}
