package com.ssj.web.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.song.search.SongCommentReplySearchFactory;
import com.ssj.model.song.search.SongCommentSearchFactory;
import com.ssj.search.SearchBase;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SongCommentsSearchController {

  private String formView = "comments";

  @RequestMapping(value = "/comment_search", params = "!search")
  public String getHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/comment_search", params = "search=true")
  public ModelAndView onSubmit(WebRequest request,
                               @ModelAttribute("commentSearch") SongCommentSearch commentSearch,
                               @RequestParam(value = "searchType", required = false) String searchType,
                               @RequestParam(value = "start", required = false, defaultValue = "1") int start,
                               @RequestParam(value = "resultsPerPage", required = false) Integer resultsPerPage) {

    SearchBase search = commentSearch;
    boolean isReplySearch = "replies".equals(searchType);

    if (commentSearch.getId() < 0) {
      if (isReplySearch) {
        search = SongCommentReplySearchFactory.getSearch(commentSearch.getId());
      } else {
        search = SongCommentSearchFactory.getSearch(commentSearch.getId());
      }
    }

    if (isReplySearch) {
      SongCommentReplySearch replySearch = new SongCommentReplySearch();

      replySearch.setCommentText(commentSearch.getCommentText());
      replySearch.setUserDisplayName(commentSearch.getUserDisplayName());

//      songCommentService.findReplies(replySearch);

      search = replySearch;

      request.setAttribute("isReplySearch", "true", WebRequest.SCOPE_SESSION);
    } else {
//      songCommentService.findComments(commentSearch);

      request.removeAttribute("isReplySearch", WebRequest.SCOPE_SESSION);
    }

    // convert 1-based start num to 0-based
    search.setStartResultNum(start - 1);
    if (resultsPerPage != null) {
      search.setResultsPerPage(resultsPerPage);
    }

    request.setAttribute("searchType", searchType, WebRequest.SCOPE_SESSION);
    request.setAttribute("commentSearch", search, WebRequest.SCOPE_SESSION);

    return new ModelAndView("redirect:/" + getFormView());

  }

  public String getFormView() {
    return formView;
  }
}
