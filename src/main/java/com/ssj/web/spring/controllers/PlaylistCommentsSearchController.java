package com.ssj.web.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.model.playlist.search.PlaylistCommentSearchFactory;
import com.ssj.model.playlist.search.PlaylistCommentReplySearchFactory;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;
import com.ssj.search.SearchBase;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class PlaylistCommentsSearchController {

  private String formView = "playlist_comments";

  @RequestMapping(value = "/playlist_comment_search", params = "!search")
  public String getHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/playlist_comment_search", params = "search=true")
  public ModelAndView onSubmit(WebRequest request,
                               @ModelAttribute("commentSearch") PlaylistCommentSearch commentSearch,
                               @RequestParam(value = "searchType", required = false) String searchType,
                               @RequestParam(value = "start", required = false, defaultValue = "1") int start,
                               @RequestParam(value = "resultsPerPage", required = false) Integer resultsPerPage) {

    SearchBase search = commentSearch;
    boolean isReplySearch = "replies".equals(searchType);

    if (commentSearch.getId() < 0) {
      if (isReplySearch) {
        search = PlaylistCommentReplySearchFactory.getSearch(commentSearch.getId());
      } else {
        search = PlaylistCommentSearchFactory.getSearch(commentSearch.getId());
      }
    }

    if (isReplySearch) {
      PlaylistCommentReplySearch replySearch = new PlaylistCommentReplySearch();

      replySearch.setCommentText(commentSearch.getCommentText());
      replySearch.setUserDisplayName(commentSearch.getUserDisplayName());

//      songCommentService.findReplies(replySearch);

      search = replySearch;

      request.setAttribute("isPlaylistReplySearch", "true", WebRequest.SCOPE_SESSION);
    } else {
//      songCommentService.findComments(commentSearch);

      request.removeAttribute("isPlaylistReplySearch", WebRequest.SCOPE_SESSION);
    }

    search.setStartResultNum(start - 1);
    if (resultsPerPage != null) {
      search.setResultsPerPage(resultsPerPage);
    }

    request.setAttribute("playlistSearchType", searchType, WebRequest.SCOPE_SESSION);
    request.setAttribute("playlistCommentSearch", search, WebRequest.SCOPE_SESSION);

    return new ModelAndView("redirect:/" + getFormView());

  }

  public String getFormView() {
    return formView;
  }
}
