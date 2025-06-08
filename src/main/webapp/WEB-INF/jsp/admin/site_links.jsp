<%@ page import="com.ssj.model.content.PageContent" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
  <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
  <title><spring:message code="site.name"/>: Site Links</title>
</head>
<body>
<div id="doc3" class="yui-t5">
  <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
  <div id="bd">

    <div id="yui-main">

      <div class="yui-b"><div class="yui-g">

        <div id="pageHeaderDiv" class="pageHeader">Site Links:</div>

        <c:if test="${param['saved'] eq 'true'}">
          <div class="successMessage">
            The site link has been saved.
          </div>
        </c:if>
        <c:if test="${param['cleared'] eq 'true'}">
          <div class="successMessage">
            The site link cache has been cleared.
          </div>
        </c:if>
        <c:if test="${param['deleted'] eq 'true'}">
          <div class="successMessage">
            The site link has been deleted.
          </div>
        </c:if>
<%--
        <c:if test="${deleteError ne null}">
          <div class="errors">
            <c:out value="${deleteError}"/>
          </div>
        </c:if>
--%>

        <div><a href="<c:url value="/admin/site_link?clearCache=true"/>">Clear Site Links Cache</a></div>

        <div><a href="<c:url value="/admin/site_link"/>">Add Site Link</a></div>

        <br>

          <c:choose>
            <c:when test="${siteLinks ne null and fn:length(siteLinks) > 0}">
              <table width="900" border="1">
                <tr>
                  <th>URL</th>
                  <th>Label</th>
                  <th>Hover</th>
                  <th>Position</th>
                  <th>&nbsp;</th>
                </tr>
              <c:forEach var="siteLink" items="${siteLinks}" varStatus="loopStatus">
                <c:set var="rowStyle" value="oddRow"/>
                <c:if test="${loopStatus.index % 2 != 0}">
                  <c:set var="rowStyle" value="evenRow"/>
                </c:if>
                <tr class="<c:out value="${rowStyle}"/>">
                  <td>
                      ${siteLink.url}
                  </td>
                  <td>
                      ${siteLink.label}
                  </td>
                  <td>
                      ${siteLink.hover}
                  </td>
                  <td>
                      ${siteLink.positionText}
                  </td>
                  <td>
                    <a href="<c:url value="/admin/site_link"/>?id=<c:out value="${siteLink.id}"/>" title="Click to edit site link">edit</a> |
                    <a href="<c:url value="/admin/delete_site_link"/>?id=<c:out value="${siteLink.id}"/>">delete</a>
                  </td>
                </tr>
              </c:forEach>
            </table>
            </c:when>
            <c:otherwise>
              No site links available.
            </c:otherwise>
          </c:choose>

        <br> <br>

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