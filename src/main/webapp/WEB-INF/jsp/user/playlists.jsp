<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: My Playlists</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">
            <span style="float:right"><a id="addPlaylistLink" href="<c:url value="/user/add_playlist"/>" onclick="return false">+ Add Playlist</a>&nbsp;</span>
            <span>My Playlists</span>
          </div>

            <c:choose>
              <c:when test="${fn:length(playlists) eq 0}">
            <p>You do not have any playlists.</p>
              </c:when>
              <c:otherwise>
                <ul id="playlists" class="myInfoList" style="width:400px;overflow:hidden">
              <c:forEach var="myPlaylist" items="${playlists}" varStatus="loopStatus">
                <c:set var="itemStyle" value="oddRow"/>
                <c:if test="${loopStatus.index % 2 != 0}">
                  <c:set var="itemStyle" value="evenRow"/>
                </c:if>
                <c:url var="playlistLink" value="/playlist">
                  <c:param name="id" value="${myPlaylist.id}"/>
                </c:url>
                <li id="pl<c:out value="${myPlaylist.id}"/>" class="<c:out value="${itemStyle}"/>">
                  <a style="font-weight:bold;" href="<c:out value="${playlistLink}"/>" title="View playlist '<c:out value="${myPlaylist.title}"/>'"><c:out value="${myPlaylist.title}"/></a><br>
                  <c:out value="${myPlaylist.description}"/>
                </li>
              </c:forEach>
                </ul>
              </c:otherwise>
            </c:choose>
          <!--</table>-->

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">

      </div>

    </div>

    <jsp:include page="../include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
  <script type="text/javascript"><!--
    var playlistURL = '<c:url value="/playlist"/>';
  //--></script>
  <script type="text/javascript" src="<c:url value="/js/add_playlist.js"/>"></script>
  <script type="text/javascript"><!--
  var addEventHandlers = function() {
    var myInfoList = $('playlists');
    if (myInfoList) {
      var listItems = myInfoList.childElements();
      listItems.each(function(listItem) {
        listItem.observe('mouseover', listItemMouseoverHandler)
      });
    }
  }
  var displayedId;
  var listItemMouseoverHandler = function(event) {
    var element = event.element();
    if (!element.id) {
      // event is from the link, we want the list item
      element = element.up();
    }
    if (element.id.match(/^delete/)) {
      // event was from the delete link, ignore it
      return;
    }
    if (displayedId && displayedId != element.id) {
      $('delete'+displayedId).hide();
    }
    if (element.childElements().size() == 2) {
      element.insert({top:'<span id="delete'+element.id+'" style="float:right"><a class="myInfoDelete" title="Delete" href="javascript:deleteMyInfo(\''+element.id+'\')">X</a></span>'});
    } else {
      $('delete'+element.id).show();
    }
    displayedId = element.id;
  }
  Event.observe(window,'load',addEventHandlers);
  var deleteItemURL = '<c:url value="/user/delete_item"/>';
  function deleteMyInfo(itemId) {
    new Ajax.Request(deleteItemURL, {
      asynchronous : false,
      parameters : {
        itemId : itemId
      },
      method : 'post',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          // TODO reload just the changed section of the page
          location.reload();
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not delete playlist, please try again');
      }
    });
  }
//--></script>
</body>
</html>