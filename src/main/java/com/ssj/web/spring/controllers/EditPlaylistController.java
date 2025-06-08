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

import javax.validation.Valid;

import com.ssj.model.playlist.Playlist;
import com.ssj.model.user.User;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistException;
import com.ssj.web.spring.security.SecurityUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class EditPlaylistController {

  private static final Logger LOGGER = Logger.getLogger(EditPlaylistController.class);

  private String formView = "user/edit_playlist";
  private String successView = "redirect:/playlist_info";

  private PlaylistService playlistService;

  @ModelAttribute
  protected Playlist formBackingObject(@RequestParam("playlistId") int playlistId) {
    Playlist playlist = playlistService.getPlaylist(playlistId);
    if (playlist != null) {
      User user = SecurityUtil.getUser();
      if (user != null) {
        if (!playlistService.canEditPlaylist(playlist, user)) {
          throw new PlaylistException("You do not have permission to edit this playlist");
        }
      } else {
        throw new PlaylistException("You must be logged in to edit a playlist");
      }
    } else {
      throw new PlaylistException("Unable to find playlist with id " + playlistId);
    }

    return playlist;
  }

/*
  @Override
  protected Map referenceData(HttpServletRequest httpServletRequest, Object o, Errors errors) throws Exception {
    Map referenceData = new HashMap();
    if (o != null) {
      Playlist playlist = (Playlist) o;

      referenceData.put("items", playlist.getItems());
    }
    return referenceData;
  }
*/
  @RequestMapping(value = "/user/edit_playlist", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/edit_playlist", method = RequestMethod.POST)
  protected ModelAndView onSubmit(@ModelAttribute @Valid Playlist playlist, BindingResult errors) {
    if (!errors.hasErrors()) {
      try {
        playlistService.save(playlist);
      } catch (Exception e) {
        LOGGER.error(e);
        errors.reject("error.playlist", e.getMessage());
      }
    }
    ModelAndView modelAndView;
    if (errors.hasErrors()) {
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    } else {
      modelAndView = new ModelAndView(getSuccessView(), "id", playlist.getId());
    }
    return modelAndView;
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}