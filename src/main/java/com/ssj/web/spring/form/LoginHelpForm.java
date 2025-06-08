package com.ssj.web.spring.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class LoginHelpForm {

  @Length(max = 128, message = "E-mail cannot be longer than 128 characters")
  @Email
  private String email;
  @Length(min = 4, max = 64, message = "Login must be 4 to 64 characters")
  private String login;

  @NotEmpty(message = "Please enter your e-mail address or login")
  private String formValue;

  @NotEmpty(message = "Please click one of the buttons to submit the form")
  private String formType;

  public LoginHelpForm() {

  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getFormType() {
    return formType;
  }

  public void setFormType(String formType) {
    this.formType = formType;
  }

  public String getFormValue() {
    return formValue;
  }

  public void setFormValue(String formValue) {
    this.formValue = formValue;
  }
}
