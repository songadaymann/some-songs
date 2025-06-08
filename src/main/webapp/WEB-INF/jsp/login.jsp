<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Login</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms //--%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">LOGIN</div>

          <form id="tw_signin" action="<c:url value="/signin/twitter"/>" method="POST"></form>
          <form id="fb_signin" action="<c:url value="/signin/facebook"/>" method="POST">
            <%--<input type="hidden" name="scope" value="publish_stream,offline_access">--%>
          </form>

          <form name="loginForm" action="j_spring_security_check" class="cmxform" method="post">

            <c:if test="${not empty param.loginError}">
              <div id="errorsDiv">
                <div class="errors">
                  Login and/or Password incorrect, please try again.<br>
                  <a href="<c:url value="/login_help"/>">Forgot your Login or Password?</a>
                  <!--Login failed:<br>-->
                  <%--<%= ((AuthenticationException) request.getSession(true).getAttribute(AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>--%>
                </div>
              </div>
            </c:if>

            <fieldset>
              <legend>Log In With Twitter and Facebook</legend>
              <ol>
                <li>
                    <a href="javascript:document.forms.tw_signin.submit()" title="Log In With Twitter">Log In With Twitter</a>
                </li>
                <li>
                    <a href="javascript:document.forms.fb_signin.submit()" title="Log In With Facebook">Log In With Facebook</a>
                </li>
              </ol>
            </fieldset>

            <fieldset>
              <legend>Enter your login and password below:</legend>
              <ol>
                <li>
                  <label for="j_username">Login:</label>
                  <%--<form:input path="username" id="username" />--%>
                  <input type="text" id="j_username" name="j_username"<c:if test="${not empty param.loginError}">value='<c:out value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"/>'</c:if>>
                  <%--<%= request.getSession().getAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY) %>--%>
                </li>
                <li>
                  <label for="j_password">Password:</label>
                  <input type="password" id="j_password" name="j_password">
                  <%--<form:input path="displayName" id="displayName"/>--%>
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
          <a style="font-weight:bold;padding:2px;" href="<c:url value="/login_help"/>">Login Help</a><br>
          <ul style="font-size:90%;padding:2px;list-style:circle;padding-left:20px">
            <li>Forgot your login?</li>
            <li>Get a login reminder via e-mail</li>
            <li>Forgot your password?</li>
            <li>Get a new password via e-mail</li>
          </ul>
          <br>
        </div>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script language="JavaScript" type="text/javascript"><!--
    document.forms.loginForm.j_username.focus();
  //--></script>
  </body>
</html>