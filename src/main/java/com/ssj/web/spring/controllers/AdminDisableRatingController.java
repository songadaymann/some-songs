package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.service.song.SongRatingService;
import com.ssj.model.user.User;
import com.ssj.model.song.SongRating;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.Map;
import java.util.HashMap;

@Controller
public class AdminDisableRatingController {

  private SongRatingService songRatingService;

  @RequestMapping("/admin/disable_rating")
  protected ModelAndView handleRequestInternal(@RequestParam("ratingId") int ratingId) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      User user = SecurityUtil.getUser();
      SongRating rating = songRatingService.toggleDisabled(user, ratingId);
      model.put("success", "true");
      model.put("disabled", rating.isDisabled());
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }

    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setSongRatingService(SongRatingService songRatingService) {
    this.songRatingService = songRatingService;
  }

}
