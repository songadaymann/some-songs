<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: Message Board Search</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div class="songListHeader">
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
            <div class="songListPaging">
            <c:url var="pagingLink" value="/posts"/>
            <c:if test="${sidebarPostSearch.previousPage}">
              <c:set var="pagingLinkText" value="newer"/>
              <c:if test="${!sidebarPostSearch.descending}">
                <c:set var="pagingLinkText" value="older"/>
              </c:if>
              <a href="<c:out value="${pagingLink}"/>?start=<c:out value="${sidebarPostSearch.previousPageStartNum + 1}"/>">[ <c:out value="${pagingLinkText}"/> ]</a>
            </c:if>
            <c:if test="${sidebarPostSearch.nextPage}">
              <c:set var="pagingLinkText" value="older"/>
              <c:if test="${!sidebarPostSearch.descending}">
                <c:set var="pagingLinkText" value="newer"/>
              </c:if>
              <a href="<c:out value="${pagingLink}"/>?start=<c:out value="${sidebarPostSearch.nextPageStartNum + 1}"/>">[ <c:out value="${pagingLinkText}"/> ]</a>
            </c:if>
            </div>
            <div class="songListName">Post Search</div>
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
          </div>
          <table class="songList">
            <tr class="songListFooterRow">
              <td>Thread</td>
              <td>Posted By</td>
              <td>Last Edited</td>
            </tr>
          <c:choose>
            <c:when test="${sidebarPostSearch.totalResults eq 0}">
            <tr class="songListOddRow">
              <td colspan="3">No posts match the search criteria.</td>
            </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="post" items="${postSearchResults}" varStatus="loopStatus">
                <tr class="<c:choose><c:when test="${loopStatus.index % 2 == 0}">songListOddRow</c:when><c:otherwise>songListEvenRow</c:otherwise></c:choose>">
                  <c:url var="viewPostLink" value="/view_post">
                    <c:param name="id" value="${post.id}"/>
                  </c:url>
                  <c:choose>
                    <c:when test="${post.originalPost eq null}">
                      <c:set var="subject" value="${post.subject}"/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="subject" value="${post.originalPost.subject}"/>
                    </c:otherwise>
                  </c:choose>
                  <td><a href="<c:out value="${viewPostLink}"/>" title="Read Post"><c:out value="${subject}"/></a></td>
                  <c:url var="viewUserLink" value="/profile">
                    <c:param name="id" value="${post.user.id}"/>
                  </c:url>
                  <td><a href="<c:out value="${viewUserLink}"/>" title="View <c:out value="${post.user.displayName}"/> User Info"><c:out value="${post.user.displayName}"/></a></td>
                  <td><span class="medium"><fmt:formatDate value="${post.changeDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></span></td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
          </table>

          <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="/WEB-INF/jsp/include/message_board_rightnav.jsp"/>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>
