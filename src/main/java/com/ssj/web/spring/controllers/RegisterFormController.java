package com.ssj.web.spring.controllers;

import com.ssj.service.user.RegistrationUserValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ssj.service.user.UserService;
import com.ssj.service.user.UserException;
import com.ssj.model.user.User;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("registration")
public class RegisterFormController {

  protected final Log logger = LogFactory.getLog(getClass());

  private String formView = "register";
  private String registerConfirmView = "register_confirm";
  private String successView = "redirect:/register_done";
  private String registerDoneView = "register_done";

  private UserService userService;
  private SignInAdapter signInAdapter;

  private RegistrationUserValidator validator = new RegistrationUserValidator();

  public RegisterFormController() {

  }

  @ModelAttribute("registration")
  public User getUser() {
    return new User();
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public ModelAndView showFormHandler(NativeWebRequest request, @ModelAttribute("registration") User user, BindingResult errors) {
    ModelAndView modelAndView = new ModelAndView(formView);

    Connection<?> connection = ProviderSignInUtils.getConnection(request);
    if (connection != null) {
      // new user tried signing in with twitter/facebook/etc.

      // copy profile data from twitter/facebook/etc.
      UserProfile userProfile = connection.fetchUserProfile();
      user.setUsername(userProfile.getUsername());
      user.setDisplayName(userProfile.getUsername());
      user.setSignupPath(connection.getKey().getProviderId());

      try {
        // register user now, skip confirmation step
        userService.registerUser(user);

        // finish social signup/login
        ProviderSignInUtils.handlePostSignUp(user.getUsername(), request);

        // sign the user in
        signInAdapter.signIn(user.getUsername(), connection, request);

        // send the user to the "my info" page
        modelAndView.setViewName("redirect:/user/my_info");
      } catch (UserException e) {
        errors.reject("error.user", e.getMessage());
      }
    }
    return modelAndView;
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String submitFormHandler(@ModelAttribute("registration") User user, BindingResult errors) {
    String viewName = formView;
    validator.validate(user, errors);
    if (!errors.hasErrors()) {
      try {
        userService.checkUserInfoAvailability(user);

        viewName = registerConfirmView;
      } catch (UserException e) {
        logger.error("error submitting registration form", e);
        errors.reject("error.user", e.getMessage());
      }
    }
    return viewName;
  }

  @RequestMapping(value = "/register_confirm", params = "confirm=true")
  public String registerConfirmationHandler(@ModelAttribute("registration") User user, BindingResult errors, SessionStatus sessionStatus) {
    String viewName = formView;
    if (!errors.hasErrors()) {
      // copy the username/login to the display name if no display name was provided
      if (StringUtils.isBlank(user.getDisplayName())) {
        user.setDisplayName(user.getUsername());
      }
      user.setSignupPath("form");
      try {
        userService.registerUser(user);
        sessionStatus.isComplete();
        viewName = successView;
      } catch (UserException e) {
        logger.error("error confirming user registration", e);
        errors.reject("error.user", e.getMessage());
      }
    }
    return viewName;
  }

  @RequestMapping("/register_done")
  public String registerDoneHandler() {
    return registerDoneView;
  }
  
  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setSignInAdapter(SignInAdapter signInAdapter) {
    this.signInAdapter = signInAdapter;
  }

  public void setFormView(String formView) {
    this.formView = formView;
  }

  public void setSuccessView(String successView) {
    this.successView = successView;
  }

  public void setRegisterConfirmView(String registerConfirmView) {
    this.registerConfirmView = registerConfirmView;
  }

  public void setRegisterDoneView(String registerDoneView) {
    this.registerDoneView = registerDoneView;
  }
}
