package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.artist.Artist;
import com.ssj.service.artist.ArtistService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class OtherUsersController {

  private String viewName = "include/other_users";

  private ArtistService artistService;

  @RequestMapping("/include/other_users")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int artistId) {

    Artist artist = artistService.getArtist(artistId);

    if (artist == null) {
      throw new RuntimeException("No artist with id " + artistId);
    }

    return new ModelAndView(getViewName(), "otherUsers", artist.getOtherUsers());
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  public String getViewName() {
    return viewName;
  }
}
