<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<ul class="myInfoListNoDelete">
  <c:forEach var="myArtist" items="${myArtists}" varStatus="loopStatus">
    <c:set var="itemStyle" value="evenRow"/>
    <c:if test="${loopStatus.index mod 2 eq 0}">
      <c:set var="itemStyle" value="oddRow"/>
    </c:if>
    <c:url var="artistURL" value="/artists/${myArtist.nameForUrl}-${myArtist.id}"/>
    <li id="ma<c:out value="${myArtist.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${artistURL}"/>"><c:out value="${myArtist.name}"/></a></li>
  </c:forEach>
  <c:url var="myArtistsURL" value="/artist_search">
    <c:param name="search" value="true"/>
    <c:param name="user.id" value="${userId}"/>
  </c:url>
  <c:if test="${myArtistsSearch.totalResults gt fn:length(myArtists)}">
    <li><a href="<c:out value="${myArtistsURL}"/>">Show all <c:out value="${myArtistsSearch.totalResults}"/></a></li>
  </c:if>
</ul>
