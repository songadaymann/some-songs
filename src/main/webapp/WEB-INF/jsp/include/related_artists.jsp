<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<ul class="myInfoList">
  <c:forEach var="relatedArtist" items="${relatedArtists}" varStatus="loopStatus">
    <c:set var="itemStyle" value="evenRow"/>
    <c:if test="${loopStatus.index mod 2 eq 0}">
      <c:set var="itemStyle" value="oddRow"/>
    </c:if>
    <c:url var="artistURL" value="/artists/${relatedArtist.relatedArtist.nameForUrl}-${relatedArtist.relatedArtist.id}"/>
    <li id="ra<c:out value="${relatedArtist.relatedArtist.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${artistURL}"/>"><c:out value="${relatedArtist.relatedArtist.name}"/></a></li>
  </c:forEach>
</ul>
