package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import com.ssj.service.song.SongCommentService;
import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.user.User;
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
public class SongCommentsController {

  private static final Logger LOGGER = Logger.getLogger(SongCommentsController.class);

  private String viewName = "comments";

  private SongCommentService songCommentService;

  @RequestMapping("/comments")
  protected ModelAndView handleRequestInternal(WebRequest request,
                                               @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                               @RequestParam(value = "resultsPerPage", required = false, defaultValue = "0") int resultsPerPage) {

    boolean isReplySearch = "replies".equals(request.getAttribute("searchType", WebRequest.SCOPE_SESSION));
    SearchBase search = (SearchBase) request.getAttribute("commentSearch", WebRequest.SCOPE_SESSION);
    if (search == null) {
      search = new SongCommentSearch();
    }

    if (start > 0) {
      // turn 1-based start into 0-based row num
      start--;
      search.setStartResultNum(start);
    }

    if (resultsPerPage > 0) {
      search.setResultsPerPage(resultsPerPage);
      // put user back on first page
//      search.setStartResultNum(0);
    }

    // handle sorting also?

    User user = SecurityUtil.getUser();

    List searchResults = null;
    try {
      if (isReplySearch) {
        try {
          SongCommentReplySearch replySearch = (SongCommentReplySearch) search;
          if (user != null) {
            replySearch.setNotByIgnoredUsers(user.getId());
          }

          searchResults = songCommentService.findReplies(replySearch);

          search = replySearch;

          request.setAttribute("isReplySearch", "true", WebRequest.SCOPE_SESSION);
        } catch (ClassCastException e) {
          e.printStackTrace();
        }
      } else {
        SongCommentSearch commentSearch = (SongCommentSearch) search;
        if (user != null) {
          commentSearch.setNotByIgnoredUsers(user.getId());
        }

        searchResults = songCommentService.findComments(commentSearch);

        request.removeAttribute("isReplySearch", WebRequest.SCOPE_SESSION);
      }
      LOGGER.debug("total song comment/reply search results = ");
      LOGGER.debug(search.getTotalResults());
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModelMap model = new ModelMap();
    model.addAttribute("commentSearch", search);
    model.addAttribute("commentSearchResults", searchResults);

//    boolean foundSongs = search.getTotalResults() > 0;
    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}
