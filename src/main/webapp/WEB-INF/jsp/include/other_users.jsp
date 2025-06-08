<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<ul class="myInfoList">
  <c:forEach var="otherUser" items="${otherUsers}" varStatus="loopStatus">
    <c:set var="itemStyle" value="evenRow"/>
    <c:if test="${loopStatus.index mod 2 eq 0}">
      <c:set var="itemStyle" value="oddRow"/>
    </c:if>
    <c:url var="userURL" value="/profile">
      <c:param name="id" value="${otherUser.user.id}"/>
    </c:url>
    <li id="ou<c:url value="${otherUser.user.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${userURL}"/>"><c:out value="${otherUser.user.displayName}"/></a></li>
  </c:forEach>
</ul>
