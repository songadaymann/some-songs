<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Admin</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">Admin Functions:</div>

          <ul style="list-style:circle;list-style-position:inside;">
            <li><a href="<c:url value="/admin/connections"/>">Configure Database/Mail Connections</a></li>
            <li><a href="<c:url value="/admin/topics"/>">Manage Message Board Topics</a></li>
            <li><a href="<c:url value="/admin/content_list"/>">Manage Site Content</a></li>
            <li><a href="<c:url value="/admin/site_links"/>">Manage Site Links</a></li>
            <li><a href="<c:url value="/admin/messages"/>">Manage Messages to Admin</a></li>
            <li><a href="<c:url value="/admin/ratings"/>">View/Cancel Song Ratings</a></li>
            <li><a href="<c:url value="/admin/broken_link_reports"/>">View Broken Link Reports</a></li>
          </ul>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="include/right_nav.jsp"/>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>