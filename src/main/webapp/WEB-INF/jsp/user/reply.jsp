<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>--%>
<c:if test="${reply.id ne 0}">
  <c:set var="formNumber" value="${reply.id}"/>
</c:if>
<form:form action="user/reply" name="replyForm${formNumber}" cssClass="cmxform" commandName="reply">
  <c:choose>
    <c:when test="${reply.id ne 0}">
      <form:hidden path="id"/>
    </c:when>
    <c:when test="${reply.originalPost.id ne 0}">
      <input type="hidden" name="threadId" value="<c:out value="${reply.originalPost.id}"/>">
    </c:when>
  </c:choose>
  <form:hidden path="subject"/>

<%--
  <div id="global_errors">
    <form:errors path="*" cssClass="errors" element="div"/>
  </div>
--%>

  <%--<jsr303js:validate commandName="reply"/>--%>

  <fieldset>
    <legend><c:choose><c:when test="${reply.id eq 0}">Post to Thread</c:when><c:otherwise>Edit Post</c:otherwise></c:choose></legend>
    <ol>
      <li>
        <c:choose>
          <c:when test="${reply.id eq 0 and quotePosts ne null and fn:length(quotePosts) > 0}">
            <textarea name="content" id="postBody" style="width:100%;height:250px;"><c:forEach
              var="quotePost" items="${quotePosts}"><blockquote><c:out value="${quotePost.content}"/></blockquote>
-- <b><c:out value="${quotePost.user.displayName}"/></b>, <fmt:formatDate value="${quotePost.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>
</c:forEach></textarea>
          </c:when>
          <c:otherwise>
            <form:textarea path="content" id="postBody${formNumber}" cssStyle="width:100%;height:250px;"/>
          </c:otherwise>
        </c:choose>
      </li>
      <li>
        <div class="allowedTags">Allowed HTML: &lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
      </li>
      <li>
        <label for="submitBtn">&nbsp;</label>
        <input type="button" id="cancelBtn" name="cancelBtn" class="submitBtn" value="Cancel" onclick="cancelPost(<c:out value="${formNumber}"/>)">
        <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Post">
      </li>
    </ol>

  </fieldset>

</form:form>
