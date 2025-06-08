<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="../include/head.jsp"%>
    <title><spring:message code="site.name"/>: Post Song</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms //--%>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jsr303js-codebase.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="../include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">Post Song</div>

          <c:if test="${soundcloudAppId}">
          <form name="soundcloud" action="<c:url value="/connect/soundcloud"/>" method="post">
            <input type="hidden" name="scope" value="non-expiring">
          </form>
          </c:if>

          <form:form name="songForm" cssClass="cmxform" commandName="song">
            <form:hidden path="duration" id="duration"/>
            <form:hidden path="bandcampTrackId" id="bandcampTrackId"/>
            <form:hidden path="soundCloudTrackId" id="soundCloudTrackId"/>

            <div id="global_errors">
              <c:if test="${postError ne null}">
                <ul><li><c:out value="${postError}"/></li></ul>
              </c:if>
              <form:errors path="*" cssClass="errors" element="div"/>
            </div>

              <jsr303js:validate commandName="song"/>

            <fieldset>
              <legend>Import From...</legend>
              <ol>
                <li>
                  <a id="bandcampImportLink" href="<c:url value="/user/bandcamp/song/import"/>"><img src="<c:url value="/img/bandcamp-85x15.png"/>" width="85" height="15" style="margin-top:35"></a>
                  <c:if test="${soundcloudAppId}">
                  <c:choose>
                    <c:when test="${soundCloudConnected}">
                      <a id="soundcloudImportLink" href="<c:url value="/user/soundcloud/song/import"/>"><img src="<c:url value="/img/soundcloud-80x50.png"/>" width="80" height="50"></a>
                    </c:when>
                    <c:otherwise>
                      <img src="<c:url value="/img/soundcloud-80x50.png"/>" width="80" height="50">
                      <a href="#" onclick="document.forms.soundcloud.submit()">Connect to SoundCloud</a> to import tracks.
                    </c:otherwise>
                  </c:choose>
                  </c:if>
                </li>
              </ol>
            </fieldset>
            <fieldset>
              <legend>Song Information</legend>
              <ol>
                <c:choose>
                  <c:when test="${not empty artists}">
                <li>
                  <label for="artist">Artist:</label>
                  <form:select path="artist.id" id="artist" onchange="handleArtistSelection(this)">
                    <c:forEach var="menuArtist" items="${artists}">
                      <form:option value="${menuArtist.id}"><c:out value="${menuArtist.name}"/></form:option>
                    </c:forEach>
                    <option value="0">New artist...</option>
                  </form:select>
                </li>
                <li id="newArtistItem" style="display:none">
                  <label for="newArtistName">New Artist Name:</label>
                  <form:input path="artist.name" id="newArtistName"/>
                </li>
                  </c:when>
                  <c:otherwise>
                    <li id="newArtistItem">
                      <form:hidden path="artist.id"/>
                      <label for="newArtistName">New Artist Name:</label>
                      <form:input path="artist.name" id="newArtistName"/>
                    </li>
                  </c:otherwise>
                </c:choose>
                <li>
                  <label for="title">Title:</label>
                  <form:input path="title" id="title"/>
                </li>
                <li>
                  <label for="url">MP3 URL:</label>
                  <form:input path="url" id="url"/>
                </li>
                <li>
                  <label for="info">Info:</label>
                  <form:textarea path="info" id="info" cssClass="infoTextArea"/>
                  <script>TextareaLimiter.addLimiter('info', 1000)</script>
                </li>
                <li>
                  <label for="bandcampUrl"><a href="http://bandcamp.com" target="_blank"><img src="<c:url value="/img/bandcamp-85x15.png"/>" width="85" height="15"/></a>
                    Page URL (optional):</label>
                  <form:input path="bandcampUrl" id="bandcampUrl"/>
                </li>
                <li>
                  <label for="soundCloudUrl" style="vertical-align: bottom"><a href="http://www.soundcloud.com" target="_blank"><img src="<c:url value="/img/soundcloud-80x50.png"/>" width="80" height="50"/></a>
                    Page URL (optional):</label>
                  <form:input path="soundCloudUrl" id="soundCloudUrl" cssStyle="vertical-align: bottom"/>
                </li>
                <li>
                  <label for="album">Album (optional):</label>
                  <form:input path="album" id="album"/>
                </li>
                <li>
                  <label for="album">Track # (optional):</label>
                  <form:input path="albumTrackNumber" id="albumTrackNumber"/>
                </li>
                <li>
                  <label for="moreInfo">More Info (optional):</label>
                  <form:textarea path="moreInfo" id="moreInfo"/>
                  <script>TextareaLimiter.addLimiter('moreInfo', 3000)</script>
                </li>
                <li>
                  <label for="submitBtn" style="font-size:80%;line-height:120%;">More Info is only shown when<br>users click on the "More Info" link.</label>
                  <c:if test="${postError eq null}">
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Add Song">
                  </c:if>
                </li>
              </ol>
            </fieldset>

          </form:form>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <div class="infoText">
          <p class="rightNavBoxHeader">Requirements:</p>
          <ul class="rightNavBoxList">
            <li>Users may only post one song every 24 hours</li>
            <li>Title is required</li>
            <li>MP3 URL is required</li>
            <li>Info is required</li>
          </ul>
          <br> <br>
          <p class="rightNavBoxHeader">Notes:</p>
          <ul class="rightNavBoxList">
            <li>MP3 URL should be a link to an MP3 file, NOT a link to a Web page.</li>
            <li>Please only link to a song if you are hosting it on your website or have permission from whomever is hosting it.</li>
            <li>Also, please only link to a song if you had a part in its creation or are acting on behalf of someone who did.</li>
            <li>If the MP3 URL for this song breaks (the hosting falls through, the file gets deleted, etc.), please be courteous and hide the song.</li>
            <li>You can hide a song by logging in, viewing your song's "Song Info" page, clicking on the red "Edit" button, unchecking the "Show Song" box, and clicking "Save".</li>
            <li>Hidden songs are still visible to you on your "Artist Info" page if you're logged in.</li>
            <li>Click the Bandcamp logo to copy song data from Bandcamp and use the MP3 hosted on BandCamp. You will
            need to provide the URL to the song's page on bandcamp.com.</li>
            <c:if test="${soundcloudAppId}">
            <li>Click the SoundCloud logo to copy song data from SoundCloud and use the MP3 hosted on SoundCloud. You
            will need to give SomeSongs permission to access your SoundCloud account, then you can choose a song to copy.</li>
            </c:if>
          </ul>
        </div>
      </div>

    </div>

    <jsp:include page="../include/footer.jsp"/>
  </div>
  <%--<script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>--%>
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/effects-1.8.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
  <script type="text/javascript"><!--
  function handleArtistSelection(artistMenu) {
    artistMenu.form['artist.name'].value = '';
    var selectedArtistIndex = artistMenu.selectedIndex;
    var selectedAristId = artistMenu[selectedArtistIndex].value;
    if (selectedAristId != 0) {
      $('newArtistItem').hide();
    } else {
      $('newArtistItem').show();
    }
  }
  var bandcampModal = new Control.Modal('bandcampImportLink', {
    requestOptions:{method:'get'},
    fade:true,
    width:400,
    height:100,
    fadeDuration:0.25
  });
  function cancelBandcampImport() {
    bandcampModal.close();
  }
  function loadFromBandcamp(bandcampUrl) {
    $('copyFromBandcampSpinner').show();
    new Ajax.Request('<c:url value="/user/bandcamp/song/info"/>', {
      asynchronous:false,
      parameters : {
        bandcampUrl : bandcampUrl
      },
      method : 'get',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          var theForm = document.forms.songForm;
          theForm.duration.value = responseJSON.song.duration;
          theForm.albumTrackNumber.value = responseJSON.song.albumTrackNumber;
          theForm.title.value = responseJSON.song.title;
          theForm.bandcampUrl.value = responseJSON.song.bandcampUrl;
          theForm.bandcampTrackId.value = responseJSON.song.bandcampTrackId;
          theForm.url.value = responseJSON.song.url;
          theForm.album.value = responseJSON.song.album || '';
          theForm.info.value = responseJSON.song.info || 'Song imported from Bandcamp.';
          theForm.info.onkeydown();
          theForm.moreInfo.value = responseJSON.song.moreInfo;
          theForm.moreInfo.onkeydown();
          var newArtist = true;
          var artistMenu = theForm['artist.id'];
          var theOptions = artistMenu.options;
          if (theOptions) {
            for (var i = 0; i < theOptions.length; i++) {
              var theOption = theOptions[i];
              if (responseJSON.song.artist.name == theOption.text) {
                artistMenu.selectedIndex = i;
                newArtist = false;
                break;
              }
            }
            if (newArtist) {
              artistMenu.selectedIndex = theOptions.length - 1;
            }
            handleArtistSelection(artistMenu);
          }
          if (newArtist) {
            theForm['artist.name'].value = responseJSON.song.artist.name;
          }
        } else {
          alert(responseJSON.error);
        }
        $('copyFromBandcampSpinner').hide();
        bandcampModal.close();
      },
      onFailure: function() {
//        showCopyingIcon = false;
        $('copyFromBandcampSpinner').hide();
        alert('Could not load song info from Bandcamp, please try again');
      }
    });
    return false;
  }
<c:if test="${soundCloudConnected}">
  var soundcloudSongs = [], page = 0, pageSize = 4;
  var soundcloudModal = new Control.Modal('soundcloudImportLink', {
    requestOptions:{method:'get'},
    fade:true,
    width:500,
    height:200,
    fadeDuration:0.25,
    onSuccess : loadSoundcloudData
  });
  function cancelSoundcloudImport() {
    soundcloudModal.close();
    soundcloudSongs = [];
    page = 0;
  }
  function loadSoundcloudData() {
    if (page > 0) {
      $('soundcloudSpinnerRow').show();
      $('loadMoreRow'+(page-1)).hide();
    }
    new Ajax.Request('<c:url value="/user/soundcloud/tracks"/>?page='+page+'&size='+pageSize, {
      method : 'get',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          for (var i = 0; i < responseJSON.songs.length; i++) {
            var html = [], j = 0;
            html[j++] = '<tr><td align="middle"><input type="radio" name="track" value="';
            html[j++] = soundcloudSongs.length;
            html[j++] = '"></td><td>';
            html[j++] = responseJSON.songs[i].title;
            html[j++] = '</td></tr>';
            $$('#soundcloudImportTable tr').last().insert({before : html.join('')});
            soundcloudSongs.push(responseJSON.songs[i]);
          }
          if (responseJSON.songs.length == pageSize) {
            // show 'load more' link/button
            $$('#soundcloudImportTable tr').last().insert({
              before : '<tr id="loadMoreRow'+page+'"><td>&nbsp;</td><td><a href="#" onclick="loadSoundcloudData()">Load More</a></td></tr>'
            });
          }
          page++;
          $('soundcloudSpinnerRow').hide();
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        $('soundcloudSpinnerRow').hide();
        alert('Could not load song info from SoundCloud, please try again');
      }
    });
  }
  function copySoundcloudSong(soundcloudForm) {
    var songIndex = $(soundcloudForm).select('input[type=\'radio\']:checked').first().getValue();
    var theForm = document.forms.songForm;
    theForm.title.value = soundcloudSongs[songIndex].title;
    theForm.duration.value = soundcloudSongs[songIndex].duration;
    theForm.info.value = soundcloudSongs[songIndex].info;
    theForm.info.onkeydown();
    theForm.url.value = soundcloudSongs[songIndex].url;
    theForm.soundCloudUrl.value = soundcloudSongs[songIndex].soundCloudUrl;
    theForm.soundCloudTrackId.value = soundcloudSongs[songIndex].soundCloudTrackId;
    soundcloudModal.close();
    return false;
  }
</c:if>
  //--></script>
  </body>
</html>
