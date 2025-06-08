<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: Message to Admin</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:if test="${param['msg'] ne null}">
            <div class="successMessage">
              <c:out value="${param['msg']}"/>
            </div>
            <br>
          </c:if>

          <div class="songComments">
            <div class="songCommentsHeader">
              <span style="font-weight:bold">Anonymous Messages:</span>
            </div>
            <div class="songCommentsHeader">
              <c:choose>
                <c:when test="${anonymousMessages ne null and fn:length(anonymousMessages) > 0}">
                  <c:forEach var="anonymousMessage" items="${anonymousMessages}" varStatus="loopStatus">
                    <c:set var="rowStyle" value="oddRow"/>
                    <c:if test="${loopStatus.index % 2 != 0}">
                      <c:set var="rowStyle" value="evenRow"/>
                    </c:if>
              <div id="anonymousMessage<c:out value="${anonymousMessage.id}"/>" style="padding:4px" class="<c:out value="${rowStyle}"/>">
                  <ssj:escapeMessageBoardPost content="${anonymousMessage.content}"/>
                <br>
                <div>
                  ---
                  <span class="medium"><fmt:formatDate value="${anonymousMessage.createDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></span>
                </div>
                <br> 
                <a class="editLink" href="javascript:deleteAnonymousMessage(<c:out value="${anonymousMessage.id}"/>)">Delete</a>
              </div>
                  </c:forEach>
                </c:when>
                <c:otherwise>
              <div>
                No anonymous messages.
              </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>

          <br> <br>
          
          <div id="comments" class="songComments">
            <div class="songCommentsHeader">
              <span style="font-weight:bold">User Messages:</span>
            </div>
            <div id="noComments"<c:if test="${userMessages ne null and fn:length(userMessages) gt 0}"> style="display:none"</c:if>>None.</div>
            <div class="songListHeader" style="border-top:0<c:if test="${userMessages eq null or fn:length(userMessages) eq 0}">;display:none</c:if>">
              <c:forEach var="userMessage" items="${userMessages}" varStatus="loopStatus">
              <c:set var="commentStyle" value="evenRow"/>
              <c:if test="${loopStatus.index % 2 == 0}">
                <c:set var="commentStyle" value="oddRow"/>
              </c:if>
              <div id="userMessage<c:out value="${userMessage.id}"/>" class="<c:out value="${commentStyle}"/> songComment">
                <div><ssj:escapeSongComment content="${userMessage.content}"/></div>
                <span>---
                  <c:url var="userLink" value="/profile">
                    <c:param name="id" value="${userMessage.user.id}"/>
                  </c:url>
                  <a href="<c:out value="${userLink}"/>"><c:out value="${userMessage.user.displayName}"/></a>
                  <span class="medium"><fmt:formatDate value="${userMessage.createDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></span>
                </span>
                <br> <br>
                <div><a class="editLink" href="javascript:deleteUserMessage(<c:out value="${userMessage.id}"/>)">Delete</a></div>
              </div>
              </c:forEach>
            </div>
          </div>

          <br> <br> <br>

          <%--<jsp:include page="include/thread_paging.jsp"/>--%>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="include/right_nav.jsp"/>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" language="JavaScript"><!--
    var deleteMessageURL = '<c:url value="/admin/delete_message"/>';
    function deleteAnonymousMessage(id) {
      deleteMessage(id, true);
    }
    function deleteUserMessage(id) {
      deleteMessage(id, false);
    }
    function deleteMessage(id, anonymous) {
      new Ajax.Request(deleteMessageURL, {
        asynchronous:false,
        parameters:{
          id : id,
          anonymous : anonymous
        },
        method:'post',
        onSuccess:function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            $((anonymous ? 'anonymous' : 'user') + 'Message' + id).remove();
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure:function() {
          alert("Unable to delete message, please try again");
        }
      })
    }
  //--></script>
  </body>
</html>