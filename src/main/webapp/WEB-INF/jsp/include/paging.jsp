<%--<%@ page import="com.ssj.model.song.search.SongSearch"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${songSearch.totalResults > 0}">
<%--<c:if test="${songSearch.id ne null}">--%>
<%--</c:if>--%>
<div class="songListPaging">
  <c:set var="cBaseLink" value="/songs"/>
  <c:if test="${isInclude eq 'true'}">
    <c:set var="cBaseLink" value="/search"/>
    <c:choose>
      <c:when test="${songSearch.id ne 0}">
        <c:set var="linkParamName" value="songSearchId"/>
        <c:set var="linkParamValue" value="${songSearch.id}"/>
      </c:when>
      <c:when test="${songSearch.artistName ne null}">
        <c:set var="linkParamName" value="artistName"/>
        <c:set var="linkParamValue" value="${songSearch.artistName}"/>
      </c:when>
<%--
      <c:when test="${songSearch.favoriteSong ne null}">
        <c:set var="linkParamName" value="favoriteSong"/>
        <c:set var="linkParamValue" value="true"/>
      </c:when>
--%>
      <c:when test="${songSearch.inUsersFavorites ne null}">
        <c:set var="linkParamName" value="inUsersFavorites"/>
        <c:set var="linkParamValue" value="${songSearch.inUsersFavorites}"/>
      </c:when>
    </c:choose>
  </c:if>
  <c:if test="${songSearch.previousPage}">
    <c:url var="cPrevLink" value="${cBaseLink}">
      <c:if test="${isInclude eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
        <c:param name="search" value="true"/>
      </c:if>
      <c:param name="start" value="${songSearch.previousPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="previous"/>
    <c:if test="${songSearch.previousPageStartNum eq 0}">
      <c:set var="linkWord" value="first"/>
    </c:if>
  <a class="previousPageLink" href="<c:out value="${cPrevLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${songSearch.resultsPerPage}"/>]</a>
  </c:if>
  <span class="currentPageTest"><c:out value="${songSearch.startResultNum + 1}"/>-<c:out value="${songSearch.endResultNum + 1}"/> of <c:out value="${songSearch.totalResults}"/></span>
  <c:if test="${songSearch.nextPage}">
    <c:set var="nextResults" value="${songSearch.resultsPerPage}"/>
    <c:if test="${songSearch.resultsPerNextPage > 0}">
      <c:set var="nextResults" value="${songSearch.resultsPerNextPage}"/>
    </c:if>
    <c:url var="cNextLink" value="${cBaseLink}">
      <c:if test="${isInclude eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
        <c:param name="search" value="true"/>
      </c:if>
      <c:if test="${songSearch.resultsPerNextPage > 0}">
        <c:param name="resultsPerPage" value="${songSearch.resultsPerNextPage}"/>
      </c:if>
      <c:param name="start" value="${songSearch.nextPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="next"/>
    <c:if test="${(songSearch.totalResults - nextResults) le (songSearch.nextPageStartNum)}">
      <c:set var="linkWord" value="last"/>
    </c:if>
  <a class="nextPageLink" href="<c:out value="${cNextLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${nextResults}"/>]</a>
  </c:if>
  <c:if test="${songSearch.numPages > 2}">
    <c:url var="cJumpLink" value="${cBaseLink}">
      <c:param name="${linkParamName}" value="${linkParamValue}"/>
      <c:if test="${isInclude eq 'true'}">
        <c:param name="search" value="true"/>
      </c:if>
    </c:url>
  <span class="clickable" title="Jump to results" onclick="jumpPages(this, '<c:out value="${cJumpLink}"/>', <c:out value="${songSearch.totalResults}"/>, <c:out value="${songSearch.resultsPerPage}"/>);">[jump...]</span>
  </c:if>
</div>
</c:if>