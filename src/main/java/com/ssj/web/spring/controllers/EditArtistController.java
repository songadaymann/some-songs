package com.ssj.web.spring.controllers;

import com.ssj.model.artist.ArtistBandcampAlbum;
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
import com.ssj.model.artist.Artist;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class EditArtistController {

  private static final Logger LOGGER = Logger.getLogger(EditArtistController.class);

  private String formView = "user/edit_artist";
  private String successView = "redirect:/artist_info";

  private ArtistService artistService;

  @ModelAttribute
  protected Artist formBackingObject(@RequestParam("artistId") int artistId) {
    Artist artist = artistService.getArtist(artistId);
    if (artist != null) {
      User user = SecurityUtil.getUser();
      if (user != null) {
        if (!artistService.canEditArtist(artist, user)) {
          throw new ArtistException("You do not have permission to edit this artist");
        }
      } else {
        throw new ArtistException("You must be logged in to edit an artist");
      }
    } else {
      throw new ArtistException("Unable to find artist with id " + artistId);
    }
    return artist;
  }

  @RequestMapping(value = "/user/edit_artist", method = RequestMethod.GET)
  public ModelAndView formHandler(@ModelAttribute Artist artist) {
    ModelAndView modelAndView = new ModelAndView(getFormView());
    List<ArtistBandcampAlbum> bandcampAlbums = artist.getBandcampAlbums();
    long[] bandcampAlbumIds = new long[bandcampAlbums.size()];
    for (int i = 0; i < bandcampAlbums.size(); i++) {
      ArtistBandcampAlbum bandcampAlbum = bandcampAlbums.get(i);
      bandcampAlbumIds[i] = bandcampAlbum.getBandcampAlbumId();
    }
    modelAndView.addObject("bandcampAlbumIds", bandcampAlbumIds);
    return modelAndView;
  }

  @RequestMapping(value = "/user/edit_artist", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute @Valid Artist artist, BindingResult errors,
                               @RequestParam(value = "bandcampAlbumIds", required = false) long[] bandcampAlbumIds) {
    if (!errors.hasErrors()) {
      try {
        artistService.save(artist);
        artistService.saveBandcampSycnhAlbums(artist, bandcampAlbumIds);
      } catch (Exception e) {
        LOGGER.error(e);
        errors.reject("error.artist", e.getMessage());
      }
    }
    ModelAndView modelAndView;
    if (errors.hasErrors()) {
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    } else {
      modelAndView = new ModelAndView(getSuccessView(), "id", artist.getId());
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
