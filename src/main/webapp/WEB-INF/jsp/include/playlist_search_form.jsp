<%@ page import="com.ssj.model.song.search.SongSearch" %>
<%@ page import="com.ssj.model.playlist.search.PlaylistSearch" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<form name="playlistSearchForm" action="<c:url value="/playlist_search"/>" method="post">
  <input type="hidden" name="search" value="true">
  <input type="hidden" name="titleExactMatch" value="false">
  <c:set var="tableStyle" value=""/>
  <c:if test="${param['showBorder'] eq 'true'}">
    <c:set var="tableStyle" value="songInfoTable"/>
  </c:if>
  <table class="<c:out value="${tableStyle}"/>">
    <tr>
      <td colspan="2">
        <b>Playlist Search:</b>
      </td>
    </tr>
    <tr>
      <td><b>Title:</b></td>
      <td><input name="title" value=""></td>
    </tr>
    <tr>
      <td><b>Min. # of Songs:</b></td>
      <td><input name="numItemsMin" value=""></td>
    </tr>
    <tr>
      <td><b>Max. # of Songs:</b></td>
      <td><input name="numItemsMax" value=""></td>
    </tr>
    <tr>
      <td><b>Sort:</b></td>
      <td>
        <select name="orderBy">
          <option value="<%= PlaylistSearch.ORDER_BY_NUM_SONGS %>">Most Songs</option>
          <option value="<%= PlaylistSearch.ORDER_BY_AVG_RATING %>">Highest Rating</option>
          <option value="<%= PlaylistSearch.ORDER_BY_CREATE_DATE %>">Newest</option>
          <option value="<%= PlaylistSearch.ORDER_BY_NUM_RATINGS %>">Most Votes</option>
          <option value="-<%= PlaylistSearch.ORDER_BY_NUM_SONGS %>">Fewest Songs</option>
          <option value="-<%= PlaylistSearch.ORDER_BY_AVG_RATING %>">Lowest Rating</option>
          <option value="-<%= PlaylistSearch.ORDER_BY_CREATE_DATE %>">Oldest</option>
          <option value="-<%= PlaylistSearch.ORDER_BY_NUM_RATINGS %>">Fewest Votes</option>
          <option value="<%= PlaylistSearch.ORDER_BY_RANDOM %>">Random</option>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="2"><input type="submit" name="submitBtn" value="Search"><br>
        <%--<a class="nextPageLink" href="<c:url value="/advanced_search"/>">[ Advanced Search ]</a></td>--%>
    </tr>
  </table>
</form>
