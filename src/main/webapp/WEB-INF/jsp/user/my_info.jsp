<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: My Info</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms --%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">My Information</div>

          <a href="<c:url value="/profile?id=${myInfoUser.id}"/>">View My Profile</a>

          <c:if test="${param['saved'] eq 'true'}">
            <div id="successMessage" class="successMessage">
              Your changes have been saved.
            </div>
          </c:if>
          <c:if test="${param['playlistDeleted'] eq 'true'}">
            <div id="successMessage" class="successMessage">
              Playlist deleted.
            </div>
          </c:if>

          <form:form action="my_info" name="userForm" cssClass="cmxform" commandName="myInfoUser">

            <c:if test="${isAdmin}">
              <form:hidden path="id"/>
            </c:if>

            <div id="global_errors">
              <form:errors path="*" cssClass="errors" element="div"/>
            </div>

            <fieldset>
              <legend>Account Information</legend>
              <ol>
                <li>
                  <label for="username" title="Only used for logging in, not shown to other users.">Login:</label>
                  <form:input path="username" id="username" />
                </li>
                <li>
                  <label for="email" title="Used when the system needs to contact you (eg to send your initial password).">E-mail:</label>
                  <form:input path="email" id="email"/>
                </li>
                <li>
                  <label for="displayName" title="Name shown to other users.">Display Name:</label>
                  <form:input path="displayName" id="displayName"/>
                </li>
              </ol>
            </fieldset>

            <fieldset>
              <legend>Change Password</legend>
              <ol>
                <li>
                  <label for="newPassword" title="Passwords must be 6 to 32 characters long, and should contain letters, numbers, and punctuation.">New Password</label>
                  <%--<form:password path="newPassword" id="newPassword" />--%>
                  <input type="password" id="newPassword" name="newPassword" value="<c:out value="${param.newPassword}"/>">
                </li>
                <li>
                  <label for="confirmPassword" title="Enter your new password again for confirmation.">Confirm New Password</label>
                  <%--<form:password path="confirmPassword" id="confirmPassword" />--%>
                  <input type="password" id="confirmPassword" name="confirmPassword" value="<c:out value="${param.confirmPassword}"/>">
                </li>
              </ol>
            </fieldset>

            <c:if test="${not empty facebookAppId or not empty twitterAppId or not empty soundcloudAppId}">
            <fieldset>
              <legend>Connected Accounts</legend>
              <ol>
                <c:if test="${not empty facebookAppId}">
                <li>
                    <c:choose>
                      <c:when test="${facebookConnected}">
                        <label for="disconnectFacebook" style="color:green">Facebook account Connected!</label>
                        <input id="disconnectFacebook" class="submitBtn" type="button" name="disconnectFacebookBtn"
                               value="Disconnect" onclick="document.forms.facebook.submit()">
                      </c:when>
                      <c:otherwise>
                        <label for="connectFacebook">Facebook account not connected</label>
                        <input id="connectFacebook" class="submitBtn" type="button" name="connectFacebookBtn"
                               value="Connect Your Facebook Account" onclick="document.forms.facebook.submit()">
                      </c:otherwise>
                    </c:choose>
                </li>
                <c:if test="${facebookConnected}">
                  <li>
                    <c:choose>
                      <c:when test="${myInfoUser.publishToTimeline}">
                        <label for="disableTimeline"><font style="color:green">Publishing to Timeline</font><br>
                          Actions (rating, etc.)<br>will be published to your Facebook Timeline.</label>
                        <input id="disableTimeline" class="submitBtn" type="button" name="disableTimelineBtn"
                               value="Disable Publishing To Timeline" onclick="document.forms.timeline.submit()">
                      </c:when>
                      <c:otherwise>
                        <label for="enableTimeline"><font style="color:red">Not Publishing to Timeline</font><br>
                          Actions (rating,etc.)<br>will not be published to your Facebook Timeline.</label>
                        <input id="enableTimeline" class="submitBtn" type="button" name="enableTimelineBtn"
                               value="Enable Publishing To Timeline" onclick="document.forms.timeline.submit()">
                      </c:otherwise>
                    </c:choose>
                  </li>
                </c:if>
                </c:if>
                <c:if test="${not empty twitterAppId}">
                <li>
                  <c:choose>
                    <c:when test="${twitterConnected}">
                      <label for="disconnectTwitter" style="color:green">Twitter account Connected!</label>
                      <input id="disconnectTwitter" class="submitBtn" type="button" name="disconnectTwitterBtn"
                             value="Disconnect" onclick="document.forms.twitter.submit()">
                    </c:when>
                    <c:otherwise>
                      <label for="connectTwitter">Twitter account not connected</label>
                      <input id="connectTwitter" class="submitBtn" type="button" name="connectTwitterBtn"
                             value="Connect Your Twitter Account" onclick="document.forms.twitter.submit()">
                    </c:otherwise>
                  </c:choose>
                </li>
                </c:if>
                <c:if test="${not empty soundcloudAppId}">
                <li>
                  <c:choose>
                    <c:when test="${soundcloudConnected}">
                      <label for="disconnectSoundcloud" style="color:green">Soundcloud account Connected!</label>
                      <input id="disconnectSoundcloud" class="submitBtn" type="button" name="disconnectSoundcloudBtn"
                             value="Disconnect" onclick="document.forms.soundcloud.submit()">
                    </c:when>
                    <c:otherwise>
                      <label for="connectSoundcloud">Soundcloud account not connected</label>
                      <input id="connectSoundcloud" class="submitBtn" type="button" name="connectSoundcloudBtn"
                             value="Connect Your Soundcloud Account" onclick="document.forms.soundcloud.submit()">
                    </c:otherwise>
                  </c:choose>
                </li>
                </c:if>
              </ol>
            </fieldset>
            </c:if>

            <fieldset>
              <legend>Profile Information</legend>
              <ol>
                <li>
                  <label for="location">Location (eg city, state, country):</label>
                  <form:input path="location" id="location"/>
                </li>
                <li>
                  <label for="showEmailInUserInfo">Show E-mail In Profile:</label>
                  <form:select path="showEmailInUserInfo" id="showEmailInUserInfo">
                    <form:option value="true">Yes</form:option>
                    <form:option value="false">No</form:option>
                  </form:select>
                </li>
                <c:if test="${isAdmin}">
                  <li>
                    <label for="canPostSongs">Can Post Songs:</label>
                    <form:checkbox path="canPostSongs" id="canPostSongs" value="true"/>
                  </li>
                  <li>
                    <label for="canSynchFromBandcamp">Can Synch From Bandcamp:</label>
                    <form:checkbox path="canSynchFromBandcamp" id="canSynchFromBandcamp" value="true"/>
                  </li>
                </c:if>
                <li>
                  <label for="websiteName">Name of Your Website:</label>
                  <form:input path="websiteName" id="websiteName"/>
                </li>
                <li>
                  <label for="websiteURL">URL of Your Website:</label>
                  <form:input path="websiteURL" id="websiteURL"/>
                </li>
                <li>
                  <label for="goodBand">A Good Band:</label>
                  <form:input path="goodBand" id="goodBand"/>
                </li>
                <li>
                  <label for="goodAlbum">A Good Album:</label>
                  <form:input path="goodAlbum" id="goodAlbum"/>
                </li>
                <li>
                  <label for="goodSong">A Good Song:</label>
                  <form:input path="goodSong" id="goodSong"/>
                </li>
                <li>
                  <label for="goodMovie">A Good Movie:</label>
                  <form:input path="goodMovie" id="goodMovie"/>
                </li>
                <li>
                  <label for="goodWebsiteName">Name of A Good Website:</label>
                  <form:input path="goodWebsiteName" id="goodWebsiteName"/>
                </li>
                <li>
                  <label for="goodWebsiteURL">URL of A Good Website:</label>
                  <form:input path="goodWebsiteURL" id="goodWebsiteURL"/>
                </li>
                <li>
                  <label for="submitBtn" style="font-size:80%;">&nbsp;</label>
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Update My Info">
                </li>
              </ol>
            </fieldset>

          </form:form>

          <form name="facebook" action="<c:url value="/connect/facebook"/>" method="post">
            <%--<input type="hidden" name="scope" value="publish_stream,offline_access">--%>
            <c:if test="${facebookConnected}">
              <input type="hidden" name="_method" value="delete" />
            </c:if>
          </form>

          <form name="twitter" action="<c:url value="/connect/twitter"/>" method="post">
          <c:if test="${twitterConnected}">
            <input type="hidden" name="_method" value="delete" />
          </c:if>
          </form>

          <form name="soundcloud" action="<c:url value="/connect/soundcloud"/>" method="post">
            <c:choose>
              <c:when test="${soundcloudConnected}">
                <input type="hidden" name="_method" value="delete" />
              </c:when>
              <c:otherwise>
                <input type="hidden" name="scope" value="non-expiring">
              </c:otherwise>
            </c:choose>
          </form>

          <c:if test="${facebookConnected}">
          <c:choose>
          <c:when test="${myInfoUser.publishToTimeline}">
            <form name="timeline" action="<c:url value="/user/my_info/timeline"/>" method="post">
            </form>
          </c:when>
          <c:otherwise>
          <form name="timeline" action="<c:url value="/connect/facebook"/>" method="post">
            <input type="hidden" name="scope" value="publish_actions">
          </form>
          </c:otherwise>
          </c:choose>
          </c:if>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <div class="infoText">
            <c:url var="myArtistsURL" value="/artist_search">
              <c:param name="search" value="true"/>
              <c:param name="user.id" value="${myInfoUser.id}"/>
            </c:url>
          <div class="myInfoHeader">
            <c:if test="${canAddArtist}">
            <span style="float:right"><a id="addArtistLink" href="<c:url value="/user/add_artist"/>" onclick="return false">+ Add Artist</a></span>
            </c:if>
            <span><a href="<c:out value="${myArtistsURL}"/>" title="View my artists">My Artists:</a></span>
          </div>
          <div id="myArtistsDiv">
          <jsp:include page="/include/my_artists" flush="true"/>
          </div>
          <br>
          <div class="myInfoHeader">
            <span>Shared Artists:</span>
          </div>
          <div id="sharedArtistsDiv">
            <ul class="myInfoListNoDelete">
              <c:forEach var="sharedArtist" items="${sharedArtists}" varStatus="loopStatus">
                <c:set var="itemStyle" value="evenRow"/>
                <c:if test="${loopStatus.index mod 2 eq 0}">
                  <c:set var="itemStyle" value="oddRow"/>
                </c:if>
                <c:url var="artistURL" value="/artists/${sharedArtist.nameForUrl}-${sharedArtist.id}"/>
                <li class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${artistURL}"/>"><c:out value="${sharedArtist.name}"/></a></li>
              </c:forEach>
            </ul>
          </div>
          <br>
          <div class="myInfoHeader">
            <span style="float:right"><a id="addPlaylistLink" href="<c:url value="/user/add_playlist"/>" onclick="return false">+ Add Playlist</a></span>
            <span><a href="<c:url value="/user/playlists"/>" title="View my playlists">My Playlists:</a></span>
          </div>
          <div id="myPlaylistsDiv">
          <jsp:include page="/include/my_playlists" flush="true"/>
          </div>
          <br>
          <c:url var="favoriteSongsURL" value="/search">
            <c:param name="inUsersFavorites" value="${myInfoUser.id}"/>
            <c:param name="search" value="true"/>
          </c:url>
          <p class="myInfoHeader"><a href="<c:out value="${favoriteSongsURL}"/>" title="Search for Favorite Songs">Favorite Songs:</a></p>
          <ul class="myInfoList">
            <c:forEach var="favoriteSong" items="${favoriteSongs}" varStatus="loopStatus">
              <c:set var="itemStyle" value="evenRow"/>
              <c:if test="${loopStatus.index mod 2 eq 0}">
                <c:set var="itemStyle" value="oddRow"/>
              </c:if>
              <c:url var="songLink" value="/songs/${favoriteSong.titleForUrl}-${favoriteSong.id}"/>
              <li id="fs<c:out value="${favoriteSong.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${songLink}"/>"><c:out value="${favoriteSong.title}"/></a></li>
            </c:forEach>
            <c:if test="${favoriteSongSearch.totalResults gt fn:length(favoriteSongs)}">
              <li><a href="<c:out value="${favoriteSongsURL}"/>">Show all <c:out value="${favoriteSongSearch.totalResults}"/></a></li>
            </c:if>
          </ul>
          <br>
          <c:url var="favoritePlaylistsURL" value="/search_playlists">
            <c:param name="inUsersFavorites" value="${myInfoUser.id}"/>
            <c:param name="search" value="true"/>
          </c:url>
          <p class="myInfoHeader"><a href="<c:out value="${favoritePlaylistsURL}"/>" title="Search for Favorite Playlists">Favorite Playlists:</a></p>
          <ul class="myInfoList">
            <c:forEach var="favoritePlaylist" items="${favoritePlaylists}" varStatus="loopStatus">
              <c:set var="itemStyle" value="evenRow"/>
              <c:if test="${loopStatus.index mod 2 eq 0}">
                <c:set var="itemStyle" value="oddRow"/>
              </c:if>
              <c:url var="playlistLink" value="/playlist">
                <c:param name="id" value="${favoritePlaylist.id}"/>
              </c:url>
              <li id="fp<c:out value="${favoritePlaylist.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${playlistLink}"/>"><c:out value="${favoritePlaylist.title}"/></a></li>
            </c:forEach>
            <c:if test="${favoritePlaylistSearch.totalResults gt fn:length(favoritePlaylists)}">
              <li><a href="<c:out value="${favoritePlaylistsURL}"/>">Show all <c:out value="${favoritePlaylistSearch.totalResults}"/></a></li>
            </c:if>
          </ul>
          <br>
          <c:url var="favoriteArtistsURL" value="/artist_search">
            <c:param name="inUsersFavorites" value="${myInfoUser.id}"/>
            <c:param name="search" value="true"/>
          </c:url>
          <p class="myInfoHeader"><a href="<c:out value="${favoriteArtistsURL}"/>" title="Search for Favorite Artists">Favorite Artists:</a></p>
          <ul class="myInfoList">
            <c:forEach var="favoriteArtist" items="${favoriteArtists}" varStatus="loopStatus">
              <c:set var="itemStyle" value="evenRow"/>
              <c:if test="${loopStatus.index mod 2 eq 0}">
                <c:set var="itemStyle" value="oddRow"/>
              </c:if>
              <c:url var="artistLink" value="/artists/${favoriteArtist.nameForUrl}-${favoriteArtist.id}"/>
              <li id="fa<c:out value="${favoriteArtist.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${artistLink}"/>"><c:out value="${favoriteArtist.name}"/></a></li>
            </c:forEach>
            <c:if test="${favoriteArtistSearch.totalResults gt fn:length(favoriteArtists)}">
              <li><a href="<c:out value="${favoriteArtistsURL}"/>">Show all <c:out value="${favoriteArtistSearch.totalResults}"/></a></li>
            </c:if>
          </ul>
          <br>
          <p class="myInfoHeader">Preferred Users:</p>
          <ul class="myInfoList">
            <c:forEach var="preferredUser" items="${preferredUsers}" varStatus="loopStatus">
              <c:set var="itemStyle" value="evenRow"/>
              <c:if test="${loopStatus.index mod 2 eq 0}">
                <c:set var="itemStyle" value="oddRow"/>
              </c:if>
              <c:url var="preferredUserLink" value="/profile">
                <c:param name="id" value="${preferredUser.preferredUser.id}"/>
              </c:url>
              <li id="pu<c:out value="${preferredUser.preferredUser.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${preferredUserLink}"/>"><c:out value="${preferredUser.preferredUser.displayName}"/></a></li>
            </c:forEach>
<%--
            <c:if test="${fn:length(myUserInfo.user.preferredUsers) gt 8}">
              <li><a href="favorite_songs">... more</a></li>
            </c:if>
--%>
          </ul>
          <br>
          <p class="myInfoHeader">Ignored Users:</p>
          <ul class="myInfoList">
            <c:forEach var="ignoredUser" items="${ignoredUsers}" varStatus="loopStatus">
              <c:set var="itemStyle" value="evenRow"/>
              <c:if test="${loopStatus.index mod 2 eq 0}">
                <c:set var="itemStyle" value="oddRow"/>
              </c:if>
              <c:url var="ignoredUserLink" value="/profile">
                <c:param name="id" value="${ignoredUser.ignoredUser.id}"/>
              </c:url>
              <li id="iu<c:out value="${ignoredUser.ignoredUser.id}"/>" class="<c:out value="${itemStyle}"/>"><a href="<c:out value="${ignoredUserLink}"/>"><c:out value="${ignoredUser.ignoredUser.displayName}"/></a></li>
            </c:forEach>
<%--
            <c:if test="${fn:length(myUserInfo.user.ignoredUsers) gt 8}">
              <li><a href="favorite_songs">... more</a></li>
            </c:if>
--%>
          </ul>
          <br>
          <jsp:include page="/include/my_saved_searches" flush="true">
            <jsp:param name="resultsPerPage" value="8"/>
          </jsp:include>
      </div>

    </div>
    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
  <script type="text/javascript"><!--
  <c:if test="${canAddArtist}">
    // onclick starts out as "return false" so that clicks on the link before the Control.Modal code has
    // been attached to it don't do anything
    $('addArtistLink').onclick = '';
    var addArtistModal = new Control.Modal('addArtistLink', {
      fade:true,
      fadeDuration:0.25,
      width:250,
      requestOptions : {
        method : 'get',
        evalScripts:true
      }
    });
    function saveArtist() {
      $('addArtistForm').request({
        onSuccess : function(transport) {
          if (transport.responseJSON && transport.responseJSON.success) {
            new Ajax.Request('<c:url value="/include/my_artists"/>', {
              method : 'get',
              onSuccess : function(transport) {
                $('myArtistsDiv').update(transport.responseText);
                addArtistModal.close()
                new Effect.Highlight('myArtistsDiv');
              },
              onFailure : function() {
                alert('Could not update artist list, please refresh page and try again');
              }
            })
          } else {
            $('modal_container').update(transport.responseText);
          }
        }
      });
    }
    function cancelArtist() {
      addArtistModal.close();
    }
  </c:if>
    var addEventHandlers = function() {
      var myInfoLists = $('rightNavBox').select('ul.myInfoList');
      myInfoLists.each(function(myInfoList) {
        var listItems = myInfoList.childElements();
        listItems.each(function(listItem) {
          if (listItem.id) {
            listItem.observe('mouseover', listItemMouseoverHandler)
          }
        });
      });
    };
    var displayedId;
    var listItemMouseoverHandler = function(event) {
      var element = event.element();
      if (!element.id) {
        // event is from the link, we want the list item
        element = element.up();
      }
      if (element.id.match(/^delete/)) {
        // event was from the delete link, ignore it
        return;
      }
      if (displayedId && displayedId != element.id) {
        $('delete'+displayedId).hide();
      }
      if (element.childElements().size() == 1) {
        element.insert({top:'<span id="delete'+element.id+'" style="float:right"><a class="myInfoDelete" title="Delete" href="javascript:deleteMyInfo(\''+element.id+'\')">X</a></span>'});
      } else {
        $('delete'+element.id).show();
      }
      displayedId = element.id;
    };
    Event.observe(window,'load',addEventHandlers);
    var deleteItemURL = '<c:url value="/user/delete_item"/>';
    function deleteMyInfo(itemId) {
      new Ajax.Request(deleteItemURL, {
        asynchronous : false,
        parameters : {
          itemId : itemId
        },
        method : 'post',
        onSuccess : function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            // TODO reload just the changed section of the page
            location.href = '<c:url value="/user/my_info"/>';
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure : function() {
          alert('Could not delete item, please try again');
        }
      });
    }
  var playlistURL = '<c:url value="/playlist"/>';
  //--></script>
    <script type="text/javascript" src="<c:url value="/js/add_playlist.js"/>"></script>
  </body>
</html>
