package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.service.user.UserException;
import com.ssj.model.playlist.PlaylistComment;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AdminEditPlaylistCommentController {

  private String formView = "admin/edit_playlist_comment";
  private String successView = "redirect:/playlist#comments";

  private PlaylistCommentService playlistCommentService;

  @ModelAttribute
  protected PlaylistComment formBackingObject(@RequestParam("id") int playlistCommentId) {
    return playlistCommentService.getPlaylistComment(playlistCommentId);
  }

  @RequestMapping(value = "/admin/edit_playlist_comment", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/admin/edit_playlist_comment", method = RequestMethod.POST)
  protected ModelAndView onSubmit(@ModelAttribute PlaylistComment comment, BindingResult errors) {
    // check if user is allowed to save changes to this comment comment?

    Map modelMap = new HashMap();
//    modelMap.put("comment", comment.getOriginalComment().getId());
//    modelMap.put("id", comment.getOriginalComment().getPlaylist().getId());
    ModelAndView modelAndView;
    try {
      comment.setChangeDate(new Date());

      playlistCommentService.saveComment(comment);

      modelMap.put("id", comment.getPlaylist().getId());
      modelMap.put("comment", comment.getId());

      modelAndView = new ModelAndView(getSuccessView(), modelMap);
    } catch (UserException e) {
      e.printStackTrace();
      errors.reject("error.thread", e.getMessage());
      Map errorsModel = errors.getModel();
      errorsModel.putAll(modelMap);
      modelAndView = new ModelAndView(getFormView(), errorsModel);
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
