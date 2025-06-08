<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Login</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div>
    <div id="bd">

      <div id="yui-main">

        <div><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader" style="margin-top:0">Log In to Rate, Comment, and More!</div>


          <form name="loginForm" action="<c:url value="/j_spring_security_check"/>" class="cmxform" method="post">

            <fieldset>
              <legend>Sign up now!</legend>
              <ol>
                <li>
                  <a href="javascript:document.forms.fb_signin.submit()" title="Log In With Facebook">Log In With Facebook</a>
                </li>
                <li>
                  <a href="javascript:document.forms.tw_signin.submit()" title="Log In With Twitter">Log In With Twitter</a>
                </li>
                <li>
                  <a href="<c:url value="/register"/>" title="Register">Register</a>
                </li>
              </ol>
            </fieldset>

            <fieldset style="margin-bottom:0">
              <legend>Or enter your login and password below:</legend>
              <ol>
                <li>
                  <label for="j_username">Login:</label>
                  <input type="text" id="j_username" name="j_username"<c:if test="${not empty param.loginError}">value='<c:out value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"/>'</c:if>>
                </li>
                <li>
                  <label for="j_password">Password:</label>
                  <input type="password" id="j_password" name="j_password">
                </li>
                <li>
                  <label for="_spring_security_remember_me">Remember my login:</label>
                  <input type="checkbox" id="_spring_security_remember_me" name="_spring_security_remember_me" value="true" style="width:auto" checked="checked">
                </li>
                <li>
                  <label for="submitBtn">&nbsp;</label>
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Log In!">
                </li>
              </ol>
            </fieldset>

          </form>

        </div></div>

      </div>

    </div>

  </div>
  </body>
</html>