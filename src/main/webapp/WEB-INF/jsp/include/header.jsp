<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:set var="hRequestURI" value="${fn:substringAfter(pageContext.request.requestURI, 'WEB-INF/jsp')}"/>
<c:set var="hSelectedPage" value="${pageContext.request.contextPath}${fn:substringBefore(hRequestURI, '.')}"/>
<%--<!-- URI: ${pageContext.request.requestURI} -->--%>
<%--<!-- Context: ${pageContext.request.contextPath} -->--%>
<%--<!-- hURI: ${hRequestURI} -->--%>
<%--<!-- hPage: ${hSelectedPage} -->--%>
<div id="hd">
  <c:url var="hLoginPage" value="/login"/>
  <c:url var="hRegisterLink" value="/register"/>
  <c:choose>
    <c:when test="${loggedIn}">
      <div class="loginBox">
        Logged in as: <c:out value="${authName}"/>
        &nbsp;<a href="<c:url value="/logout"/>" id="logoutLink">Log Out</a>
      </div>
    </c:when>
    <c:when test="${not fn:startsWith(hSelectedPage, hLoginPage) and not fn:startsWith(hSelectedPage, hRegisterLink)}">
        <div class="loginBox">
<%-- this won't work because one url is being sent for both connect and signin, and i can only configure one or the other to work, and i chose conenct
          <form id="sc_signin" action="<c:url value="/signin/soundcloud"/>" method="POST"><input
                type="hidden" name="scope" value="non-expiring"><a
                href="javascript:document.forms.sc_signin.submit()" title="Log In With SoundCloud">Log In With SoundCloud</a></form>&nbsp;&nbsp;&nbsp;
--%>
          <c:if test="${not empty twitterAppId}">
          <form id="tw_signin" action="<c:url value="/signin/twitter"/>" method="POST"><a
                href="javascript:document.forms.tw_signin.submit()" title="Log In With Twitter">Log In With Twitter</a></form>&nbsp;&nbsp;&nbsp;
          </c:if>
          <c:if test="${not empty facebookAppId}">
          <form id="fb_signin" action="<c:url value="/signin/facebook"/>" method="POST"><input
                type="hidden" name="scope" value="publish_actions"><a
                href="javascript:document.forms.fb_signin.submit()" title="Log In With Facebook">Log In With Facebook</a></form>&nbsp;&nbsp;&nbsp;
          </c:if>
          <a href="<c:url value="/login_help"/>">Login Help</a>&nbsp;&nbsp;&nbsp;
          <form name="loginForm" action="<c:url value="/j_spring_security_check"/>" method="post"
              onsubmit="document.forms.loginForm['spring-security-redirect'].value=location.href">
            <input type="hidden" name="spring-security-redirect" value="">
            <input type="hidden" name="_spring_security_remember_me" value="true">
            <input type="submit" name="submitBtn" style="display:none">
          Username:&nbsp;<input
            type="text" name="j_username" size="10" style="font-size:75%;">&nbsp;Password:&nbsp;<input
            type="password" name="j_password" size="10" style="font-size:75%;">&nbsp;<input
            type="submit" name="submitBtn" value="Log In!" style="font-size:70%"></form></div>

      <%--<span onclick="document.forms.loginForm.submit()" style="cursor:pointer">Log In!</span>--%>
    </c:when>
  </c:choose>
  <c:url var="hIndexLink" value="/"/>
  <c:url var="hRootLink" value="/"/>
  <c:url var="hFaqLink" value="/faq"/>
  <c:url var="hSearchLink" value="/search"/>
  <c:url var="hPlaylistLink" value="/playlists"/>
  <%-- these next links are just used for highlighting the search link when searching artists and comments --%>
  <c:url var="hArtistsLink" value="/artists"/>
  <c:url var="hArtistsPath" value="/artist"/>
  <c:url var="hCommentsLink" value="/comments"/>
  <c:url var="hSongsLink" value="/songs"/>
  <c:url var="hSongsPath" value="/song"/>
  <c:url var="hPostSongLink" value="/user/post_song"/>
  <c:url var="hRecentLink" value="/recent"/>
  <c:url var="hMyInfoLink" value="/user/my_info"/>
  <c:url var="hFirstTimeLink" value="/first_time"/>
  <%--<span style="display:none">${hSelectedPage}|${hIndexLink}|${hRequestURI}|${hRootLink}</span>--%>
  <c:forEach var="siteLink" items="${siteLinks}">
    <c:if test="${siteLink.position eq 1}">
     <span><a href="${siteLink.url}" target="_blank" title="${siteLink.hover}">${siteLink.label}</a></span>
    </c:if>
  </c:forEach>
  <span<c:if test="${hSelectedPage eq hIndexLink or hRequestURI eq hRootLink}"> class="selected"</c:if>><a href="<c:out value="${hIndexLink}"/>"><spring:message code="site.name"/></a></span>
  <span<c:if test="${hSelectedPage eq hFaqLink}"> class="selected"</c:if>><a href="<c:out value="${hFaqLink}"/>">FAQ</a></span>
  <span<c:if test="${fn:startsWith(hSelectedPage, hSongsPath)}"> class="selected"</c:if>><a href="<c:out value="${hSongsLink}"/>">Songs</a></span>
  <span<c:if test="${fn:startsWith(hSelectedPage, hArtistsPath)}"> class="selected"</c:if>><a href="<c:out value="${hArtistsLink}"/>">Artists</a></span>
  <span<c:if test="${hSelectedPage eq hPlaylistLink}"> class="selected"</c:if>><a href="<c:out value="${hPlaylistLink}"/>">Playlists</a></span>
  <span<c:if test="${hSelectedPage eq hSearchLink or hSelectedPage eq hCommentsLink}"> class="selected"</c:if>><a href="<c:out value="${hSearchLink}"/>">Search</a></span>
  <c:if test="${canPostSongs or userCanPostSongs}">
  <span<c:if test="${hSelectedPage eq hPostSongLink}"> class="selected"</c:if>><a href="<c:out value="${hPostSongLink}"/>">Post Song</a></span>
  </c:if>
  <span<c:if test="${hSelectedPage eq hRecentLink}"> class="selected"</c:if>><a href="<c:out value="${hRecentLink}"/>">Recent Activity</a></span>
  <c:choose>
    <c:when test="${loggedIn}">
      <span<c:if test="${hSelectedPage eq hMyInfoLink}"> class="selected"</c:if>><a href="<c:out value="${hMyInfoLink}"/>">My Info</a></span>
    </c:when>
    <c:otherwise>
      <span<c:if test="${hSelectedPage eq hRegisterLink}"> class="selected"</c:if>><a href="<c:out value="${hRegisterLink}"/>">Register</a></span>
      <span<c:if test="${hSelectedPage eq hFirstTimeLink}"> class="selected"</c:if>><a style="color:green" href="<c:out value="${hFirstTimeLink}"/>">First Time?</a></span>
    </c:otherwise>
  </c:choose>
  <c:if test="${isAdmin}">
    <c:url var="hAdminLink" value="/admin/"/>
    <span<c:if test="${fn:contains(hRequestURI, hAdminLink)}"> class="selected"</c:if>><a href="<c:out value="${hAdminLink}"/>">Admin</a></span>
  </c:if>
  <c:forEach var="siteLink" items="${siteLinks}">
    <c:if test="${siteLink.position eq 2}">
      <span><a href="${siteLink.url}" target="_blank" title="${siteLink.hover}">${siteLink.label}</a></span>
    </c:if>
  </c:forEach>
</div>
