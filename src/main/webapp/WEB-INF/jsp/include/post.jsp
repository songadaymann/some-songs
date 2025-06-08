<%@ page import="java.util.Set" %>
<%@ page import="com.ssj.model.messageboard.MessageBoardPost" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%-- wrap in a div with a unique id? --%>
<%--<div id="postContent<c:out value="${post.id}"/>">--%>
<a name="post<c:out value="${post.id}"/>"></a>
<table class="postTable<c:if test="${oddPost}">Odd</c:if>">
  <c:url var="postUserLink" value="/profile">
    <c:param name="id" value="${post.user.id}"/>
  </c:url>
  <tr class="postTopRow">
    <td align="left"><a href="<c:out value="${postUserLink}"/>"><c:out value="${post.user.displayName}"/></a></td>
    <td align="right">
      <c:if test="${loggedIn}">
      <c:if test="${!thread.locked and (post.user.id eq user.id or isAdmin)}">
        <c:choose>
          <c:when test="${post.originalPost eq null}">
            <c:url var="editPostLink" value="/user/edit_thread">
              <c:param name="id" value="${thread.id}"/>
            </c:url>
            <c:set var="deleteJavaScript" value="deleteThread"/>
          </c:when>
          <c:otherwise>
            <c:set var="editPostLink" value="javascript:editPost(${post.id})"/>
            <c:set var="deleteJavaScript" value="deletePost"/>
          </c:otherwise>
        </c:choose>
      <a class="editLink" href="<c:out value="${editPostLink}"/>">Edit</a>
      <a class="editLink" href="javascript:<c:out value="${deleteJavaScript}"/>(<c:out value="${post.id}"/>)">Delete</a>
      </c:if>
      <%
        request.setAttribute("quotePost", false);
        if (session.getAttribute("multiquotePostIds") != null) {
          MessageBoardPost mbp = (MessageBoardPost) pageContext.getAttribute("post");
          // TODO replace Set with Map so I can do all this with taglibs
          if (((Set) session.getAttribute("multiquotePostIds")).contains(mbp.getId())) {
            request.setAttribute("quotePost", true);
          }
        }
      %>
      <c:set var="title" value="Add post to multiquote"/>
      <c:if test="${quotePost eq true}">
        <c:set var="title" value="Remove post from multiquote"/>
      </c:if>
      <a id="multiquote<c:out value="${post.id}"/>" title="<c:out value="${title}"/>"
         class="quotePostLink<c:if test="${quotePost eq true}"> quoted</c:if>"
         href="javascript:toggleMultiquote(<c:out value="${post.id}"/>)">Multiquote</a>
      <c:url var="quotePostLink" value="/user/reply">
        <c:param name="threadId" value="${thread.id}"/>
        <c:param name="quoteId" value="${post.id}"/>
      </c:url>
      <a class="quotePostLink" href="javascript:postToThread(<c:out value="${post.id}"/>)">Quote</a>
      </c:if>
    </td>
  </tr>
  <tr>
    <td colspan="2" class="postContent"><div id="post<c:out value="${post.id}"/>">
      <ssj:escapeMessageBoardPost content="${post.content}"/><c:if
        test="${post.moreContent ne null}"><c:url
          var="wholePostLink" value="/whole_post"><c:param
          name="type" value="post"/><c:param
          name="id" value="${post.id}"/></c:url>...
      <br><br><a class="wholePostLink" href="<c:out value="${wholePostLink}"/>">Read Whole Post</a>
      </c:if>
    </div></td>
  </tr>
  <tr>
    <td colspan="2" class="postBottomRow">
      Posted: <fmt:formatDate value="${post.createDate}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
      &nbsp;&nbsp;&nbsp;&nbsp;
      Edited: <fmt:formatDate value="${post.changeDate}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
    </td>
  </tr>
</table>

<br> <br>
<%--</div--%>