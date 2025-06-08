<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
  <ssj:escapeSongComment content="${comment.commentText}"/>
  <c:if test="${comment.moreCommentText ne null}">
    ..................
    <div style="width:100%;border-top:1px solid #bbb;">
      <c:url var="wholeReplyLink" value="/whole_post">
        <c:param name="type" value="playlistComment"/>
        <c:param name="id" value="${comment.id}"/>
      </c:url>
      <br>
      <a class="wholePostLink" href="<c:out value="${wholeReplyLink}"/>">Read Whole Post</a>
    </div>
  </c:if>
  <br>
  <c:url var="commentURL" value="/playlist">
    <c:param name="id" value="${comment.playlist.id}"/>
    <c:param name="comment" value="${comment.id}"/>
  </c:url>
  <span style="float:right"><a class="medium" href="<c:out value="${commentURL}"/>#comments">[
    <c:out value="${comment.numReplies}"/>
    <c:choose>
      <c:when test="${comment.numReplies eq 1}">
        reply
      </c:when>
      <c:otherwise>
        replies
      </c:otherwise>
    </c:choose>
  ]</a></span>
  <c:choose>
    <c:when test="${comments.user ne null}">
  <span>&nbsp;
    Re:
    <c:url var="playlistLink" value="/playlist">
      <c:param name="id" value="${comment.playlist.id}"/>
    </c:url>
    <a href="<c:out value="${playlistLink}"/>"><c:out value="${comment.playlist.title}"/></a>
    by
    <c:url var="playlistUserLink" value="/profile">
      <c:param name="id" value="${comment.playlist.user.id}"/>
    </c:url>
    <a href="<c:out value="${playlistUserLink}"/>"><c:out value="${comment.playlist.user.displayNme}"/></a>
    <span class="medium"><fmt:formatDate value="${comment.createDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></span>
  </span>
    </c:when>
    <c:otherwise>
  <span>---
    <c:url var="userLink" value="/profile">
      <c:param name="id" value="${comment.user.id}"/>
    </c:url>
    <a href="<c:out value="${userLink}"/>"><c:out value="${comment.user.displayName}"/></a>
    <span class="medium"><fmt:formatDate value="${comment.createDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></span>
  </span>
    </c:otherwise>
  </c:choose>