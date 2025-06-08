package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.user.User;
import com.ssj.service.song.SongCommentService;
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
public class SongCommentsListController {

  private static final Logger LOGGER = Logger.getLogger(SongCommentsListController.class);

  private String viewName = "include/comment_search_list";

  private SongCommentService songCommentService;

  @RequestMapping("/comment_list")
  protected ModelAndView commentListHandler(@RequestParam("commentSearchId") Integer searchId,
                                            @RequestParam(value = "searchType", required = false) String searchType) {
    boolean showReplies = "replies".equals(searchType);

    SearchBase search = null;
    if (searchId != null) {
      if (showReplies) {
        search = songCommentService.getSongCommentReplySearch(searchId);
      } else {
        search = songCommentService.getSongCommentSearch(searchId);
      }
    }
    if (search == null) {
      if (showReplies) {
        search = songCommentService.getDefaultCommentReplySearch();
      } else {
        search = songCommentService.getDefaultCommentSearch();
      }
    }
    User user = SecurityUtil.getUser();
    List searchResults = null;
    try {
      if (showReplies) {
        SongCommentReplySearch songCommentReplySearch = (SongCommentReplySearch) search;
        if (user != null) {
          songCommentReplySearch.setNotByIgnoredUsers(user.getId());
        }
        searchResults = songCommentService.findReplies(songCommentReplySearch);
      } else {
        SongCommentSearch songCommentSearch = (SongCommentSearch) search;
        if (user != null) {
          songCommentSearch.setNotByIgnoredUsers(user.getId());
        }
        searchResults = songCommentService.findComments(songCommentSearch);
      }
      LOGGER.debug("total song comments/replies search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModelMap model = new ModelMap();
    model.addAttribute("commentSearch", search);
    model.addAttribute("isReplySearch", showReplies);
    model.addAttribute("commentSearchResults", searchResults);
//    model.addAttribute("songListHidePaging", request.getParameter("songListHidePaging"));
    model.addAttribute("isInclude", "true");

    return new ModelAndView(getViewName(), model);//"songSearch", search);
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}