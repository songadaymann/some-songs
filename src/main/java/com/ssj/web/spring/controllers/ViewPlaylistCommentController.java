package com.ssj.web.spring.controllers;

import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.service.playlist.PlaylistCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ViewPlaylistCommentController {

  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/view_playlist_comment")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "id", required = false, defaultValue = "1") int requestCommentId) {
    int playlistId = 1;
    Integer start = null;
    Integer commentId = null;

    PlaylistComment comment = playlistCommentService.getPlaylistComment(requestCommentId);

    if (comment != null) {
      playlistId = comment.getPlaylist().getId();

      int commentStartNum = playlistCommentService.getCommentPosition(comment);

      PlaylistCommentSearch search = new PlaylistCommentSearch();
      // divide post start by results per page to get page num, multiply by results per page to get page start num
      int pageStartNum = (commentStartNum / search.getResultsPerPage()) * search.getResultsPerPage();

      if (pageStartNum > 0) {
        start = pageStartNum;
      }

      if (commentStartNum != pageStartNum) {
        commentId = comment.getId();
      }
    }

    String url = "/playlist?id=" + playlistId;
    if (start != null) {
      // playlist comment indexes are 1-based
      url += "&start=" + (start + 1);
    }
    if (commentId != null) {
      url += "#comment" + commentId;
    }

    RedirectView view = new RedirectView(url);
    view.setContextRelative(true);

    return new ModelAndView(view);
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }
}