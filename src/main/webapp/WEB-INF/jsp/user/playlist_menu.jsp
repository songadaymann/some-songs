<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<ul id="playlistList">
  <li onclick="addToNewPlaylist()" class="row1" title="Add song to a new playlist...">New...</li>
  <c:choose>
    <c:when test="${fn:length(playlists) eq 0}">
  <li class="row0">You do not have any playlists</li>
    </c:when>
    <c:otherwise>
      <c:forEach var="myPlaylist" items="${playlists}" varStatus="loopStatus">
  <li onclick="addToPlaylist(<c:out value="${myPlaylist.id}"/>)" class="row<c:out value="${loopStatus.index % 2}"/>" title="Add song to '<c:out value="${myPlaylist.title}"/>"><c:out value="${myPlaylist.title}"/></li>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</ul>
