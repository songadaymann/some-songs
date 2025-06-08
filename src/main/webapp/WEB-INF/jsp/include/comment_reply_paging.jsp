<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<c:if test="${replySearch.totalResults > 0}">
<div class="songListHeader">
  <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
  <div class="songListPaging">
    <c:if test="${replySearch.numPages > 1}">
      Page:
      <c:forEach var="pageStartNum" items="${replySearch.pageStartNums}" varStatus="loopStatus">
        <c:url var="pageLink" value="/songs/${song.titleForUrl}-${song.id}">
          <c:param name="comment" value="${originalComment.id}"/>
          <c:param name="start" value="${pageStartNum + 1}"/>
        </c:url>
        <a href="<c:out value="${pageLink}"/>"<c:if test="${replySearch.startResultNum eq pageStartNum}"> style="border: 1px solid black"</c:if>><c:out value="${loopStatus.index + 1}"/></a>
      </c:forEach>
    </c:if>
  </div>
  <div class="songListName">
    Replies to Comment
  </div>
  <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
</div>

<br>
</c:if>

<c:url var="replyThreadLink" value="/user/comment_reply">
  <c:param name="comment" value="${originalComment.id}"/>
</c:url>
<a href="<c:out value="${replyThreadLink}"/>" class="postLink">Reply to Comment</a>
