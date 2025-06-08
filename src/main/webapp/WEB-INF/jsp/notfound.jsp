<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%! private static final Logger LOGGER = Logger.getLogger("com.ssj.jsp.error"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<c:set var="theException" value="${requestScope['exception']}"/>
<c:set var="message" value="${theException ne null ? theException.message : null}"/>
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Not Found<c:if test="${message ne null}">: ${message}</c:if></title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <h3>Not found!</h3>

          <br> <br>

          <p>Whatever you were looking for, it ain't there. Weird!</p>

          <p>If this doesn't seem right, please <a href="<c:url value="/contact_admin"/>">contact the administrator</a>,
            or <a href="<c:url value="/message_board"/>">post in the message board</a>.</p>

          <br> <br>
          <c:if test="${message ne null}">
          <p style="font-weight:bold;">${message}</p>

          <div id="errorDetails" style="display:none;">
            <%
              StringWriter sw = new StringWriter();
              PrintWriter pw = new PrintWriter(sw);
              ((Exception) request.getAttribute("exception")).printStackTrace(pw);
            %>
            <%= sw.toString() %>
          </div>

            <%
              LOGGER.error(exception);
            %>
          </c:if>

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
