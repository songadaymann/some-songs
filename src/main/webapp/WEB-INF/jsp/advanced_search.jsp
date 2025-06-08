<%@ page import="com.ssj.model.song.search.SongSearch" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Search</title>
    <script type="text/javascript"><!--
    //--></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <form:form action="advanced_search" name="advancedSearchForm" commandName="advancedSearch">
          <%--<form name="songSearchForm" action="<c:url value="/advanced_search"/>" method="post">--%>
            <%--<form:hidden path="titleExactMatch" id="titleExactMatch"/>--%>
            <!--<input type="hidden" name="search" value="true">-->
            <!--<input type="hidden" name="titleExactMatch" value="false">-->
            <%--<c:set var="tableStyle" value=""/>--%>
            <%--<c:if test="${param['showBorder'] eq 'true'}">--%>
              <%--<c:set var="tableStyle" value="songInfoTable"/>--%>
            <%--</c:if>--%>
            <%--<table class="<c:out value="${tableStyle}"/>">--%>
            <div id="errorsDiv">
              <form:errors path="*" cssClass="errors" element="div"/>
            </div>
            <table class="songInfoTable">
              <tr class="grayBottom">
                <td colspan="2">
                  <b>Advanced Song Search:</b>
                </td>
              </tr>
              <tr>
                <td><b>Title:</b></td>
                <td><form:input path="title" id="title"/></td>
                <!--<td><input name="title" value=""></td>-->
              </tr>
              <tr>
                <td><b>Artist:</b></td>
                <td><form:input path="artistName" id="artistName"/></td>
                <!--<td><input name="artistName" value=""></td>-->
              </tr>
              <tr>
                <td><b>Album:</b></td>
                <td><form:input path="album" id="album"/></td>
              </tr>
              <tr>
                <td><b>Min. # of Ratings:</b></td>
                <td><form:input path="numRatingsMin" id="numRatingsMin"/></td>
                <!--<td><input name="numRatingsMin" value=""></td>-->
              </tr>
              <tr>
                <td><b>Date Posted:</b></td>
                <td>
                  <form:select path="datePosted" id="datePosted">
                    <form:option value="" label="Any Time"/>
                    <form:option value="1" label="Today"/>
                    <form:option value="7" label="In the Last Week"/>
                    <form:option value="30" label="In the Last 30 Days"/>
                    <form:option value="365" label="In the Last Year"/>
                    <form:option value="-1" label="Before Today"/>
                    <form:option value="-7" label="Before Last Week"/>
                    <form:option value="-30" label="Before 30 Days Ago"/>
                    <form:option value="-365" label="Before a Year Ago"/>
                  </form:select>
<%--
                  <select name="datePosted">
                    <option value="">Any Time</option>
                    <option value="1">Today</option>
                    <option value="7">In the Last Week</option>
                    <option value="30">In the Last 30 Days</option>
                    <option value="365">In the Last Year</option>
                    <option value="-1">Before Today</option>
                    <option value="-7">Before Last Week</option>
                    <option value="-30">Before 30 Days Ago</option>
                    <option value="-365">Before a Year Ago</option>
                  </select>
--%>
                </td>
              </tr>
            <c:if test="${loggedIn}">
              <tr>
                <td colspan="2">
                  <%--<form:checkbox path="inUsersFavorites" id="inUsersFavorites" value="user.id"/>--%>
                  <input type="checkbox" name="inUsersFavorites" value="<c:out value="${user.id}"/>"<c:if test="${advancedSearch.inUsersFavorites ne null}"> checked="true"</c:if>>
                  <input type="hidden" value="on" name="_inUsersFavorites"/>
                  Favorite Song
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <input type="checkbox" name="inUsersFavoriteArtists" value="<c:out value="${user.id}"/>"<c:if test="${advancedSearch.inUsersFavoriteArtists ne null}"> checked="true"</c:if>>
                  <input type="hidden" value="on" name="_inUsersFavoriteArtists"/>
                  By Favorite Artist
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <input type="checkbox" name="hasCommentByUser" value="<c:out value="${user.id}"/>"<c:if test="${advancedSearch.hasCommentByUser ne null}"> checked="true"</c:if>>
                  <input type="hidden" value="on" name="_hasCommentByUser"/>
                  Has Comment By Me
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <input type="checkbox" name="notRatedByUser" value="1"<c:if test="${advancedSearch.notRatedByUser ne null}"> checked="true"</c:if>>
                  Not Rated By Me
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <input type="checkbox" name="ratedGoodByUser" value="1"<c:if test="${advancedSearch.ratedGoodByUser ne null}"> checked="true"</c:if>>
                  Rated 'Good' By Me
                </td>
              </tr>
            </c:if>
              <tr>
                <td><b>Sort:</b></td>
                <td>
                  <select name="orderBy">
                    <option value="<%= SongSearch.ORDER_BY_AVG_RATING %>">Highest Rating</option>
                    <option value="<%= SongSearch.ORDER_BY_CREATE_DATE %>">Newest</option>
                    <option value="<%= SongSearch.ORDER_BY_NUM_RATINGS %>">Most Votes</option>
                    <option value="-<%= SongSearch.ORDER_BY_AVG_RATING %>">Lowest Rating</option>
                    <option value="-<%= SongSearch.ORDER_BY_CREATE_DATE %>">Oldest</option>
                    <option value="-<%= SongSearch.ORDER_BY_NUM_RATINGS %>">Fewest Votes</option>
                    <option value="<%= SongSearch.ORDER_BY_RANDOM %>">Random</option>
                    <option value="<%= SongSearch.ORDER_BY_ALBUM %>">Random</option>
                    <%-- TODO order by preferred users' ratings --%>
                  </select>
                </td>
              </tr>
              <tr>
                <td colspan="2"><input type="submit" name="submitBtn" value="Search"><br>
                  <%--<a class="nextPageLink" href="<c:url value="/advanced_search"/>">[ Advanced Search ]</a></td>--%>
              </tr>
            </table>
          <!--</form>-->
          </form:form>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b"><%@ include file="/WEB-INF/jsp/include/song_list_right_nav.jsp"%></div>
      <!--<div id="rightNavBox" class="yui-b"></div>-->

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>
