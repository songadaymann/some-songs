package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.model.user.User;
import com.ssj.service.playlist.PlaylistException;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class PlaylistCommentsIncludeController {

  private String viewName = "include/playlist_comment_list";

  private PlaylistService playlistService;
  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/include/playlist_comments")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int playlistId,
                                               @RequestParam(value = "refresh", required = false) Boolean refresh,
                                               @RequestParam(value = "start", required = false) Integer start) {

    Playlist playlist = playlistService.getPlaylist(playlistId);
    if (playlist == null) {
      throw new PlaylistException("Unable to find playlist with id " + playlistId);
    }

    // showing a page of comments
    PlaylistCommentSearch commentSearch = new PlaylistCommentSearch();
    commentSearch.setDescending(false);
    commentSearch.setPlaylist(playlist);

    User user = SecurityUtil.getUser();
    if (user != null) {
      commentSearch.setNotByIgnoredUsers(user.getId());
    }

    ModelAndView modelAndView = new ModelAndView(getViewName(), "commentSearch", commentSearch);

    if (refresh != null && refresh) {
      // make sure to show the user their comment, since they just edited it (the "start" param used later won't be set in this case)
      PlaylistComment comment = playlistCommentService.getComment(user, playlist);

      if (comment != null) {
        modelAndView.addObject("myComment", comment);

        int commentStartNum = playlistCommentService.getCommentPosition(comment);
        int pageStartNum = (commentStartNum / commentSearch.getResultsPerPage()) * commentSearch.getResultsPerPage();
        commentSearch.setStartResultNum(pageStartNum);
      } // else comment was just deleted
    } else {
      // default to Integer.MAX_VALUE to select last page of comments if no start param is present
      if (start == null) {
        // default to Integer.MAX_VALUE to select last page of comments if no start param is present
        start = Integer.MAX_VALUE;
      }
      // turn 1-based start into 0-based row num
      start--;
      commentSearch.setStartResultNum(start);
    }

    List<PlaylistComment> comments = playlistCommentService.findComments(commentSearch);

    modelAndView.addObject("commentSearchResults", comments);
    modelAndView.addObject("playlist", playlist);
    return modelAndView;
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}
