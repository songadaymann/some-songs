package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

import com.ssj.model.artist.Artist;
import com.ssj.model.user.User;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.user.UserService;
import com.ssj.web.spring.security.SecurityUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AddOtherUserController {

  private static final Logger LOGGER = Logger.getLogger(AddOtherUserController.class);

  private ArtistService artistService;
  private UserService userService;

  @RequestMapping("/user/add_other_user")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int artistId,
                                               @RequestParam("userId") int userId) {

    Map model = new HashMap();

    Artist artist = artistService.getArtist(artistId);
    if (artist != null) {
      User securityUser = SecurityUtil.getUser();
      if (artistService.canEditArtist(artist, securityUser)) {
        User user = userService.getUser(userId);
        if (user != null) {
          try {
            artistService.addOtherUser(artist, user);
            model.put("success", true);
          } catch (Exception e) {
            LOGGER.error(e);
            model.put("error", e.getMessage());
          }
        } else {
          model.put("error", "No user with id " + userId);
        }
      } else {
        model.put("error", "You do not have permission to modify this artist");
      }
    } else {
      model.put("error", "No artist with id " + artistId);
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
