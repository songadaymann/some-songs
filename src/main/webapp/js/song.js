var savingPopupTimeout = 500;
function setRating(rating) {
  if (!$(rating+'RatingLink').hasClassName('selectedRating')) {
    new Ajax.Request(ratingURL, {
      asynchronous : false,
      parameters : {
        songId : songId,
        rating : rating
      },
      method : 'post',
      onSuccess : function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          removeSelectedClass();
          $(rating+'RatingLink').addClassName('selectedRating');
          $('numRatings').innerHTML = responseJSON.numRatings;
          if (responseJSON.rating) {
            $('needsRatings').hide();
            $('rating').update(responseJSON.rating);
            $('rating').show();
          } else if (responseJSON.numRatingsNeeded) {
            $('rating').hide()
            $('numRatingsNeeded').update(responseJSON.numRatingsNeeded);
            $('needsRatings').show();
          }
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not save rating, please try again');
      }
    });
  }
}
function removeSelectedClass() {
  $('goodRatingLink').removeClassName('selectedRating');
  $('okayRatingLink').removeClassName('selectedRating');
  $('badRatingLink').removeClassName('selectedRating');
  $('noneRatingLink').removeClassName('selectedRating');
  $('noscoreRatingLink').removeClassName('selectedRating');
}
function toggleFavorite() {
  new Ajax.Request(favoriteURL, {
    asynchronous : false,
    parameters : {
      songId : songId
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
function saveComment() {
  var commentText = document.forms.commentForm.commentText.value;
  new Ajax.Request(commentURL, {
    asynchronous : false,
    parameters : {
      songId : songId,
      comment : commentText
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON) {
        if (responseJSON.error) {
          alert(responseJSON.error);
        } else {
          if (!hasComment) {
            // adding new comment, will show up at end of list, reset start to go to last page
            hasComment = true;
            start = -1;
          }
          refreshComments();
        }
      } else {
        alert("Unexpected response, comment may not have been saved");
      }
    },
    onFailure : function() {
      alert('Could not save comment, please try again');
    }
  })
}
if ($('commentsLoading')) {
  $('commentsLoading').clonePosition('commentsContainer');
}
function refreshComments() {
//  $('commentsContainer').innerHTML = "... loading ...";
  $('commentsContainer').hide();
  $('commentsLoading').show();
  new Ajax.Updater('commentsContainer', refreshCommentsURL, {
    asynchronous : false,
    onSuccess : function() {
//      setTimeout("$('commentsLoading').hide(); $('commentsContainer').show();", savingPopupTimeout);
      setTimeout("afterRefresh()", savingPopupTimeout);
    },
    parameters : {
      id : songId,
      refresh : true
    }
  })
}
function afterRefresh() {
  $('commentsLoading').hide();
  $('commentsContainer').show();
  var myComment = $('myComment');
  if (myComment) new Effect.Highlight('myComment');
}
function deleteComment() {
  new Ajax.Request(commentURL, {
    asynchronous : false,
    parameters : {
      songId : songId
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON.success) {
        hasComment = false;
        refreshComments();
      } else {
        alert(responseJSON.error);
      }
    },
    onFailure : function() {
      alert('Could not delete comment, please try again');
    }
  })
}
if ($('expandCommentLink')) {
  var commentModal = new Control.Modal('expandCommentLink', {
    requestOptions:{method:'get'},
    fade:true,
    fadeDuration:0.25,
    onSuccess:bigCommentOpenHandler
  });
}
function expandComment() {
  commentModal.open();
//      document.forms.bigCommentForm.bigCommentText.value = document.forms.commentForm.commentText.value;
}
function bigCommentOpenHandler() {
  $('bigCommentText').value = document.forms.commentForm.commentText.value;
}
function shrinkComment(saveChanges) {
  if (saveChanges) {
    document.forms.commentForm.commentText.value = $F('bigCommentText');
  }
  commentModal.close();
}
function saveBigComment() {
  shrinkComment(true);
  saveComment();
}
function deleteReply(replyId) {
  if (confirm("Delete the selected reply?\nThis cannot be undone.")) {
    location.href = deleteReplyURL + replyId;
  }
}
var loadingPlaylistMenu = false;
function togglePlaylistMenu() {
  if (!loadingPlaylistMenu) {
    if ($('playlistMenu').style.display != 'none') {
      $('playlistMenu').hide();
    } else {
      setTimeout("if (loadingPlaylistMenu) { $('playlistSpinner').show(); }", 50);
      loadingPlaylistMenu = true;
      new Ajax.Request(playlistMenuURL, {
        asynchronous : false,
        method : 'get',
        onSuccess : function(transport) {
          loadingPlaylistMenu = false;
          $('playlistMenu').innerHTML = transport.responseText;
//          $('playlistMenu').absolutize();
//          $('playlistMenu').clonePosition($('playlistLink'), {
//            setLeft: true,
//            setTop : true,
//            setWidth : false,
//            setHeight: false,
//            offsetTop : $('playlistLink').getDimensions().height
//            offsetLeft : 0
//          });
//          $('playlistMenu').setDimensions()
          $('playlistMenu').show();
          $('playlistMenu').style.height = Math.min(200, $('playlistList').getHeight()) + "px";
          $('playlistSpinner').hide();
        },
        onFailure : function() {
          loadingPlaylistMenu = false;
          alert('Could not load playlists, please try again');
          $('playlistSpinner').hide();
        }
      });
    }
  }
}
function addToPlaylist(playlistId) {
  new Ajax.Request(addToPlaylistURL, {
    asynchronous : false,
    parameters : {
      songId : songId,
      playlistId : playlistId
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON.success) {
        $('playlistMenu').innerHTML = '<p class="successMessage">Song added!</p>';
        setTimeout("$('playlistMenu').fade();", 1000);
      } else {
        alert(responseJSON.error);
      }
    },
    onFailure : function() {
      alert('Could not add song to playlist, please try again');
    }
  });
}
var addPlaylistModal;
function addToNewPlaylist() {
  if (!addPlaylistModal) {
    addPlaylistModal = new Control.Modal('addPlaylistLink', {
      fade:true,
      fadeDuration:0.25,
      width:400,
      requestOptions : {
        method : 'get',
        evalScripts:true
      }
    });
  }
  addPlaylistModal.open();
  setTimeout('try { document.forms.addPlaylistForm.title.focus(); } catch(err) { /*ignore*/ }', 500);
}
function savePlaylist() {
  $('addPlaylistForm').request({
  onSuccess : function (transport) {
    var responseJSON = transport.responseJSON;
    if (responseJSON && responseJSON.success) {
      addToPlaylist(responseJSON.id);
      addPlaylistModal.close();
    } else {
      $('modal_container').update(transport.responseText);
    }
  }
});
}
function cancelPlaylist() {
  addPlaylistModal.close();
}
function loadFromBandcamp(bandcampUrl) {
  $('copyFromBandcampSpinner').show();
  new Ajax.Request(copyFromBandcampUrl, {
    asynchronous:false,
    parameters : {
      bandcampUrl : bandcampUrl
    },
    method : 'get',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON && responseJSON.success) {
        var theForm = document.forms.editSongForm;
        theForm.duration.value = responseJSON.duration;
        theForm.albumTrackNumber.value = responseJSON.albumTrackNumber;
        theForm.title.value = responseJSON.title;
        theForm.bandcampTrackId.value = responseJSON.trackId;
        theForm.url.value = responseJSON.url;
        theForm.album.value = responseJSON.albumTitle || '';
        theForm.info.value = responseJSON.about || 'Song imported from Bandcamp.';
        theForm.info.onkeydown();
        theForm.moreInfo.value = responseJSON.credits;
        theForm.moreInfo.onkeydown();
      } else {
        alert(responseJSON.error);
      }
      $('copyFromBandcampSpinner').hide();
    },
    onFailure: function() {
      showCopyingIcon = false;
      $('copyFromBandcampSpinner').hide();
      alert('Could not load song info from Bandcamp, please try again');
    }
  });
  return false;
}
