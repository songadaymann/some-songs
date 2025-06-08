package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ssj.service.user.UserService;
import com.ssj.service.user.UserException;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class LoginHelpController {

  protected final Log logger = LogFactory.getLog(getClass());

  private String formView = "login_help";
  private String successView = "redirect:/login_help_done";

  private UserService userService;

  @RequestMapping(value = "/login_help", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/login_help", method = RequestMethod.POST)
  public ModelAndView onSubmit(@RequestParam("formValue") String formValue,
                               @RequestParam("formType") String formType) {

    String error = null;
    if (!"password".equals(formType) && !"login".equals(formType)) {
      throw new RuntimeException("Unrecognized form type value");
    }
    if (formValue == null || formValue.trim().length() == 0) {
      error = "Please provide your " + ("password".equals(formType) ? "login." : "e-mail address.");
    } else {
      try {
        if ("password".equals(formType)) {
          userService.sendNewPassword(formValue);
        } else if ("login".equals(formType)) {
          userService.sendLoginReminder(formValue);
        }
      } catch (UserException e) {
        e.printStackTrace();
        error = e.getMessage();
      }
    }

    ModelAndView modelAndView;
    if (error != null) {
      modelAndView = new ModelAndView(getFormView(), "error", error);
    } else {
      modelAndView = new ModelAndView(getSuccessView(), "emailSent", formType);
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

  public String getSuccessView() {
    return successView;
  }
}
