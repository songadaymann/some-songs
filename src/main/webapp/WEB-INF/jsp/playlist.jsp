<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Playlist: '<c:out value="${playlist.title}"/>' by <c:out value="${playlist.user.displayName}"/></title>
    <%@ include file="/WEB-INF/jsp/include/pageplayer_head.jsp"%>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jump_nav.js"/>"></script>
    <c:if test="${canEdit}">
    <style type="text/css">
      input[type="submit"].editor_ok_button, input[type="submit"].editor_cancel_button {
        width:80px;
      }
      .inplaceeditor-saving {
        background: url('<c:url value="/"/>/img/ajax-loader.gif') bottom right no-repeat;
      }
      .playlistItemNote, .inplaceeditor-form {
        margin-left:40px;
      }
    </style>
    </c:if>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="playlistInfoDiv">
            <%@ include file="/WEB-INF/jsp/include/playlist_info.jsp"%>
          </div>
          <br>

          <c:choose>
            <c:when test="${param['deleted'] eq 'true'}">
              <div class="successMessage">
                Your reply has been deleted.
              </div>
              <br>
            </c:when>
            <c:when test="${param['commmentDeleted'] eq 'true'}">
              <div class="successMessage">
                Playlist comment deleted.
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

          <a href="#comments" title="Click to scroll down to the comments">Jump to Comments</a>
          <c:if test="${playlist.items ne null and fn:length(playlist.items) gt 0}">
            <c:url var="m3uURL" value="/somesongs_playlist.m3u">
              <c:forEach var="item" items="${playlist.items}" varStatus="loopStatus">
                <c:param name="songId" value="${item.song.id}"/>
              </c:forEach>
            </c:url>
            | <a href="<c:out value="${m3uURL}"/>" title="Click to download a .m3u playlist of these songs">Download .m3u</a>
          </c:if>
          <br>
          <br>

          <div class="playlistItemsDiv">
            <ul id="playlistItemList" class="playlist">
              <c:choose>
                <c:when test="${playlist.items eq null or fn:length(playlist.items) eq 0}">
                  <li style="cursor:default">No songs saved to playlist.</li>
                  <%-- TODO add autocomplete form for adding songs --%>
                  <%-- TODO or maybe simple song search in right column and drag and drop to playlist? --%>
                </c:when>
                <c:otherwise>
                  <c:forEach var="item" items="${playlist.items}" varStatus="loopStatus">
                    <li id="item_<c:out value="${item.id}"/>">
                      <%--<input id="item_<c:out value="${item.id}"/>" type="hidden" name="ordinal<c:out value="${item.id}"/>" value="<c:out value="${item.ordinal}"/>">--%>
                      <div style="display:none">${item.song.bandcampTrackId}</div>
                      <div style="float:left;margin-right:6px"><a class="playable" href="<c:url value="/songs/stream/${item.song.titleForUrl}-${item.song.id}.mp3"/>" title="Click to play/pause this
                          song"><img src="<c:url value="/img/play-pause-icon.png"/>" alt="Play/Pause" width="16" height="16"
                                     style="vertical-align:bottom;"></a></div>
                      <div class="playlistItemMain">
                        <c:if test="${canEdit}"><div style="float:right;margin-left:20px;text-align:right;"><a class="myInfoDelete" style="color:red;font-size:80%;cursor:pointer"
                        title="Remove song from playlist" href="javascript:deletePlaylistItem(<c:out value="${item.id}"/>)">X</a></div></c:if>
                        <c:if test="${canEdit}"><img class="itemHandle" src="<c:url value="/img/drag-handle2.png"/>" width="16" height="16" alt="Drag" style="cursor:move;vertical-align:middle;" title="Drag to reorder songs"></c:if>
                        <span class="plItemNum"><c:out value="${loopStatus.index + 1}"/>.</span>
                        <div style="display:inline"><a href="<c:url value="/songs/${item.song.titleForUrl}-${item.song.id}"/>" title="View song info"><c:out value="${item.song.title}"/></a>
                        by
                        <a href="<c:url value="/artists/${item.song.artist.nameForUrl}-${item.song.artist.id}"/>" title="View artist info"><c:out value="${item.song.artist.name}"/></a></div></div>
                      <div id="note_<c:out value="${item.id}"/>" class="playlistItemNote"<c:if test="${canEdit}"> title="Edit note for this playlist item" style="cursor:pointer"</c:if>>
                      <c:choose>
                        <c:when test="${item.note ne null}">
                          <c:out value="${item.note}" escapeXml="false"/>
                        </c:when>
                        <c:otherwise>
                          <c:choose>
                            <c:when test="${canEdit}">
                          <i>Add note</i>
                            </c:when>
                            <c:otherwise>
                              &nbsp;
                            </c:otherwise>
                          </c:choose>
                        </c:otherwise>
                      </c:choose>
                      </div>
                          <%--<input type="hidden" name="note<c:out value="${item.id}"/>" value="<c:out value="${item.note}"/>">--%>
                    </li>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
            </ul>
          </div>

          <br> <br> <a name="comments"><br></a>

          <c:choose>
            <c:when test="${originalComment ne null}">
              <div class="originalComment">
                <div class="songCommentsHeader">
                  <c:url var="quoteCommentLink" value="/user/playlist_comment_reply">
                     <c:param name="comment" value="${originalComment.id}"/>
                     <c:param name="quoteCommentId" value="${originalComment.id}"/>
                   </c:url>
                   <span style="float:right;margin-left:6px">
                    <a class="quotePostLink" href="<c:out value="${quoteCommentLink}"/>">Quote</a>
                  </span>
                  <c:set var="originalIsQuoted" value="${sessionScope['mqPlaylistCommentId'] eq originalComment.id and sessionScope['mqPlaylistQuoteOriginal'] eq 'true'}"/>
                  <span style="float:right">
                    <a id="multiquoteComment<c:out value="${originalComment.id}"/>" class="quotePostLink<c:if test="${originalIsQuoted}"> quoted</c:if>" href="javascript:toggleMultiquote(true, '<c:out value="${originalComment.id}"/>');">Multiquote</a>
                  </span>
                  <span style="font-weight:bold">Comment:</span>
                  <span class="smallBoldLink">
                    <a href="playlist?id=<c:out value="${playlist.id}"/>#comments">[Show All Comments]</a>
                    <c:if test="${isAdmin}">
                      <a class="editLink" href="<c:url value="/admin/delete_playlist_comment"/>?id=<c:out value="${originalComment.id}"/>">Delete Comment</a>
                      <a class="editLink" href="<c:url value="/admin/edit_playlist_comment"/>?id=<c:out value="${originalComment.id}"/>">Edit Comment</a>
                    </c:if>
                  </span>
                </div>
                <div class="songListHeader">
                  <div class="oddRow">
                    <%@ include file="/WEB-INF/jsp/include/original_playlist_comment.jsp"%>
                  </div>
                </div>
              </div>

              <br>

              <jsp:include page="/WEB-INF/jsp/include/playlist_comment_reply_paging.jsp"/>

              <br> <br> <br>

              <c:if test="${replySearch.totalResults > 0}">
                <c:forEach var="reply" items="${replies}" varStatus="loopStatus">
                  <c:set var="oddPost" value="${loopStatus.index mod 2 eq 0}"/>
                  <%@ include file="/WEB-INF/jsp/include/playlist_comment_reply.jsp" %>
                </c:forEach>

                <jsp:include page="/WEB-INF/jsp/include/playlist_comment_reply_paging.jsp"/>

                <br> <br> <br>

              </c:if>


            </c:when>
            <c:otherwise>
              <div id="commentsContainer">
                <jsp:include page="/include/playlist_comments"/>
                  <%--<jsp:param name="id" value="${playlist.id}"/>--%>
                  <%--<jsp:param name="start" value="${start}"/>--%>
                <%--</jsp:include>--%>
              </div>
              <div id="commentsLoading" style="display:none;">... loading ...</div>
            </c:otherwise>
          </c:choose>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <c:if test="${loggedIn}">
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
        <form action="<c:url value="/user/edit_playlist_comment"/>" name="commentForm">
          <textarea name="commentText" rows="5" style="width:100%"><c:out value="${myComment.commentText}"/><c:out value="${myComment.moreCommentText}"/></textarea>
          <div class="allowedTags">&lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
          <input type="button" onclick="saveComment()" class="commentButton" name="saveBtn" value="Save">
          <input type="button" onclick="deleteComment()" class="commentButton" name="deleteBtn" value="Delete">
          <input type="button" onclick="expandComment()" class="commentButton" name="biggerBtn" value="Bigger Box">
          <a id="expandCommentLink" href="<c:url value="/html/big_comment.html"/>" style="display:none;"> </a>
          <div class="commentDisclaimer">
            You can comment on a playlist only once, but you can edit your comment as many times as you like.
          </div>
        </form>
        <br>
          </c:if>
        <br>
        <br>
        </c:if>
        <b>Go To:</b><br>
        <a href="random_playlist">Random Playlist</a><br>
        <a href="newer_playlist?id=<c:out value="${playlist.id}"/>">Newer Playlist</a><br>
        <a href="older_playlist?id=<c:out value="${playlist.id}"/>">Older Playlist</a><br>
        <a href="higher_rated_playlist?id=<c:out value="${playlist.id}"/>">Higher Rated Playlist</a><br>
        <a href="lower_rated_playlist?id=<c:out value="${playlist.id}"/>">Lower Rated Playlist</a><br>
        <br>
<%--        <c:if test="${originalComment ne null}">
          <b>Go To:</b><br>
          <a href="newer_playlist_comment?id=<c:out value="${originalComment.id}"/>&playlistId=<c:out value="${playlist.id}"/>">Newer Comment, This Playlist</a><br>
          <a href="older_playlist_comment?id=<c:out value="${originalComment.id}"/>&playlistId=<c:out value="${playlist.id}"/>">Older Comment, This Playlist</a><br>
          <a href="newer_playlist_comment?id=<c:out value="${originalComment.id}"/>">Newer Comment, Any Playlist</a><br>
          <a href="older_playlist_comment?id=<c:out value="${originalComment.id}"/>">Older Comment, Any Playlist </a>
          <br><br>
        </c:if>
--%>
        <c:if test="${loggedIn}">
          <a id="noscoreRatingLink" href="javascript:setRating('noscore')" class="postLink<c:if test="${rating.rating eq -1}"> selectedRating</c:if>">Mark as rated without rating</a>
          <br><br>
        </c:if>
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
  <script type="text/javascript"><!--
  var multiquoteURL = '<c:url value="/multiquote_playlist"/>';
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
  //--></script>
  <c:if test="${loggedIn}">
    <script type="text/javascript" src="<c:url value="/js/effects-1.8.1.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/control.modal.2.2.3.js"/>"></script>
    <script type="text/javascript"><!--
    var ratingURL = '<c:url value="/user/rate_playlist"/>';
    var playlistId = <c:out value="${playlist.id}"/>;
    var start = <c:out value="${start}" default="-1"/>;
    var favoriteURL = '<c:url value="/user/favorite_playlist"/>';
    var commentURL = '<c:url value="/user/edit_playlist_comment"/>';
    var refreshCommentsURL = '<c:url value="/include/playlist_comments"/>';
    var hasComment = <c:out value="${myComment ne null}"/>;
    var deleteReplyURL = '<c:url value="/user/delete_playlist_reply"/>?id=';
    var multiquoteURL = '<c:url value="/user/multiquote_playlist"/>';
    //--></script>
    <script type="text/javascript" src="<c:url value="/js/playlist.js"/>"></script>
  <c:if test="${canEdit}">
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/dragdrop-1.8.1.js"/>"></script>
  <script type="text/javascript"><!--
    var playlistId = <c:out value="${playlist.id}"/>;
    var editURL = '<c:url value="/user/edit_playlist"/>';
    var oldPlaylistInfoHTML;
    function editPlaylist() {
      oldPlaylistInfoHTML = $('playlistInfoDiv').innerHTML;
      new Ajax.Request(editURL, {
        asynchronous : false,
        parameters : {
          playlistId : playlistId
        },
        method : 'get',
        onSuccess : function(transport) {
          $('playlistInfoDiv').update(transport.responseText);
          setTimeout('try { document.forms.editPlaylistForm.title.focus(); } catch (err) { }', 500);
        },
        onFailure : function() {
          alert('Could not edit playlist, please try again');
        }
      })
    }
    function cancelEdit() {
      $('playlistInfoDiv').update(oldPlaylistInfoHTML);
    }
    function savePlaylist() {
      $('editPlaylistForm').request({
        asynchronous : false,
        onSuccess : function(transport) {
          $('playlistInfoDiv').update(transport.responseText);
          // re-initialize JSAmp
          initStuff();
  //          setTimeout("initStuff();", 50);
        }
      });
    }
    var deleteURL = '<c:url value="/user/delete_playlist"/>';
    function deletePlaylist() {
      if (confirm('Are you sure you want to delete this playlist?\nThis cannot be undone, and will\ndelete all comments, ratings, etc.')) {
        new Ajax.Request(deleteURL, {
          asynchronous: false,
          parameters : {
            playlistId : playlistId
          },
          method : 'post',
          onSuccess : function(transport) {
            var responseJSON = transport.responseJSON;
            if (responseJSON && responseJSON.success) {
              location.href = "<c:url value="/user/my_info"><c:param name="playlistDeleted" value="true"/></c:url>";
            } else {
              alert(responseJSON.error);
            }
          },
          onFailure : function() {
            alert('Could not delete playlist, please try again');
          }
        });
      }
    }
    var reorderPlaylistItemsURL = '<c:url value="/user/reorder_playlist_items"/>';
    Sortable.create("playlistItemList", {
      name : 'ordinal',
      handle : 'itemHandle',
      onUpdate: function() {
        setTimeout("sortableUpdateHandler()", 100);
      }
    });
    var sortableUpdateHandler = function() {
      new Ajax.Request(reorderPlaylistItemsURL, {
        asynchronous:false,
        method:'POST',
        parameters:{
          playlistId:'<c:out value="${playlist.id}"/>',
          itemIds:Sortable.sequence('playlistItemList').join(',')
        },
        onSuccess:function(transport) {
          if (transport && transport.responseJSON) {
            if (transport.responseJSON.success) {
              // update item ordinals
              var indexSpans = $('playlistItemList').select(".plItemNum");
              for (var i = 0; i < indexSpans.length; i++) {
                indexSpans[i].innerHTML = (i + 1) + '.';
              }
            } else if (transport.responseJSON.error) {
              alert(transport.responseJSON.error);
            }
          }
        },
        onFailure:function() {
          alert("Unable to save song order, please try again");
        }
      });
    }
    var inPlaceEditor = [];
    var emptyFunction = function() { };
    var savePlaylistItemNoteURL = '<c:url value="/user/edit_playlist_item_note"/>';
  // TODO do this loop on the client side, to avoid it adding to the size of the page when there are lots of items
  <c:forEach var="item" items="${playlist.items}" varStatus="loopStatus">
    inPlaceEditor[<c:url value="${item.id}"/>] = new Ajax.InPlaceEditor("note_<c:url value="${item.id}"/>", savePlaylistItemNoteURL, {
      submitOnBlur : false,
      htmlResponse : false,
      cancelControl : 'button',
      okText : 'Save',
      cancelText : 'Cancel',
      rows : 3,
      onEnterHover : emptyFunction,
      onLeaveHover : emptyFunction,
      callback : function(form, value) {
        return 'id=<c:url value="${item.id}"/>&note=' + encodeURIComponent(value);
      },
      ajaxOptions : {
        method : 'POST',
        onSuccess : function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON) {
            if (responseJSON.success) {
              $('note_<c:url value="${item.id}"/>').innerHTML = (responseJSON.note || '');
            } else if (responseJSON.error){
              alert(responseJSON.error)
            }
          }
        }
      }
    });
    inPlaceEditor[<c:url value="${item.id}"/>].getText = function() {
      return this.element.innerHTML.replace(/^\s*/,'').replace(/\s*$/,'');
    };
  </c:forEach>
  var deletePlaylistItemURL = '<c:url value="/user/delete_playlist_item"/>';
  function deletePlaylistItem(playlistItemId) {
    if (confirm('Remove this playlist item?')) {
      new Ajax.Request(deletePlaylistItemURL, {
        asynchronous:false,
        method:'POST',
        parameters:{
          playlistItemId:playlistItemId
        },
        onSuccess:function(transport) {
          if (transport && transport.responseJSON) {
            if (transport.responseJSON.success) {
              $('playlistItemList').removeChild($('item_'+playlistItemId));
              inPlaceEditor[playlistItemId].dispose();
            } else if (transport.responseJSON.error) {
              alert(transport.responseJSON.error);
            }
          }
        },
        onFailure:function(){
          alert('Unable to delete playlist item, please try again');
        }
      });
    }
  }
  //--></script>
  </c:if>
  </c:if>
  </body>
</html>
