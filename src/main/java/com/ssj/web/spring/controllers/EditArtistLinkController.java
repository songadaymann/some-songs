package com.ssj.web.spring.controllers;

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
public class EditArtistLinkController {

  private String formView = "user/edit_link";
  private String successView = "redirect:/artist_links";

  private ArtistService artistService;

  @ModelAttribute
  protected ArtistLink formBackingObject(@RequestParam("id") int artistLinkId) {
    ArtistLink artistLink;
    User user = SecurityUtil.getUser();
    if (user != null) {
      artistLink = artistService.getArtistLink(artistLinkId);
      if (artistLink != null) {
        if (!artistService.canEditArtist(artistLink.getArtist(), user)) {
          throw new ArtistException("You do not have permission to edit this artist link");
        }
      } else {
        throw new ArtistException("Unable to find artist link with id " + artistLinkId);
      }
    } else {
      throw new ArtistException("You must be logged in to edit an artist link");
    }
    return artistLink;
  }

  @RequestMapping(value = "/user/edit_link", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/edit_link", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute @Valid ArtistLink artistLink, BindingResult errors) {
    if (!errors.hasErrors()) {
      try {
        artistService.saveLink(artistLink);

  //      modelAndView.addObject("canEdit", true);
      } catch (Exception e) {
        e.printStackTrace();
        errors.reject("error.artist", e.getMessage());
      }
    }
    ModelAndView modelAndView;
    if (errors.hasErrors()) {
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    } else {
      modelAndView = new ModelAndView(getSuccessView(), "id", artistLink.getArtist().getId());
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
