package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistRating;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;
import com.ssj.model.user.User;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.service.playlist.PlaylistException;
import com.ssj.service.playlist.PlaylistRatingService;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.service.user.UserService;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class PlaylistController {

  private String viewName = "playlist";

  private UserService userService;
  private PlaylistService playlistService;
  private PlaylistRatingService playlistRatingService;
  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/playlist")
  protected ModelAndView handleRequestInternal(WebRequest request,
                                               @RequestParam("id") int playlistId, 
                                               @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
                                               @RequestParam(value = "comment", required = false) Integer playlistCommentId) {

    Playlist playlist;

    if (playlistId > 0) {
      playlist = playlistService.getPlaylist(playlistId);
      if (playlist == null) {
        throw new PlaylistException("Unable to find playlist with id " + playlistId);
      }
    } else {
      throw new PlaylistException("Invalid playlist id");
    }

    ModelAndView modelAndView = new ModelAndView(getViewName(), "playlist", playlist);

    if (start != null) {
      modelAndView.addObject("start", start);
    }

    try {
      User user = SecurityUtil.getUser();

      if (playlistCommentId != null) {
        // showing a comment and some page of its replies
        PlaylistCommentReplySearch replySearch = new PlaylistCommentReplySearch();
        // show oldest reply first
        replySearch.setDescending(false);
        replySearch.setAlwaysShowFullPage(false);
        if (user != null) {
          replySearch.setNotByIgnoredUsers(user.getId());
        }

        PlaylistComment originalComment = playlistCommentService.getPlaylistComment(playlistCommentId);

        Integer multiquoteCommentId = (Integer) request.getAttribute("mqPlaylistCommentId", WebRequest.SCOPE_SESSION);
        if (multiquoteCommentId != null && !multiquoteCommentId.equals(originalComment.getId())) {
          request.removeAttribute("mqPlaylistCommentId", WebRequest.SCOPE_SESSION);
          request.removeAttribute("mqPlaylistCommentReplyIds", WebRequest.SCOPE_SESSION);
          request.removeAttribute("mqPlaylistQuoteOriginal", WebRequest.SCOPE_SESSION);
        }

        modelAndView.addObject("originalComment", originalComment);

        replySearch.setOriginalComment(originalComment);

        // turn 1-based start into 0-based row num?
        start--;
        replySearch.setStartResultNum(start);

/*
        search = replySearch;
        searchObjectName = "replies";
*/
        modelAndView.addObject("replySearch", replySearch);
        List<PlaylistCommentReply> replies = playlistCommentService.findReplies(replySearch);
        modelAndView.addObject("replies", replies);
/*
      } else {
        // showing a page of comments
        SongCommentSearch commentSearch = new SongCommentSearch();
        commentSearch.setSong(song);

        int start = NumberUtils.toInt(request.getParameter("start"), 1);
        // turn 1-based start into 0-based row num
        start--;
        commentSearch.setStartResultNum(start);

        songCommentService.findComments(commentSearch);

        search = commentSearch;
        searchObjectName = "comments";
*/
      }

      if (user != null) {
        if (!playlist.isPubliclyAvailable() && (playlist.getUser().getId() != user.getId() && !user.isAdmin())) {
          throw new PlaylistException("Unable to find playlist with id " + playlistId);
        }

        // used in comments, etc.
        modelAndView.addObject("user", user);

        boolean canEdit = playlistService.canEditPlaylist(playlist, user);
        modelAndView.addObject("canEdit", canEdit);

        PlaylistRating rating = playlistRatingService.getRating(user, playlist);

        if (rating != null) {
          modelAndView.addObject("rating", rating);
        }

        PlaylistComment comment = playlistCommentService.getComment(user, playlist);

        if (comment != null) {
          modelAndView.addObject("myComment", comment);
        }

        boolean isFavorite = userService.isFavoritePlaylist(user, playlist);
        modelAndView.addObject("isFavorite", isFavorite);
      }
    } catch (Exception e) {
      throw new PlaylistException(e);
    }

    return modelAndView;
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setPlaylistRatingService(PlaylistRatingService playlistRatingService) {
    this.playlistRatingService = playlistRatingService;
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}
