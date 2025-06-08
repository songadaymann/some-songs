<%@ page import="com.ssj.model.song.search.SongSearch" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%--<c:set scope="session" var="artistSearch" value="${artistSearch}"/>--%>
<c:set var="commentSearch" value="${sessionScope['commentSearch']}"/>
<c:set var="isReplySearch" value="${sessionScope['isReplySearch'] eq 'true'}"/>
<c:set var="searchLabel" value="Comment"/>
<c:if test="${isReplySearch eq 'true'}">
  <c:set var="searchLabel" value="Reply"/>
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: <c:out value="${searchLabel}"/> Search Results</title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <%@ include file="/WEB-INF/jsp/include/comment_search_list.jsp"%>

          <br> <br> <br>

          <%-- show comment search form, unless the user is viewing a canned comment search's results --%>
          <c:if test="${commentSearch.id > -1}">
            <%@ include file="/WEB-INF/jsp/include/comment_search_form.jsp" %>
          </c:if>

        </div></div>

      </div>

      <%-- show the song list right nav if the user is viewing a canned comment search's results --%>
      <c:if test="${commentSearch.id < 0}">
      <div id="rightNavBox" class="yui-b"><%@ include file="/WEB-INF/jsp/include/song_list_right_nav.jsp"%></div>
      </c:if>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>