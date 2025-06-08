package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.service.user.UserService;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AdvancedSongSearchController {

  private String formView = "advanced_search";
  private String successView = "redirect:/songs";

  @ModelAttribute("user")
  public User getUser() {
    return SecurityUtil.getUser();
  }

  @RequestMapping(value = "/advanced_search", method = RequestMethod.GET)
  public String showForm(@ModelAttribute("advancedSearch") SongSearch search) {
//    search.setTitleExactMatch(false);

    return getFormView();
  }

  @RequestMapping(value = "/advanced_search", method = RequestMethod.POST)
  public String submitForm(@ModelAttribute("advancedSearch") @Valid SongSearch search, BindingResult errors,
                                 @RequestParam(value = "notRatedByUser", required = false) String notRatedByUser,
                                 @RequestParam(value = "ratedGoodByUser", required = false) String ratedGoodByUser,
                                 WebRequest webRequest) {

    String viewName = getFormView();
    if (!errors.hasErrors()) {
      User user = SecurityUtil.getUser();
      if (user != null) {
        if ("1".equals(notRatedByUser)) {
          search.setNotRatedByUser(user.getId());
        } else {
          search.setNotRatedByUser(null);
        }
        if ("1".equals(ratedGoodByUser)) {
          search.setRatedGoodByUser(user.getId());
        } else {
          search.setRatedGoodByUser(null);
        }
      }

      webRequest.setAttribute("songSearch", search, WebRequest.SCOPE_SESSION);

      viewName = getSuccessView();
    }


/*
    int start = NumberUtils.toInt(request.getParameter("start"), 0);
    search.setStartResultNum(start);
*/
    return viewName;
  }

  public String getFormView() {
    return formView;
  }

  public void setFormView(String formView) {
    this.formView = formView;
  }

  public String getSuccessView() {
    return successView;
  }

  public void setSuccessView(String successView) {
    this.successView = successView;
  }
}
