<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<style type="text/css">
  .songInfoTable input[type="text"], .songInfoTable textarea {
    width:300px;
  }
</style>
<form:form id="editArtistForm" cssClass="cmxform" commandName="artist" onsubmit="saveArtist(); return false;">
  <form:hidden path="id"/>
<div id="errorsDiv">
  <form:errors path="*" cssClass="errors" element="div"/>
</div>
<table class="songInfoTable">
  <tr class="grayBottom">
    <td align="right" width="120"><b>Artist Info:</b>&nbsp;</td>
    <td>
      <c:out value="${artist.name}"/>
    </td>
  </tr>
  <tr>
    <td align="right"><b>Name:</b></td>
    <td><form:input path="name" id="name"/></td>
  </tr>
  <tr>
    <td align="right"><b><a target="_blank" href="http://bandcamp.com/">Bandcamp</a> URL:</b></td>
    <td><form:input path="bandcampUrl" id="bandcampUrl"/></td>
  </tr>
  <c:if test="${isAdmin or artist.user.canSynchFromBandcamp}">
  <tr>
    <td align="right" valign="top"><b>Bandcamp Synch:</b></td>
    <td valign="top">
      <form:checkbox path="synchFromBandcamp" id="synchFromBandcamp" value="true" onchange="bandcampSynchChanged(this)"/>
      <div id="albumToggle"<c:if test="${artist.synchFromBandcamp ne true}"> style="display:none"</c:if>>
        <input type="radio" name="albumSelection" value="all" onclick="synchAlbumsChanged()"<c:if test="${fn:length(bandcampAlbumIds) eq 0}"> checked="true"</c:if>> All Albums<br>
        <c:choose><c:when test="${fn:length(bandcampAlbumIds) gt 0}">
          <input type="radio" name="albumSelection" value="some" onclick="synchAlbumsChanged()" checked="true"> ${fn:length(bandcampAlbumIds)} Albums
          (<a href="javascript:synchAlbumsChanged()">edit</a>)
        </c:when><c:otherwise>
          <input type="radio" name="albumSelection" value="some" onclick="synchAlbumsChanged()"> Specific Albums
        </c:otherwise></c:choose>
        <span id="copyFromBandcampSpinner" style="display:none"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Working..."></span>
      </div>
      <div id="bandcampAlbums" style="display:none;height:200px;overflow-y:auto;">
        <c:forEach var="bandcampAlbumId" items="${bandcampAlbumIds}">
          <input type="hidden" name="bandcampAlbumIds" value="${bandcampAlbumId}">
        </c:forEach>
      </div>
    </td>
  </tr>
  </c:if>
  <tr>
    <td align="right" valign="top"><b>Notes:</b></td>
    <td><form:textarea path="info" id="info"/></td>
  </tr>
  <tr>
    <td colspan="2">
      <span style="float:right">
        <input type="button" name="cancelBtn" value="Cancel" onclick="cancelEdit()"/>
        <input type="submit" name="submitBtn" value="Save"/>
      </span>
      <input type="button" name="deleteBtn" value="Delete" onclick="deleteArtist()"/>
    </td>
  </tr>
</table>
  </form:form>
<script>TextareaLimiter.addLimiter('info', 1000)</script>
