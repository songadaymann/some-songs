<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<style type="text/css">
  .linkTable input[type="text"], .linkTable textarea {
    width:100%;
  }
</style>
<div id="linkFormDiv">
<form:form id="editLinkForm" name="editLinkForm" commandName="artistLink" onsubmit="saveLink(); return false;">
  <form:hidden path="artist.id"/>
<div id="errorsDiv">
  <form:errors path="*" cssClass="errors" element="div"/>
</div>
<table class="linkTable" width="100%" cellspacing="4">
  <tr>
    <td align="right" width="80"><b>Artist Link:&nbsp;</b></td>
    <td><c:out value="${artistLink.artist.name}"/></td>
  </tr>
  <tr>
    <td align="right"><b>Label:&nbsp;</b></td>
    <td><form:input path="label" id="label"/></td>
  </tr>
  <tr>
    <td align="right"><b>URL:&nbsp;</b></td>
    <td><form:input path="url" id="url"/></td>
  </tr>
  <tr>
    <td align="right" valign="top"><b>Notes:&nbsp;</b></td>
    <td><form:textarea path="notes" id="notes"/></td>
  </tr>
  <tr>
    <td colspan="2">
      <span style="float:right">
        <input type="button" name="cancelBtn" class="commentButton" value="Cancel" onclick="cancelLinkEdit()"/>
        <input type="submit" name="submitBtn" class="commentButton" value="Save"/>
      </span>
      <c:if test="${artistLink.id > 0}">
      <input type="button" name="deleteBtn" class="commentButton" value="Delete" onclick="deleteLink(<c:out value="${artistLink.id}"/>)"/>
      </c:if>
    </td>
  </tr>
</table>
</form:form>
</div>
<script>setTimeout('document.forms.editLinkForm.label.focus()', 500);</script>
<script>TextareaLimiter.addLimiter('notes', 256);</script>