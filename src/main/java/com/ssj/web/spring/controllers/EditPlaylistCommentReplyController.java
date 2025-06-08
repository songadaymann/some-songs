package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.user.UserException;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.service.playlist.PlaylistException;
import com.ssj.model.user.User;
import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.*;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class EditPlaylistCommentReplyController {

  private static final Logger LOGGER = Logger.getLogger(EditPlaylistCommentReplyController.class);

  private String formView = "user/playlist_comment_reply";
  private String successView = "redirect:/view_playlist_comment_reply";

  private PlaylistCommentService playlistCommentService;

  @ModelAttribute
  protected void populateModel(WebRequest request,
                               Model model,
                               @RequestParam(value = "id", required = false) Integer replyId,
                               @RequestParam(value = "comment", required = false) Integer commentId,
                               @RequestParam(value = "quoteCommentId", required = false) Integer quoteCommentId,
                               @RequestParam(value = "quoteId", required = false) Integer quoteReplyId) {
    PlaylistCommentReply reply = null;
    if (replyId != null) {
      reply = playlistCommentService.getPlaylistCommentReply(replyId);
    } else if (commentId != null) {
      PlaylistComment comment = playlistCommentService.getPlaylistComment(commentId);
      if (comment != null) {
        reply = new PlaylistCommentReply();
        reply.setOriginalComment(comment);
      }
    }
    if (reply == null) {
      throw new PlaylistException("Could not find comment or reply");
    } else if (reply.getId() != 0) {
      User user = SecurityUtil.getUser();
      if (reply.getUser().getId() != user.getId() && !user.isAdmin()) {
        throw new PlaylistException("You cannot edit replies made by other users");
      }
    }
    reply.mergeContent();

    model.addAttribute("reply", reply);

    if (reply.getId() == 0) {
      // adding a comment reply, handle quoting (don't quote when editing)
      PlaylistComment quoteComment = null;
      Set<PlaylistCommentReply> quoteReplies = new HashSet<PlaylistCommentReply>();

      if (quoteCommentId != null) {
        quoteComment = playlistCommentService.getPlaylistComment(quoteCommentId);
        if (quoteComment != null) {
          quoteComment.mergeContent();
        }
      } else if (quoteReplyId != null) {
        PlaylistCommentReply quoteReply = playlistCommentService.getPlaylistCommentReply(quoteReplyId);
        if (quoteReply != null) {
          quoteReply.mergeContent();
          quoteReplies.add(quoteReply);
        }
      }

      Integer multiquoteCommentId = (Integer) request.getAttribute("mqPlaylistCommentId", WebRequest.SCOPE_SESSION);
      Set multiquoteReplyIds = (Set) request.getAttribute("mqPlaylistCommentReplyIds", WebRequest.SCOPE_SESSION);
      Boolean quoteOriginalComment = (Boolean) request.getAttribute("mqPlaylistQuoteOriginal", WebRequest.SCOPE_SESSION);

      if (multiquoteCommentId != null && multiquoteCommentId.equals(reply.getOriginalComment().getId())) {
        if (multiquoteReplyIds != null) {
          List multiquoteReplies = playlistCommentService.getSongCommentReplies(multiquoteReplyIds);
          for (Object multiquoteReplyObj : multiquoteReplies) {
            PlaylistCommentReply multiquoteReply = (PlaylistCommentReply) multiquoteReplyObj;
            multiquoteReply.mergeContent();
            quoteReplies.add(multiquoteReply);
          }
        }
        if (quoteOriginalComment != null && quoteComment == null) {
          quoteComment = playlistCommentService.getPlaylistComment(multiquoteCommentId);
          if (quoteComment != null) {
            quoteComment.mergeContent();
          }
        }
      }

      model.addAttribute("quoteComment", quoteComment);
      model.addAttribute("quoteReplies", quoteReplies);
    }
  }

  @RequestMapping(value = "/user/playlist_comment_reply", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/playlist_comment_reply", method = RequestMethod.POST)
  public ModelAndView onSubmit(WebRequest request, @ModelAttribute("reply") @Valid PlaylistCommentReply reply, BindingResult errors) {

    // TODO check if user is allowed to save changes to this comment reply

    Map<String, Object> modelMap = new HashMap<String, Object>();
//    modelMap.put("comment", reply.getOriginalComment().getId());
//    modelMap.put("id", reply.getOriginalComment().getSong().getId());
    if (!errors.hasErrors()) {
      try {
        if (reply.getUser() == null) {
          User user = SecurityUtil.getUser();
          reply.setUser(user);
        }

        playlistCommentService.saveReply(reply);

        request.removeAttribute("mqPlaylistCommentId", WebRequest.SCOPE_SESSION);
        request.removeAttribute("mqPlaylistCommentReplyIds", WebRequest.SCOPE_SESSION);
        request.removeAttribute("mqPlaylistQuoteOriginal", WebRequest.SCOPE_SESSION);

        modelMap.put("id", reply.getId());

      } catch (UserException e) {
        LOGGER.error(e);
        errors.reject("error.thread", e.getMessage());
        Map<String, Object> errorsModel = errors.getModel();
        modelMap.putAll(errorsModel);
      }
    }
    ModelAndView modelAndView;
    if (errors.hasErrors()) {
      modelAndView = new ModelAndView(getFormView(), modelMap);
    } else {
      modelAndView = new ModelAndView(getSuccessView(), modelMap);
    }
    return modelAndView;
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}
