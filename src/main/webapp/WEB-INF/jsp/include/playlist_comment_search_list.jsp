<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:set var="foundComments" value="${commentSearch.totalResults > 0}"/>
<%--<c:set var="isReplySearch" value="${sessionScope['isReplySearch'] eq 'true'}"/>--%>
<%--<c:set var="searchLabel" value="Comment"/>--%>
<%--<c:if test="${isReplySearch}">--%>
  <%--<c:set var="searchLabel" value="Reply"/>--%>
<%--</c:if>--%>
<div class="songListHeader" style="width:100%">
  <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
  <c:if test="${param['hidePaging'] ne 'true'}">
    <%@ include file="/WEB-INF/jsp/include/playlist_comment_search_paging.jsp"%>
  </c:if>
  <div class="songListName"><c:out value="${commentSearch.name}" default="Comment Search"/></div>
  <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
</div>
<table class="songList" style="width:100%">
  <tr class="songListFooterRow">
    <td>&nbsp;</td>
    <td>Author</td>
    <c:choose>
      <c:when test="${isReplySearch}">
    <td>Comment Author</td>
      </c:when>
      <c:otherwise>
    <td>Replies</td>
      </c:otherwise>
    </c:choose>
    <td>Playlist</td>
    <td>Artist</td>
    <td>Posted</td>
  </tr>
  <c:choose>
  <c:when test="${!foundComments}">
  <tr class="songListOddRow">
    <td colspan="6">No comments or replies match the search criteria.</td>
  </tr>
  </c:when>
  <c:otherwise>
    <c:forEach var="comment" items="${commentSearchResults}" varStatus="loopStatus">
      <c:set var="rowStyle" value="songListOddRow"/>
      <c:if test="${loopStatus.index % 2 != 0}">
        <c:set var="rowStyle" value="songListEvenRow"/>
      </c:if>
      <c:choose>
        <c:when test="${isReplySearch}">
          <c:set var="playlist" value="${comment.originalComment.playlist}"/>
          <c:set var="commentId" value="${comment.originalComment.id}"/>
        </c:when>
        <c:otherwise>
          <c:set var="playlist" value="${comment.playlist}"/>
          <c:set var="commentId" value="${comment.id}"/>
        </c:otherwise>
      </c:choose>
  <tr class="<c:out value="${rowStyle}"/>">
    <c:url var="viewCommentURL" value="/view_playlist_comment">
      <c:param name="id" value="${comment.id}"/>
    </c:url>
    <c:if test="${isReplySearch}">
      <c:url var="viewCommentURL" value="/view_playlist_comment_reply">
        <c:param name="id" value="${comment.id}"/>
      </c:url>  
    </c:if>
    <td class="nowrap"><a href="<c:out value="${viewCommentURL}"/>">[ read ]</a></td>
    <c:url var="authorURL" value="/profile">
      <c:param name="id" value="${comment.user.id}"/>
    </c:url>
    <td><a href="<c:out value="${authorURL}"/>" title="View profile for <c:out value="${comment.user.displayName}"/>"><str:truncateNicely lower="16" upper="24" appendToEnd="..."><c:out value="${comment.user.displayName}"/></str:truncateNicely></a></td>
    <c:choose>
      <c:when test="${isReplySearch}">
        <c:url var="originalAuthorURL" value="/profile">
          <c:param name="id" value="${comment.originalComment.user.id}"/>
        </c:url>
        <td>to&nbsp;<a href="<c:out value="${originalAuthorURL}"/>" title="View profile for <c:out value="${comment.originalComment.user.displayName}"/>"><str:truncateNicely lower="16" upper="24" appendToEnd="..."><c:out value="${comment.originalComment.user.displayName}"/></str:truncateNicely></a></td>
      </c:when>
      <c:otherwise>
        <td class="nowrap"><a href="<c:out value="${viewCommentURL}"/>"><c:out value="${comment.numReplies}"/>
          <c:choose>
            <c:when test="${comment.numReplies eq 1}">
              reply
            </c:when>
            <c:otherwise>
              replies
            </c:otherwise>
          </c:choose>
        </a></td>
      </c:otherwise>
    </c:choose>
    <c:url var="playlistURL" value="/playlist">
      <c:param name="id" value="${playlist.id}"/>
    </c:url>
    <td>re: <a href="<c:out value="${playlistURL}"/>" title="View playlist info for '<c:out value="${playlist.title}"/>'"><str:truncateNicely lower="24" upper="32" appendToEnd="..."><c:out value="${playlist.title}"/></str:truncateNicely></a></td>
    <c:url var="userURL" value="/profile">
      <c:param name="id" value="${playlist.user.id}"/>
    </c:url>
    <td>by <a href="<c:out value="${userURL}"/>" title="View info for user <c:out value="${playlist.user.displayName}"/>"><str:truncateNicely lower="20" upper="28" appendToEnd="..."><c:out value="${playlist.user.displayName}"/></str:truncateNicely></a></td>
    <td class="nowrap"><fmt:formatDate value="${comment.createDate}" pattern="yyyy-MM-dd HH:mm" type="both"/></td>
  </tr>
    </c:forEach>
  </c:otherwise>
  </c:choose>
</table>
