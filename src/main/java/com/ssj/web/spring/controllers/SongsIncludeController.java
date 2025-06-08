package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.apache.commons.lang.StringUtils;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.SongRating;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongRatingService;
import com.ssj.service.song.SongSearchService;
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
public class SongsIncludeController {

  private static final Logger LOGGER = Logger.getLogger(SongsIncludeController.class);

  private SongService songService;
  private SongSearchService songSearchService;
  private SongRatingService songRatingService;

  @RequestMapping("/song_list")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "songSearchId", required = false) Integer songSearchId,
                                               @RequestParam(value = "artistName", required = false) String artistName,
                                               @RequestParam(value = "inUsersFavorites", required = false) Integer inUsersFavorites,
                                               @RequestParam(value = "songListHidePaging", required = false) String songListHidePaging) {
//    String isFavorite = request.getParameter("favorite");

    SongSearch search = null;
    if (songSearchId != null) {
      search = songSearchService.getSongSearch(songSearchId);
    } else if (StringUtils.isNotBlank(artistName)) {
      search = new SongSearch();
      search.setArtistName(artistName);
      search.setArtistNameExactMatch(true);
      search.setResultsPerPage(20);
    } else if (inUsersFavorites != null) {
      search = new SongSearch();
      search.setName("Favorite Songs");
      search.setInUsersFavorites(inUsersFavorites);
      search.setResultsPerPage(5);
      search.setResultsPerNextPage(20);
    }
    if (search == null) {
      search = songSearchService.getDefaultSearch();
    }
    List<Song> songs = null;
    try {
      songs = songService.findSongs(search);
      LOGGER.debug("total artist song search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModelMap model = new ModelMap();
    model.addAttribute("songSearch", search);
    model.addAttribute("songSearchResults", songs);
    model.addAttribute("songListHidePaging", songListHidePaging);
    model.addAttribute("isInclude", "true");

    User user = SecurityUtil.getUser();
    if (user != null && search != null && songs != null && !songs.isEmpty()) {
      // load the user's ratings for the songs
      List<SongRating> ratings = songRatingService.getRatings(user, songs);
      Map<Integer, SongRating> ratingsMap = new HashMap<Integer, SongRating>();
      for (SongRating songRating : ratings) {
        ratingsMap.put(songRating.getSong().getId(), songRating);
      }

      model.addAttribute("ratingsMap", ratingsMap);
    }

//    boolean foundSongs = search.getTotalResults() > 0;
    return new ModelAndView("include/song_list", model);//"songSearch", search);
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
}
