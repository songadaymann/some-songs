<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>
<c:set var="actionTitle" value="Post New"/>
<c:if test="${reply.id ne 0}">
  <c:set var="actionTitle" value="Edit"/>
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: <c:out value="${actionTitle}"/> Comment Reply</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms //--%>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jsr303js-codebase.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div class="originalComment">
            <div class="songCommentsHeader">
              <span style="font-weight:bold">Comment:</span>
              <c:if test="${playlist ne null}">
              <span class="smallBoldLink"><a href="playlist.do?id=<c:out value="${playlist.id}"/>#comments">[Show All Comments]</a></span>
              </c:if>
            </div>
            <div class="songListHeader">
              <div class="oddRow">
                <c:set var="originalComment" value="${reply.originalComment}"/>
                <%@ include file="/WEB-INF/jsp/include/original_playlist_comment.jsp"%>
              </div>
            </div>
          </div>

          <br>

          <c:url var="playlistURL" value="/playlist">
            <c:param name="id" value="${reply.originalComment.playlist.id}"/>
          </c:url>
          <c:url var="userURL" value="/profile">
            <c:param name="id" value="${reply.originalComment.playlist.user.id}"/>
          </c:url>
          <div style="font-weight:bold">Reply to Comment:
            re: <a href="<c:out value="${playlistURL}"/>"><c:out value="${reply.originalComment.playlist.title}"/></a>
            by <a href="<c:out value="${userURL}"/>"><c:out value="${reply.originalComment.playlist.user.displayName}"/></a>
          </div>

          <form:form action="playlist_comment_reply" name="replyForm" cssClass="cmxform" commandName="reply">
            <c:choose>
              <c:when test="${reply.id ne 0}">
                <form:hidden path="id"/>
              </c:when>
              <c:when test="${reply.originalComment.id ne 0}">
                <input type="hidden" name="comment" value="<c:out value="${reply.originalComment.id}"/>">
              </c:when>
            </c:choose>

            <div id="global_errors">
              <form:errors path="*" cssClass="errors" element="div"/>
            </div>

            <jsr303js:validate commandName="reply"/>

            <fieldset>
              <legend>Required Information</legend>
              <ol>
                <li>
                  <label for="body">Body:</label><br>
                  <c:choose>
                    <c:when test="${reply.id eq 0 and (quoteComment ne null or (quoteReplies ne null and fn:length(quoteReplies) > 0))}">
                      <textarea name="commentText" id="body" style="width:100%;height:250px;"><c:if test="${quoteComment ne null}"><blockquote><c:out value="${quoteComment.commentText}"/></blockquote>
-- <b><c:out value="${quoteComment.user.displayName}"/></b>, <fmt:formatDate value="${quoteComment.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>
</c:if><c:forEach
                        var="quoteReply" items="${quoteReplies}"><blockquote><c:out value="${quoteReply.commentText}"/></blockquote>
-- <b><c:out value="${quoteReply.user.displayName}"/></b>, <fmt:formatDate value="${quoteReply.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></c:forEach></textarea>
                    </c:when>
                    <c:otherwise>
                      <form:textarea path="commentText" id="body" cssStyle="width:100%;height:250px;"/>
                    </c:otherwise>
                  </c:choose>
                  <script>TextareaLimiter.addLimiter('body', 4000)</script>
                  <div class="allowedTags">Allowed HTML: &lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
                </li>
                <li>
                  <label for="submitBtn">&nbsp;</label>
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="<c:out value="${actionTitle}"/> Reply">
                </li>
              </ol>

            </fieldset>

          </form:form>

          <script type="text/javascript"><!--
            document.forms.replyForm.commentText.focus();
          //--></script>

          <br> <br> <br>

        </div></div>

      </div>

      <!--<div id="rightNavBox" class="yui-b">DEFAULT RIGHT NAV HERE</div>-->

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  </body>
</html>
