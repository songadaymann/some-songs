<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<style type="text/css">
  .songInfoTable input[type="text"], .songInfoTable textarea {
    width:500px;
  }
</style>
<%-- TODO figure out if I can use valang here or not --%>
<!--<valang:validate commandName="playlist"/>-->
<form:form id="editPlaylistForm" cssClass="cmxform" commandName="playlist" onsubmit="savePlaylist(); return false;">
  <form:hidden path="id"/>
  <div id="errorsDiv">
    <form:errors path="*" cssClass="errors" element="div"/>
  </div>
  <table class="songInfoTable">
    <tr class="grayBottom">
      <td class="nowrap" align="right" valign="top"><b>Playlist Info:</b>&nbsp;</td>
      <td>
        <c:url var="sUserLink" value="/profile">
          <c:param name="id" value="${playlist.user.id}"/>
        </c:url>
        <span>'<c:out value="${playlist.title}"/>'&nbsp;by&nbsp;<a href="<c:out value="${sUserLink}"/>"><c:out value="${playlist.user.displayName}"/></a></span>
      </td>
    </tr>
    <tr class="oddRow">
      <td align="right">Title:&nbsp;</td>
      <td><form:input path="title" id="title"/></td>
    </tr>
    <tr class="evenRow">
      <td align="right">Description:&nbsp;</td>
      <td><form:textarea path="description" id="description"/></td>
    </tr>
    <tr class="evenRow">
      <td>&nbsp;</td>
      <td>
        <div class="allowedTags">&lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt;<br>
          &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
      </td>
    </tr>
    <tr class="oddRow">
      <td align="right">Public:&nbsp;</td>
      <td><form:checkbox path="publiclyAvailable" id="public"/></td>
    </tr>
    <tr class="evenRow">
      <td colspan="2">
        <span style="float:right">
          <input type="button" name="cancelBtn" value="Cancel" onclick="cancelEdit()"/>
          <input type="submit" name="submitBtn" value="Save"/>
        </span>
        <input type="button" name="deleteBtn" value="Delete" onclick="deletePlaylist()"/>
      </td>
    </tr>
  </table>
</form:form>
</div>
<script>TextareaLimiter.addLimiter('description', 2000)</script>
