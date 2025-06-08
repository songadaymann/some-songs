<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<div id="linkFormDiv">
  <form onsubmit="return copySoundcloudSong(this)">
    <table id="soundcloudImportTable" class="linkTable" width="100%" cellspacing="4">
      <tr>
        <td align="right" width="80"><b>Import From:&nbsp;</b></td>
        <td><a href="http://www.soundcloud.com" target="_blank"><img src="<c:url value="/img/soundcloud-80x50.png"/>" width="80" height="50"></a></td>
      </tr>
      <tr>
        <td id="soundcloudSpinnerRow" colspan="2" valign="top"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Working..." style="vertical-align:middle"><b>Loading Songs...</b></td>
      </tr>
      <tr>
        <td colspan="2">
        <span style="float:right">
          <input type="button" name="cancelBtn" class="commentButton" value="Cancel" onclick="cancelSoundcloudImport()"/>
          <input type="submit" name="submitBtn" class="commentButton" value="Import"/>
          <%--<span id="copyFromSoundcloudSpinner" style="display:none"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Working..."></span>--%>
        </span>
        </td>
      </tr>
    </table>
  </form>
</div>
