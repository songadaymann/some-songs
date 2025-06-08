<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Message Board</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:if test="${param['msg'] ne null}">
            <div class="successMessage">
              <c:out value="${param['msg']}"/>
            </div>
            <br>
          </c:if>

          <c:set var="foundThreads" value="${sessionScope['threadSearch'].totalResults > 0}"/>
          <div class="songListHeader">
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
            <div class="songListPaging">
              <c:if test="${sessionScope['threadSearch'].previousPage}">
                <c:url var="cPrevLink" value="/message_board">
                  <c:param name="threadsStart" value="${sessionScope['threadSearch'].previousPageStartNum + 1}"/>
                </c:url>
              <a class="previousPageLink" href="<c:out value="${cPrevLink}"/>">[newer]</a>
              </c:if>
              <c:if test="${sessionScope['threadSearch'].nextPage}">
                <c:url var="cNextLink" value="/message_board">
                  <c:param name="threadsStart" value="${sessionScope['threadSearch'].nextPageStartNum + 1}"/>
                </c:url>
              <a class="nextPageLink" href="<c:out value="${cNextLink}"/>">[older]</a>
              </c:if>
            </div>
            <div class="songListName">
              <c:choose>
                <c:when test="${sessionScope['threadSearch'].topic ne null}">
                  Topic: <c:out value="${sessionScope['threadSearch'].topic.name}"/>
                </c:when>
                <c:otherwise>
                  All Topics
                </c:otherwise>
              </c:choose></div>
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
          </div>
          <table class="songList">
            <tr class="songListFooterRow">
              <td>Subject</td>
              <td>Posted By</td>
              <td># of Replies</td>
              <td>Last Post</td>
            </tr>
            <c:choose>
            <c:when test="${!foundThreads}">
            <tr class="songListOddRow">
              <td colspan="5">No threads found.</td>
            </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="thread" items="${threads}" varStatus="loopStatus">
            <tr class="<c:choose><c:when test="${loopStatus.index % 2 == 0}">songListOddRow</c:when><c:otherwise>songListEvenRow</c:otherwise></c:choose>">
              <c:url var="threadLink" value="/thread">
                <c:param name="id" value="${thread.id}"/>
              </c:url>
              <td><a href="<c:out value="${threadLink}"/>" title="View Thread"><c:out value="${thread.subject}"/></a></td>
              <c:url var="userLink" value="/profile">
                <c:param name="id" value="${thread.user.id}"/>
              </c:url>
              <td><a href="<c:out value="${userLink}"/>" title="View User Profile"><c:out value="${thread.user.displayName}"/></a></td>
              <td><c:out value="${thread.numReplies}"/></td>
              <td><fmt:formatDate value="${thread.lastReplyDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
              </c:forEach>
            </c:otherwise>
            </c:choose>
          </table>

          <br>

          <a href="user/edit_thread" class="postLink">Post New Thread</a>

          <br> <br> <br>

          <c:set var="foundPosts" value="${sessionScope['postSearch'].totalResults > 0}"/>
          <div class="songListHeader">
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
            <div class="songListPaging">
              <c:if test="${sessionScope['postSearch'].previousPage}">
                <c:url var="cPrevLink" value="/message_board">
                  <c:param name="postsStart" value="${sessionScope['postSearch'].previousPageStartNum + 1}"/>
                </c:url>
              <a class="previousPageLink" href="<c:out value="${cPrevLink}"/>">[ newer ]</a>
              </c:if>
              <c:if test="${sessionScope['postSearch'].nextPage}">
                <c:url var="cNextLink" value="/message_board">
                  <c:param name="postsStart" value="${sessionScope['postSearch'].nextPageStartNum + 1}"/>
                </c:url>
              <a class="nextPageLink" href="<c:out value="${cNextLink}"/>">[ older ]</a>
              </c:if>
            </div>
            <div class="songListName">Newest Posts</div>
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
          </div>
          <table class="songList">
            <tr class="songListFooterRow">
              <td>Thread</td>
              <td>Posted By</td>
              <td>Posted</td>
            </tr>
            <c:choose>
            <c:when test="${!foundPosts}">
            <tr class="songListOddRow">
              <td colspan="5">No posts found.</td>
            </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="post" items="${posts}" varStatus="loopStatus">
            <tr class="<c:choose><c:when test="${loopStatus.index % 2 == 0}">songListOddRow</c:when><c:otherwise>songListEvenRow</c:otherwise></c:choose>">
              <c:url var="postLink" value="/view_post">
                <c:param name="id" value="${post.id}"/>
              </c:url>
              <td><a href="<c:out value="${postLink}"/>" title="View Post"><c:out value="${post.originalPost ne null ? post.originalPost.subject : post.subject}"/></a></td>
              <c:url var="userLink" value="/profile">
                <c:param name="id" value="${post.user.id}"/>
              </c:url>
              <td><a href="<c:out value="${userLink}"/>" title="View User Profile"><c:out value="${post.user.displayName}"/></a></td>
              <td><fmt:formatDate value="${post.createDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></td>
            </tr>
              </c:forEach>
            </c:otherwise>
            </c:choose>
          </table>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <%@ include file="/WEB-INF/jsp/include/message_board_rightnav.jsp" %>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>