<%@ page import="com.ssj.model.song.search.SongSearch" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%--<c:set scope="session" var="artistSearch" value="${artistSearch}"/>--%>
<%--<c:set var="artistSearch" value="${sessionScope['artistSearch']}"/>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%--<title><spring:message code="site.name"/>: <c:out value="${artistSearch.name}"/>, <c:out value="${artistSearch.startResultNum + 1}"/>-<c:out value="${artistSearch.startResultNum + artistSearch.resultsPerPage}"/></title>--%>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
      <title><spring:message code="site.name"/>: Artist Search Results</title>
      <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <table class="songInfoTable">
            <tr>
              <td>
                <b>Artist Index:</b>
                <br>
                <div style="white-space:nowrap;">
                  <c:forEach var="firstCharacter" items="${artistIndex}" varStatus="loopStatus">
                    <c:url var="searchURL" value="/artist_search">
                      <c:param name="search" value="true"/>
                      <c:param name="nameStartsWith" value="${firstCharacter}"/>
                    </c:url>
                    <c:if test="${fn:substring(artistSearch.nameStartsWith, 0, 1) eq firstCharacter}">
                      <b>
                    </c:if>
                    <a href="<c:out value="${searchURL}"/>">[ <c:out value="${firstCharacter}"/> ]</a>
                    <c:if test="${fn:substring(artistSearch.nameStartsWith, 0, 1) eq firstCharacter}">
                      </b>
                    </c:if>
                  </c:forEach>
                </div>
              </td>
            </tr>
          </table>

          <br> <br>

          <table class="songInfoTable">
            <tr>
              <td>
                <c:if test="${artistSearch.totalResults > 0}">
                   <div class="songListPaging" style="margin-left:20px;">
                     <c:set var="cBaseLink" value="/artists"/>
                     <c:if test="${artistSearch.previousPage}">
                       <c:url var="cPrevLink" value="${cBaseLink}">
                         <c:param name="start" value="${artistSearch.previousPageStartNum + 1}"/>
                       </c:url>
                       <c:set var="linkWord" value="previous"/>
                       <c:if test="${artistSearch.previousPageStartNum eq 0}">
                         <c:set var="linkWord" value="first"/>
                       </c:if>
                     <a class="previousPageLink" href="<c:out value="${cPrevLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${artistSearch.resultsPerPage}"/>]</a>
                     </c:if>
                     <span class="currentPageTest"><c:out value="${artistSearch.startResultNum + 1}"/>-<c:out value="${artistSearch.endResultNum + 1}"/> of <c:out value="${artistSearch.totalResults}"/></span>
                     <c:if test="${artistSearch.nextPage}">
                       <c:set var="nextResults" value="${artistSearch.resultsPerPage}"/>
                       <c:if test="${artistSearch.resultsPerNextPage > 0}">
                         <c:set var="nextResults" value="${artistSearch.resultsPerNextPage}"/>
                       </c:if>
                       <c:url var="cNextLink" value="${cBaseLink}">
                         <c:if test="${artistSearch.resultsPerNextPage > 0}">
                           <c:param name="resultsPerPage" value="${artistSearch.resultsPerNextPage}"/>
                         </c:if>
                         <c:param name="start" value="${artistSearch.nextPageStartNum + 1}"/>
                       </c:url>
                       <c:set var="linkWord" value="next"/>
                       <c:if test="${(artistSearch.totalResults - nextResults) le (artistSearch.nextPageStartNum)}">
                         <c:set var="linkWord" value="last"/>
                       </c:if>
                     <a class="nextPageLink" href="<c:out value="${cNextLink}"/>">[<c:out value="${linkWord}"/> <c:out value="${nextResults}"/>]</a>
                     </c:if>
                     <c:if test="${artistSearch.totalResults > artistSearch.resultsPerPage}">
                     <span class="clickable" title="Jump to results" onclick="jumpPages(this, '<c:url value="/artists"/>', <c:out value="${artistSearch.totalResults}"/>, <c:out value="${artistSearch.resultsPerPage}"/>)">[jump...]</span>
                     </c:if>
                   </div>
                 </c:if>
                 <b>Artist Search:</b>
              </td>
            </tr>
            <c:choose>
              <c:when test="${artistSearch.totalResults eq 0}">
                <tr class="oddRow">
                  <td>No artists matched your search.</td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="artist" items="${artists}" varStatus="loopStatus">
                  <c:set var="rowStyle" value="oddRow"/>
                  <c:if test="${loopStatus.index % 2 != 0}">
                    <c:set var="rowStyle" value="evenRow"/>
                  </c:if>
                  <tr class="<c:out value="${rowStyle}"/>">
                    <td><a href="<c:url value="/artists/${artist.nameForUrl}-${artist.id}"/>"><c:out value="${artist.name}"/></a></td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </table>

          <br> <br>

          <%@ include file="/WEB-INF/jsp/include/artist_search_form.jsp" %>

          <br> <br> <br>

        </div></div>

      </div>

      <%--<div id="rightNavBox" class="yui-b"><%@ include file="include/song_list_right_nav.jsp"%></div>--%>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>