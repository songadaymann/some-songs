package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.user.UserService;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.Map;
import java.util.HashMap;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AddPlaylistController {

  private static final Logger LOGGER = Logger.getLogger(AddPlaylistController.class);

  private String formView = "user/add_playlist";

  private PlaylistService playlistService;

  @RequestMapping(value = "/user/add_playlist", method = RequestMethod.GET)
  public String getHandler(@ModelAttribute Playlist playlist) {
    return getFormView();
  }

  @RequestMapping(value = "/user/add_playlist", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute @Valid Playlist playlist, BindingResult errors) {
    ModelAndView modelAndView = new ModelAndView(getFormView());
    if (!errors.hasErrors()) {
      try {
        User user = SecurityUtil.getUser();
        if (user != null) {
          playlist.setUser(user);
          playlistService.save(playlist);
        }
        Map model = new HashMap();
        model.put("success", true);
        model.put("id", playlist.getId());
        modelAndView.setView(new JSONView());
        modelAndView.addObject("jsonModel", model);
      } catch (Exception e) {
        LOGGER.error(e);
        errors.reject("error.playlist", e.getMessage());
      }
    }
    if (errors.hasErrors()) {
      modelAndView.addAllObjects(errors.getModel());
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
}
