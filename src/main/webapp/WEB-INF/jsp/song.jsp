<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head<c:if test="${facebookAppNamespace}"> prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# ${facebookAppNamespace}: http://ogp.me/ns/fb/${facebookAppNamespace}#"</c:if>>
    <c:if test="${facebookAppId}">
    <meta property="fb:app_id"      content="${facebookAppId}" />
    </c:if>
    <meta property="og:site_name"   content="<spring:message code="site.name"/>" />
    <c:if test="${facebookAppNamespace}">
    <meta property="og:type"        content="${facebookAppNamespace}:song" />
    <meta property="${facebookAppNamespace}:artist" content="${siteUrl}/artists/${song.artist.nameForUrl}-${song.artist.id}" />
    <meta property="${facebookAppNamespace}:album" content="${song.album}" />
    </c:if>
    <meta property="og:url"         content="${siteUrl}/songs/${song.titleForUrl}-${song.id}" />
    <meta property="og:title"       content="${fn:replace(song.title, '\"', '&quot;')}" />
    <meta property="og:description" content="${fn:replace(song.title, '\"', '&quot;')}, by ${song.artist.name}" />
    <c:if test="${openGraphActionImageUrl}">
    <meta property="og:image"       content="${openGraphActionImageUrl}" />
    </c:if>
    <meta property="og:video"       content="http://s3.amazonaws.com/somesongs/dewplayer-bubble-vol.swf?mp3=${siteUrl}/songs/stream/${song.titleForUrl}-${song.id}.mp3&autostart=1" />
    <meta property="og:video:secure_url" content="https://s3.amazonaws.com/somesongs/dewplayer-bubble-vol.swf?mp3=${siteUrl}/songs/stream/${song.titleForUrl}-${song.id}.mp3&autostart=1" />
    <meta property="og:video:width" content="260" />
    <meta property="og:video:height" content="75" />
    <meta property="og:video:type" content="application/x-shockwave-flash" />
    <title><spring:message code="site.name"/>: '<c:out value="${song.title}"/>' by <c:out value="${song.artist.name}"/></title>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    <%@ include file="/WEB-INF/jsp/include/pageplayer_head.jsp" %>
    <style type="text/css">
      .wideInput {
        width:300px;
      }
      ul.playlist li {
       position:relative;
       display:block;
       width:auto;
       /*font-size:2em;*/
       color:#000;
       padding:0.25em 0.5em 0.25em 0.5em;
       background-color:#fff;
      }
      ul.playlist li a {
        font-size:86%;
      }
      ul.playlist li:hover {
        background-color:#fff;
      }
      ul.playlist li.sm2_paused:hover {
       background-color:#999;
      }
    </style>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="songInfoDiv">
            <%@ include file="/WEB-INF/jsp/include/song_info.jsp"%>
          </div>
          <br>

          <c:choose>
            <c:when test="${param['deleted'] eq 'true'}">
              <div class="successMessage">
                Your reply has been deleted.
              </div>
              <br>
            </c:when>
            <c:when test="${param['commentDeleted'] eq 'true'}">
              <div class="successMessage">
                Song comment deleted.
              </div>
              <br>
            </c:when>
            <c:when test="${param['error'] ne null}">
              <div class="errors">
                Unable to delete reply:
                <br>
                <c:out value="${param['error']}"/>
              </div>
              <br>
            </c:when>
          </c:choose>

          <c:choose>
            <c:when test="${originalComment ne null}">
              <div class="originalComment">
                <div class="songCommentsHeader">
                  <c:url var="quoteCommentLink" value="/user/comment_reply">
                    <c:param name="comment" value="${originalComment.id}"/>
                    <c:param name="quoteCommentId" value="${originalComment.id}"/>
                  </c:url>
                  <div style="float:right;margin-left:6px">
                    <a class="quotePostLink" href="<c:out value="${quoteCommentLink}"/>">Quote</a>
                  </div>
                  <c:set var="originalIsQuoted" value="${sessionScope['mqSongCommentId'] eq originalComment.id and sessionScope['mqSongQuoteOriginal'] eq 'true'}"/>
                  <div style="float:right">
                    <a id="multiquoteComment<c:out value="${originalComment.id}"/>" class="quotePostLink<c:if test="${originalIsQuoted}"> quoted</c:if>" href="javascript:toggleMultiquote(true, '<c:out value="${originalComment.id}"/>');">Multiquote</a>
                  </div>
                  <span style="font-weight:bold">Comment:</span>
                  <span class="smallBoldLink">
                    <a href="<c:url value="/songs/${song.titleForUrl}-${song.id}"/>">[Show All Comments]</a>
                    <c:if test="${isAdmin}">
                      <a class="editLink" href="<c:url value="/admin/delete_comment"/>?id=<c:out value="${originalComment.id}"/>">Delete Comment</a>
                      <a class="editLink" href="<c:url value="/admin/edit_song_comment"/>?id=<c:out value="${originalComment.id}"/>">Edit Comment</a>
                    </c:if>
                  </span>
                </div>
                <div class="songListHeader">
                  <div class="oddRow">
                    <%@ include file="/WEB-INF/jsp/include/original_comment.jsp"%>
                  </div>
                </div>
              </div>

              <br>

              <jsp:include page="/WEB-INF/jsp/include/comment_reply_paging.jsp"/>

              <br> <br> <br>

              <c:if test="${replySearch.totalResults > 0}">
                <c:forEach var="reply" items="${replies}" varStatus="loopStatus">
                  <c:set var="oddPost" value="${loopStatus.index mod 2 eq 0}"/>
                  <%@ include file="/WEB-INF/jsp/include/comment_reply.jsp" %>
                </c:forEach>

                <jsp:include page="/WEB-INF/jsp/include/comment_reply_paging.jsp"/>

                <br> <br> <br>

              </c:if>


            </c:when>
            <c:otherwise>
              <div id="commentsContainer">
                <jsp:include page="/include/song_comments?id=${song.id}"/>
              </div>
              <div id="commentsLoading" style="display:none;">... loading ...</div>
            </c:otherwise>
          </c:choose>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <div><b>Rating:</b></div>
        <div style="margin-left:2px;">
          <a id="goodRatingLink" href="javascript:setRating('good')" class="ratingLink good<c:if test="${rating.rating eq 10}"> selectedRating</c:if>">good</a>
          <a id="okayRatingLink" href="javascript:setRating('okay')" class="ratingLink okay<c:if test="${rating.rating eq 5}"> selectedRating</c:if>">okay</a>
          <a id="badRatingLink" href="javascript:setRating('bad')" class="ratingLink bad<c:if test="${rating.rating eq 0}"> selectedRating</c:if>">bad</a>
          <a id="noneRatingLink" href="javascript:setRating('none')" class="ratingLink none<c:if test="${rating eq null}"> selectedRating</c:if>">x</a>
        </div>
        <br>
        <div><b>Favorites:</b></div>
        <div>
          <a id="addFavoriteLink" class="addLink" <c:if test="${isFavorite}">style="display:none" </c:if>href="javascript:toggleFavorite()"><span>&nbsp;+&nbsp;</span> Add to Favorites</a>
          <a id="removeFavoriteLink" class="removeLink" <c:if test="${!isFavorite}">style="display:none" </c:if>href="javascript:toggleFavorite()"><span>&nbsp;-&nbsp;</span> Remove from Favorites</a>
        </div>
        <br>
          <c:if test="${originalComment eq null}">
        <b>Comment:</b>
        <form action="user/edit_comment" name="commentForm">
          <textarea name="commentText" id="commentText" rows="5" style="width:100%"><c:out value="${myComment.commentText}"/><c:out value="${myComment.moreCommentText}"/></textarea>
          <script type="text/javascript">TextareaLimiter.addLimiter('commentText', 4000)</script>
          <div class="allowedTags">&lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
          <input type="button" onclick="saveComment()" class="commentButton" name="saveBtn" value="Save">
          <input type="button" onclick="deleteComment()" class="commentButton" name="deleteBtn" value="Delete">
          <input type="button" onclick="expandComment()" class="commentButton" name="biggerBtn" value="Bigger Box">
          <a id="expandCommentLink" href="<c:url value="/html/big_comment.html"/>" style="display:none;"> </a>
          <div class="commentDisclaimer">
            You can comment on a song only once, but you can edit your comment as many times as you like.
          </div>
        </form>
        <br>
          </c:if>
        <br>
        <span style="vertical-align:middle;">
          <%-- hidden link for new playlist modal control --%>
          <a id="addPlaylistLink" href="<c:url value="/user/add_playlist"/>" style="display:none;"/>
          <a id="playlistLink" class="playlistLink" href="<c:url value="/songs/${song.titleForUrl}-${song.id}"/>" onclick="togglePlaylistMenu(); return false;" title="Click to view playlists">Add to Playlist</a>
          <img alt="...loading..." id="playlistSpinner" src="<c:url value="/img/ajax-loader.gif"/>" width="16" height="16" style="display:none;vertical-align:middle;">
          <br><div id="playlistMenu" class="popupMenu" style="display:none;position:absolute;"></div>
        </span>
        <br>
        <br>
        <jsp:include page="/WEB-INF/jsp/include/simple_song_search_form.jsp"/>
        <br>
        <br>
        <b>Go To:</b><br>
        <a href="<c:url value="/random_song"/>">Random Song</a><br>
        <a href="<c:url value="/newer_song?id=${song.id}"/>">Newer Song</a><br>
        <a href="<c:url value="/older_song?id=${song.id}"/>">Older Song</a><br>
        <a href="<c:url value="/higher_rated_song?id=${song.id}"/>">Higher Rated Song</a><br>
        <a href="<c:url value="/lower_rated_song?id=${song.id}"/>">Lower Rated Song</a>
        <br><br>
        <c:if test="${originalComment ne null}">
          <b>Go To:</b><br>
          <a href="<c:url value="/newer_comment?id=${originalComment.id}"/>&songId=<c:out value="${song.id}"/>">Newer Comment, This Song</a><br>
          <a href="<c:url value="/older_comment?id=${originalComment.id}"/>&songId=<c:out value="${song.id}"/>">Older Comment, This Song</a><br>
          <a href="<c:url value="/newer_comment?id=${originalComment.id}"/>">Newer Comment, Any Song</a><br>
          <a href="<c:url value="/older_comment?id=${originalComment.id}"/>">Older Comment, Any Song</a>
          <br><br>
        </c:if>
        <c:if test="${loggedIn}">
        <a id="noscoreRatingLink" href="javascript:setRating('noscore')" class="postLink<c:if test="${rating.rating eq -1}"> selectedRating</c:if>">Mark as rated without rating</a>
        <br><br>
        </c:if>
        <a href="<c:url value="/report_broken_link"/>?songId=<c:out value="${song.id}"/>" class="postLink">Report this link as broken</a><br>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <div id="control-template">
   <!-- control markup inserted dynamically after each link -->
   <div class="controls">
    <div class="statusbar">
     <div class="loading"></div>
      <div class="position"></div>

    </div>
   </div>
   <div class="timing">
    <div id="sm2_timing" class="timing-data">
     <span class="sm2_position">%s1</span> / <span class="sm2_total">%s2</span></div>
   </div>
   <div class="peak">

    <div class="peak-box"><span class="l"></span><span class="r"></span>
    </div>
   </div>
  </div>

  <div id="spectrum-container" class="spectrum-container">
   <div class="spectrum-box">
    <div class="spectrum"></div>
   </div>

  </div>
  <%-- hidden link for new login modal --%>
  <a id="loginLink" href="<c:url value="/login-ajax"/>" style="display:none;"/>
  <script type="text/javascript" src="<c:url value="/js/effects-1.8.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
  <script type="text/javascript">
    <c:if test="${not loggedIn}">
    var loginModal = new Control.Modal('loginLink', {
      requestOptions:{method:'get'},
      fade:true,
      width:500,
      height:310,
      fadeDuration:0.25
//      onSuccess:bigCommentOpenHandler
    });
    Ajax.Responders.register({
      onCreate: function(transport) {
        if (transport.url.match(/\/user/)) {
          throw 'login';
        }
      },
      onException: function() {
        loginModal.open();
      }
    });
    </c:if>
    function toggleMoreInfo() {
      if ($('moreInfo').style.display == 'none') {
        $('moreInfo').show();//style.display = 'block';
        $('moreInfoLink').update('[ Hide More Info ]');
      } else {
        $('moreInfo').hide();//style.display = 'none';
        $('moreInfoLink').update('[ Show More Info ]');
      }
    }
    var multiquoteURL = '<c:url value="/multiquote_song"/>';
    var addTitle = 'Add to multiquote';
    var removeTitle = 'Remove from multiquote';
    function toggleMultiquote(isComment, itemId) {
      var params = (isComment ? { "commentId" : itemId } : { "replyId" : itemId });
      new Ajax.Request(multiquoteURL, {
        asynchronous:false,
        parameters: params,
        method:'post',
        onSuccess:function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            var mqlink = $('multiquote'+(isComment?"Comment":"")+itemId);
            if (mqlink.hasClassName('quoted')) {
              mqlink.removeClassName('quoted');
              mqlink.title = addTitle;
            } else {
              mqlink.addClassName('quoted');
              mqlink.title = removeTitle;
            }
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure:function() {
          alert("Could not multquote item, please try again");
        }
      })
    }
    var ratingURL = '<c:url value="/user/rate_song"/>';
    var songId = <c:out value="${song.id}"/>;
    var start = <c:out value="${start}" default="-1"/>;
    var favoriteURL = '<c:url value="/user/favorite_song"/>';
    var commentURL = '<c:url value="/user/edit_comment"/>';
    var refreshCommentsURL = '<c:url value="/include/song_comments"/>';
    var hasComment = <c:out value="${myComment ne null}"/>;
    var playlistMenuURL = '<c:url value="/user/playlist_menu"/>';
    var addToPlaylistURL = '<c:url value="/user/add_to_playlist"/>';
    var playlistURL = '<c:url value="/playlist"/>';
    var deleteReplyURL = '<c:url value="/user/delete_reply"/>?id=';
  <c:if test="${canEdit}">
    var copyFromBandcampUrl = '<c:url value="/user/bandcamp/song/info"/>';
    var editURL = '<c:url value="/user/edit_song"/>';
    var oldSongInfoHTML;
    var oldSongUrL;
    function editSong() {
      oldSongInfoHTML = $('songInfoDiv').innerHTML;
      oldSongUrl = $('song1') ? $('song1').href : null;
      new Ajax.Request(editURL, {
        asynchronous : false,
        parameters : {
          songId : songId
        },
        method : 'get',
        onSuccess : function(transport) {
          $('songInfoDiv').update(transport.responseText);
        },
        onFailure : function() {
          alert('Could not edit song, please try again');
        }
      })
    }
    function cancelEdit() {
      $('songInfoDiv').update(oldSongInfoHTML);
    }
    function saveSong() {
      $('editSongForm').request({
        asynchronous : false,        
        onSuccess : function(transport) {
          $('songInfoDiv').update(transport.responseText);
          // re-initialize soundmanager
          if (oldSongUrl && $('song1') && oldSongUrl != $('song1').href) {
            soundManager.reboot();
          }
        }
      });
    }
    var deleteURL = '<c:url value="/user/delete_song"/>';
    function deleteSong() {
      if (confirm('Are you sure you want to delete this song?\nThis cannot be undone, and will\ndelete all comments, ratings, etc.')) {
        new Ajax.Request(deleteURL, {
          asynchronous: false,
          parameters : {
            songId : songId
          },
          method : 'get',
          onSuccess : function(transport) {
            var responseJSON = transport.responseJSON;
            if (responseJSON && responseJSON.success) {
              location.href = "<c:url value="/artists/${song.artist.nameForUrl}-${song.artist.id}"><c:param name="msg" value="Song deleted"/></c:url>";
            } else {
              alert(responseJSON.error);
            }
          },
          onFailure : function() {
            alert('Could not delete song, please try again');
          }
        });
      }
    }
  </c:if>
  </script>
  <script type="text/javascript" src="<c:url value="/js/song.js"/>"></script>
  </body>
</html>