package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.model.user.User;
import com.ssj.model.artist.Artist;
import com.ssj.service.user.UserService;
import com.ssj.service.artist.ArtistService;

@Controller
public class DeleteArtistInfoController {
  private UserService userService;
  private ArtistService artistService;

  @RequestMapping("/user/delete_artist_info")
  protected ModelAndView handleRequestInternal(@RequestParam("itemId") String artistInfoId,
                                               @RequestParam("id") int artistId) {
    Map<String, Object> model = new HashMap<String, Object>();

    if (artistInfoId != null) {
      Artist artist = artistService.getArtist(artistId);
      if (artist != null) {
        User user = SecurityUtil.getUser();
        if (artistService.canEditArtist(artist, user)) {
          int itemId = Integer.parseInt(artistInfoId.substring(2));
          if (artistInfoId.startsWith("ou")) {
            // delete other user by id
            User otherUser = userService.getUser(itemId);
            if (otherUser != null) {
              artistService.deleteOtherUser(artist, otherUser);
            } else {
              model.put("error", "Could not find user with id " + itemId);
            }
          } else if (artistInfoId.startsWith("ra")) {
            // delete related artist by id
            Artist relatedArtist = artistService.getArtist(itemId);
            if (relatedArtist != null) {
              artistService.deleteRelatedArtist(artist, relatedArtist);
            } else {
              model.put("error", "Could not find artist with id " + itemId);
            }
          } else {
            model.put("error", "Invalid artist info id " + itemId);
          }
        } else {
          model.put("error", "You do not have permission to modify this artist");
        }
      } else {
        model.put("error", "Could not find artist with id " + artistId);
      }
    } else {
      model.put("error", "Invalid artist id " + artistId);
    }
    if (model.isEmpty()) {
      model.put("success", "true");
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