<%--<%@ page import="com.ssj.model.song.search.SongSearch"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${commentSearch.totalResults > 0}">
<%--<c:if test="${songSearch.id ne null}">--%>
<%--</c:if>--%>
<div class="songListPaging">
  <c:set var="cBaseLink" value="/comments"/>
  <c:if test="${isInclude eq 'true'}">
    <c:set var="cBaseLink" value="/comment_search"/>
    <c:choose>
      <%-- this next condition should always be true for now, as only saved searches with ids are being used in
      comment search results included in other pages (ie on the recent activity page) --%>
      <c:when test="${commentSearch.id ne 0}">
        <c:set var="linkParamName" value="id"/>
        <c:set var="linkParamValue" value="${commentSearch.id}"/>
      </c:when>
    </c:choose>
  </c:if>
  <c:if test="${isProfileComments eq 'true'}">
    <c:set var="cBaseLink" value="/profile"/>
    <c:set var="linkParamName" value="id"/>
    <c:set var="linkParamValue" value="${profileUser.id}"/>
  </c:if>
  <c:if test="${commentSearch.previousPage}">
    <c:url var="cPrevLink" value="${cBaseLink}">
      <c:if test="${isInclude eq 'true'}">
        <c:if test="${isReplySearch}">
          <c:param name="searchType" value="replies"/>
        </c:if>
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
        <c:param name="search" value="true"/>
      </c:if>
      <c:if test="${isProfileComments eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
      </c:if>
      <c:param name="start" value="${commentSearch.previousPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="previous"/>
    <c:if test="${commentSearch.previousPageStartNum eq 0}">
      <c:set var="linkWord" value="first"/>
    </c:if>
  <a class="previousPageLink" href="<c:out value="${cPrevLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${commentSearch.resultsPerPage}"/>]</a>
  </c:if>
  <span class="currentPageTest"><c:out value="${commentSearch.startResultNum + 1}"/>-<c:out value="${commentSearch.endResultNum + 1}"/> of <c:out value="${commentSearch.totalResults}"/></span>
  <c:if test="${commentSearch.nextPage}">
    <c:set var="nextResults" value="${commentSearch.resultsPerPage}"/>
    <c:if test="${commentSearch.resultsPerNextPage > 0}">
      <c:set var="nextResults" value="${commentSearch.resultsPerNextPage}"/>
    </c:if>
    <c:url var="cNextLink" value="${cBaseLink}">
      <c:if test="${isReplySearch}">
        <c:param name="searchType" value="replies"/>
      </c:if>
      <c:if test="${isInclude eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
        <c:param name="search" value="true"/>
      </c:if>
      <c:if test="${isProfileComments eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
      </c:if>
      <c:if test="${commentSearch.resultsPerNextPage > 0}">
        <c:param name="resultsPerPage" value="${commentSearch.resultsPerNextPage}"/>
      </c:if>
      <c:param name="start" value="${commentSearch.nextPageStartNum + 1}"/>
    </c:url>
    <c:set var="linkWord" value="next"/>
    <c:if test="${(commentSearch.totalResults - nextResults) le (commentSearch.nextPageStartNum)}">
      <c:set var="linkWord" value="last"/>
    </c:if>
  <a class="nextPageLink" href="<c:out value="${cNextLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${nextResults}"/>]</a>
  </c:if>
  <c:if test="${commentSearch.numPages > 2}">
    <c:url var="cJumpLink" value="${cBaseLink}">
      <c:if test="${isReplySearch}">
        <c:param name="searchType" value="replies"/>
      </c:if>
      <c:if test="${isInclude eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
        <c:param name="search" value="true"/>
      </c:if>
      <c:if test="${isProfileComments eq 'true'}">
        <c:param name="${linkParamName}" value="${linkParamValue}"/>
      </c:if>
      <c:if test="${commentSearch.resultsPerNextPage > 0}">
        <c:param name="resultsPerPage" value="${commentSearch.resultsPerNextPage}"/>
      </c:if>
    </c:url>
  <span class="clickable" title="Jump to results" onclick="jumpPages(this, '<c:out value="${cJumpLink}"/>', <c:out value="${commentSearch.totalResults}"/>, <c:out value="${commentSearch.resultsPerPage}"/>);">[jump...]</span>
  </c:if>
</div>
</c:if>