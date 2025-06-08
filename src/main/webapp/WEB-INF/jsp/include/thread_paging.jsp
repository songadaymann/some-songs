<%--<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="songListHeader">
  <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
  <div class="songListPaging">
    <c:if test="${search.numPages > 1}">
      Page:
      <c:forEach var="pageStartNum" items="${search.pageStartNums}" varStatus="loopStatus">
        <c:url var="pageLink" value="/thread">
          <c:param name="id" value="${thread.id}"/>
          <c:param name="start" value="${pageStartNum}"/>
        </c:url>
        <a href="<c:out value="${pageLink}"/>"<c:if test="${search.startResultNum eq pageStartNum}"> style="border: 1px solid black"</c:if>><c:out value="${loopStatus.index + 1}"/></a>
      </c:forEach>
    </c:if>
  </div>
  <div class="songListName">
    <c:out value="${thread.subject}"/>
  </div>
  <%--<div style="clear:both;height:1px;">&nbsp;</div>--%>
</div>

<br>

<c:if test="${loggedIn}">
<c:choose>
  <c:when test="${isAdmin or !thread.locked}">
    <c:url var="replyThreadLink" value="/user/reply">
      <c:param name="threadId" value="${thread.id}"/>
    </c:url>
    <a href="javascript:postToThread()" class="postLink">Post to Thread</a>
  </c:when>
  <c:otherwise>
    <p style="color:red">This thread has been locked by an administrator.</p>
  </c:otherwise>
</c:choose>
<c:if test="${isAdmin}">
  <br> <br>
  <a class="addLink" <c:if test="${thread.locked}">style="display:none" </c:if>href="javascript:toggleLocked()"><span>&nbsp;+&nbsp;</span> Lock Thread</a>
  <a class="removeLink" <c:if test="${!thread.locked}">style="display:none" </c:if>href="javascript:toggleLocked()"><span>&nbsp;-&nbsp;</span> Unlock Thread</a>
</c:if>
</c:if>
