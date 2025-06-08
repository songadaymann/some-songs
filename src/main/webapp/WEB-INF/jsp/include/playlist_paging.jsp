<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${playlistSearch.totalResults > 0}">
<%--<c:if test="${playlistSearch.id ne null}">--%>
<%--</c:if>--%>
<div class="songListPaging">
  <c:set var="cBaseLink" value="/playlists"/>
  <c:if test="${isInclude eq 'true'}">
    <c:set var="cBaseLink" value="/playlist_search"/>
    <c:choose>
      <c:when test="${playlistSearch.id ne 0}">
        <c:set var="linkParamName" value="playlistSearchId"/>
        <c:set var="linkParamValue" value="${playlistSearch.id}"/>
      </c:when>
      <c:when test="${playlistSearch.userDisplayName ne null}">
        <c:set var="linkParamName" value="userDisplayName"/>
        <c:set var="linkParamValue" value="${playlistSearch.userDisplayName}"/>
      </c:when>
<%--
      <c:when test="${playlistSearch.favoritePlaylist ne null}">
        <c:set var="linkParamName" value="favoritePlaylist"/>
        <c:set var="linkParamValue" value="true"/>
      </c:when>
--%>
      <c:when test="${playlistSearch.inUsersFavorites ne null}">
        <c:set var="linkParamName" value="inUsersFavorites"/>
        <c:set var="linkParamValue" value="${playlistSearch.inUsersFavorites}"/>
      </c:when>
    </c:choose>
  </c:if>
  <c:if test="${playlistSearch.previousPage}">
    <c:url var="cPrevLink" value="${cBaseLink}">
      <c:if test="${isInclude eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
        <c:param name="search" value="true"/>
      </c:if>
      <c:param name="start" value="${playlistSearch.previousPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="previous"/>
    <c:if test="${playlistSearch.previousPageStartNum eq 0}">
      <c:set var="linkWord" value="first"/>
    </c:if>
  <a class="previousPageLink" href="<c:out value="${cPrevLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${playlistSearch.resultsPerPage}"/>]</a>
  </c:if>
  <span class="currentPageTest"><c:out value="${playlistSearch.startResultNum + 1}"/>-<c:out value="${playlistSearch.endResultNum + 1}"/> of <c:out value="${playlistSearch.totalResults}"/></span>
  <c:if test="${playlistSearch.nextPage}">
    <c:set var="nextResults" value="${playlistSearch.resultsPerPage}"/>
    <c:if test="${playlistSearch.resultsPerNextPage > 0}">
      <c:set var="nextResults" value="${playlistSearch.resultsPerNextPage}"/>
    </c:if>
    <c:url var="cNextLink" value="${cBaseLink}">
      <c:if test="${isInclude eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
        <c:param name="search" value="true"/>
      </c:if>
      <c:if test="${playlistSearch.resultsPerNextPage > 0}">
        <c:param name="resultsPerPage" value="${playlistSearch.resultsPerNextPage}"/>
      </c:if>
      <c:param name="start" value="${playlistSearch.nextPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="next"/>
    <c:if test="${(playlistSearch.totalResults - nextResults) le (playlistSearch.nextPageStartNum)}">
      <c:set var="linkWord" value="last"/>
    </c:if>
  <a class="nextPageLink" href="<c:out value="${cNextLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${nextResults}"/>]</a>
  </c:if>
  <c:if test="${playlistSearch.numPages > 2}">
  <span class="clickable" title="Jump to results" onclick="jumpPages(this, '<c:url value="${cBaseLink}"/>', <c:out value="${playlistSearch.totalResults}"/>, <c:out value="${playlistSearch.resultsPerPage}"/>);">[jump...]</span>
  </c:if>
</div>
</c:if>