package com.ssj.web.spring.controllers;

import com.ssj.web.spring.exception.NotFoundException;
import com.ssj.web.util.PathsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.song.SongComment;
import com.ssj.service.song.SongCommentService;
import com.ssj.service.user.UserException;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

@Controller
public class AdminEditSongCommentController {

  private String formView = "admin/edit_song_comment";

  private SongCommentService songCommentService;

  @ModelAttribute
  protected SongComment populateSongComment(@RequestParam("id") int songCommentId) {
    SongComment songComment = songCommentService.getSongComment(songCommentId);
    if (songComment == null) {
      throw new NotFoundException("Could not find comment with id " + songCommentId);
    }
    return songComment;
  }

  @RequestMapping(value = "/admin/edit_song_comment", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/admin/edit_song_comment", method = RequestMethod.POST)
  protected ModelAndView onSubmit(SongComment comment, BindingResult errors) throws Exception {
    // check if user is allowed to save changes to this comment comment?

    Map modelMap = new HashMap();
//    modelMap.put("comment", comment.getOriginalComment().getId());
//    modelMap.put("id", comment.getOriginalComment().getSong().getId());
    ModelAndView modelAndView;
    try {
      comment.setChangeDate(new Date());

      songCommentService.saveComment(comment);

      modelMap.put("comment", comment.getId());

      String songRedirect = "redirect:" + PathsUtil.makePath(comment.getSong());
      modelAndView = new ModelAndView(songRedirect, modelMap);
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
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  public String getFormView() {
    return formView;
  }
}
