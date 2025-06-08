<%@ page isErrorPage="true" session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%! private static final Logger LOGGER = Logger.getLogger("com.ssj.jsp.error"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Error: ${ exception.getMessage() }</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <h3>Oops, an error occurred!</h3>

          <br> <br>

          <p>Please press your browser's "Back" button and try again.</p>

          <p>If the error happens again, please <a href="<c:url value="/contact_admin"/>">contact the administrator</a>,
            or <a href="<c:url value="/message_board"/>">post in the message board</a>.</p>

          <br> <br>

          <p style="font-weight:bold;">${ exception.getMessage() }</p>

          <!--
<c:forEach items="${exception.stackTrace}" var="stackTrace">
  ${stackTrace}
</c:forEach>
-->

          <br> <br> <br>

          <!--
          <%
          try {
            LOGGER.error(exception);
          } catch (Exception e) {
            // dang
          }
          %>
          -->
        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="/WEB-INF/jsp/include/song_list_right_nav.jsp"/>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>
