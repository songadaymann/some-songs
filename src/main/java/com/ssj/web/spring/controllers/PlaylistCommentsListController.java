package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.ssj.model.user.User;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;
import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.search.SearchBase;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class PlaylistCommentsListController {

  private static final Logger LOGGER = Logger.getLogger(PlaylistCommentsListController.class);

  private String viewName = "include/playlist_comment_search_list";

  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/playlist_comment_list")
  protected ModelAndView handleRequestInternal(@RequestParam("commentSearchId") Integer searchId,
                                            @RequestParam(value = "searchType", required = false) String searchType) {
    boolean showReplies = "replies".equals(searchType);

    SearchBase search = null;
    if (searchId != null) {
      if (showReplies) {
        search = playlistCommentService.getPlaylistCommentReplySearch(searchId);
      } else {
        search = playlistCommentService.getPlaylistCommentSearch(searchId);
      }
    }
    if (search == null) {
      if (showReplies) {
        search = playlistCommentService.getDefaultCommentReplySearch();
      } else {
        search = playlistCommentService.getDefaultCommentSearch();
      }
    }
    User user = SecurityUtil.getUser();
    List searchResults = null;
    try {
      if (showReplies) {
        PlaylistCommentReplySearch playlistCommentReplySearch = (PlaylistCommentReplySearch) search;
        if (user != null) {
          playlistCommentReplySearch.setNotByIgnoredUsers(user.getId());
        }
        searchResults = playlistCommentService.findReplies(playlistCommentReplySearch);
      } else {
        PlaylistCommentSearch playlistCommentSearch = (PlaylistCommentSearch) search;
        if (user != null) {
          playlistCommentSearch.setNotByIgnoredUsers(user.getId());
        }
        searchResults = playlistCommentService.findComments(playlistCommentSearch);
      }
      LOGGER.debug("total playlist comment/reply search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModelMap model = new ModelMap();
    model.addAttribute("commentSearch", search);
    model.addAttribute("isReplySearch", showReplies);
    model.addAttribute("commentSearchResults", searchResults);
    model.addAttribute("isInclude", "true");

    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}