package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.artist.ArtistException;
import com.ssj.service.user.UserService;
import com.ssj.model.artist.Artist;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ArtistLinksController {

  private String viewName = "include/artist_links";

  private ArtistService artistService;

  @RequestMapping("/artist_links")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int artistId) {


    Artist artist = artistService.getArtist(artistId);
    // TODO handle exception/error better (need new view to send people to)
    if (artist == null) {
      throw new ArtistException("Unable to find artist with id " + artistId);
    }

    ModelMap model = new ModelMap();
    model.addAttribute("artist", artist);

    User user = SecurityUtil.getUser();
    if (artistService.canEditArtist(artist, user)) {
      model.addAttribute("canEdit", true);
    }

    return new ModelAndView(getViewName(), model);
  }


  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  public String getViewName() {
    return viewName;
  }
}
