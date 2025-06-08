package com.ssj.web.spring.controllers;

import com.ssj.model.user.User;
import com.ssj.service.user.UserService;
import com.ssj.web.spring.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.math.NumberUtils;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.artist.ArtistException;
import com.ssj.model.artist.Artist;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ArtistInfoController {

  private String viewName = "include/artist_info";

  private ArtistService artistService;

  @RequestMapping("/artist_info")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int artistId) {
    Artist artist = artistService.getArtist(artistId);
    // TODO handle exception/error better (need new view to send people to)
    if (artist == null) {
      throw new ArtistException("Unable to find artist with id " + artistId);
    }

    ModelAndView modelAndView = new ModelAndView(getViewName());
    modelAndView.addObject("artist", artist);

    User user = SecurityUtil.getUser();
    if (artistService.canEditArtist(artist, user)) {
      modelAndView.addObject("canEdit", true);
    }

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
