package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import com.ssj.service.user.UserService;
import com.ssj.service.user.UserException;
import com.ssj.service.song.SongCommentService;
import com.ssj.service.song.SongRatingService;
import com.ssj.model.user.User;
import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.song.SongComment;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class ProfileController {

  private static final int COMMENTS_FOR_FIRST_PAGE = 10;
  private static final int COMMENTS_PER_PAGE = 20;

  private String viewName = "profile";

  private UserService userService;
  private SongCommentService songCommentService;
  private SongRatingService songRatingService;

  @RequestMapping("/profile")
  protected ModelAndView profileHandler(@RequestParam("id") int id,
                                        @RequestParam(value = "start", required = false) Integer start,
                                        @RequestParam(value = "resultsPerPage", required = false) Integer resultsPerPage) {
    if (id > -1) {
      User profileUser = userService.getUser(id);
      if (profileUser != null) {
        User user = SecurityUtil.getUser();
        if (profileUser.getLastLoginDate() == null && (user == null || !user.isAdmin())) {
          // don't show profiles for users who haven't logged in yet (and could be spam) to anyone but admins
          throw new UserException("Could not find user with id " + id);
        }
        ModelMap model = new ModelMap();

        // load favorite artists
        profileUser.getFavoriteArtists();
        // load favorite songs
        profileUser.getFavoriteSongs();
        model.addAttribute("profileUser", profileUser);

        try {
          if (user != null) {
            boolean isPreferred = userService.isPreferredUser(user, profileUser);
            model.addAttribute("isPreferred", isPreferred);

            boolean isIgnored = userService.isIgnoredUser(user, profileUser);
            model.addAttribute("isIgnored", isIgnored);

            Integer agreement = songRatingService.getRatingAgreement(user, profileUser);
            if (agreement != null) {
              model.addAttribute("agreement", agreement);
            }

            if (user.isAdmin()) {
              model.addAttribute("isUserAdmin", profileUser.isAdmin());
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          SongCommentSearch search = new SongCommentSearch();
          search.setUser(profileUser);
          // set default paging
          if (start == null) {
            // viewing first page of comments
            search.setResultsPerPage(COMMENTS_FOR_FIRST_PAGE);
            search.setResultsPerNextPage(COMMENTS_PER_PAGE);
          } else {
            // user has clicked on comment list navigation links
            search.setResultsPerPage(COMMENTS_PER_PAGE);
          }

//          int start = NumberUtils.toInt(request.getParameter("start"), 1);
          // turn 1-based start into 0-based row num
//          start--;
          search.setStartResultNum(start == null ? 0 : --start);

          if (resultsPerPage != null && resultsPerPage > 0) {
            search.setResultsPerPage(resultsPerPage);
            // put user back on first page
//      search.setStartResultNum(0);
          }

          List<SongComment> comments = songCommentService.findComments(search);

          model.addAttribute("commentSearch", search);
          model.addAttribute("commentSearchResults", comments);
        } catch (Exception e) {
          e.printStackTrace();
        }

        return new ModelAndView(getViewName(), model);
      } else {
        throw new RuntimeException("Could not find user with id " + id);
      }
    } else {
      throw new RuntimeException("Invalid user id");
    }
  }


  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  @Autowired
  public void setSongRatingService(SongRatingService songRatingService) {
    this.songRatingService = songRatingService;
  }

  public String getViewName() {
    return viewName;
  }
}
