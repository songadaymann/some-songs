<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<p class="myInfoHeader"><a href="<c:url value="/user/saved_searches"/>" title="View all saved searches">Saved Searches:</a></p>
<ul class="myInfoList">
  <c:forEach var="savedSearch" items="${savedSearches}" varStatus="loopStatus">
    <c:set var="itemStyle" value="evenRow"/>
    <c:if test="${loopStatus.index mod 2 eq 0}">
      <c:set var="itemStyle" value="oddRow"/>
    </c:if>
    <c:url var="savedSearchLink" value="/search">
      <c:param name="id" value="${savedSearch.id}"/>
      <c:param name="search" value="true"/>
    </c:url>
    <li id="ss<c:out value="${savedSearch.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${savedSearchLink}"/>"><c:out value="${savedSearch.name}"/></a></li>
  </c:forEach>
  <c:if test="${numSavedSearches gt fn:length(savedSearches)}">
    <li style="border-top:1px solid #999"><a href="<c:url value="/user/saved_searches"/>" title="View all saved searches">... more</a></li>
  </c:if>
</ul>
