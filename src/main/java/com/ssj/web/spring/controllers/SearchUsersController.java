package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.ssj.service.user.UserService;
import com.ssj.model.user.User;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SearchUsersController {

  private String viewName = "include/autocomplete_results";

  private UserService userService;

  @RequestMapping("/user/search_users")
  protected ModelAndView handleRequestInternal(@RequestParam("name") String name) {

    ModelAndView modelAndView = new ModelAndView(getViewName());

    List users = userService.findUsersByName(name);

    Map results = new HashMap();

    if (users != null) {
      for (Object userObj : users) {
        User user = (User) userObj;

        results.put(user.getId(), user.getDisplayName());
      }
    }

    modelAndView.addObject("results", results);

    modelAndView.addObject("titleText", "Share artist with user ");

    return modelAndView;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public String getViewName() {
    return viewName;
  }
}
