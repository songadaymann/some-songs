package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.model.user.User;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.artist.Artist;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.service.artist.ArtistService;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class MyArtistsController {

  private String viewName = "include/my_artists";

  private static final int NUM_MY_ARTISTS = 8;

  private ArtistService artistService;

  @RequestMapping("/include/my_artists")
  protected ModelAndView handleRequestInternal() {
    User user = SecurityUtil.getUser();

    ArtistSearch search = new ArtistSearch();
    search.setUser(user);
    search.setResultsPerPage(NUM_MY_ARTISTS);

    List<Artist> myArtists = artistService.findArtists(search);

    ModelAndView modelAndView = new ModelAndView(getViewName(), "myArtists", myArtists);
    modelAndView.addObject("myArtistsSearch", search);

    modelAndView.addObject("userId", user.getId());

    return modelAndView;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  public String getViewName() {
    return viewName;
  }
}