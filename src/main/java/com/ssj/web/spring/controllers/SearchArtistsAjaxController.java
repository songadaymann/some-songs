package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.ssj.service.artist.ArtistService;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.artist.Artist;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SearchArtistsAjaxController {

  private String viewName = "include/autocomplete_results";

  private ArtistService artistService;

  @RequestMapping("/user/search_artists")
  protected ModelAndView handleRequestInternal(@RequestParam("name") String name) {

    ModelAndView modelAndView = new ModelAndView(getViewName());

    ArtistSearch search = new ArtistSearch();
    search.setName(name);
    List artists = artistService.findArtists(search);

    Map results = new HashMap();

    if (artists != null) {
      for (Object artistObj : artists) {
        Artist artist = (Artist) artistObj;

        results.put(artist.getId(), artist.getName());
      }
    }

    modelAndView.addObject("results", results);

    modelAndView.addObject("titleText", "Relate artist to artist ");

    return modelAndView;
  }

  @Autowired
  public void setArtistService(ArtistService userService) {
    this.artistService = userService;
  }

  public String getViewName() {
    return viewName;
  }
}