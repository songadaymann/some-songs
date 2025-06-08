package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import com.ssj.model.song.SongCommentReply;
import com.ssj.model.song.SongComment;
import com.ssj.service.song.SongCommentService;

@Controller
public class MultiquoteSongController {
  private SongCommentService songCommentService;

  @RequestMapping("/multiquote_song")
  protected ModelAndView handleRequestInternal(WebRequest request,
                                               @RequestParam(value = "commentId", required = false) Integer commentId,
                                               @RequestParam(value = "replyId", required = false) Integer replyId) {
    Map<String, String> model = new HashMap<String, String>();

    if (commentId == null) {
      if (replyId == null) {
        model.put("error", "Unable to find comment or reply id");
      } else {
        SongCommentReply reply = songCommentService.getSongCommentReply(replyId);
        if (reply == null) {
          model.put("error", "Unable to find reply with id " + replyId);
        } else {
          int originalCommentId = reply.getOriginalComment().getId();

          Integer multiquoteCommentId = (Integer) request.getAttribute("mqSongCommentId", WebRequest.SCOPE_SESSION);
          Set multiquoteReplyIds = (Set) request.getAttribute("mqSongCommentReplyIds", WebRequest.SCOPE_SESSION);
          Boolean quoteOriginalComment = (Boolean) request.getAttribute("mqSongQuoteOriginal", WebRequest.SCOPE_SESSION);

          if (multiquoteCommentId == null || !multiquoteCommentId.equals(originalCommentId)) {
            multiquoteCommentId = originalCommentId;
            multiquoteReplyIds = null;
            quoteOriginalComment = null;
          }
          if (multiquoteReplyIds == null) {
            multiquoteReplyIds = new HashSet();
          }
          if (multiquoteReplyIds.contains(replyId)) {
            multiquoteReplyIds.remove(replyId);
          } else {
            multiquoteReplyIds.add(replyId);
          }

          request.setAttribute("mqSongCommentId", multiquoteCommentId, WebRequest.SCOPE_SESSION);
          request.setAttribute("mqSongCommentReplyIds", multiquoteReplyIds, WebRequest.SCOPE_SESSION);
          request.setAttribute("mqSongQuoteOriginal", quoteOriginalComment, WebRequest.SCOPE_SESSION);

          model.put("success", "true");
        }
      }
    } else {
      SongComment comment = songCommentService.getSongComment(commentId);
      if (comment == null) {
        model.put("error", "Unable to find comment with id " + commentId);
      } else {

        Integer multiquoteCommentId = (Integer) request.getAttribute("mqSongCommentId", WebRequest.SCOPE_SESSION);
        Boolean quoteOriginalComment = (Boolean) request.getAttribute("mqSongQuoteOriginal", WebRequest.SCOPE_SESSION);

        if (multiquoteCommentId == null || !multiquoteCommentId.equals(comment.getId())) {
          multiquoteCommentId = comment.getId();
          quoteOriginalComment = true;
        } else {
          quoteOriginalComment = (quoteOriginalComment == null ? true : null);
        }

        request.setAttribute("mqSongCommentId", multiquoteCommentId, WebRequest.SCOPE_SESSION);
        request.setAttribute("mqSongQuoteOriginal", quoteOriginalComment, WebRequest.SCOPE_SESSION);

        model.put("success", "true");
      }
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }
}