<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Properties" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.ssj.web.SomeSongsProperties" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
  private static final Logger LOGGER = Logger.getLogger("com.ssj.jsp.admin.database");
%>
<%

  String databaseURL = StringUtils.defaultString(request.getParameter("databaseURL"), "jdbc:mysql://localhost:3306/database?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=utf8");
  String databaseUsername = StringUtils.defaultString(request.getParameter("databaseUsername"), "username");
  String databasePassword = StringUtils.defaultString(request.getParameter("databasePassword"), "password");

  String smtpServerHost = StringUtils.defaultString(request.getParameter("smtpServerHost"), "smtp.gmail.com");
  String smtpServerPort = StringUtils.defaultString(request.getParameter("smtpServerPort"), "465");
  String smtpServerProtocol = StringUtils.defaultString(request.getParameter("smtpServerProtocol"), "smtps");
  String smtpServerUsername = StringUtils.defaultString(request.getParameter("smtpServerUsername"), "admin email address");
  String smtpServerPassword = StringUtils.defaultString(request.getParameter("smtpServerPassword"), "admin email password");
  String smtpServerAuthenticate = StringUtils.defaultString(request.getParameter("smtpServerAuthenticate"), "true");
  String smtpServerStartTLS = StringUtils.defaultString(request.getParameter("smtpServerStartTLS"), "true");
  String smtpServerDebug = StringUtils.defaultString(request.getParameter("smtpServerDebug"), "true");
  String systemFromAddress = StringUtils.defaultString(request.getParameter("systemFromAddress"), "admin email address");

  String errorMessage = null;

  boolean saveChanges = "true".equals(request.getParameter("saveChanges"));

  if (saveChanges) {
    LOGGER.debug("Saving changes ...");
    LOGGER.debug("Testing database connection ...");
    try {
      SomeSongsProperties.getInstance().setAndTestDatabaseProperties(databaseURL, databaseUsername, databasePassword);
//      response.sendRedirect(request.getContextPath());
//      return;
    } catch (Exception e) {
      LOGGER.error("Error testing/saving database properties", e);
      e.printStackTrace();
      errorMessage = "Could not connect to database: " + e.getMessage();
    }

    if (errorMessage == null) {
      LOGGER.debug("Testing mail connection ...");
      try {
        SomeSongsProperties.getInstance().setAndTestSMTPProperties(smtpServerHost, smtpServerPort, smtpServerProtocol,
            smtpServerUsername, smtpServerPassword, smtpServerAuthenticate, smtpServerStartTLS, smtpServerDebug, systemFromAddress);
      } catch (Exception e) {
        LOGGER.error("Error testing/saving mail properties", e);
        e.printStackTrace();
        errorMessage = "Could not send test e-mail: " + e.getMessage();
      }
    }

    if (errorMessage == null) {
      SomeSongsProperties.getInstance().saveOnContextDestroy();
    }
  }
%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <title>SomeSongs: Admin: Configure Database Connection</title>
    <%--<%@ include file="/WEB-INF/jsp/include/head.jsp"%>--%>
    <!--<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">-->
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <%--<jsp:include page="/WEB-INF/jsp/include/header.jsp"/>--%>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">


          <form method="post" action="<c:url value="/"/>" name="databaseForm" class="cmxform">
            <input type="hidden" name="saveChanges" value="true">

            <div id="errorsDiv">
              <%= StringUtils.defaultString(errorMessage, "") %>
            </div>

            <%
              if (SomeSongsProperties.getInstance().isSaveOnContextDestroy()) {
            %>
            <h3>Connections Configured!</h3>
            <b>You must redeploy the application now.</b>
            <p/>
            <%
              }
            %>

            <div id="pageHeaderDiv" class="pageHeader">Configure Database Connection:</div>

            <fieldset>
              <legend>Required Information</legend>
              <ol>
                <li>
                  <label for="databaseURL">MySQL Database URL:</label>
                  <input type="text" name="databaseURL" id="databaseURL" value="<%= databaseURL %>">
                </li>
                <li>
                  <label for="databaseUsername">MySQL Database Username:</label>
                  <input type="text" name="databaseUsername" id="databaseUsername" value="<%= databaseUsername %>">
                </li>
                <li>
                  <label for="databasePassword">MySQL Database Password:</label>
                  <input type="text" name="databasePassword" id="databasePassword" value="<%= databasePassword %>">
                </li>
<%--
                <li>
                  <label for="submitBtn" style="font-size:80%;">You must also put your jdbc driver jar into your app server's classpath.</label>
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Save">
                </li>
--%>
              </ol>
            </fieldset>

            <div id="pageHeaderDiv" class="pageHeader">Configure SMTP Connection:</div>

            <fieldset>
              <legend>Required Information</legend>
              <ol>
                <li>
                  <label for="smtpServerHost">SMTP Server Host:</label>
                  <input type="text" name="smtpServerHost" id="smtpServerHost" value="<%= smtpServerHost %>">
                </li>
                <li>
                  <label for="smtpServerPort">SMTP Server Port:</label>
                  <input type="text" name="smtpServerPort" id="smtpServerPort" value="<%= smtpServerPort %>">
                </li>
                <li>
                  <label for="smtpServerProtocol">SMTP Server Protocol:</label>
                  <input type="text" name="smtpServerProtocol" id="smtpServerProtocol" value="<%= smtpServerProtocol %>">
                </li>
                <li>
                  <label for="smtpServerUsername">SMTP Server Username:</label>
                  <input type="text" name="smtpServerUsername" id="smtpServerUsername" value="<%= smtpServerUsername %>">
                </li>
                <li>
                  <label for="smtpServerAuthenticate">SMTP Server Requires Authentication:</label>
                  <input type="text" name="smtpServerAuthenticate" id="smtpServerAuthenticate" value="<%= smtpServerAuthenticate %>">
                </li>
                <li>
                  <label for="smtpServerStartTLS">SMTP Server Requires StartTLS:</label>
                  <input type="text" name="smtpServerStartTLS" id="smtpServerStartTLS" value="<%= smtpServerStartTLS %>">
                </li>
                <li>
                  <label for="smtpServerDebug">SMTP Server Connection Debugging:</label>
                  <input type="text" name="smtpServerDebug" id="smtpServerDebug" value="<%= smtpServerDebug %>">
                </li>
                <li>
                  <label for="systemFromAddress">From Address for System E-mails:</label>
                  <input type="text" name="systemFromAddress" id="systemFromAddress" value="<%= systemFromAddress %>">
                </li>
                <li>
                  <label for="submitBtn" style="font-size:80%;">You must also make sure your JDBC driver and JavaMail jars are in the app's classpath.</label>
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Save">
                </li>
              </ol>
            </fieldset>

          </form>

          <br> <br> <br>

        </div></div>

      </div>

      <!--<div id="rightNavBox" class="yui-b">DEFAULT RIGHT NAV HERE</div>-->

    </div>

    <%--<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>--%>
  </div>
  </body>
</html>