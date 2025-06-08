package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.authentication.ProviderManager;
import com.ssj.service.user.UserService;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class AdminSignInController {

  private String viewName = "redirect:/user/my_info";

  private UserService userService;
  private ProviderManager authenticationManager;

  @RequestMapping("/admin/sign_in")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int signInUserId) {

    User signInUser = userService.getUser(signInUserId);
    if (signInUser != null) {
      SecurityUtil.updateAuthentication(signInUser, authenticationManager);
    }

    return new ModelAndView(getViewName());
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setAuthenticationManager(ProviderManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  public String getViewName() {
    return viewName;
  }
}
