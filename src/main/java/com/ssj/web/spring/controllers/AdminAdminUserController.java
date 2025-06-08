package com.ssj.web.spring.controllers;

import com.ssj.service.user.UserService;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;
import com.ssj.web.spring.view.JSONView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

@Controller
public class AdminAdminUserController {

  private UserService userService;

  @RequestMapping("/admin/admin_user")
  protected ModelAndView handleRequestInternal(@RequestParam("userId") int userId) {
    Map<String, Object> model = new HashMap<String, Object>();

    try {
      User user = SecurityUtil.getUser();

      if (user != null) {
        User adminUser = userService.getUser(userId);

        if (adminUser != null) {
          adminUser.setAdmin(!adminUser.isAdmin());
          userService.save(user);
          model.put("success", "true");
        } else {
          model.put("error", "Unable to find user with id " + userId);
        }
      } else {
        model.put("error", "You must be logged in to perform this action");
      }
    } catch (Exception e) {
      e.printStackTrace();
      model.put("error", e.getMessage());
    }
    return new ModelAndView(new JSONView(), "jsonModel", model);
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

}
