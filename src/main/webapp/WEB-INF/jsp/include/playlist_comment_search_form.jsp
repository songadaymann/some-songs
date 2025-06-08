<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<form name="commentSearchForm" action="<c:url value="/playlist_comment_search"/>" method="post">
  <input type="hidden" name="search" value="true">
  <table class="songInfoTable">
    <tr>
      <td colspan="2"><b>Playlist Comment/Reply Search:</b></td>
    </tr>
    <tr>
      <td><b>In Comment/Reply:</b></td>
      <td><input type="text" name="commentText" value="<c:out value="${commentSearch.commentText}"/>"></td>
    </tr>
    <tr>
      <td><b>Author Name:</b></td>
      <td><input type="text" name="userDisplayName" value="<c:out value="${commentSearch.userDisplayName}"/>"></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="radio" name="searchType" value="comments"<c:if test="${sessionScope['isPlaylistReplySearch'] ne 'true'}"> checked="true"</c:if>> Search Comments<br>
        <input type="radio" name="searchType" value="replies"<c:if test="${sessionScope['isPlaylistReplySearch'] eq 'true'}"> checked="true"</c:if>> Search Replies<br>
        <input type="submit" name="submitBtn" value="Search">
      </td>
    </tr>
  </table>
</form>
