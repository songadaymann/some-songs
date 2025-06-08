package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.user.UserService;
import com.ssj.service.artist.ArtistService;
import com.ssj.model.user.User;
import com.ssj.model.artist.Artist;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class FavoriteArtistController {

  private UserService userService;
  private ArtistService artistService;

  @RequestMapping("/user/favorite_artist")
  protected ModelAndView handleRequestInternal(@RequestParam("artistId") int artistId) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      User user = SecurityUtil.getUser();

      if (user != null) {
        Artist artist = artistService.getArtist(artistId);

        if (artist != null) {
          userService.toggleFavoriteArtist(user, artist);
          model.put("success", "true");
        } else {
          model.put("error", "Unable to find artist with id " + artistId);
        }
      } else {
        model.put("error", "You must be logged in to perform this action");
      }
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }
}