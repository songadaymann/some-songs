var savingPopupTimeout = 500;
function setRating(rating) {
  if (!$(rating+'RatingLink').hasClassName('selectedRating')) {
    new Ajax.Request(ratingURL, {
      asynchronous : false,
      parameters : {
        playlistId : playlistId,
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
      playlistId : playlistId
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
      playlistId : playlistId,
      comment : commentText
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON) {
        alert(responseJSON.error);
      } else {
        if (!hasComment) {
          // adding new comment, will show up at end of list, reset start to go to last page
          hasComment = true;
          start = -1;
        }
        refreshComments();
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
      id : playlistId,
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
      playlistId : playlistId
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
