<%@ page import="com.ssj.model.song.search.SongSearch" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<form name="songSearchForm" action="<c:url value="/search"/>" method="post">
  <input type="hidden" name="search" value="true">
  <%--<input type="hidden" name="titleExactMatch" value="false">--%>
  <c:set var="tableStyle" value=""/>
  <c:if test="${param['showBorder'] eq 'true'}">
    <c:set var="tableStyle" value="songInfoTable"/>
  </c:if>
  <table class="<c:out value="${tableStyle}"/>">
    <tr>
      <td colspan="2">
        <b>Song Search:</b>
      </td>
    </tr>
    <tr>
      <td><b>Title:</b></td>
      <td><input name="title" value=""></td>
    </tr>
    <tr>
      <td><b>Artist:</b></td>
      <td><input name="artistName" value=""></td>
    </tr>
    <tr>
      <td><b>Album:</b></td>
      <td><input name="album" value=""></td>
    </tr>
    <tr>
      <td><b>Sort:</b></td>
      <td>
        <select name="orderBy">
          <option value="<%= SongSearch.ORDER_BY_AVG_RATING %>">Highest Rating</option>
          <option value="<%= SongSearch.ORDER_BY_CREATE_DATE %>">Newest</option>
          <option value="<%= SongSearch.ORDER_BY_NUM_RATINGS %>">Most Votes</option>
          <option value="-<%= SongSearch.ORDER_BY_AVG_RATING %>">Lowest Rating</option>
          <option value="-<%= SongSearch.ORDER_BY_CREATE_DATE %>">Oldest</option>
          <option value="-<%= SongSearch.ORDER_BY_NUM_RATINGS %>">Fewest Votes</option>
          <option value="<%= SongSearch.ORDER_BY_RANDOM %>">Random</option>
          <option value="<%= SongSearch.ORDER_BY_ALBUM %>">Album</option>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="2"><input type="submit" name="submitBtn" value="Search"><br>
        <a class="nextPageLink" href="<c:url value="/advanced_search"/>">[ Advanced Search ]</a></td>
    </tr>
  </table>
</form>
