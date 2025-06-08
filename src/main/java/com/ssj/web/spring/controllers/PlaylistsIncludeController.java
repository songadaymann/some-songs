package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.apache.commons.lang.StringUtils;

import com.ssj.model.user.User;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistRating;
import com.ssj.service.playlist.PlaylistRatingService;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistSearchService;
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
public class PlaylistsIncludeController {

  private static final Logger LOGGER = Logger.getLogger(PlaylistsIncludeController.class);

  private String viewName = "include/playlist_list";

  private PlaylistService playlistService;
  private PlaylistSearchService playlistSearchService;
  private PlaylistRatingService playlistRatingService;

  @RequestMapping("/playlist_list")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "playlistSearchId", required = false) Integer playlistSearchId,
                                               @RequestParam(value = "userDisplayName", required = false) String userDisplayName,
                                               @RequestParam(value = "inUsersFavorites", required = false) Integer inUsersFavorites,
                                               @RequestParam(value = "playlistListHidePaging", required = false) String playlistListHidePaging) {
//    String isFavorite = request.getParameter("favorite");

    PlaylistSearch search = null;
    if (playlistSearchId != null) {
      search = playlistSearchService.getPlaylistSearch(playlistSearchId);
    } else if (StringUtils.isNotBlank(userDisplayName)) {
      search = new PlaylistSearch();
      search.setUserDisplayName(userDisplayName);
      search.setResultsPerPage(20);
    } else if (inUsersFavorites != null) {
      search = new PlaylistSearch();
      search.setName("Favorite Playlists");
      search.setInUsersFavorites(inUsersFavorites);
      search.setResultsPerPage(5);
      search.setResultsPerNextPage(20);
    }
    if (search == null) {
      search = playlistSearchService.getDefaultSearch();
    }
    List<Playlist> playlists = null;
    try {
      playlists = playlistService.findPlaylists(search);
      LOGGER.debug("total playlists search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModelMap model = new ModelMap();
    model.addAttribute("playlistSearch", search);
    model.addAttribute("playlistSearchResults", playlists);
    model.addAttribute("playlistListHidePaging", playlistListHidePaging);
    model.addAttribute("isInclude", "true");

    User user = SecurityUtil.getUser();
    if (user != null && search != null && playlists != null && !playlists.isEmpty()) {
      // load the user's ratings for the playlists
      List<PlaylistRating> ratings = playlistRatingService.getRatings(user, playlists);
      Map<Integer, PlaylistRating> ratingsMap = new HashMap<Integer, PlaylistRating>();
      for (PlaylistRating playlistRating : ratings) {
        ratingsMap.put(playlistRating.getPlaylist().getId(), playlistRating);
      }

      model.addAttribute("ratingsMap", ratingsMap);
    }

//    boolean foundPlaylists = search.getTotalResults() > 0;
    return new ModelAndView(getViewName(), model);//"playlistSearch", search);
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  @Autowired
  public void setPlaylistRatingService(PlaylistRatingService playlistRatingService) {
    this.playlistRatingService = playlistRatingService;
  }

  @Autowired
  public void setPlaylistSearchService(PlaylistSearchService playlistSearchService) {
    this.playlistSearchService = playlistSearchService;
  }

  public String getViewName() {
    return viewName;
  }
}