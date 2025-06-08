<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form name="editSongForm" id="editSongForm" cssClass="cmxform" commandName="song" onsubmit="saveSong(); return false;">
  <form:hidden path="id"/>
  <form:hidden path="duration" id="duration"/>
  <form:hidden path="bandcampTrackId" id="bandcampTrackId"/>
<div id="errorsDiv">
  <form:errors path="*" cssClass="errors" element="div"/>
</div>
<table class="songInfoTable">
  <tr class="grayBottom">
    <td class="nowrap" align="right" valign="top"><b>Song Info:</b>&nbsp;</td>
    <td>
      <c:url var="sArtistLink" value="/artists/${song.artist.nameForUrl}-${song.artist.id}"/>
      <span>'<c:out value="${song.title}"/>' by <a href="<c:out value="${sArtistLink}"/>"><c:out value="${song.artist.name}"/></a></span>
    </td>
  </tr>
  <tr class="oddRow">
    <td class="nowrap" align="right">Avg. Rating:&nbsp;</td>
    <td><span id="rating"<c:if test="${song.showRating ne true}">
      style="display:none"</c:if>><c:out value="${song.ratingString}"/></span><span
      id="needsRatings" class="needsRatings"<c:if test="${song.showRating}">
      style="display:none"</c:if>>This song needs [ <b id="numRatingsNeeded"><c:out value="${song.numRatingsNeeded}"/></b> ] more ratings.</span></td>
  </tr>
  <tr class="evenRow">
    <td class="nowrap" align="right"># of Ratings:&nbsp;</td>
    <td id="numRatings"><c:out value="${song.numRatings}"/></td>
  </tr>
  <tr class="oddRow">
    <td align="right">Posted:&nbsp;</td>
    <td><fmt:formatDate value="${song.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
  </tr>
  <tr class="evenRow">
    <td align="right">Title:&nbsp;</td>
    <td><form:input path="title" id="title" cssClass="wideInput"/></td>
  </tr>
  <tr class="oddRow">
    <td align="right">MP3 URL:&nbsp;</td>
    <td><form:input path="url" id="url" cssClass="wideInput"/></td>
  </tr>
  <tr class="evenRow">
    <td align="right">Album:&nbsp;</td>
    <td><form:input path="album" id="album" cssClass="wideInput"/></td>
  </tr>
  <tr class="oddRow">
    <td align="right">Track #:&nbsp;</td>
    <td><form:input path="albumTrackNumber" id="albumTrackNumber" cssClass="wideInput"/></td>
  </tr>
  <tr class="evenRow">
    <td align="right">Bandcamp URL:&nbsp;</td>
    <td><form:input path="bandcampUrl" id="bandcampUrl" cssClass="wideInput"/></td>
  </tr>
<%--
  <tr class="oddRow">
    <td>&nbsp;</td>
    <td>
      <input type="button" class="subtmiBtn" style="width:auto" onclick="return loadFromBandcamp(this.form.bandcampUrl.value)" name="loadFromBandcampBtn" value="Copy Song Info From Bandcamp">
      <span id="copyFromBandcampSpinner" style="display:none"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Working..."></span>
    </td>
  </tr>
--%>
  <tr class="oddRow">
    <td align="right">SoundCloud URL:&nbsp;</td>
    <td><form:input path="soundCloudUrl" id="soundCloudUrl" cssClass="wideInput"/></td>
  </tr>
<%--
  <tr class="oddRow">
    <td>&nbsp;</td>
    <td>
      <input type="button" class="subtmiBtn" style="width:auto" onclick="return loadFromBandcamp(this.form.bandcampUrl.value)" name="loadFromBandcampBtn" value="Copy Song Info From Bandcamp">
      <span id="copyFromBandcampSpinner" style="display:none"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Working..."></span>
    </td>
  </tr>
--%>
  <tr class="evenRow">
    <td align="right" valign="top">Info:&nbsp;</td>
    <td valign="top"><form:textarea path="info" id="info" cssClass="wideInput"/></td>
  </tr>
  <tr class="evenRow">
    <td>&nbsp;</td>
    <td>
      <div class="allowedTags">&lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
    </td>
  </tr>
  <tr class="evenRow">
    <td align="right" valign="top">More Info:&nbsp;</td>
    <td valign="top"><form:textarea path="moreInfo" id="moreInfo" cssClass="wideInput"/></td>
  </tr>
  <tr class="evenRow">
    <td>&nbsp;</td>
    <td>
      <div class="allowedTags">&lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt;<br>
        &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
    </td>
  </tr>
  <tr class="evenRow">
    <td class="nowrap" align="right">Show Song:&nbsp;</td>
    <td><form:checkbox path="showSong" id="showSong"/></td>
  </tr>
  <tr class="evenRow">
    <td colspan="2">
      <span style="float:right">
        <input type="button" name="cancelBtn" value="Cancel" onclick="cancelEdit()"/>
        <input type="submit" name="submitBtn" value="Save"/>
      </span>
      <input type="button" name="deleteBtn" value="Delete" onclick="deleteSong()"/>
    </td>
  </tr>
  <tr class="evenRow">
    <td colspan="2"><a class="songLink" href="<c:out value="${song.url}"/>" title="Hear/Download '<c:out value="${song.title}"/>'">MP3</a></td>
  </tr>
</table>
</form:form>
<script>TextareaLimiter.addLimiter('info', 1000)</script>
<script>TextareaLimiter.addLimiter('moreInfo', 3000)</script>
