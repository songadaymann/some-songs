package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ViewPlaylistCommentReplyController {

  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/view_playlist_comment_reply")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "id", required = false, defaultValue = "1") int requestReplyId) {
    int playlistId = 1;
    int commentId = 1;
    Integer start = null;
    Integer replyId = null;

    PlaylistCommentReply reply = playlistCommentService.getPlaylistCommentReply(requestReplyId);

    if (reply != null) {
      commentId = reply.getOriginalComment().getId();
      playlistId = reply.getOriginalComment().getPlaylist().getId();

      int replyStartNum = playlistCommentService.getReplyPosition(reply);

      PlaylistCommentReplySearch search = new PlaylistCommentReplySearch();
      // divide post start by results per page to get page num, multiply by results per page to get page start num
      int pageStartNum = (replyStartNum / search.getResultsPerPage()) * search.getResultsPerPage();

      if (pageStartNum > 0) {
        start = pageStartNum;
      }

      if (replyStartNum != pageStartNum) {
        replyId = reply.getId();
      }
    }

    String url = "/playlist?id=" + playlistId + "&comment=" + commentId;
    if (start != null) {
      // convert 0-based start num to 1-based for URL
      url += "&start=" + (start + 1);
    }
    if (replyId != null) {
      url += "#reply" + replyId;
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