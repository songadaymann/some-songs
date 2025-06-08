<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:if test="${isProfileComments eq 'true'}">
  <a href="<c:url value="/songs/${comment.song.titleForUrl}-${comment.song.id}"/>" title="View '${comment.song.title}' Info/Comments">${comment.song.title}</a>
  by
  <a href="<c:url value="/artists/${comment.song.artist.nameForUrl}-${comment.song.artist.id}"/>" title="View ${comment.song.artist.name} Artist Info">${comment.song.artist.name}</a>
  <br>
</c:if>
  <ssj:escapeSongComment content="${comment.commentText}"/>
  <c:if test="${comment.moreCommentText ne null}">
    ..................
    <div style="width:100%;border-top:1px solid #bbb;">
      <c:url var="wholeReplyLink" value="/whole_post">
        <c:param name="type" value="songComment"/>
        <c:param name="id" value="${comment.id}"/>
      </c:url>
      <br>
      <a class="wholePostLink" href="<c:out value="${wholeReplyLink}"/>">Read Whole Post</a>
    </div>
  </c:if>
  <br>
  <c:url var="commentURL" value="/songs/${comment.song.titleForUrl}-${comment.song.id}">
    <c:param name="comment" value="${comment.id}"/>
  </c:url>
  <span style="float:right"><a class="medium" href="<c:out value="${commentURL}"/>">[
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
    <c:url var="songLink" value="/songs/${comment.song.titleForUrl}-${comment.song.id}"/>
    <a href="<c:out value="${songLink}"/>"><c:out value="${comment.song.title}"/></a>
    by
    <c:url var="artistLink" value="/artists/${comment.song.artist.nameForUrl}-${comment.song.artist.id}"/>
    <a href="<c:out value="${artistLink}"/>"><c:out value="${comment.song.artist.name}"/></a>
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