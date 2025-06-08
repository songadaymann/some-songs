package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.user.UserService;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.service.playlist.PlaylistException;
import com.ssj.model.user.User;
import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.web.spring.security.SecurityUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class DeletePlaylistCommentReplyController {

  private String viewName = "redirect:/playlist";

  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/user/delete_playlist_reply")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int replyId) {

    ModelAndView modelAndView = new ModelAndView(getViewName());

    try {
      User user = SecurityUtil.getUser();

      PlaylistCommentReply reply = playlistCommentService.getPlaylistCommentReply(replyId);


      if (reply == null) {
        throw new PlaylistException("Could not find reply");
      } else {
        if (reply.getUser().getId() != user.getId() && !user.isAdmin()) {
          throw new PlaylistException("You cannot delete replies that were made by other users");
        }
      }

      PlaylistComment originalComment = reply.getOriginalComment();
      modelAndView.addObject("id", originalComment.getPlaylist().getId());
      modelAndView.addObject("comment", originalComment.getId());

      playlistCommentService.deleteReply(reply);

      modelAndView.addObject("deleted", "true");
    } catch (Exception e) {
      modelAndView.addObject("error", e.getMessage());
    }

    return modelAndView;
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}