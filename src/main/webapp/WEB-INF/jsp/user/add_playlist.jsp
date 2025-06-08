<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div id="playlistFormDiv">
<form:form id="addPlaylistForm" commandName="playlist" onsubmit="savePlaylist(); return false;">

<input type="hidden" name="songId" value="<c:out value="param.songId"/>">

<div id="errorsDiv">
  <form:errors path="*" cssClass="errors" element="div"/>
</div>

<%-- TODO figure out if I can use valang here or not --%>
<!--<valang:validate commandName="playlist"/>-->

<table id="playlistTable" width="100%" cellspacing="4">
  <tr>
    <td><b>Add Playlist</b></td>
  </tr>
  <tr>
    <td><form:input path="title" id="title" cssStyle="float:right;width:80%"/>Title:</td>
  </tr>
  <tr>
    <td>
      Description (optional):<br>
      <form:textarea path="description" id="description" cssStyle="width:100%;height:100px;"/>
      <div class="allowedTags">Allowed HTML: &lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
    </td>
  </tr>
  <tr>
    <td>
      <br>
      <span style="float:right">
        <input type="button" name="cancelBtn" class="commentButton" value="Cancel" onclick="cancelPlaylist()"/>
        <input type="submit" name="submitBtn" class="commentButton" value="Save"/>
      </span>
    </td>
  </tr>
</table>
</form:form>
</div>
<script>setTimeout('document.forms.addPlaylistForm.title.focus()', 500);</script>
<script>TextareaLimiter.addLimiter("description", 2000);</script>

