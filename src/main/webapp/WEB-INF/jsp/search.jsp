<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Search</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <jsp:include page="/WEB-INF/jsp/include/simple_song_search_form.jsp">
            <jsp:param name="showBorder" value="true"/>
          </jsp:include>

          <br> <br>

          <%@ include file="/WEB-INF/jsp/include/artist_search_form.jsp" %>

          <br>

          <a class="nextPageLink" href="<c:url value="/artist_search"/>?search=true">[ Show All Artists ]</a>

          <br> <br>

          <%@ include file="/WEB-INF/jsp/include/comment_search_form.jsp" %>

          <br> <br>

          <jsp:include page="/WEB-INF/jsp/include/playlist_search_form.jsp">
            <jsp:param name="showBorder" value="true"/>
          </jsp:include>

          <br> <br>

          <%@ include file="/WEB-INF/jsp/include/playlist_comment_search_form.jsp" %>

        </div></div>

      </div>

      <c:if test="${loggedIn}">
      <div id="rightNavBox" class="yui-b">
        <jsp:include page="/include/my_saved_searches" flush="true">
          <jsp:param name="resultsPerPage" value="20"/>
        </jsp:include>
      </div>
      </c:if>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>
