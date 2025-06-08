<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Edit Playlist Comment</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms //--%>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:url var="userURL" value="/profile">
            <c:param name="id" value="${playlistComment.user.id}"/>
          </c:url>
          <c:url var="playlistURL" value="/playlist">
            <c:param name="id" value="${playlistComment.playlist.id}"/>
          </c:url>
          <c:url var="playlistUserURL" value="/profile">
            <c:param name="id" value="${playlistComment.playlist.user.id}"/>
          </c:url>

          <div style="font-weight:bold">Edit Comment By <a href="<c:out value="${userURL}"/>"><c:out value="${playlistComment.user.displayName}"/></a>
            re: <a href="<c:out value="${playlistURL}"/>"><c:out value="${playlistComment.playlist.title}"/></a>
            by <a href="<c:out value="${playlistUserURL}"/>"><c:out value="${playlistComment.playlist.user.displayName}"/></a>
          </div>

          <br>

          <form:form action="edit_playlist_comment" name="commentForm" cssClass="cmxform" commandName="playlistComment">
            <form:hidden path="id"/>

            <fieldset>
              <legend>Required Information</legend>
              <ol>
                <li>
                  <label for="body">Body:</label><br>
                    <form:textarea path="commentText" id="body" cssStyle="width:100%;height:250px;"/>
                  <script type="text/javascript">TextareaLimiter.addLimiter('body', 4000)</script>
                  <div class="allowedTags">Allowed HTML: &lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
                </li>
                <li>
                  <label for="submitBtn">&nbsp;</label>
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Edit">
                </li>
              </ol>

            </fieldset>

          </form:form>

          <script type="text/javascript"><!--
            document.forms.commentForm.commentText.focus();
          //--></script>

          <br> <br> <br>

        </div></div>

      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>
