package com.ssj.web.spring.controllers;

import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.user.UserService;
import com.ssj.model.artist.Artist;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.Map;
import java.util.HashMap;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AddArtistController {

  private String formView = "user/add_artist";

  private UserService userService;

  @RequestMapping(value = "/user/add_artist", method = RequestMethod.GET)
  public String getHandler(@ModelAttribute Artist artist) {
    return getFormView();
  }

  @RequestMapping(value = "/user/add_artist", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute @Valid Artist artist, BindingResult errors) {

    ModelAndView modelAndView = new ModelAndView(getFormView());
    if (!errors.hasErrors()) {
      try {
        User user = SecurityUtil.getUser();
        if (user != null) {
          userService.addArtist(user, artist);
        }
        modelAndView.setView(new JSONView());
        Map model = new HashMap();
        model.put("success", true);
        modelAndView.addObject("jsonModel", model);
      } catch (Exception e) {
        e.printStackTrace();
        errors.reject("error.artist", e.getMessage());
      }
    }

    return modelAndView;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public String getFormView() {
    return formView;
  }
}
