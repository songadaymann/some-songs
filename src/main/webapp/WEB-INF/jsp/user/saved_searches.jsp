<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="../include/head.jsp"%>
    <title><spring:message code="site.name"/>: Saved Searches</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">Saved Searches</div>

          <!--<table class="songInfoTable" width="400">-->
            <c:choose>
              <c:when test="${fn:length(savedSearches) eq 0}">
<%--
            <tr>
              <td>You do not have any saved searches.</td>
            </tr>
--%>
            <p>You do not have any saved searches.</p>
              </c:when>
              <c:otherwise>
                <ul id="searches" class="myInfoList" style="width:400px;overflow:hidden">
              <c:forEach var="savedSearch" items="${savedSearches}" varStatus="loopStatus">
                <c:set var="itemStyle" value="oddRow"/>
                <c:if test="${loopStatus.index % 2 != 0}">
                  <c:set var="itemStyle" value="evenRow"/>
                </c:if>
                <c:url var="savedSearchLink" value="/search">
                  <c:param name="id" value="${savedSearch.id}"/>
                  <c:param name="search" value="true"/>
                </c:url>
<%--
                <tr class="<c:out value="${rowStyle}"/>">
                  <td align="left" style="overflow:hidden;"><a href="<c:out value="${savedSearchLink}"/>" title="Run search '<c:out value="${savedSearch.name}"/>'"><c:out value="${savedSearch.name}"/></a></td>
                  <td width="50">Edit</td>
                  <td width="50">Delete</td>
                </tr>
--%>
                <li id="ss<c:out value="${savedSearch.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${savedSearchLink}"/>" title="Run search '<c:out value="${savedSearch.name}"/>'"><c:out value="${savedSearch.name}"/></a></li>
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
  <%--<script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>--%>
  <script type="text/javascript"><!--
  var addEventHandlers = function() {
    var myInfoList = $('searches');
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
    if (element.childElements().size() == 1) {
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
        alert('Could not delete saved search, please try again');
      }
    });
  }
//--></script>
</body>
</html>