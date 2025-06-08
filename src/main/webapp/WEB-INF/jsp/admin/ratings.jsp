<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: View Ratings</title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

<c:set var="foundRatings" value="${fn:length(ratings) > 0}"/>
<div class="songListHeader">
  <c:if test="${foundRatings}">
  <c:url var="pagingLink" value="/admin/ratings"/>
    <div class="songListPaging">
      <c:if test="${start gt 0}">
      <a class="previousPageLink" href="<c:out value="${pagingLink}"/>?start=<c:out value="${start - pageSize}"/>">[Previous]</a>
      </c:if>
      <a class="nextPageLink" href="<c:out value="${pagingLink}"/>?start=<c:out value="${start + pageSize}"/>">[Next]</a>
    </div>
  </c:if>
  <div class="songListName">
    <span id="searchNameEl">Ratings</span>
  </div>
</div>
<table class="songList">
  <tr class="songListFooterRow">
    <td>&nbsp;</td>
    <td>Song</td>
    <td>User</td>
    <td>Rating</td>
    <td>Disabled</td>
    <td>Time</td>
  </tr>
  <c:choose>
  <c:when test="${!foundRatings}">
  <tr class="songListOddRow">
    <td colspan="6">No ratings.</td>
  </tr>
  </c:when>
  <c:otherwise>
    <c:forEach var="rating" items="${ratings}" varStatus="loopStatus">
      <c:set var="song" value="${rating.song}"/>
  <tr class="<c:choose><c:when test="${loopStatus.index % 2 == 0}">songListOddRow</c:when><c:otherwise>songListEvenRow</c:otherwise></c:choose>">
    <td><c:out value="${start + loopStatus.index + 1}"/><c:if test="${(start + loopStatus.index + 1) < 10}">&nbsp</c:if></td>
    <c:url var="viewSongLink" value="/songs/${song.titleForUrl}-${song.id}"/>
    <td><a id="title<c:out value="${loopStatus.index}"/>" href="<c:out value="${viewSongLink}"/>" title="View '<c:out value="${song.title}"/>' Info/Comments"><c:out value="${song.title}"/></a></td>
    <c:url var="viewUserLink" value="/profile">
      <c:param name="id" value="${rating.user.id}"/>
    </c:url>
    <td><a id="user<c:out value="${loopStatus.index}"/>" href="<c:out value="${viewUserLink}"/>" title="View <c:out value="${rating.user.displayName}"/> User Info"><c:out value="${rating.user.displayName}"/></a></td>
    <td><c:out value="${rating.rating}"/></td>
    <td><a id="disabled<c:out value="${rating.id}"/>" href="javascript:toggleDisabled(<c:out value="${rating.id}"/>)" title="Toggle disabled"><c:out value="${rating.disabled}"/></a></td>
    <td><fmt:formatDate value="${rating.changeDate}" pattern="MM/dd/yyyy hh:mm:ssa"/></td>
  </tr>
    </c:forEach>
  </c:otherwise>
  </c:choose>
</table>

          <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="include/right_nav.jsp"/>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript"><!--
  var toggleDisabledURL = '<c:url value="/admin/disable_rating"/>';
  function toggleDisabled(ratingId) {
    new Ajax.Request(toggleDisabledURL, {
      asynchronous : false,
      parameters : {
        ratingId : ratingId
      },
      method : 'post',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          $('disabled'+ratingId).update(responseJSON.disabled);
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not toggle disabled flag for rating, please try again');
      }
    });
  }
  //--></script>
  </body>
</html>