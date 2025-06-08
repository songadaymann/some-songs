<%@ page import="com.ssj.model.messageboard.search.MessageBoardPostSearch" %>
<%--<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<b>Topics:</b>
<ul>
  <li><a href="message_board">All</a></li>
  <c:forEach var="topic" items="${topics}" varStatus="loopStatus">
    <c:url var="topicLink" value="/message_board">
      <c:param name="topicId" value="${topic.id}"/>
    </c:url>
    <c:set var="topicStyle" value=""/>
    <c:if test="${topic.id eq currentTopic.id}">
      <c:set var="topicStyle" value=" class=boxed"/>
    </c:if>
    <li><a href="<c:out value="${topicLink}"/>"<c:out value="${topicStyle}"/>><c:out value="${topic.name}"/></a></li>
  </c:forEach>
</ul>
<br>
<form name="messageBoardSearchForm" action="<c:url value="/search_message_board"/>" method="post">
  <input type="hidden" name="search" value="true">
  <table>
    <tr>
      <td colspan="2">
        <b>Search:</b>
      </td>
    </tr>
    <tr>
      <td><b>In post:</b></td>
      <td><input name="content" value="<c:out value="${postSearch.content}"/>"></td>
    </tr>
    <tr>
      <td><b>Posted by:</b></td>
      <td><input name="authorName" value="<c:out value="${postSearch.authorName}"/>"></td>
    </tr>
    <tr>
      <td><b>Topic:</b></td>
      <td>
        <select name="topic.id">
          <option value="0">All Topics</option>
          <c:forEach var="topic" items="${topics}" varStatus="loopStatus">
            <c:set var="topicSelected" value=""/>
            <c:if test="${postSearch.topic.id eq topic.id}">
              <c:set var="topicSelected" value=" selected"/>
            </c:if>
          <option value="<c:out value="${topic.id}"/>"<c:out value="${topicSelected}"/>><c:out value="${topic.name}"/></option>
          </c:forEach>
        </select>
      </td>
    </tr>
    <tr>
      <td><b>Sort:</b></td>
      <td>
        <select name="orderBy">
          <option value="<%= MessageBoardPostSearch.ORDER_BY_POST_DATE %>">Newest</option>
          <option value="<%= MessageBoardPostSearch.ORDER_BY_POST_DATE_ASC %>">Oldest</option>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="2"><input type="submit" value="Search"><br>
    </tr>
  </table>
</form>
