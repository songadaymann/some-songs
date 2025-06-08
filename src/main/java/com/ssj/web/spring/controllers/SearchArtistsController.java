package com.ssj.web.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.model.artist.search.ArtistSearch;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SearchArtistsController {

  private String formView = "artists";

  @RequestMapping(value = "/artist_search", params = "!search")
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/artist_search", params = "search=true")
  public ModelAndView onSubmit(WebRequest request, ArtistSearch artistSearch) {

//    artistService.findArtists(search);

    request.setAttribute("artistSearch", artistSearch, WebRequest.SCOPE_SESSION);

    return new ModelAndView("redirect:/" + getFormView());
  }

  public String getFormView() {
    return formView;
  }
}