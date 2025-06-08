<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: View Broken Link Reports</title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

<c:set var="foundBrokenLinkReports" value="${fn:length(reports) > 0}"/>
<div class="songListHeader">
  <c:if test="${foundBrokenLinkReports}">
  <c:url var="pagingLink" value="/admin/broken_link_reports"/>
    <div class="songListPaging">
      <c:if test="${start gt 0}">
      <a class="previousPageLink" href="<c:out value="${pagingLink}"/>?start=<c:out value="${start - pageSize}"/>">[Previous]</a>
      </c:if>
      <a class="nextPageLink" href="<c:out value="${pagingLink}"/>?start=<c:out value="${start + pageSize}"/>">[Next]</a>
    </div>
  </c:if>
  <div class="songListName">
    <span id="searchNameEl">Broken Link Reports</span>
  </div>
</div>
<table class="songList">
  <tr class="songListFooterRow">
    <td>&nbsp;</td>
    <td>Song</td>
    <%--<td>Artist</td>--%>
    <td>Link</td>
    <td>Visible</td>
    <td>Created</td>
    <td>Processed</td>
  </tr>
  <c:choose>
  <c:when test="${!foundBrokenLinkReports}">
  <tr class="songListOddRow">
    <td colspan="6">No reports.</td>
  </tr>
  </c:when>
  <c:otherwise>
    <c:forEach var="report" items="${reports}" varStatus="loopStatus">
      <c:set var="song" value="${report.song}"/>
  <tr class="<c:choose><c:when test="${loopStatus.index % 2 == 0}">songListOddRow</c:when><c:otherwise>songListEvenRow</c:otherwise></c:choose>">
    <td><c:out value="${start + loopStatus.index + 1}"/><c:if test="${(start + loopStatus.index + 1) < 10}">&nbsp</c:if></td>
    <c:url var="viewSongLink" value="/songs/${song.titleForUrl}-${song.id}"/>
    <td><a id="title<c:out value="${loopStatus.index}"/>" href="<c:out value="${viewSongLink}"/>" title="View '<c:out value="${song.title}"/>' Info/Comments"><c:out value="${song.title}"/></a></td>
<%--
    <c:url var="viewArtistLink" value="/artist">
      <c:param name="id" value="${song.artist.id}"/>
    </c:url>
    <td><a href="<c:out value="${viewArtistLink}"/>" title="View <c:out value="${song.artist.name}"/> Info"><c:out value="${song.artist.name}"/></a></td>
--%>
    <td><a target="_blank" href="<c:out value="${song.url}"/>"><c:out value="${song.url}"/></a></td>
    <td id="showSong<c:out value="${report.id}"/>">
      <c:choose>
        <c:when test="${song.showSong}">
          <a href="javascript:hideSong(<c:out value="${song.id}"/>, <c:out value="${report.id}"/>)" title="Hide song">true</a>
        </c:when>
        <c:otherwise>false</c:otherwise>
      </c:choose>
    </td>
    <td><fmt:formatDate value="${report.createDate}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
    <td id="processed<c:out value="${report.id}"/>"><a style="color:red;font-weight:bold" href="javascript:markProcessed(<c:out value="${report.id}"/>)">X</a></td>
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
  var hideSongURL = '<c:url value="/admin/hide_song"/>';
  function hideSong(songId, reportId) {
    new Ajax.Request(hideSongURL, {
      asynchronous : false,
      parameters : {
        id : songId
      },
      method : 'get',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          $('showSong'+reportId).innerHTML = 'false';
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not hide song, please try again');
      }
    });
  }
  var markProcessedURL = '<c:url value="/admin/process_broken_link_report"/>';
  function markProcessed(reportId) {
    new Ajax.Request(markProcessedURL, {
      asynchronous : false,
      parameters : {
        id : reportId
      },
      method : 'get',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          var procDate = new Date();
          $('processed'+reportId).innerHTML = (procDate.getMonth()+1)+'/'+procDate.getDate()+'/'+procDate.getFullYear()+' '+procDate.getHours()+':'+procDate.getMinutes()+':'+procDate.getSeconds();
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not process report, please try again');
      }
    });
  }
  //--></script>
  </body>
</html>