package com.ssj.web.spring.controllers;

import com.ssj.model.song.Song;
import com.ssj.model.song.search.SongSearch;
import com.ssj.model.user.User;
import com.ssj.service.artist.ArtistException;
import com.ssj.service.song.SongService;
import com.ssj.service.user.UserService;
import com.ssj.web.spring.exception.NotFoundException;
import com.ssj.web.spring.security.SecurityUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.apache.commons.lang.math.NumberUtils;

import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.artist.Artist;
import com.ssj.service.artist.ArtistService;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ArtistsController {

  private static final Logger LOGGER = Logger.getLogger(ArtistsController.class);

  private String singleArtistViewName = "artist";
  private String allArtistsViewName = "artists";

  private UserService userService;
  private ArtistService artistService;
  private SongService songService;

//  @RequestMapping("/artist")
//  protected ModelAndView singleArtistHandler(@RequestParam("id") int artistId) {
  @RequestMapping("/artists/{name:[a-zA-Z0-9\\-]+}-{id:[0-9]+}")
  protected ModelAndView singleArtistHandler(@PathVariable("name") String name,
                                             @PathVariable("id") int id) {

    Artist artist = artistService.getArtist(id);
    if (artist == null) {
      throw new ArtistException("Unable to find artist with id " + id);
    }
    if (!artist.getNameForUrl().equalsIgnoreCase(name)) {
      // return 404
      throw new NotFoundException("Could not find artist with url " + name);
    }

    ModelMap model = new ModelMap();
    model.addAttribute("artist", artist);

    User user = SecurityUtil.getUser();
    if (user != null) {
      if (userService.isFavoriteArtist(user, artist)) {
        model.addAttribute("isFavorite", true);
      }

      if (artistService.canEditArtist(artist, user)) {
        model.addAttribute("canEdit", true);

        SongSearch search = new SongSearch();
        search.setResultsPerPage(20);
        search.setName("Hidden Songs");
        search.setArtistName(artist.getName());
        search.setArtistNameExactMatch(true);
        search.setHidden(true);
        List<Song> hiddenSongs = null;
        try {
          hiddenSongs = songService.findSongs(search);
          LOGGER.debug("total artist song search results = ");
          LOGGER.debug(search.getTotalResults());
        } catch (Exception e) {
          e.printStackTrace();
        }
        model.addAttribute("hiddenSongSearch", search);
        model.addAttribute("hiddenSongs", hiddenSongs);
      }
    }

    return new ModelAndView(singleArtistViewName, model);
  }

  @RequestMapping("/artists")
  protected ModelAndView allArtistsHandler(WebRequest request,
                                           @RequestParam(value = "start", required = false) String startString,
                                           @RequestParam(value = "resultsPerPage", required = false) String resultsPerPageString) {

    ArtistSearch search = (ArtistSearch) request.getAttribute("artistSearch", WebRequest.SCOPE_REQUEST);
    if (search == null) {
      search = (ArtistSearch) request.getAttribute("artistSearch", WebRequest.SCOPE_SESSION);
    }
    if (search == null) {
      search = new ArtistSearch();
    }

    int start = NumberUtils.toInt(startString, 0);
    if (start > 0) {
      // turn 1-based start into 0-based row num
      start--;
      search.setStartResultNum(start);
    }

    int resultsPerPage = NumberUtils.toInt(resultsPerPageString, 0);
    if (resultsPerPage > 0) {
      search.setResultsPerPage(resultsPerPage);
      // put user back on first page
      search.setStartResultNum(0);
    }

    ModelMap model = new ModelMap();
    model.addAttribute("artistSearch", search);

    try {
      List<Artist> artists = artistService.findArtists(search);
      model.addAttribute("artists", artists);
      LOGGER.debug("total artist search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    List artistIndex = artistService.getFirstCharactersOfArtistNames();
    model.addAttribute("artistIndex", artistIndex);

    return new ModelAndView(allArtistsViewName, model);
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}
