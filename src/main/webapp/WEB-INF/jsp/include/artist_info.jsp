<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<table class="songInfoTable">
  <tr class="grayBottom">
    <td align="right" width="120" valign="middle"><b>Artist Info:</b>&nbsp;</td>
    <td valign="middle">
      <c:if test="${canEdit}">
        <%--<a id="editArtistLink" href="<c:url value="/user/edit_artist"/>" style="display:none;"> </a>--%>
        <span style="float:right;margin-left:20px;"><a class="editLink" href="javascript:editArtist()">Edit</a></span>
      </c:if>
      <c:out value="${artist.name}"/>
    </td>
  </tr>
  <c:if test="${not empty artist.bandcampUrl}">
  <tr>
    <td align="right"><b><a target="_blank" href="http://bandcamp.com/">Bandcamp</a> URL:</b>&nbsp;</td>
    <td><a target="_blank" href="<c:if test="${!fn:startsWith(artist.bandcampUrl, 'http://')}">http://</c:if><c:out
    value="${artist.bandcampUrl}"/>"><c:out value="${artist.bandcampUrl}"/></a></td>
  </tr>
  </c:if>
  <tr>
    <td colspan="2">&nbsp;
      <c:out value="${artist.info}"/>
      <%--
      <c:if test="${artist.relatedArtists ne null and not empty artist.relatedArtists}">
        <b>See also:</b>
        <ul>
          <c:forEach var="relatedArtist" items="${artist.relatedArtists}">
            <c:url var="artistUrl" value="artist">
              <c:param name="id" value="${relatedArtist.id}"/>
            </c:url>
            <li><a href="<c:out value="${artistUrl}"/>"><c:out value="${relatedArtist.name}"/></a></li>
          </c:forEach>
        </ul>
      </c:if>
--%>
      <c:if test="${isAdmin}">
        <br> <br>
        <c:url var="ownerURL" value="/profile">
          <c:param name="id" value="${artist.user.id}"/>
        </c:url>
        (User: <a href="<c:out value="${ownerURL}"/>"><c:out value="${artist.user.displayName}"/></a>)
      </c:if>
    </td>
  </tr>
</table>
