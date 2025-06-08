<%@ page import="com.ssj.model.playlist.PlaylistCommentReply" %>
<%@ page import="java.util.Set" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<a name="reply<c:out value="${reply.id}"/>"></a>
<table class="postTable<c:if test="${oddPost}">Odd</c:if>">
  <c:url var="replyUserLink" value="/profile">
    <c:param name="id" value="${reply.user.id}"/>
  </c:url>
  <tr class="postTopRow">
    <td align="left"><a href="<c:out value="${replyUserLink}"/>"><c:out value="${reply.user.displayName}"/></a></td>
    <td align="right">
      <c:if test="${reply.user.id eq user.id or isAdmin}">
        <c:url var="editReplyLink" value="/user/playlist_comment_reply">
          <c:param name="id" value="${reply.id}"/>
        </c:url>
      <a class="editLink" href="<c:out value="${editReplyLink}"/>">Edit</a>
      <a class="editLink" href="javascript:deleteReply(<c:out value="${reply.id}"/>)">Delete</a>
      </c:if>
      <%
        request.setAttribute("quoteReply", false);
        if (session.getAttribute("mqPlaylistCommentReplyIds") != null) {
          PlaylistCommentReply reply = (PlaylistCommentReply) pageContext.getAttribute("reply");
          // TODO replace Set with Map so I can do all this with taglibs
          if (((Set) session.getAttribute("mqPlaylistCommentReplyIds")).contains(reply.getId())) {
            request.setAttribute("quoteReply", true);
          }
        }
      %>
      <c:set var="title" value="Add post to multiquote"/>
      <c:if test="${quotePost eq true}">
        <c:set var="title" value="Remove post from multiquote"/>
      </c:if>
      <a id="multiquote<c:out value="${reply.id}"/>" title="<c:out value="${title}"/>"
         class="quotePostLink<c:if test="${quoteReply}"> quoted</c:if>"
         href="javascript:toggleMultiquote(false, '<c:out value="${reply.id}"/>')">Multiquote</a>
      <c:url var="quoteReplyLink" value="/user/playlist_comment_reply">
        <c:param name="comment" value="${reply.originalComment.id}"/>
        <c:param name="quoteId" value="${reply.id}"/>
      </c:url>
      <a class="quotePostLink" href="<c:out value="${quoteReplyLink}"/>">Quote</a>
    </td>
  </tr>
  <tr>
    <td colspan="2" class="postContent">
      <ssj:escapeSongComment content="${reply.commentText}"/>
      <c:if test="${reply.moreCommentText ne null}">
        <c:url var="wholeReplyLink" value="/whole_post">
          <c:param name="type" value="playlistCommentReply"/>
          <c:param name="id" value="${reply.id}"/>
        </c:url>
        <a class="wholePostLink" href="<c:out value="${wholeReplyLink}"/>">Read Whole Reply</a>
      </c:if>
    </td>
  </tr>
  <tr>
    <td colspan="2" class="postBottomRow">
      Posted: <fmt:formatDate value="${reply.createDate}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
      &nbsp;&nbsp;&nbsp;&nbsp;
      Edited: <fmt:formatDate value="${reply.changeDate}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
    </td>
  </tr>
</table>

<br> <br>