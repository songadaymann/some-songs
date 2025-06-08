<%@ page import="com.ssj.service.song.SongSearchFactory"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Home</title>
    <%@ include file="/WEB-INF/jsp/include/inlineplayer_head.jsp"%>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <jsp:include page="/song_list" flush="true"/>

          <br> <br> <br>

          <jsp:include page="/song_list" flush="true">
            <jsp:param name="songSearchId" value="<%= SongSearchFactory.SEARCH_ID_TOP_SONGS %>"/>
            <jsp:param name="songListHidePaging" value="true"/>
            <jsp:param name="resultsPerPage" value="5"/>
          </jsp:include>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b"><%@ include file="/WEB-INF/jsp/include/song_list_right_nav.jsp"%></div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <%-- hidden link for new login modal --%>
  <a id="loginLink" href="<c:url value="/login-ajax"/>" style="display:none;"/>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/effects-1.8.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
  <script type="text/javascript"><!--
  <c:if test="${not loggedIn}">
  var loginModal = new Control.Modal('loginLink', {
    requestOptions:{method:'get'},
    fade:true,
    width:500,
    height:310,
    fadeDuration:0.25
//      onSuccess:bigCommentOpenHandler
  });
  Ajax.Responders.register({
    onCreate: function(transport) {
      if (transport.url.match(/\/user/)) {
        throw 'login';
      }
    },
    onException: function() {
      loginModal.open();
    }
  });
  </c:if>
  var ratingURL = '<c:url value="/user/rate_song"/>';
  //--></script>
  <script type="text/javascript" src="<c:url value="/js/song_list.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
  </body>
</html>