<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:if test="${artist.links ne null and not empty artist.links}">
  <table class="songInfoTable">
    <tr class="grayBottom">
      <td><b>Links:</b></td>
    </tr>
    <c:forEach var="artistLink" items="${artist.links}" varStatus="loopStatus">
    <tr class="<c:choose><c:when test="${loopStatus.index mod 2 eq 0}">oddRow</c:when><c:otherwise>evenRow</c:otherwise></c:choose>">
      <td>
        <c:if test="${canEdit}">
          <span style="float:right;margin-left:20px;"><a class="editLink" href="javascript:editLink(<c:out value="${artistLink.id}"/>)">Edit</a></span>
        </c:if>
        <a href="<c:out value="${artistLink.url}"/>" target="_blank"><c:out value="${artistLink.label}"/></a><br>
        <c:out value="${artistLink.notes}"/>
      </td>
    </tr>
    </c:forEach>
  </table>

  <br> <br>
</c:if>
