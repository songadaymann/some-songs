<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Login Help</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms --%>
    <script type="text/javascript" src="<c:url value="/js/jsr303js-codebase.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">LOGIN HELP</div>

          <div id="global_errors">
            <c:if test="${error ne null}">
              <ul><li><c:out value="${error}"/></li></ul>
            </c:if>
          </div>

          <form action="<c:url value="/login_help"/>" method="post" name="loginForm" class="cmxform" onsubmit="return verifyEmail()">
            <input type="hidden" name="formType" value="login">

            <fieldset>
              <legend>Forgot your login?</legend>
              <ol>
                <li>Enter your e-mail address to receive an e-mail with your login.</li>
                <li>
                  <label for="email">E-mail:</label>
                  <input type="text" name="formValue" id="email" maxlength="128">
                </li>
                <li>
                  <label for="loginButton">&nbsp;</label>
                  <input type="submit" id="loginButton" name="loginButton" class="submitBtn" value="Send Login Reminder E-mail">
                  <%--<input type="button" id="loginButton" name="loginButton" class="submitBtn" value="Send Login Reminder E-mail" onclick="submitLoginHandler()">--%>
                </li>
              </ol>
            </fieldset>

          </form>

          <form action="<c:url value="/login_help"/>" method="post" name="passwordForm" class="cmxform" onsubmit="return verifyLogin()">
            <input type="hidden" name="formType" value="password">

            <fieldset>
              <legend>Forgot your password?</legend>
              <ol>
                <li>Enter your login to receive an e-mail with a new password.</li>
                <li>
                  <label for="login">Login:</label>
                  <input type="text" name="formValue" id="login" maxlength="64">
                </li>
                <li>
                  <label for="passwordButton">&nbsp;</label>
                  <input type="submit" id="passwordButton" name="passwordButton" class="submitBtn" value="Send New Password E-mail">
                  <%--<input type="button" id="passwordButton" name="passwordButton" class="submitBtn" value="Send New Password E-mail" onclick="submitPasswordHandler()">--%>
                </li>
              </ol>
            </fieldset>

          </form>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <div class="infoText">
          <a style="font-weight:bold;padding:2px;margin-bottom:4px;" href="<c:url value="/register"/>">Register Now!</a><br>
          <p class="boldGrayBottom">Registered users can:</p>
          <ul style="font-size:90%;padding:2px;list-style:circle;padding-left:20px">
            <li>Rate and comment on songs</li>
            <li>Create artists and upload songs</li>
            <li>Post on the message board</li>
            <li>Save custom song searches</li>
            <li>Save lists of favorite artists</li>
            <li>Save lists of preferred/ignored users</li>
            <li>And more!</li>
          </ul>
          <br>
        </div>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  <script type="text/javascript"><!--
    function verifyEmail() {
      var valid = false;
      var theForm = document.forms.loginForm;
      if (theForm.formValue.value.match(/\S/)) {
        valid = true;
      } else {
        $('global_errors').innerHTML = "<ul><li>Please enter your email address.</li></ul>";
      }
      return valid;
    }
    function verifyLogin() {
      var valid = false;
      var theForm = document.forms.passwordForm;
      if (theForm.formValue.value.match(/\S/)) {
        valid = true;
      } else {
        $('global_errors').innerHTML = "<ul><li>Please enter your login.</li></ul>";
      }
      return valid;
    }
  //--></script>
  </body>
</html>