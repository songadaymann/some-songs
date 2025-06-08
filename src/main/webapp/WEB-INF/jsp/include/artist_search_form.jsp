<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<form name="artistSearchForm" action="<c:url value="/artist_search"/>" method="post">
  <input type="hidden" name="search" value="true">
  <table class="songInfoTable">
    <tr>
      <td colspan="2">
        <b>Artist Search:</b><br>
      </td>
    </tr>
    <tr>
      <td><b>Name:</b></td>
      <td><input name="name" value="<c:out value="${artistSearch.name}"/>"></td>
    </tr>
    <tr>
      <td><b>Description:</b></td>
      <td><input name="info" value="<c:out value="${artistSearch.info}"/>"></td>
    </tr>
    <tr>
      <td colspan="2">
        <c:set var="checked" value=""/>
        <c:if test="${requestScope['artistSearch'] eq null or artistSearch.hasShownSongs}">
          <c:set var="checked" value=" checked=\"true\""/>
        </c:if>
        <input type="checkbox" name="hasShownSongs" value="true"<c:out value="${checked}"/>">
        Only Artists With Non-Hidden Songs
      </td>
    </tr>
    <tr>
      <td colspan="2"><input type="submit" name="submitBtn" value="Search"></td>
    </tr>
  </table>
</form>
