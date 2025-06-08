package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.ssj.model.song.SongRating;
import com.ssj.model.user.User;
import com.ssj.service.song.SongRatingService;
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
public class AdminViewRatingsController {

  private String viewName = "admin/ratings";

  private SongRatingService songRatingService;

  @RequestMapping("/admin/ratings")
  protected ModelAndView handleRequestInternal(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "25") int pageSize) {

    List<SongRating> songRatings = null;
    try {
      User user = SecurityUtil.getUser();
      songRatings = songRatingService.getRecentRatingsWithSongs(user, start, pageSize);
    } catch (Exception e) {
      e.printStackTrace();
    }

    ModelMap model = new ModelMap();
    model.addAttribute("ratings", songRatings);
    model.addAttribute("start", start);
    model.addAttribute("pageSize", pageSize);

    return new ModelAndView(getViewName(), model);//"songSearch", search);
  }

  @Autowired
  public void setSongRatingService(SongRatingService songRatingService) {
    this.songRatingService = songRatingService;
  }

  public String getViewName() {
    return viewName;
  }
}