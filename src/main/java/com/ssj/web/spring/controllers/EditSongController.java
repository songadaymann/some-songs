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
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongException;
import com.ssj.service.artist.ArtistService;
import com.ssj.model.song.Song;
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
public class EditSongController {

  private static final Logger LOGGER = Logger.getLogger(EditSongController.class);

  private String formView = "user/edit_song";
  private String successView = "redirect:/song_info";

  private SongService songService;
  private ArtistService artistService;

  @ModelAttribute
  protected Song formBackingObject(@RequestParam("songId") int songId) {
    Song song = songService.getSong(songId);
    if (song != null) {
      User user = SecurityUtil.getUser();
      if (user != null) {
        if (!artistService.canEditArtist(song.getArtist(), user)) {
          throw new SongException("You do not have permission to edit this song");
        }
      } else {
        throw new SongException("You must be logged in to edit a song");
      }
    } else {
      throw new SongException("Unable to find song with id " + songId);
    }
    return song;
  }

  @RequestMapping(value = "/user/edit_song", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/edit_song", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute @Valid Song song, BindingResult errors) {
    ModelAndView modelAndView = new ModelAndView(getFormView());
    if (!errors.hasErrors()) {
      try {
        songService.save(song);

        modelAndView.setViewName(getSuccessView());
        modelAndView = modelAndView.addObject("id", song.getId());
      } catch (Exception e) {
        LOGGER.error(e);
        errors.reject("error.song", e.getMessage());
      }
    }
    if (errors.hasErrors()) {
      modelAndView.addAllObjects(errors.getModel());
    }
    return modelAndView;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
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
