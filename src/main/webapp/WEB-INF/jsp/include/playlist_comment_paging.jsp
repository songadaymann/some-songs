<%--<%@ page import="com.ssj.model.song.search.SongSearch"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${commentSearch.totalResults > 0}">
<%--<c:if test="${songSearch.id ne null}">--%>
<%--</c:if>--%>
<div class="songListPaging">
  <c:set var="cBaseLink" value="/playlist"/>
  <c:set var="linkParamName" value="id"/>
  <c:set var="linkParamValue" value="${playlist.id}"/>
  <c:if test="${commentSearch.previousPage}">
    <c:url var="cPrevLink" value="${cBaseLink}">
      <c:param name="${linkParamName}" value="${linkParamValue}"/>
      <c:param name="start" value="${commentSearch.previousPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="previous"/>
    <c:if test="${commentSearch.previousPageStartNum eq 0}">
      <c:set var="linkWord" value="first"/>
    </c:if>
  <a class="previousPageLink" href="<c:out value="${cPrevLink}"/>#comments">[<c:out value="${linkWord}"/> <c:out value="${commentSearch.resultsPerPage}"/>]</a>
  </c:if>
  <span class="currentPageTest"><c:out value="${commentSearch.startResultNum + 1}"/>-<c:out value="${commentSearch.endResultNum + 1}"/> of <c:out value="${commentSearch.totalResults}"/></span>
  <c:if test="${commentSearch.nextPage}">
    <c:set var="nextResults" value="${commentSearch.resultsPerPage}"/>
    <c:if test="${commentSearch.resultsPerNextPage > 0}">
      <c:set var="nextResults" value="${commentSearch.resultsPerNextPage}"/>
    </c:if>
    <c:url var="cNextLink" value="${cBaseLink}">
      <c:param name="${linkParamName}" value="${linkParamValue}"/>
      <c:param name="start" value="${commentSearch.nextPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="next"/>
    <c:if test="${(commentSearch.totalResults - nextResults) le (commentSearch.nextPageStartNum)}">
      <c:set var="linkWord" value="last"/>
    </c:if>
  <a class="nextPageLink" href="<c:out value="${cNextLink}"/>#comments">[<c:out value="${linkWord}"/> <c:out value="${nextResults}"/>]</a>
  </c:if>
  <c:if test="${commentSearch.numPages > 2}">
  <span class="clickable" title="Jump to results" onclick="jumpPages(this, '<c:url value="${cBaseLink}"/>?id=<c:out value="${playlist.id}"/>', <c:out value="${commentSearch.totalResults}"/>, <c:out value="${commentSearch.resultsPerPage}"/>);">[jump...]</span>
  </c:if>
</div>
</c:if>