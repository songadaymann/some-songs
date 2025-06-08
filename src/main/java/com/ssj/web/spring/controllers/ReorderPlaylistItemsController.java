package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.math.NumberUtils;

import com.ssj.service.playlist.PlaylistService;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class ReorderPlaylistItemsController {

  private PlaylistService playlistService;

  @RequestMapping("/user/reorder_playlist_items")
  protected ModelAndView handleRequestInternal(@RequestParam("playlistId") int playlistId,
                                               @RequestParam("itemIds") String itemIdsString) {
    Map model = new HashMap();
    User user = SecurityUtil.getUser();
    if (user != null) {
      String[] itemIdStrings = itemIdsString.split(",");
      if (itemIdStrings.length > 0) {
        int[] itemIds = new int[itemIdStrings.length];
        String badId = null;
        for (int i = 0; i < itemIds.length; i++) {
          int itemId = NumberUtils.toInt(itemIdStrings[i], -1);
          if (itemId < 0) {
            badId = itemIdStrings[i];
            break;
          }
          itemIds[i] = NumberUtils.toInt(itemIdStrings[i], -1);
        }
        if (badId == null) {
          playlistService.reorderItems(user, playlistId, itemIds);
          model.put("success", "true");
        } else {
          model.put("error", "Invalid id " + badId);
        }
      }
    } else {
      model.put("error", "You must be logged in to reorder a playlist's songs");
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }
}
