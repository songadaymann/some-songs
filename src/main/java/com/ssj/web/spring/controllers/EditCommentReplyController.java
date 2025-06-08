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
import com.ssj.service.user.UserService;
import com.ssj.service.user.UserException;
import com.ssj.service.song.SongCommentService;
import com.ssj.service.song.SongException;
import com.ssj.model.song.SongCommentReply;
import com.ssj.model.song.SongComment;
import com.ssj.model.user.User;
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
public class EditCommentReplyController {

  private static final Logger LOGGER = Logger.getLogger(EditCommentReplyController.class);

  private String formView = "user/comment_reply";
  private String successView = "redirect:/view_comment_reply";

  private SongCommentService songCommentService;

  @ModelAttribute
  protected void populateModel(WebRequest request,
                               Model model,
                               @RequestParam(value = "id", required = false) Integer replyId,
                               @RequestParam(value = "comment", required = false) Integer commentId,
                               @RequestParam(value = "quoteCommentId", required = false) Integer quoteCommentId,
                               @RequestParam(value = "quoteId", required = false) Integer quoteReplyId) {

    SongCommentReply reply = null;
    if (replyId != null) {
      reply = songCommentService.getSongCommentReply(replyId);
    } else if (commentId != null) {
      SongComment comment = songCommentService.getSongComment(commentId);
      if (comment != null) {
        reply = new SongCommentReply();
        reply.setOriginalComment(comment);
      }
    }
    if (reply == null) {
      throw new SongException("Could not find comment or reply");
    } else if (reply.getId() != 0) {
      User user = SecurityUtil.getUser();
      if (reply.getUser().getId() != user.getId() && !user.isAdmin()) {
        throw new SongException("You cannot edit replies made by other users");
      }
    }
    reply.mergeContent();

    model.addAttribute("reply", reply);

    if (reply.getId() == 0) {
      // adding a comment reply, handle quoting (don't quote when editing)
      SongComment quoteComment = null;
      Set<SongCommentReply> quoteReplies = new HashSet<SongCommentReply>();

      if (quoteCommentId != null) {
        quoteComment = songCommentService.getSongComment(quoteCommentId);
        if (quoteComment != null) {
          quoteComment.mergeContent();
        }
      } else if (quoteReplyId != null) {
        SongCommentReply quoteReply = songCommentService.getSongCommentReply(quoteReplyId);
        if (quoteReply != null) {
          quoteReply.mergeContent();
          quoteReplies.add(quoteReply);
        }
      }

      Integer multiquoteCommentId = (Integer) request.getAttribute("mqSongCommentId", WebRequest.SCOPE_SESSION);
      Set multiquoteReplyIds = (Set) request.getAttribute("mqSongCommentReplyIds", WebRequest.SCOPE_SESSION);
      Boolean quoteOriginalComment = (Boolean) request.getAttribute("mqSongQuoteOriginal", WebRequest.SCOPE_SESSION);

      if (multiquoteCommentId != null && multiquoteCommentId.equals(reply.getOriginalComment().getId())) {
        if (multiquoteReplyIds != null) {
          List multiquoteReplies = songCommentService.getSongCommentReplies(multiquoteReplyIds);
          for (Object multiquoteReplyObj : multiquoteReplies) {
            SongCommentReply multiquoteReply = (SongCommentReply) multiquoteReplyObj;
            multiquoteReply.mergeContent();
            quoteReplies.add(multiquoteReply);
          }
        }
        if (quoteOriginalComment != null && quoteComment == null) {
          quoteComment = songCommentService.getSongComment(multiquoteCommentId);
          if (quoteComment != null) {
            quoteComment.mergeContent();
          }
        }
      }

      model.addAttribute("quoteComment", quoteComment);
      model.addAttribute("quoteReplies", quoteReplies);
    }

  }

  @RequestMapping(value = "/user/comment_reply", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/comment_reply", method = RequestMethod.POST)
  public ModelAndView onSubmit(WebRequest request, @ModelAttribute("reply") @Valid SongCommentReply reply, BindingResult errors) {
    // check if user is allowed to save changes to this comment reply?
    
    Map modelMap = new HashMap();
//    modelMap.put("comment", reply.getOriginalComment().getId());
//    modelMap.put("id", reply.getOriginalComment().getSong().getId());
    if (!errors.hasErrors()) {
      try {
        if (reply.getUser() == null) {
          User user = SecurityUtil.getUser();
          reply.setUser(user);
        }

        songCommentService.saveReply(reply);

        request.removeAttribute("mqSongCommentId", WebRequest.SCOPE_SESSION);
        request.removeAttribute("mqSongCommentReplyIds", WebRequest.SCOPE_SESSION);
        request.removeAttribute("mqSongQuoteOriginal", WebRequest.SCOPE_SESSION);

        modelMap.put("id", reply.getId());

      } catch (UserException e) {
        LOGGER.error(e);
        errors.reject("error.thread", e.getMessage());
        Map errorsModel = errors.getModel();
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
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  public String getSuccessView() {
    return successView;
  }

  public String getFormView() {
    return formView;
  }
}
