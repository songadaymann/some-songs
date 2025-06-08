package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;

import com.ssj.service.playlist.PlaylistService;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class EditPlaylistItemNoteController {
  private PlaylistService playlistService;

  @RequestMapping("/user/edit_playlist_item_note")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int playlistItemId,
                                               @RequestParam(value = "note", required = false) String note) {
    Map model = new HashMap();
    User user = SecurityUtil.getUser();
    if (user != null) {
      if (StringUtils.isBlank(note)) {
        note = null;
      }
      playlistService.saveNote(user, playlistItemId, note);
      model.put("success", "true");
      model.put("note", note);
    } else {
      model.put("error", "You must be logged in to edit a playlist item note");
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }
}
