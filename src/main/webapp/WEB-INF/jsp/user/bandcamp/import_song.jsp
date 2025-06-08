<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<div id="linkFormDiv">
  <form onsubmit="return loadFromBandcamp(this.url.value)">
    <table class="linkTable" width="100%" cellspacing="4">
      <tr>
        <td align="right" width="100" valign="middle"><b>Import From:&nbsp;</b></td>
        <td valign="middle"><a href="http://www.bandcamp.com" target="_blank"><img src="<c:url value="/img/bandcamp-85x15.png"/>" width="85" height="15"></a></td>
      </tr>
      <tr>
        <td align="right" valign="middle"><b>Song Page URL:&nbsp;</b></td>
        <td valign="middle"><input type="text" name="url" style="width:100%;"></td>
      </tr>
      <tr>
        <td colspan="2">
        <span style="float:right">
          <input type="button" name="cancelBtn" class="commentButton" value="Cancel" onclick="cancelBandcampImport()"/>
          <input type="submit" name="submitBtn" class="commentButton" value="Import"/>
          <span id="copyFromBandcampSpinner" style="display:none"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Working..."></span>
        </span>
        </td>
      </tr>
    </table>
  </form>
</div>
