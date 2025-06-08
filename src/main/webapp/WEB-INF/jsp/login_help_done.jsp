<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Login Help Finished</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:choose>
            <c:when test="${param['emailSent'] eq 'password'}">
              <c:set var="emailType" value="new password"/>
            </c:when>
            <c:when test="${param['emailSent'] eq 'login'}">
              <c:set var="emailType" value="login reminder"/>
            </c:when>
          </c:choose>

          <div id="pageHeaderDiv" class="pageHeader"><c:out value="${fn:toUpperCase(emailType)}"/> E-MAIL SENT!</div>

          Your <c:out value="${emailType}"/> e-mail has been sent, and you should receive it shortly.

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <div class="infoText">
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
  </body>
</html>