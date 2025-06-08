package com.ssj.web.spring.controllers;

import com.ssj.service.artist.ArtistService;
import com.ssj.model.user.User;
import com.ssj.model.artist.ArtistLink;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DeleteArtistLinkController {
  private ArtistService artistService;

  @RequestMapping("/user/delete_artist_link")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int artistLinkId) {
    Map<String, Object> model = new HashMap<String, Object>();

    User user = SecurityUtil.getUser();
    if (user != null) {
      ArtistLink artistLink = artistService.getArtistLink(artistLinkId);
      if (artistLink != null) {
        if (artistService.canEditArtist(artistLink.getArtist(), user)) {
          artistService.deleteArtistLink(artistLink);
          model.put("success", "true");
        } else {
          // error
          model.put("error", "You do not have permission to delete links from this artist");
        }
      } else {
        // error
        model.put("error", "Can not find artist link with id " + artistLinkId);
      }
    } else {
      // error
      model.put("error", "You must be logged in to delete an artist link");
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }
}
