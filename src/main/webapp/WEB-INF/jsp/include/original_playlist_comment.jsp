<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
  <ssj:escapeSongComment content="${originalComment.commentText}"/>
  <c:if test="${originalComment.moreCommentText ne null}">
    ..................
    <div style="width:100%;border-top:1px solid #bbb;">
      <c:url var="wholeReplyLink" value="/whole_post">
        <c:param name="type" value="playlistComment"/>
        <c:param name="id" value="${originalComment.id}"/>
      </c:url>
      <br>
      <a class="wholePostLink" href="<c:out value="${wholeReplyLink}"/>">Read Whole Post</a>
    </div>
  </c:if>
  <br>
  <span>---
    <c:url var="userLink" value="/profile">
      <c:param name="id" value="${originalComment.user.id}"/>
    </c:url>
    <a href="<c:out value="${userLink}"/>"><c:out value="${originalComment.user.displayName}"/></a>
    <span class="medium"><fmt:formatDate value="${originalComment.createDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></span>
  </span>
