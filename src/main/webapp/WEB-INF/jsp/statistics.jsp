<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Site<c:if test="${loggedIn}">/User</c:if> Statistics</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">


          <div class="songListHeader">
            <div class="songListName">Site Statistics</div>
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
          </div>
          <table class="songList">
            <tr class="songListOddRow">
              <td>
                Songs that aren't hidden:
              </td>
              <td width="200">
                <c:out value="${numSongs}"/>
              </td>
            </tr>
            <tr class="songListEvenRow">
              <td>
                Artists with at least one song:
              </td>
              <td>
                <c:out value="${numArtists}"/>
              </td>
            </tr>
            <tr class="songListOddRow">
              <td>
                Users who have visited in the last 4 weeks:
              </td>
              <td>
                <c:out value="${numRecentLogins}"/>
              </td>
            </tr>
            <tr class="songListEvenRow">
              <td>
                Average song rating:
              </td>
              <td>
                <c:out value="${averageSongRating}"/>
              </td>
            </tr>
            <tr class="songListOddRow">
              <td>
                Average votes per song:
              </td>
              <td>
                <c:out value="${averageNumRatings}"/>
              </td>
            </tr>
          </table>

          <br> <br>

          <c:if test="${loggedIn}">
          <div class="songListHeader">
            <div class="songListName">Your Statistics</div>
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
          </div>
          <table class="songList">
            <tr class="songListOddRow">
              <td>
                Non-hidden songs you've rated:
              </td>
              <td width="200">
                <c:out value="${nonHiddenSongRated}"/>
              </td>
            </tr>
            <tr class="songListEvenRow">
              <td>
                Your average rating (10 = good, 0 = bad):
              </td>
              <td>
                <c:out value="${userAverageRating}"/>
              </td>
            </tr>
            <tr class="songListOddRow">
              <td>
                # of comments:
              </td>
              <td>
                <c:out value="${userNumComments}"/>
              </td>
            </tr>
            <tr class="songListEvenRow">
              <td>
                # of comment replies:
              </td>
              <td>
                <c:out value="${numCommentReplies}"/>
              </td>
            </tr>
            <tr class="songListOddRow">
              <td>
                # of message board posts:
              </td>
              <td>
                <c:out value="${numMessageBoardPosts}"/>
              </td>
            </tr>
            <tr class="songListEvenRow">
              <td>
                # of threads started:
              </td>
              <td>
                <c:out value="${numMessageBoardThreads}"/>
              </td>
            </tr>
          </table>

          <br> <br>
          </c:if>

          <div class="songListHeader">
            <div class="songListName">Top Raters</div>
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
          </div>
          <table class="songList">
            <c:forEach var="userNumberArray" items="${topRaters}" varStatus="loopStatus">
              <c:if test="${userNumberArray[1] gt 0}">
              <tr class="songList${loopStatus.index % 2 != 0 ? 'Odd' : 'Even'}Row">
                <td>
                  <a href="<c:url value="/profile?id=${userNumberArray[0].id}"/>" title="View User Profile">${userNumberArray[0].displayName}</a>
                </td>
                <td width="200">
                  ${userNumberArray[1]}
                </td>
              </tr>
              </c:if>
            </c:forEach>
          </table>

          <br> <br>

          <div class="songListHeader">
            <div class="songListName">Top Commenters</div>
            <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
          </div>
          <table class="songList">
            <c:forEach var="userNumberArray" items="${topCommenters}" varStatus="loopStatus">
              <c:if test="${userNumberArray[1] gt 0}">
              <tr class="songList${loopStatus.index % 2 != 0 ? 'Odd' : 'Even'}Row">
                <td>
                  <a href="<c:url value="/profile?id=${userNumberArray[0].id}"/>" title="View User Profile">${userNumberArray[0].displayName}</a>
                </td>
                <td width="200">
                  ${userNumberArray[1]}
                </td>
              </tr>
              </c:if>
            </c:forEach>
          </table>

          <br> <br>

        </div></div>

      </div>

      <!--<div id="rightNavBox" class="yui-b"></div>-->

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>
