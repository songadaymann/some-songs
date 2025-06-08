package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.artist.ArtistService;
import com.ssj.model.user.User;
import com.ssj.model.artist.Artist;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DeleteArtistController {
  private ArtistService artistService;

  @RequestMapping("/user/delete_artist")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int artistId) {
    Map<String, Object> model = new HashMap<String, Object>();

    User user = SecurityUtil.getUser();
    if (user != null) {
      Artist artist = artistService.getArtist(artistId);
      if (artist != null) {
        if (artist.getUser().getId() == user.getId() || user.isAdmin()) {
          // delete stuff ... ? comment replies, comments, ratings, then song?
          artistService.deleteArtist(artist);
          model.put("success", "true");
        } else {
          // error
          model.put("error", "You can only delete artists that you created");
        }
      } else {
        // error
        model.put("error", "Can not find artist with id " + artistId);
      }
    } else {
      // error
      model.put("error", "You must be logged in to delete an artist");
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }
}