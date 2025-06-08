<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Registration</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">NEW USER REGISTRATION</div>

          <form:form name="regForm" cssClass="cmxform" commandName="registration" method="post">

            <div id="global_errors">
              <form:errors path="*" cssClass="errors" element="div"/>
            </div>

            <fieldset>
              <legend>Required Information</legend>
              <ol>
                <li>
                  <label for="username" title="Only used for logging in, not shown to other users.">Login:</label>
                  <form:input path="username" id="username" />
                </li>
                <li>
                  <label for="email" title="Used when the system needs to contact you (eg to send your initial password).">E-mail:</label>
                  <form:input path="email" id="email"/>
                </li>
              </ol>
            </fieldset>

            <fieldset>
              <legend>Optional Information</legend>
              <ol>
                <li>
                  <label for="displayName" title="Name shown to other users. Defaults to your login.">Name:</label>
                  <form:input path="displayName" id="displayName"/>
                </li>
                <li>
                  <label for="location">Location (eg. city, state, country):</label>
                  <form:input path="location" id="location"/>
                </li>
                <li>
                  <label for="showEmailInUserInfo">Show E-mail In Profile:</label>
                  <!--<br>-->
                  <form:select path="showEmailInUserInfo" id="showEmailInUserInfo">
                    <form:option value="true">Yes</form:option>
                    <form:option value="false">No</form:option>
                  </form:select>
                </li>
                <li>
                  <label for="websiteName">Name of Your Website:</label>
                  <form:input path="websiteName" id="websiteName"/>
                </li>
                <li>
                  <label for="websiteURL">URL of Your Website:</label>
                  <form:input path="websiteURL" id="websiteURL"/>
                </li>
                <li>
                  <label for="goodBand">A Good Band:</label>
                  <form:input path="goodBand" id="goodBand"/>
                </li>
                <li>
                  <label for="goodAlbum">A Good Album:</label>
                  <form:input path="goodAlbum" id="goodAlbum"/>
                </li>
                <li>
                  <label for="goodSong">A Good Song:</label>
                  <form:input path="goodSong" id="goodSong"/>
                </li>
                <li>
                  <label for="goodMovie">A Good Movie:</label>
                  <form:input path="goodMovie" id="goodMovie"/>
                </li>
                <li>
                  <label for="goodWebsiteName">Name of A Good Website:</label>
                  <form:input path="goodWebsiteName" id="goodWebsiteName"/>
                </li>
                <li>
                  <label for="goodWebsiteURL">URL of A Good Website:</label>
                  <form:input path="goodWebsiteURL" id="goodWebsiteURL"/>
                </li>
                <li>
                  <label for="submitBtn" style="font-size:80%;">You will receive an e-mail with your password.</label>
                  <!--<br>-->
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Register">
                </li>
              </ol>
            </fieldset>

          </form:form>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <div class="infoText">
          <p class="rightNavBoxHeader">Registered users can:</p>
          <ul class="rightNavBoxList">
            <li>Rate and comment on songs</li>
            <li>Create artists and upload songs</li>
            <li>Post on the message board</li>
            <li>Save custom song searches</li>
            <li>Save lists of favorite artists</li>
            <li>Save lists of preferred/ignored users</li>
            <li>And more!</li>
          </ul>
          <br>
          <p class="rightNavBoxHeader">Registration requirements:</p>
          <ul class="rightNavBoxList">
            <li>Login must be at least 4 characters long</li>
            <li>Display Name must be at least 4 characters long</li>
          </ul>
        </div>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
<%--
  <script type="text/javascript" src="<c:url value="/js/self-labeled-forms.js"/>"></script>
  <script type="text/javascript"><!--
    new Axent.SelfLabeledInput();
  //--></script>
--%>
  </body>
</html>