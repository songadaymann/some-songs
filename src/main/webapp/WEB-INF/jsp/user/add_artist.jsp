<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div id="artistFormDiv">
<form:form id="addArtistForm" commandName="artist" onsubmit="saveArtist(); return false;">
<div id="errorsDiv">
  <form:errors path="*" cssClass="errors" element="div"/>
</div>
<table id="artistTable" width="100%" cellspacing="4">
  <tr>
    <td><b>Add Artist</b></td>
    <%-- IE doesn't like colspan plus specified column width :( --%>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>Name:</td>
    <td><form:input path="name" id="name" cssStyle="width:100%"/></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right">
      <br>
      <input type="button" name="cancelBtn" class="commentButton" value="Cancel" onclick="cancelArtist()"/>
      <input type="submit" name="submitBtn" class="commentButton" value="Save"/>
    </td>
  </tr>
</table>
</form:form>
</div>
<%-- causes an error in IE :(
<script type="text/javascript" language="JavaScript"><!--
//alert('test');
setTimeout('document.forms.addArtistForm.name.focus()', 500);
//--></script>
--%>
