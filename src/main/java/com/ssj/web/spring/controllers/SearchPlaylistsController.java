package com.ssj.web.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.playlist.search.PlaylistSearchFactory;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.service.user.UserService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SearchPlaylistsController {

  private String formView = "search";
  private String successView = "redirect:/playlists";

//  private PlaylistSearchService playlistSearchService;

  @RequestMapping(value = "/playlist_search", params = "!search")
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/playlist_search", params = "search=true")
  public ModelAndView onSubmit(WebRequest request,
                               @ModelAttribute() PlaylistSearch search,
                               @RequestParam(value = "start", required = false, defaultValue = "1") int start) {

    if (search.getId() < 0) {
      // running a "system" search
      search = PlaylistSearchFactory.getSearch(search.getId());
      if (search != null) {
        User user = SecurityUtil.getUser();
        if (user != null) {
          if (PlaylistSearchFactory.SEARCH_ID_NOT_YET_RATED_BY_USER == search.getId() ||
              PlaylistSearchFactory.SEARCH_ID_LOST_PLAYLISTS == search.getId()) {
            search.setNotRatedByUser(user.getId());
          } else if (PlaylistSearchFactory.SEARCH_ID_BY_PREFERRED_USERS == search.getId()) {
            search.setInUsersPreferredUsers(user.getId());
          } else if (PlaylistSearchFactory.SEARCH_ID_PREFERRED_USERS_PICKS == search.getId()) {
            search.setInPreferredUsersFavorites(user.getId());
          }
        }
      }
/*
    } else if (search.getId() > 0) {
      // trying to run a user's saved search
      search = songSearchService.getSongSearch(search.getId());
      if (search != null) {
        User user = SecurityUtil.getUser(userService);
        if (user != null) {
          if (search.getUser().getId() != user.getId()) {
            // search doesn't belong to this user, don't let them run it
            search = null;
          }
        }
      }
*/
    }

    if (search == null) {
      search = new PlaylistSearch();
    }

    // convert from 1-based to 0-based
    start--;
    search.setStartResultNum(start);

    request.setAttribute("playlistSearch", search, WebRequest.SCOPE_SESSION);

    return new ModelAndView(getSuccessView());
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}