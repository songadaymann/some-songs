<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Message Board Topics</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">Manage Message Board Topics:</div>

          <c:choose>
            <c:when test="${param['saved'] eq 'true'}">
              <div class="successMessage">
                Your topic has been saved.
              </div>
            </c:when>
            <c:when test="${param['deleted'] eq 'true'}">
              <div class="successMessage">
                The topic has been deleted.
              </div>
            </c:when>
            <c:when test="${param['error'] ne null}">
              <div class="errors">
                Unable to delete topic:
                <br>
                <c:out value="${param['error']}"/>
              </div>
            </c:when>
          </c:choose>

          <c:choose>
            <c:when test="${topics ne null and not empty topics}">
              <table>
              <c:forEach var="topic" items="${topics}">
                <tr>
                  <td>
                    <a href="<c:url value="/admin/topic"/>?id=<c:out value="${topic.id}"/>" title="Click to edit topic"><c:out value="${topic.name}"/></a>
                    (<a href="<c:url value="/admin/delete_topic"/>?id=<c:out value="${topic.id}"/>">delete</a>)
                  </td>
                </tr>
              </c:forEach>
              </table>
            </c:when>
            <c:otherwise>
              There are no topics.
            </c:otherwise>
          </c:choose>


          <br> <br>

          <a href="<c:url value="/admin/topic"/>">Add Topic</a>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="include/right_nav.jsp"/>        
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>