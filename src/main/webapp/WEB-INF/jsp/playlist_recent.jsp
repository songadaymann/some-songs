<%@ page import="com.ssj.model.playlist.search.PlaylistCommentSearchFactory" %>
<%@ page import="com.ssj.model.playlist.search.PlaylistCommentReplySearchFactory" %>
<%@ page import="com.ssj.model.playlist.search.PlaylistSearchFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: Recent Playlist Activity</title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <%@ include file="/WEB-INF/jsp/include/inlineplayer_head.jsp" %>
    <c:if test="${loggedIn}">
      <script type="text/javascript"><!--
      var ratingURL = '<c:url value="/user/rate_playlist"/>';
      //--></script>
      <script type="text/javascript" src="<c:url value="/js/playlist_list.js"/>"></script>
    </c:if>
    <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <jsp:include page="/playlist_comment_list" flush="true">
            <jsp:param name="commentSearchId" value="<%= PlaylistCommentSearchFactory.SEARCH_ID_RECENT %>"/>
          </jsp:include>

          <br> <br>

          <jsp:include page="/playlist_comment_list" flush="true">
            <jsp:param name="commentSearchId" value="<%= PlaylistCommentReplySearchFactory.SEARCH_ID_RECENT %>"/>
            <jsp:param name="searchType" value="replies"/>
          </jsp:include>

          <br> <br>

          <jsp:include page="/playlist_list" flush="true">
            <jsp:param name="playlistSearchId" value="<%= PlaylistSearchFactory.SEARCH_ID_RECENTLY_RATED %>"/>
          </jsp:include>

          <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <a href="<c:url value="/recent"/>"><b>Recent Song Activity</b></a><br>
        <b>Recent Playlist Activity</b>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>