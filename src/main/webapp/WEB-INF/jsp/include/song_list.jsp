<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:set var="foundSongs" value="${songSearch.totalResults > 0}"/>
<div class="songListHeader">
  <c:if test="${param['hidePaging'] ne 'true'}">
    <%@ include file="/WEB-INF/jsp/include/paging.jsp"%>
  </c:if> 
    <div class="songListName">
      <span id="searchNameEl"><c:out value="${songSearch.name}"/></span>
      <c:if test="${loggedIn and songSearch.id == 0}"><a class="saveSearchLink"
          id="saveSearchLink<c:out value="${saveLinkNum}"/>" href="<c:url value="/html/save_search.html"/>" style="font-weight:normal;">[save]</a><span
          id="searchSavedMessage<c:out value="${saveLinkNum}"/>" style="color:#050;display:none">&nbsp;[saved]</span>
        <script type="text/javascript">
<%--
        var searchModal<c:out value="${saveLinkNum}"/> = new Control.Modal('saveSearchLink<c:out value="${saveLinkNum}"/>', {
          fade:true,
          fadeDuration:0.25,
  //        onSuccess:saveSearchOpenHandler,
          width:300
        });
--%>
        var saveSearchURL = '<c:url value="/user/save_search"/>';
      </script></c:if>
    </div>
</div>
<table class="songList">
  <tr class="songListFooterRow">
    <td colspan="2">&nbsp;<c:if test="${foundSongs}">
        <c:url var="m3uURL" value="/somesongs_playlist.m3u">
          <c:forEach var="song" items="${songSearchResults}" varStatus="loopStatus">
            <c:param name="songId" value="${song.id}"/>
          </c:forEach>
        </c:url>
        <a href="<c:out value="${m3uURL}"/>" title="Click to download a .m3u playlist of these songs" style="font-size:75%">Download .m3u</a>
      </c:if></td>
    <td>Song</td>
    <td>Artist</td>
    <td>Rating</td>
    <td>Your Rating</td>
  </tr>
  <c:choose>
  <c:when test="${!foundSongs}">
  <tr class="songListOddRow">
    <td colspan="6">No songs match the search criteria.</td>
  </tr>
  </c:when>
  <c:otherwise>
    <c:forEach var="song" items="${songSearchResults}" varStatus="loopStatus">
      <c:set var="rating" value="${ratingsMap[song.id]}"/>
  <tr class="<c:choose><c:when test="${loopStatus.index % 2 == 0}">songListOddRow</c:when><c:otherwise>songListEvenRow</c:otherwise></c:choose>">
    <td><c:out value="${songSearch.startResultNum + loopStatus.index + 1}"/><c:if test="${(songSearch.startResultNum + loopStatus.index + 1) < 10}">&nbsp</c:if></td>
    <td valign="middle"><a
        id="playBtn<c:out value="${song.id}"/>" class="playable" href="<c:url value="/songs/stream/${song.titleForUrl}-${song.id}.mp3"/>" title="Play/Pause '<c:out value="${song.title}"/>'"><img
        src="<c:url value="/img/play-pause-icon.png"/>" width="16" height="16" alt="Play/Pause" style="vertical-align:middle;margin-right:4px;"></a><tags:download_links song="${song}" loopStatusIndex="${loopStatus.index}"/></td>
    <c:url var="viewSongLink" value="/songs/${song.titleForUrl}-${song.id}"/>
    <td><a href="<c:out value="${viewSongLink}"/>" title="View '<c:out value="${song.title}"/>' Info/Comments"><str:truncateNicely lower="32" upper="45" appendToEnd="..."><c:out value="${song.title}"/></str:truncateNicely></a></td>
    <c:url var="viewArtistLink" value="/artists/${song.artist.nameForUrl}-${song.artist.id}"/>
    <td><a href="<c:out value="${viewArtistLink}"/>" title="View <c:out value="${song.artist.name}"/> Artist Info"><str:truncateNicely lower="24" upper="35" appendToEnd="..."><c:out value="${song.artist.name}"/></str:truncateNicely></a></td>
    <td><span id="rating<c:out value="${song.id}"/>"<c:if test="${song.showRating ne true}">
      style="display:none"</c:if>><c:out value="${song.ratingString}"/></span><span
        title="<c:out value="${song.numRatingsNeeded}"/> more rating(s) needed to show average rating"
      id="needsRatings<c:out value="${song.id}"/>" class="needsRatings"<c:if test="${song.showRating}">
      style="display:none"</c:if>>[ <b id="numRatingsNeeded<c:out value="${song.id}"/>"><c:out value="${song.numRatingsNeeded}"/></b> ]</span></td>
      <td><div class="ratingDiv"><a
          id="goodRatingLink<c:out value="${song.id}"/>" onclick="setRating('good', this);return false;" href="<c:url value="/user/rate_song"/>" class="ratingLink good<c:if test="${rating ne null and rating.rating eq 10}"> selectedRating</c:if>">good</a><a
          id="okayRatingLink<c:out value="${song.id}"/>" onclick="setRating('okay', this);return false;" href="<c:url value="/user/rate_song"/>" class="ratingLink okay<c:if test="${rating ne null and rating.rating eq 5}"> selectedRating</c:if>">okay</a><a
          id="badRatingLink<c:out value="${song.id}"/>" onclick="setRating('bad', this);return false;" href="<c:url value="/user/rate_song"/>" class="ratingLink bad<c:if test="${rating ne null and rating.rating eq 0}"> selectedRating</c:if>">bad</a><a
          id="noneRatingLink<c:out value="${song.id}"/>" onclick="setRating('none', this);return false;" href="<c:url value="/user/rate_song"/>" class="ratingLink none<c:if test="${rating eq null}"> selectedRating</c:if>">x</a></div></td>
  </tr>
    </c:forEach>
  </c:otherwise>
  </c:choose>
</table>
<c:if test="${foundSongs}">
<br>
<%--
<div class="songListHeader">
  <%@ include file="/WEB-INF/jsp/include/jsamp.html"%>
  <div class="songListName" style="margin-top:4px">Play Songs:</div>
  --%>
<%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
<%--
</div>
--%>
</c:if>