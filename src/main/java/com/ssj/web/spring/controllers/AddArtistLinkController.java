package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.user.UserService;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.artist.ArtistException;
import com.ssj.model.artist.ArtistLink;
import com.ssj.model.artist.Artist;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AddArtistLinkController {

  private static final Logger LOGGER = Logger.getLogger(AddArtistLinkController.class);

  private String formView = "user/edit_link";
  private String successView = "redirect:/artist_links";

  private ArtistService artistService;

  @ModelAttribute
  protected ArtistLink getArtistLink(@RequestParam("artistId") int artistId) {
    ArtistLink artistLink = new ArtistLink();
    Artist artist = artistService.getArtist(artistId);
    if (artist != null) {
      User user = SecurityUtil.getUser();
      if (user != null) {
        if (artistService.canEditArtist(artist, user)) {
          artistLink.setArtist(artist);
        } else {
          throw new ArtistException("You do not have permission to add links to this artist");
        }
      } else {
        throw new ArtistException("You must be logged in to edit an artist link");
      }
    } else {
      throw new ArtistException("Unable to find artist with id " + artistId);
    }
    return artistLink;
  }

  @RequestMapping(value = "/user/add_link", method = RequestMethod.GET)
  public String getHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/add_link", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute @Valid ArtistLink artistLink, BindingResult errors) {
    ModelAndView modelAndView = new ModelAndView(getFormView());
    if (!errors.hasErrors()) {
      try {
        artistService.saveLink(artistLink);

        modelAndView.setViewName(getSuccessView());
        modelAndView.addObject("id", artistLink.getArtist().getId());
  //      modelAndView.addObject("canEdit", true);
      } catch (Exception e) {
        LOGGER.error(e);
        errors.reject("error.artistLink", e.getMessage());
      }
    }
    if (errors.hasErrors()) {
      modelAndView = modelAndView.addAllObjects(errors.getModel());
    }
    return modelAndView;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}