<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<c:set var="fRequestURI" value="${fn:substringAfter(pageContext.request.requestURI, 'WEB-INF/jsp')}"/>
<c:set var="fSelectedPage" value="${pageContext.request.contextPath}${fn:substringBefore(fRequestURI, '.')}"/>
<div id="ft">
  <span id="footerLinksBox">
    <ul id="footerLinks">
      <c:url var="fIndexLink" value="/"/>
      <c:url var="fRootLink" value="/"/>
      <c:url var="fFaqLink" value="/faq"/>
      <c:url var="fStatsLink" value="/statistics"/>
      <c:url var="fFeedbackLink" value="/contact_admin"/>
      <c:url var="fMessageBoardLink" value="/message_board"/>
      <c:url var="fThreadLink" value="/thread"/>
      <c:url var="fReplyLink" value="/user/reply"/>
      <c:url var="fPostsLink" value="/WEB-INF/jsp/message_board_search.jsp"/>
      <%--<span style="display:none">${fSelectedPage}|${fIndexLink}|${fRequestURI}|${fRootLink}</span>--%>
      <c:forEach var="siteLink" items="${siteLinks}">
        <c:if test="${siteLink.position eq 3}">
          <li><a href="${siteLink.url}" target="_blank" title="${siteLink.hover}">${siteLink.label}</a></li>
        </c:if>
      </c:forEach>
      <li<c:if test="${fSelectedPage eq fIndexLink or fRequestURI eq fRootLink}"> class="selected"</c:if>><a href="<c:out value="${fIndexLink}"/>"><spring:message code="site.name"/></a></li>
      <li<c:if test="${fSelectedPage eq fFaqLink}"> class="selected"</c:if>><a href="<c:out value="${fFaqLink}"/>">FAQ</a></li>
      <li<c:if test="${fSelectedPage eq fStatsLink}"> class="selected"</c:if>><a href="<c:out value="${fStatsLink}"/>">Statistics</a></li>
      <li<c:if test="${fSelectedPage eq fFeedbackLink}"> class="selected"</c:if>><a href="<c:out value="${fFeedbackLink}"/>">Contact Admin</a></li>
      <li<c:if test="${fSelectedPage eq fMessageBoardLink or fSelectedPage eq fThreadLink or fSelectedPage eq fReplyLink or fSelectedPage eq fPostsLink}"> class="selected"</c:if>><a href="<c:out value="${fMessageBoardLink}"/>">Message Board</a></li>
      <c:forEach var="siteLink" items="${siteLinks}">
        <c:if test="${siteLink.position eq 4}">
          <li><a href="${siteLink.url}" target="_blank" title="${siteLink.hover}">${siteLink.label}</a></li>
        </c:if>
      </c:forEach>
    </ul>
  </span>
</div>
<%--<script type="text/javascript" src="<c:url value="/js/deliciousplayer.js"/>"></script>--%>
