package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistRatingService;
import com.ssj.model.user.User;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.playlist.search.PlaylistSearchFactory;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistRating;
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
public class PlaylistsController {

  private static final Logger LOGGER = Logger.getLogger(PlaylistsController.class);

  private String viewName = "playlists";

  private PlaylistService playlistService;
  private PlaylistRatingService playlistRatingService;

  @RequestMapping("/playlists")
  protected ModelAndView handleRequestInternal(WebRequest webRequest,
                                               @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                               @RequestParam(value = "resultsPerPage", required = false, defaultValue = "0") int resultsPerPage) {
//    String songSearchId = request.getParameter("songSearchId");
//    int searchId = NumberUtils.toInt(songSearchId, 0);
//    boolean hidePaging = "true".equals(request.getParameter("songListHidePaging"));

    PlaylistSearch search;
//    if (StringUtils.isNotBlank(songSearchId)) {
//      search = songSearchService.getSongSearch(searchId);
//    } else {
      search = (PlaylistSearch) webRequest.getAttribute("playlistSearch", WebRequest.SCOPE_REQUEST);
      if (search == null) {
        search = (PlaylistSearch) webRequest.getAttribute("playlistSearch", WebRequest.SCOPE_SESSION);
      }
//    }
    if (search == null) {
      // default to showing newest playlists
      search = PlaylistSearchFactory.getNewestSearch();
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
    model.addAttribute("playlistSearch", search);

    List<Playlist> playlists = null;
    try {
      playlists = playlistService.findPlaylists(search);
      model.addAttribute("playlistSearchResults", playlists);
      LOGGER.debug("total playlists search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    User user = SecurityUtil.getUser();
    if (user != null && playlists != null && !playlists.isEmpty()) {
      List<PlaylistRating> ratings = playlistRatingService.getRatings(user, playlists);
      Map<Integer, PlaylistRating> ratingsMap = new HashMap<Integer, PlaylistRating>();
      for (PlaylistRating playlistRating : ratings) {
        ratingsMap.put(playlistRating.getPlaylist().getId(), playlistRating);
      }

      model.addAttribute("ratingsMap", ratingsMap);
    }

//    boolean foundSongs = search.getTotalResults() > 0;
    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  @Autowired
  public void setPlaylistRatingService(PlaylistRatingService playlistRatingService) {
    this.playlistRatingService = playlistRatingService;
  }

  public String getViewName() {
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }
}