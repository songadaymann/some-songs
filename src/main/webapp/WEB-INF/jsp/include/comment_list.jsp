<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:set var="foundComments" value="${commentSearch.totalResults > 0}"/>
<div id="comments" class="songComments">
  <div class="songCommentsHeader">
    <%--<span style="float:right">--%>
    <span>
      <c:choose>
        <c:when test="${song ne null}">
          <%@ include file="/WEB-INF/jsp/include/comment_paging.jsp"%>
        </c:when>
        <c:otherwise>
          <%@ include file="/WEB-INF/jsp/include/comment_search_paging.jsp"%>
        </c:otherwise>
      </c:choose>
    </span>
    <span style="font-weight:bold">Comments:</span>
  </div>
  <div id="noComments"<c:if test="${commentSearchResults ne null and fn:length(commentSearchResults) gt 0}"> style="display:none"</c:if>>None.</div>
  <div class="songListHeader" style="border-top:0<c:if test="${commentSearchResults eq null or fn:length(commentSearchResults) eq 0}">;display:none</c:if>">
    <c:forEach var="comment" items="${commentSearchResults}" varStatus="loopStatus">
    <c:set var="commentStyle" value="evenRow"/>
    <c:if test="${loopStatus.index % 2 == 0}">
      <c:set var="commentStyle" value="oddRow"/>
    </c:if>
    <a name="comment<c:out value="${comment.id}"/>"></a>
    <div <c:if test="${comment.id eq myComment.id}"> id="myComment"</c:if> class="<c:out value="${commentStyle}"/> songComment">
      <%@ include file="/WEB-INF/jsp/include/song_comment.jsp"%>
    </div>
    </c:forEach>
  </div>
</div>