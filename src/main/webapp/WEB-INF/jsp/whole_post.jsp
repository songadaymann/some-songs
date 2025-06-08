<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: Whole Post '<c:out value="${subject}"/>'</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:if test="${back ne null}">
          <a href="<c:out value="${back}"/>">Return to <c:out value="${type}"/> '<c:out value="${subject}"/>'</a>

          <br> <br>
          </c:if>

          <table class="postTable">
            <c:url var="postUserLink" value="/profile">
              <c:param name="id" value="${postUser.id}"/>
            </c:url>
            <tr class="postTopRow">
              <td align="left"><a href="<c:out value="${postUserLink}"/>"><c:out value="${postUser.displayName}"/></a></td>
              <td align="right">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="2" class="postContent">
                <ssj:escapeMessageBoardPost content="${content}"/><c:if
                  test="${moreContent ne null}"><ssj:escapeMessageBoardPost content="${moreContent}"/></c:if>
              </td>
            </tr>
            <tr>
              <td colspan="2" class="postBottomRow">
                Posted: <fmt:formatDate value="${createDate}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
                &nbsp;&nbsp;&nbsp;&nbsp;
                Edited: <fmt:formatDate value="${changeDate}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
              </td>
            </tr>
          </table>

          <br> <br>

        </div></div>

      </div>

      <c:if test="${type eq 'thread'}">
      <div id="rightNavBox" class="yui-b">
        <jsp:include page="/WEB-INF/jsp/include/message_board_rightnav.jsp"/>
      </div>
      </c:if>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>
