<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<table class="songInfoTable">
  <tr class="grayBottom">
    <td style="padding-top:2px"><c:if test="${canEdit}"><span style="float:right"><a class="editLink" style="margin-top:0" href="javascript:editSong()">Edit</a></span></c:if>
      <span class="songInfoLabel" style="font-weight:bold">Song Info:&nbsp;</span>
      <c:url var="sArtistLink" value="/artists/${song.artist.nameForUrl}-${song.artist.id}"/>
      '<c:out value="${song.title}" escapeXml="false"/>' by&nbsp;<a id="artist1" href="<c:out value="${sArtistLink}"/>"><c:out value="${song.artist.name}"/></a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
  </tr>
  <tr class="oddRow">
    <td><span
        class="songInfoLabel">Avg. Rating:&nbsp;</span><span
        id="rating"<c:if test="${song.showRating ne true}">
      style="display:none"</c:if>><c:out value="${song.ratingString}"/></span><span
      id="needsRatings" class="needsRatings"<c:if test="${song.showRating}">
      title="<c:out value="${song.numRatingsNeeded}"/> more rating(s) needed to show average rating"
      style="display:none"</c:if>>This song needs [ <b id="numRatingsNeeded"><c:out value="${song.numRatingsNeeded}"/></b> ] more ratings.</span></td>
  </tr>
  <tr class="evenRow">
    <td><span
        class="songInfoLabel"># of Ratings:&nbsp;</span><span
        id="numRatings"><c:out value="${song.numRatings}"/></span>
    </td>
  </tr>
  <tr class="oddRow">
    <td><span class="songInfoLabel">Posted:&nbsp;</span><fmt:formatDate value="${song.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
  </tr>
  <c:set var="rowStyle" value="evenRow"/>
  <c:if test="${not empty song.album}">
  <tr class="${rowStyle}">
    <td><span class="songInfoLabel">Album:&nbsp;</span>${song.album}</td>
  </tr>
  <c:set var="rowStyle" value="oddRow"/>
  </c:if>
  <c:if test="${song.albumTrackNumber gt 0}">
  <tr class="${rowStyle}">
    <td><span class="songInfoLabel">Track #:&nbsp;</span>${song.albumTrackNumber}</td>
  </tr>
  <c:set var="rowStyle" value="evenRow"/>
  </c:if>
  <c:if test="${song.duration gt 0}">
  <tr class="${rowStyle}">
    <td><span class="songInfoLabel">Length:&nbsp;</span><ssj:duration duration="${song.duration}"/></td>
  </tr>
  <c:set var="rowStyle" value="oddRow"/>
  </c:if>
  <c:if test="${not empty song.bandcampUrl}">
  <tr class="${rowStyle}">
    <td><span class="songInfoLabel"><a href="${song.bandcampUrl}" target="_blank" style="font-size:80%">Bandcamp Page</a></span></td>
  </tr>
  <c:set var="rowStyle" value="evenRow"/>
  </c:if>
  <tr class="evenRow">
    <td>
      <div style="border:1px solid #bbb;padding:1px;">
        <ssj:escapeSongInfo content="${song.info}"/>
      </div>
      <c:if test="${song.moreInfo ne null && fn:trim(song.moreInfo) ne ''}">
      <a id="moreInfoLink" href="javascript:toggleMoreInfo()">[ Show More Info ]</a>
      <div id="moreInfo" style="border:1px solid #bbb;padding:1px;display:none;">
        <ssj:escapeSongMoreInfo content="${song.moreInfo}"/>
      </div>
      </c:if>
    </td>
  </tr>
  <tr class="evenRow">
    <td>
      <ul class="playlist">
        <li id="item_0">
          <div style="float:left;margin-right:6px"><a class="playable" href="<c:url value="/songs/stream/${song.titleForUrl}-${song.id}.mp3"/>" title="Play/pause '<c:out value="${song.title}"/>'"><img src="<c:url value="/img/play-pause-icon.png"/>" alt="Play/Pause" width="16" height="16" style="vertical-align:middle"></a></div>
          <div><tags:download_links song="${song}" loopStatusIndex="${1}"/></div>
        </li>
      </ul>
    </td>
  </tr>
</table>
