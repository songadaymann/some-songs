<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:set var="foundPlaylists" value="${playlistSearch.totalResults > 0}"/>
<div class="songListHeader">
  <c:if test="${param['hidePaging'] ne 'true'}">
    <%@ include file="/WEB-INF/jsp/include/playlist_paging.jsp"%>
  </c:if> 
    <div class="songListName">
      <span id="searchNameEl"><c:out value="${playlistSearch.name}"/></span>
      <%--<c:if test="${loggedIn and playlistSearch.id == 0}"><a id="saveSearchLink"
        href="<c:url value="/include/save_search.html"/>" style="font-weight:normal;">[save]</a><span
          id="searchSavedMessage" style="color:#050;display:none">&nbsp;[saved]</span>
        <script type="text/javascript">
        var searchModal = new Control.Modal('saveSearchLink', {
          fade:true,
          fadeDuration:0.25,
  //        onSuccess:saveSearchOpenHandler,
          width:300
        });
        var saveSearchURL = '<c:url value="/user/save_search"/>';
      </script></c:if>--%>
    </div>
</div>
<table class="songList">
  <tr class="songListFooterRow">
    <td>&nbsp;</td>
    <td>Playlist</td>
    <td>Songs</td>
    <td>User</td>
    <td>Rating</td>
    <c:if test="${loggedIn}">
    <td>Your Rating</td>
    </c:if>
  </tr>
  <c:choose>
  <c:when test="${!foundPlaylists}">
    <c:set var="numColumns" value="5"/>
    <c:if test="${loggedIn}">
      <c:set var="numColumns" value="6"/>
    </c:if>
  <tr class="songListOddRow">
    <td colspan="<c:out value="${numColumns}"/>">No playlists match the search criteria.</td>
  </tr>
  </c:when>
  <c:otherwise>
    <c:forEach var="playlist" items="${playlistSearchResults}" varStatus="loopStatus">
      <c:set var="rating" value="${ratingsMap[playlist.id]}"/>
  <tr class="<c:choose><c:when test="${loopStatus.index % 2 == 0}">songListOddRow</c:when><c:otherwise>songListEvenRow</c:otherwise></c:choose>">
    <td><c:out value="${playlistSearch.startResultNum + loopStatus.index + 1}"/><c:if test="${(playlistSearch.startResultNum + loopStatus.index + 1) < 10}">&nbsp</c:if></td>
    <c:url var="viewPlaylistLink" value="/playlist">
      <c:param name="id" value="${playlist.id}"/>
    </c:url>
    <str:truncateNicely var="truncTitle" lower="32" upper="40" appendToEnd="..."><c:out value="${playlist.title}"/></str:truncateNicely>
    <td><a id="title<c:out value="${loopStatus.index}"/>" href="<c:out value="${viewPlaylistLink}"/>" title="Listen to '<c:out value="${playlist.title}"/>'"><c:out value="${truncTitle}"/></a></td>
    <td><c:out value="${playlist.numItems}"/></td>
    <c:url var="viewUserLink" value="/profile">
      <c:param name="id" value="${playlist.user.id}"/>
    </c:url>
    <td><a id="user<c:out value="${loopStatus.index}"/>" href="<c:out value="${viewUserLink}"/>" title="View <c:out value="${playlist.user.displayName}"/>'s Profile"><c:out value="${playlist.user.displayName}"/></a></td>
    <td><span id="rating<c:out value="${playlist.id}"/>"<c:if test="${playlist.showRating ne true}">
      style="display:none"</c:if>><c:out value="${playlist.ratingString}"/></span><span
      id="needsRatings<c:out value="${playlist.id}"/>" class="needsRatings"<c:if test="${playlist.showRating}">
      style="display:none"</c:if>>[ <b id="numRatingsNeeded<c:out value="${playlist.id}"/>"><c:out value="${playlist.numRatingsNeeded}"/></b> ]</span></td>
    <c:if test="${loggedIn}">
      <td><div class="ratingDiv"><a
          id="goodRatingLink<c:out value="${playlist.id}"/>" onclick="setRating('good', this);return false;" href="<c:url value="/user/rate_playlist"/>" class="ratingLink good<c:if test="${rating ne null and rating.rating eq 10}"> selectedRating</c:if>">good</a><a
          id="okayRatingLink<c:out value="${playlist.id}"/>" onclick="setRating('okay', this);return false;" href="<c:url value="/user/rate_playlist"/>" class="ratingLink okay<c:if test="${rating ne null and rating.rating eq 5}"> selectedRating</c:if>">okay</a><a
          id="badRatingLink<c:out value="${playlist.id}"/>" onclick="setRating('bad', this);return false;" href="<c:url value="/user/rate_playlist"/>" class="ratingLink bad<c:if test="${rating ne null and rating.rating eq 0}"> selectedRating</c:if>">bad</a><a
          id="noneRatingLink<c:out value="${playlist.id}"/>" onclick="setRating('none', this);return false;" href="<c:url value="/user/rate_playlist"/>" class="ratingLink none<c:if test="${rating eq null}"> selectedRating</c:if>">x</a></div></td>
    </c:if>
  </tr>
    </c:forEach>
  </c:otherwise>
  </c:choose>
</table>
