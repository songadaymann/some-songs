<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: First Time Info</title>
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
              <span style="font-weight:bold">First Time Info about <spring:message code="site.name"/>:</span>
            </div>
            <div class="songCommentsHeader">
              <c:choose>
                <c:when test="${firstTimeContent ne null and fn:length(firstTimeContent) > 0}">
                  <c:forEach var="firstTime" items="${firstTimeContent}" varStatus="loopStatus">
                    <c:set var="rowStyle" value="oddRow"/>
                    <c:if test="${loopStatus.index % 2 != 0}">
                      <c:set var="rowStyle" value="evenRow"/>
                    </c:if>
              <div class="<c:out value="${rowStyle}"/>" style="padding:4px;">
                  <ssj:escapeMessageBoardPost content="${firstTime.content}"/>
              </div>
                  </c:forEach>
                </c:when>
                <c:otherwise>
              <div>
                No First Time info available.
              </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>

          <br> <br> <br>

          <%--<jsp:include page="include/thread_paging.jsp"/>--%>

        </div></div>

      </div>

<%--
      <div id="rightNavBox" class="yui-b">
        <jsp:include page="include/message_board_rightnav.jsp"/>
      </div>
--%>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>