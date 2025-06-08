<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:set scope="session" var="playlistSearch" value="${playlistSearch}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: <c:out value="${playlistSearch.name}"/>, <c:out value="${playlistSearch.startResultNum + 1}"/>-<c:out value="${playlistSearch.startResultNum + fn:length(playlistSearchResults)}"/></title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
    <c:if test="${loggedIn}">
    <script type="text/javascript"><!--
    var ratingURL = '<c:url value="/user/rate_playlist"/>';
    //--></script>
    <script type="text/javascript" src="<c:url value="/js/playlist_list.js"/>"></script>
    <%--<script type="text/javascript" src="<c:url value="/js/effects-1.8.1.js"/>"></script>--%>
    <%--<script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>--%>
    </c:if>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <%@ include file="/WEB-INF/jsp/include/playlist_list.jsp"%>
          <%--<jsp:include page="song_list"/>--%>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b"><%@ include file="/WEB-INF/jsp/include/playlist_list_right_nav.jsp"%></div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>