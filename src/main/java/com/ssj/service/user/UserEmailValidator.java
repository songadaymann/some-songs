package com.ssj.service.user;

import com.ssj.model.user.User;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;

/**
 * Validates a User's email.
 */
public class UserEmailValidator implements Validator {
  private static final int EMAIL_MAX_LENGTH = 128;

  // copied from Hibernate Validator EmailValidator.java
  private static String ATOM = "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s]";
  private static String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
  private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

  private java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
      "^" + ATOM + "+(\\." + ATOM + "+)*@"
          + DOMAIN
          + "|"
          + IP_DOMAIN
          + ")$",
      java.util.regex.Pattern.CASE_INSENSITIVE
  );

  public boolean supports(Class aClass) {
    return User.class.equals(aClass);
  }

  public void validate(Object o, Errors errors) {
    User user = (User) o;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "missing_email", "E-mail is required");

    if (!errors.hasFieldErrors("email")) {
      if (user.getEmail().length() > EMAIL_MAX_LENGTH) {
        errors.rejectValue("email", "long_email", "E-mail cannot be longer than 128 characters");
      } else {
        Matcher m = pattern.matcher(user.getEmail());
        if (!m.matches()) {
          errors.rejectValue("email", "invalid_email", "E-mail must be a valid e-mail address");
        }
      }
    }
  }
}
