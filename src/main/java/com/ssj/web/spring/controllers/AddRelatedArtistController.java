package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
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
public class AddRelatedArtistController {
  private ArtistService artistService;

  @RequestMapping("/user/add_related_artist")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int artistId,
                                               @RequestParam("artistId") int relatedArtistId) {
    Map model = new HashMap();

    Artist artist = artistService.getArtist(artistId);
    if (artist != null) {
      User securityUser = SecurityUtil.getUser();
      if (artistService.canEditArtist(artist, securityUser)) {
        if (relatedArtistId != artistId) {
          Artist relatedArtist = artistService.getArtist(relatedArtistId);
          if (relatedArtist != null) {
            try {
              artistService.addRelatedArtist(artist, relatedArtist);
              model.put("success", true);
            } catch (Exception e) {
              e.printStackTrace();
              model.put("error", e.getMessage());
            }
          } else {
            model.put("error", "No artist with id " + relatedArtistId);
          }
        } else {
          model.put("error", "An artist cannot be related to itself");
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

}