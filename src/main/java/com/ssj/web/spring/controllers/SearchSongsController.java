package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.ssj.model.song.search.SongSearch;
import com.ssj.service.song.SongSearchFactory;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.service.song.SongSearchService;

import javax.validation.Valid;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SearchSongsController {

  private String formViewName = "search";
  private String successView = "redirect:/songs";

  private SongSearchService songSearchService;
  private SongSearchFactory songSearchFactory;

  @RequestMapping(value = "/search")
  public String formGetHandler() {
    return getFormViewName();
  }

  @RequestMapping(value = "/search", params = "search=true")
  public String formPostHandler(@Valid SongSearch search, BindingResult errors,
                                      @RequestParam(value = "start", required = false, defaultValue = "1") Integer start,
                                      WebRequest webRequest) {

    String viewName;

    if (!errors.hasErrors()) {
      if (search.getId() < 0) {
        // running a "system" search
        search = songSearchFactory.getSearch(search.getId());
        if (search != null) {
          User user = SecurityUtil.getUser();
          if (user != null) {
            if (SongSearchFactory.SEARCH_ID_NOT_YET_RATED_BY_USER == search.getId() ||
                SongSearchFactory.SEARCH_ID_LOST_SONGS == search.getId()) {
              search.setNotRatedByUser(user.getId());
            } else if (SongSearchFactory.SEARCH_ID_BY_FAVORITE_ARTISTS == search.getId()) {
              search.setInUsersFavoriteArtists(user.getId());
            } else if (SongSearchFactory.SEARCH_ID_PREFERRED_USERS_PICKS == search.getId()) {
              search.setInPreferredUsersFavorites(user.getId());
            } else if (SongSearchFactory.SEARCH_ID_FAVORITE_SONGS == search.getId()) {
              search.setInUsersFavorites(user.getId());
            }
          }
        }
      } else if (search.getId() > 0) {
        // trying to run a user's saved search
        search = songSearchService.getSongSearch(search.getId());
        if (search != null) {
          User user = SecurityUtil.getUser();
          if (user != null) {
            if (search.getUser().getId() != user.getId()) {
              // search doesn't belong to this user, don't let them run it
              search = null;
            }
          }
        }
      }

      if (search == null) {
        search = new SongSearch();
      }

      // convert from 1-based to 0-based
      start--;
      search.setStartResultNum(start);

      webRequest.setAttribute("songSearch", search, WebRequest.SCOPE_SESSION);

      viewName = getSuccessView();
    } else {
      viewName = getFormViewName();
    }
    return viewName;
  }

  @Autowired
  public void setSongSearchService(SongSearchService songSearchService) {
    this.songSearchService = songSearchService;
  }

  @Autowired
  public void setSongSearchFactory(SongSearchFactory songSearchFactory) {
    this.songSearchFactory = songSearchFactory;
  }

  public String getFormViewName() {
    return formViewName;
  }

  public void setFormViewName(String formViewName) {
    this.formViewName = formViewName;
  }

  public String getSuccessView() {
    return successView;
  }

  public void setSuccessView(String successView) {
    this.successView = successView;
  }
}
