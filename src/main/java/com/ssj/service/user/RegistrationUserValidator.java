package com.ssj.service.user;

import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import com.ssj.model.user.User;

/**
 * Validates the registration form.
 *
 */
public class RegistrationUserValidator implements Validator {
  private static final int USERNAME_MIN_LENGTH = 3;
  private static final int USERNAME_MAX_LENGTH = 64;

  private UserEmailValidator emailValidator = new UserEmailValidator();

  public boolean supports(Class aClass) {
    return User.class.equals(aClass);
  }

  public void validate(Object o, Errors errors) {
    User user = (User) o;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "missing_username", "Login is required");

    if (!errors.hasFieldErrors("username")) {
      // validate length
      if (user.getUsername().length() < USERNAME_MIN_LENGTH ||
          user.getUsername().length() > USERNAME_MAX_LENGTH) {
        errors.rejectValue("username", "incorrect_username_length", "Login must be 3 to 64 characters");
      }
    }

    emailValidator.validate(user, errors);
  }
}
