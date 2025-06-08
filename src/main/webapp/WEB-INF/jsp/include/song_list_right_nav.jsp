<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%@ page import="com.ssj.service.song.SongSearchFactory" %>
<%@ page import="com.ssj.model.song.search.SongSearch" %>
<b>Searches</b><br>
<ul>
  <c:choose>
  <c:when test="${loggedIn}">
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_NOT_YET_RATED_BY_USER %>">I Haven't Rated</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_FAVORITE_SONGS %>">Favorite Songs</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_BY_FAVORITE_ARTISTS %>">From Favorite Artists</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_PREFERRED_USERS_PICKS %>">Preferred Users' Picks</a></li>
  </c:when>
  <c:otherwise>
    <li><a href="<c:url value="/login"/>">I Haven't Rated</a></li>
    <li><a href="<c:url value="/login"/>">Favorite Songs</a></li>
    <li><a href="<c:url value="/login"/>">From Favorite Artists</a></li>
    <li><a href="<c:url value="/login"/>">Preferred Users' Picks</a></li>
  </c:otherwise>
  </c:choose>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_TOP_SONGS %>">Top Songs</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_NEWEST_SONGS %>">Newest Songs</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_PAST_DAY_TOPS %>">Past 24 Hours' Tops</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_PAST_WEEK_TOPS %>">Past Week's Tops</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_PAST_MONTH_TOPS %>">Past Month's Tops</a></li>
  <li><a href="<c:url value="/search"/>?search=true&id=<%= SongSearchFactory.SEARCH_ID_LOST_SONGS %>">Lost Songs (Few Votes)</a></li>
  <!--<li><a href="???">Comments</a></li>-->
</ul>
<hr>
<%--
<ul>
  <li><a href="<c:url value="/"/>">Front Page</a></li>
  <li><a href="???">10</a></li>
</ul>
<br>
--%>
<br>
<br>
<jsp:include page="/WEB-INF/jsp/include/simple_song_search_form.jsp"/>
<br>
<br>
<b>Go To:</b><br>
<a href="<c:url value="/random_song"/>">Random Song</a>
<br>
<br>
<b>Songs Per Page:</b><br>
<form name="rightNavSongPerPageForm" action="<c:url value="/songs"/>">
  <c:set var="resultsPerPageValue" value="20"/>
  <c:if test="${songSearch ne null}">
    <c:set var="resultsPerPageValue" value="${songSearch.resultsPerPage}"/>
  </c:if>
  <input type="text" size="3" maxlength="3" name="resultsPerPage" value="<c:out value="${resultsPerPageValue}"/>">
  <input type="submit" name="submitBtn" value="Change">
</form>
<br>
<br>
