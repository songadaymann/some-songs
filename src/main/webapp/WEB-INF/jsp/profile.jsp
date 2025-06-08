<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: User Info for <c:out value="${profileUser.displayName}"/></title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <%@ include file="/WEB-INF/jsp/include/inlineplayer_head.jsp"%>
    <c:if test="${loggedIn}">
    <script type="text/javascript" src="<c:url value="/js/effects-1.8.1.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
    <script type="text/javascript"><!--
    var ratingURL = '<c:url value="/user/rate_song"/>';
    //--></script>
    <script type="text/javascript" src="<c:url value="/js/song_list.js"/>"></script>
    </c:if>
    <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="userInfoDiv">
            <table class="songInfoTable">
              <tr class="grayBottom">
                <td align="right" width="120"><b>User Info:</b>&nbsp;</td>
                <td><c:out value="${profileUser.displayName}"/></td>
              </tr>
              <c:set var="rowNum" value="0"/>
              <c:if test="${profileUser.showEmailInUserInfo or isAdmin}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">E-mail:&nbsp;</td>
                <td><a href="mailto:<c:out value="${profileUser.email}"/>"><c:out value="${profileUser.email}"/></a></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <c:if test="${not empty profileUser.websiteURL}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">Website:&nbsp;</td>
                <td><a target="_blank" href="<c:if test="${!fn:startsWith(profileUser.websiteURL, 'http://')}">http://</c:if><c:out value="${profileUser.websiteURL}"/>"><c:choose><c:when
                    test="${not empty profileUser.websiteName}"><c:out value="${profileUser.websiteName}"/></c:when><c:otherwise
                    ><c:out value="${profileUser.websiteURL}"/></c:otherwise></c:choose></a></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">Registered:&nbsp;</td>
                <td><fmt:formatDate value="${profileUser.createDate}" type="date" pattern="yyyy-MM-dd"/></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">Location:&nbsp;</td>
                <c:url var="googleMapsURL" value="http://maps.google.com/maps?">
                  <c:param name="f" value="q"/>
                  <c:param name="hl" value="en"/>
                  <c:param name="q" value="${profileUser.location}"/>
                </c:url>
                <td><a href="<c:out value="${googleMapsURL}"/>" target="_blank" title="Search for this location on Google Maps"><c:out value="${profileUser.location}"/></a></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              <c:if test="${not empty profileUser.goodBand}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">A Good Band:&nbsp;</td>
                <c:url var="allMusicSearchURL" value="http://www.allmusic.com/search/artist/"/>
                <td><a href="<c:out value="${allMusicSearchURL}"/>${profileUser.goodBand}" target="_blank" title="Search for this band on the All Music Guide"><c:out value="${profileUser.goodBand}"/></a></td>
              </tr>
                <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <c:if test="${not empty profileUser.goodAlbum}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">A Good Album:&nbsp;</td>
                <c:url var="allMusicSearchURL" value="http://www.allmusic.com/search/album/"/>
                <td><a href="<c:out value="${allMusicSearchURL}${profileUser.goodAlbum}"/>" target="_blank" title="Search for this album on the All Music Guide"><c:out value="${profileUser.goodAlbum}"/></a></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <c:if test="${not empty profileUser.goodSong}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">A Good Song:&nbsp;</td>
                <c:url var="allMusicSearchURL" value="http://www.allmusic.com/search/song/"/>
                <td><a href="<c:out value="${allMusicSearchURL}${profileUser.goodSong}"/>" target="_blank" title="Search for this song on the All Music Guide"><c:out value="${profileUser.goodSong}"/></a></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <c:if test="${not empty profileUser.goodBook}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">A Good Book:&nbsp;</td>
                <td><c:out value="${profileUser.goodBook}"/></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <c:if test="${not empty profileUser.goodMovie}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">A Good Movie:&nbsp;</td>
                <c:url var="imdbSearchURL" value="http://www.imdb.com/find">
                  <c:param name="s" value="tt"/>
                  <c:param name="q" value="${profileUser.goodMovie}"/>
                </c:url>
                <td><a href="<c:out value="${imdbSearchURL}"/>" target="_blank" title="Search for this movie on the Internet Movie Database"><c:out value="${profileUser.goodMovie}"/></a></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <c:if test="${not empty profileUser.goodWebsiteURL}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">A Good Website:&nbsp;</td>
                <td><a target="_blank" href="<c:if test="${!fn:startsWith(profileUser.goodWebsiteURL, 'http://')}">http://</c:if><c:out value="${profileUser.goodWebsiteURL}"/>"><c:choose><c:when
                    test="${not empty profileUser.goodWebsiteName}"><c:out value="${profileUser.goodWebsiteName}"/></c:when><c:otherwise
                    ><c:out value="${profileUser.goodWebsiteURL}"/></c:otherwise></c:choose></a></td>
              </tr>
              <c:set var="rowNum" value="${rowNum + 1}"/>
              </c:if>
              <c:if test="${loggedIn}">
              <tr class="row<c:out value="${rowNum % 2}"/>">
                <td align="right">Agreement %:&nbsp;</td>
                <c:if test="${agreement ne null}">
                <td>~<c:out value="${agreement}"/>% </td>
                </c:if>
                <c:if test="${agreement eq null}">
                <td><span title="You have not rated any of the same songs as this user">N/A</span></td>
                </c:if>
              </tr>
                <c:set var="rowNum" value="${rowNum + 1}"/>
                <c:if test="${isAdmin}">
                  <tr class="row${rowNum % 2}">
                    <td align="right">Last Login:&nbsp;</td>
                    <td><fmt:formatDate value="${profileUser.lastLoginDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                  </tr>
                </c:if>
              </c:if>
            </table>
          </div>

          <br> <br>

          <table class="songInfoTable" width="75%">
            <tr style="border-bottom:1px solid #ccc">
              <td align="right" width="120"><b>Favorites:</b>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="padding:8px;">
                <jsp:include page="/song_list" flush="true">
                  <jsp:param name="inUsersFavorites" value="${profileUser.id}"/>
                </jsp:include>
                </div>
                <div style="padding:8px">
                  <table class="songInfoTable">
                    <tr>
                      <td><b>Favorite Artists</b>&nbsp;</td>
                    </tr>
                    <c:choose>
                      <c:when test="${not empty profileUser.favoriteArtists}">
                        <c:forEach var="favoriteArtist" items="${profileUser.favoriteArtists}" varStatus="loopStatus">
                          <c:set var="itemStyle" value="evenRow"/>
                          <c:if test="${loopStatus.index mod 2 eq 0}">
                            <c:set var="itemStyle" value="oddRow"/>
                          </c:if>
                     <tr class="<c:out value="${itemStyle}"/>">
                       <td>
                         <a href="<c:url value="/artists/${favoriteArtist.artist.nameForUrl}-${favoriteArtist.artist.id}"/>"><c:out value="${favoriteArtist.artist.name}"/></a>
                       </td>
                     </tr>
                        </c:forEach>
                      </c:when>
                      <c:otherwise>
                    <tr>
                      <td colspan="2"><b>none</b></td>
                    </tr>
                      </c:otherwise>
                    </c:choose>
                  </table>
                </div>
                <div style="padding:8px;">
                  <jsp:include page="/playlist_list" flush="true">
                    <jsp:param name="inUsersFavorites" value="${profileUser.id}"/>
                  </jsp:include>
                </div>
              </td>
            </tr>
          </table>

          <br> <br>

          <%-- this var is checked on the comments paging page to set the proper paging link --%>
          <c:set var="isProfileComments" value="true"/>
          <%@ include file="/WEB-INF/jsp/include/comment_list.jsp"%>

          <br> <br> <br>

        </div></div>

      </div>

      <c:if test="${loggedIn}">
      <div id="rightNavBox" class="yui-b">
        <div><b>Preffered Users:</b></div>
        <div>
          <a id="addPreferredLink" class="addLink" <c:if test="${isPreferred}">style="display:none" </c:if>href="javascript:togglePreferred()"><span>&nbsp;+&nbsp;</span> Add to Preferred Users</a>
          <a id="removePreferredLink" class="removeLink" <c:if test="${!isPreferred}">style="display:none" </c:if>href="javascript:togglePreferred()"><span>&nbsp;-&nbsp;</span> Remove from Preferred Users</a>
        </div>
        <br>
        <div><b>Ignored Users:</b></div>
        <div>
          <a id="addIgnoredLink" class="addLink" <c:if test="${isIgnored}">style="display:none" </c:if>href="javascript:toggleIgnored()"><span>&nbsp;+&nbsp;</span> Add to Ignored Users</a>
          <a id="removeIgnoredLink" class="removeLink" <c:if test="${!isIgnored}">style="display:none" </c:if>href="javascript:toggleIgnored()"><span>&nbsp;-&nbsp;</span> Remove from Ignored Users</a>
        </div>
        <br>
        <c:if test="${isAdmin}">
        <div><b>Admin Users:</b></div>
        <div>
          <a id="addAdminLink" class="addLink" <c:if test="${isUserAdmin}">style="display:none" </c:if>href="javascript:toggleAdmin()"><span>&nbsp;+&nbsp;</span> Add to Admin Users</a>
          <a id="removeAdminLink" class="removeLink" <c:if test="${!isUserAdmin}">style="display:none" </c:if>href="javascript:toggleAdmin()"><span>&nbsp;-&nbsp;</span> Remove from Admin Users</a>
        </div>
        <br>
        <a class="postLink" href="<c:url value="/admin/sign_in"><c:param name="id" value="${profileUser.id}"/></c:url>">Log in as this user</a>
        <br>
        </c:if>
      </div>
      </c:if>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <c:if test="${loggedIn}">
  <%--<script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>--%>
  <script type="text/javascript"><!--
  var userId = <c:out value="${profileUser.id}"/>;
  var preferredURL = '<c:url value="/user/preferred_user"/>';
  var ignoredURL = '<c:url value="/user/ignored_user"/>';
  function togglePreferred() {
    new Ajax.Request(preferredURL, {
      asynchronous : false,
      parameters : {
        userId : userId
      },
      method : 'post',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          $('addPreferredLink').toggle();
          $('removePreferredLink').toggle();
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not add/remove preferred user, please try again');
      }
    });
  }
  function toggleIgnored() {
    new Ajax.Request(ignoredURL, {
      asynchronous : false,
      parameters : {
        userId : userId
      },
      method : 'post',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          $('addIgnoredLink').toggle();
          $('removeIgnoredLink').toggle();
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not add/remove ignored user, please try again');
      }
    });
  }
  <c:if test="${isAdmin}">
  var adminURL = '<c:url value="/admin/admin_user"/>';
  function toggleAdmin() {
    new Ajax.Request(adminURL, {
      asynchronous : false,
      parameters : {
        userId : userId
      },
      method : 'post',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          $('addAdminLink').toggle();
          $('removeAdminLink').toggle();
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not add/remove admin user, please try again');
      }
    });
  }
  </c:if>
  //--></script>
  </c:if>
  </body>
</html>