<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<ul class="myInfoList">
  <c:forEach var="myPlaylist" items="${myPlaylists}" varStatus="loopStatus">
    <c:set var="itemStyle" value="evenRow"/>
    <c:if test="${loopStatus.index mod 2 eq 0}">
      <c:set var="itemStyle" value="oddRow"/>
    </c:if>
    <c:url var="playlistLink" value="/playlist">
      <c:param name="id" value="${myPlaylist.id}"/>
    </c:url>
    <li id="pl<c:out value="${myPlaylist.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${playlistLink}"/>"><c:out value="${myPlaylist.title}"/></a></li>
  </c:forEach>
  <c:if test="${numPlaylists gt 8}">
    <li><a href="playlists" title="View all playlists">... more</a></li>
  </c:if>
</ul>
