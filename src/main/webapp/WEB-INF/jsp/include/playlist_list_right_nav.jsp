<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%@ page import="com.ssj.model.playlist.search.PlaylistSearchFactory" %>
<%@ page import="com.ssj.model.playlist.search.PlaylistSearch" %>
<a href="<c:url value="/user/playlists"/>"><b>My Playlists</b></a><br>
<br>
<b>Playlist Searches</b><br>
<ul>
  <c:choose>
  <c:when test="${loggedIn}">
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_NOT_YET_RATED_BY_USER %>">I Haven't Rated</a></li>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_BY_PREFERRED_USERS %>">From Preferred Users</a></li>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_PREFERRED_USERS_PICKS %>">Preferred Users' Picks</a></li>
  </c:when>
  <c:otherwise>
    <li><a href="<c:url value="/login"/>">I Haven't Rated</a></li>
    <li><a href="<c:url value="/login"/>">From Preferred Users</a></li>
    <li><a href="<c:url value="/login"/>">Preferred Users' Picks</a></li>
  </c:otherwise>
  </c:choose>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_TOP_PLAYLISTS %>">Top Playlists</a></li>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_NEWEST %>">Newest Playlists</a></li>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_PAST_DAY_TOPS %>">Past 24 Hours' Tops</a></li>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_PAST_WEEK_TOPS %>">Past Week's Tops</a></li>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_PAST_MONTH_TOPS %>">Past Month's Tops</a></li>
  <li><a href="<c:url value="/playlist_search"/>?search=true&id=<%= PlaylistSearchFactory.SEARCH_ID_LOST_PLAYLISTS %>">Lost Playlist (Few Votes)</a></li>
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
<%@ include file="/WEB-INF/jsp/include/playlist_search_form.jsp" %>
<br>
<br>
<b>Go To:</b><br>
<a href="<c:url value="/random_playlist"/>">Random Playlist</a>
<br>
<br>
<b>Playlists Per Page:</b><br>
<form name="rightNavPlaylistsPerPageForm" action="<c:url value="/playlists"/>">
  <c:set var="resultsPerPageValue" value="20"/>
  <c:if test="${playlistSearch ne null}">
    <c:set var="resultsPerPageValue" value="${playlistSearch.resultsPerPage}"/>
  </c:if>
  <input type="text" size="3" maxlength="3" name="resultsPerPage" value="<c:out value="${resultsPerPageValue}"/>">
  <input type="submit" name="submitBtn" value="Change">
</form>
<br>
<br>
