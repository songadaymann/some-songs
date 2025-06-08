<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="str" uri="http://jakarta.apache.org/taglibs/string-1.1" %>
<%@ taglib prefix="ssj" uri="SSJTags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<security:authentication property="authenticated" var="isAuthenticated"/>
<security:authentication property="name" var="authName"/>
<c:set scope="request" var="loggedIn" value="${isAuthenticated and authName ne 'anonymousUser'}"/>
<security:authentication property="authorities" var="authorities"/>
<c:forEach var="authority" items="${authorities}">
  <c:if test="${authority.authority eq 'ROLE_ADMIN'}">
    <c:set scope="request" var="isAdmin" value="${true}"/>
  </c:if>
  <c:if test="${authority.authority eq 'ROLE_POSTSONGS'}">
    <c:set scope="request" var="userCanPostSongs" value="${true}"/>
  </c:if>
</c:forEach>
