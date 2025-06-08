<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<table class="songInfoTable">
  <tr class="grayBottom">
    <td>
      <c:if test="${canEdit}">
        <a style="float:right;margin-left:20px;" class="editLink" href="javascript:editPlaylist()">Edit</a>
      </c:if>
      <span class="songInfoLabel"><b>Playlist Info:</b>&nbsp;</span>
      <c:url var="pUserLink" value="/profile">
        <c:param name="id" value="${playlist.user.id}"/>
      </c:url>
      '<span id="title1"><c:out value="${playlist.title}"/>'</span>&nbsp;by&nbsp;<a id="user1" href="<c:out value="${pUserLink}"/>"><c:out value="${playlist.user.displayName}"/></a>
    </td>
  </tr>
  <tr class="oddRow">
    <td><span
        class="songInfoLabel">Avg. Rating:&nbsp;</span><span
        id="rating"<c:if test="${playlist.showRating ne true}">
      style="display:none"</c:if>><c:out value="${playlist.ratingString}"/></span><span
      id="needsRatings" class="needsRatings"<c:if test="${playlist.showRating}">
      style="display:none"</c:if>>This playlist needs [ <b id="numRatingsNeeded"><c:out value="${playlist.numRatingsNeeded}"/></b> ] more ratings.</span></td>
  </tr>
  <tr class="evenRow">
    <td><span
      class="songInfoLabel"># of Ratings:&nbsp;</span><span
      id="numRatings"><c:out value="${playlist.numRatings}"/></span>
    </td>
  </tr>
  <tr class="oddRow">
    <td><span class="songInfoLabel">Updated:&nbsp;</span><fmt:formatDate value="${playlist.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
  </tr>
  <c:if test="${canEdit}">
    <tr class="evenRow">
      <td><span class="songInfoLabel">Public:&nbsp;</span><c:out value="${playlist.publiclyAvailable}"/></td>
    </tr>
  </c:if>
  <c:if test="${playlist.description ne null and fn:length(playlist.description) gt 0}">
  <tr class="evenRow">
    <td>
      <div style="border:1px solid #bbb;padding:1px;">
        <ssj:escapeSongMoreInfo content="${playlist.description}"/>
      </div>
<%--
      <c:if test="${song.moreInfo ne null && fn:trim(song.moreInfo) ne ''}">
      <a id="moreInfoLink" href="javascript:toggleMoreInfo()">[ Show More Info ]</a>
      <div id="moreInfo" style="border:1px solid #bbb;padding:1px;display:none;">
        <ssj:escapeSongMoreInfo content="${song.moreInfo}"/>
      </div>
      </c:if>
--%>
    </td>
  </tr>
  </c:if>
<%--
  <tr class="evenRow">
    <td colspan="2"><a id="song1" class="songLink" href="<c:out value="${song.url}"/>" title="Hear/Download '<c:out value="${song.title}"/>'">MP3</a></td>
  </tr>
--%>
</table>
