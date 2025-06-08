<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<ul style="background:white;border:1px solid #999;padding:2px;list-style:none;">
  <c:forEach var="result" items="${results}" varStatus="loopStatus">
    <c:set var="itemStyle" value="evenRow"/>
    <c:if test="${loopStatus.index mod 2 eq 0}">
      <c:set var="itemStyle" value="oddRow"/>
    </c:if>
    <li id="<c:out value="${result.key}"/>" class="${itemStyle}" style="cursor:pointer" title="<c:out value="${titleText}"/> '<c:out value="${result.value}"/>'"><c:out value="${result.value}"/></li>
  </c:forEach>
</ul>
