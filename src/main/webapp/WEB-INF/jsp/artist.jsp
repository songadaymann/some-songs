<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%--<c:set var="canEdit" value="${auth.name eq artist.user.username}"/>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head<c:if test="${facebookAppNamespace}"> prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# ${facebookAppNamespace}: http://ogp.me/ns/fb/${facebookAppNamespace}#"</c:if>>
    <c:if test="${facebookAppId}">
    <meta property="fb:app_id"      content="${facebookAppId}" />
    </c:if>
    <meta property="og:site_name"   content="<spring:message code="site.name"/>" />
    <c:if test="${facebookAppNamespace}">
    <meta property="og:type"        content="${facebookAppNamespace}:artist" />
    </c:if>
    <meta property="og:url"         content="${siteUrl}/artists/${artist.nameForUrl}-${artist.id}" />
    <meta property="og:title"       content="${fn:replace(artist.name, '\"', '&quot;')}" />
    <meta property="og:description" content="Listen to and rate songs by '${artist.name}'!" />
    <meta property="og:image"       content="${openGraphActionImageUrl}" />
    <title><spring:message code="site.name"/>: <c:out value="${artist.name}"/></title>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    <c:if test="${loggedIn}">
    <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
    </c:if>
    <%@ include file="/WEB-INF/jsp/include/inlineplayer_head.jsp"%>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:if test="${param['msg'] ne null}">
            <div id="successMessage" class="successMessage">
              <c:out value="${param['msg']}"/>
            </div>
            <br>
          </c:if>

          <div id="artistInfoDiv">
            <%@ include file="/WEB-INF/jsp/include/artist_info.jsp"%>
          </div>

          <br>

          <div>
            <c:if test="${canEdit}">
              <a id="addLink" class="commentButton" href="<c:url value="/user/add_link"/>?artistId=<c:out value="${artist.id}"/>" onclick="return false">Add Link</a>
              <%-- this link is used for the edit link modal dialog --%>
              <a id="editLink" style="display:none" href="<c:url value="/user/edit_link"/>?id="> </a>
            </c:if>
          </div>

          <br>

          <div id="linksDiv">
            <%@ include file="/WEB-INF/jsp/include/artist_links.jsp"%>
          </div>

          <jsp:include page="/song_list">
            <jsp:param name="artistName" value="${artist.name}"/>
          </jsp:include>

          <br> <br>

          <c:if test="${canEdit and hiddenSongSearch ne null}">
            <c:set var="songSearch" value="${hiddenSongSearch}"/>
            <c:set var="songSearchResults" value="${hiddenSongs}"/>
            <c:set var="isInclude" value="true"/>
            <c:set var="saveLinkNum" value="1"/>

            <%@ include file="/WEB-INF/jsp/include/song_list.jsp"%>

            <br> <br>
          </c:if>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">

        <c:if test="${canEdit}">
        <div>
          <a id="addFavoriteLink" class="addLink" <c:if test="${isFavorite}">style="display:none" </c:if>href="javascript:toggleFavorite()"><span>&nbsp;+&nbsp;</span> Add to Favorites</a>
          <a id="removeFavoriteLink" class="removeLink" <c:if test="${!isFavorite}">style="display:none" </c:if>href="javascript:toggleFavorite()"><span>&nbsp;-&nbsp;</span> Remove from Favorites</a>
        </div>

        <br> <br>

        <div class="myInfoHeader">
          <span>Shared With:</span>
        </div>
        <c:set var="otherUsers" value="${artist.otherUsers}"/>
        <div id="otherUsersDiv">
          <jsp:include page="/include/other_users">
              <jsp:param name="id" value="${artist.id}"/>
          </jsp:include>
        </div>
        <br>
        <div>
          <span style="float:right">
            <form name="addOtherUserForm" onsubmit="return false;">
              <span><input type="text" id="otherUserName" value="" title="Starting typing a user's name and select from the menu"></span>
              <div id="otherUsersMenu"></div>
            </form>
          </span>
          <span id="otherUserSpinner" style="display:none;float:right"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Loading..."></span>
          <span style="font-weight:bold;padding:2px">Add User:</span>
        </div>
        <br> <br>
        </c:if>

        <div class="myInfoHeader">
          <span>Related Artists:</span>
        </div>
        <c:set var="relatedArtists" value="${artist.relatedArtists}"/>
        <div id="relatedArtistsDiv">
          <jsp:include page="/include/related_artists">
            <jsp:param name="id" value="${artist.id}"/>
          </jsp:include>
        </div>
        <c:if test="${canEdit}">
        <br>
        <div>
          <span style="float:right">
            <form name="addRelatedArtistForm" onsubmit="return false;">
              <span><input type="text" id="relatedArtistName" value="" title="Starting typing an artist's name and select from the menu"></span>
              <div id="relatedArtistsMenu"></div>
            </form>
          </span>
          <span id="relatedArtistSpinner" style="display:none;float:right"><img src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" alt="..." title="Loading..."></span>
          <span style="font-weight:bold;padding:2px">Add Artist:</span>
        </div>
        </c:if>

        <br> <br>

<%--
        <c:url var="artistSearchURL" value="/search">
        </c:url>
--%>
        <div>
          <form action="<c:url value="/search"/>" method="POST">
            <input type="hidden" name="search" value="true">
            <input type="hidden" name="artistName" value="${artist.name}">
            <b>Search songs by this artist</b><br>
            Title: <input type="text" name="title" value=""><br>
            <input type="submit" name="submitBtn" value="Search">
          </form>
        </div>

      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
  <c:if test="${loggedIn}">
  <script type="text/javascript" src="<c:url value="/js/song_list.js"/>"></script>
  <script type="text/javascript">
    var ratingURL = '<c:url value="/user/rate_song"/>';
    var favoriteURL = '<c:url value="/user/favorite_artist"/>';
    var artistId = <c:out value="${artist.id}"/>;
    function toggleFavorite() {
      new Ajax.Request(favoriteURL, {
        parameters : {
          artistId : artistId
        },
        method : 'post',
        onSuccess : function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            $('addFavoriteLink').toggle();
            $('removeFavoriteLink').toggle();
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure : function() {
          alert('Could not add/remove favorite, please try again');
        }
      });
    }
  </script>
  <c:if test="${canEdit}">
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  <script type="text/javascript">
    var editURL = '<c:url value="/user/edit_artist"/>';
    var editLinkURL = '<c:url value="/user/edit_link"/>?id=';
    var deleteURL = '<c:url value="/user/delete_artist"/>';
    var myInfoURL = '<c:url value="/user/my_info"/>';
    var deleteLinkURL = '<c:url value="/user/delete_artist_link"/>';
    var artistLinksURL = '<c:url value="/artist_links"/>';
    var addOtherUserURL = '<c:url value="/user/add_other_user"/>';
    var addRelatedArtistURL = '<c:url value="/user/add_related_artist"/>';
    var searchUsersURL = '<c:url value="/user/search_users"/>';
    var searchArtistsURL = '<c:url value="/user/search_artists"/>';
    var deleteItemURL = '<c:url value="/user/delete_artist_info"/>';
    var bandcampAlbumsURL = '<c:url value="/user/bandcamp/artist/albums"/>';
    function bandcampSynchChanged(theCheckbox) {
      $('albumToggle').toggle();
    }
    function synchAlbumsChanged() {
      var theRadios = document.forms.editArtistForm.albumSelection;
      var theRadio = (theRadios[0].checked ? theRadios[0] : theRadios[1]);
      if (theRadio.value == 'some') {
        $('copyFromBandcampSpinner').show();
        new Ajax.Request(bandcampAlbumsURL, {
          parameters : {
            bandcampUrl : theRadio.form.bandcampUrl.value
          },
          method : 'post',
          onSuccess : function(transport) {
            var responseJSON = transport.responseJSON;
            if (responseJSON && responseJSON.success) {
              var albumHtml = [], i = 0;
              for (var j = 0; j < responseJSON.albumCount; j++) {
                albumHtml[i++] = '<input type="checkbox" name="bandcampAlbumIds" value="';
                albumHtml[i++] = responseJSON['albumId'+j];
                albumHtml[i++] = '">&nbsp;';
                albumHtml[i++] = responseJSON['albumName'+j];
                albumHtml[i++] = '<br>';
              }
              $('bandcampAlbums').innerHTML = albumHtml.join('');
              $('bandcampAlbums').show();
            } else {
              alert(responseJSON.error);
            }
            $('copyFromBandcampSpinner').hide();
          },
          onFailure: function() {
            showCopyingIcon = false;
            $('copyFromBandcampSpinner').hide();
            alert('Could not load artist info from Bandcamp, please try again');
          }
        });
      } else {
        $('bandcampAlbums').hide();
      }
    }
  </script>
  <script type="text/javascript" src="<c:url value="/js/artist.js"/>"></script>
  </c:if>
  </c:if>
  </body>
</html>