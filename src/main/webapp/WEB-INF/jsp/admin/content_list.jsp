<%@ page import="com.ssj.model.content.PageContent" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Site Content</title>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">Site Content:</div>

          <c:if test="${param['saved'] eq 'true'}">
            <div class="successMessage">
              The content has been saved.
            </div>
          </c:if>
          <c:if test="${param['deleted'] eq 'true'}">
            <div class="successMessage">
              The content has been deleted.
            </div>
          </c:if>
          <c:if test="${deleteError ne null}">
            <div class="errors">
              <c:out value="${deleteError}"/>
            </div>
          </c:if>

          <h3>FAQs</h3>
          
          <div><a href="<c:url value="/admin/content"/>?type=<%= PageContent.TYPE_FAQ %>">Add Faq</a></div>
          
          <br> 

          <table>
            <c:choose>
              <c:when test="${faqContent ne null and fn:length(faqContent) > 0}">
                <c:forEach var="faq" items="${faqContent}" varStatus="loopStatus">
                  <c:set var="rowStyle" value="oddRow"/>
                  <c:if test="${loopStatus.index % 2 != 0}">
                    <c:set var="rowStyle" value="evenRow"/>
                  </c:if>
            <tr class="<c:out value="${rowStyle}"/>">
              <td>
                <p><ssj:escapeMessageBoardPost content="${faq.content}"/></p>
                <a href="<c:url value="/admin/content"/>?id=<c:out value="${faq.id}"/>" title="Click to edit FAQ">edit</a> |
                <a href="<c:url value="/admin/delete_content"/>?id=<c:out value="${faq.id}"/>">delete</a>
              </td>
            </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr>
                  <td>No FAQs available.</td>
                </tr>
              </c:otherwise>
            </c:choose>
          </table>

          <br> <br>

          <h3>First Time</h3>

          <div><a href="<c:url value="/admin/content"/>?type=<%= PageContent.TYPE_FIRST_TIME %>">Add First Time</a></div>

          <br>

          <table>
            <c:choose>
              <c:when test="${firstTimeContent ne null and fn:length(firstTimeContent) > 0}">
                <c:forEach var="firstTime" items="${firstTimeContent}" varStatus="loopStatus">
                  <c:set var="rowStyle" value="oddRow"/>
                  <c:if test="${loopStatus.index % 2 != 0}">
                    <c:set var="rowStyle" value="evenRow"/>
                  </c:if>
                  <tr class="<c:out value="${rowStyle}"/>">
                    <td>
                      <p><ssj:escapeMessageBoardPost content="${firstTime.content}"/></p>
                      <a href="<c:url value="/admin/content"/>?id=<c:out value="${firstTime.id}"/>" title="Click to edit First Time">edit</a> |
                      <a href="<c:url value="/admin/delete_content"/>?id=<c:out value="${firstTime.id}"/>">delete</a>
                    </td>
                  </tr>
                      </c:forEach>
                    </c:when>
                    <c:otherwise>
                      <tr>
                        <td>No First Time content available.</td>
                      </tr>
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
  </body>
</html>