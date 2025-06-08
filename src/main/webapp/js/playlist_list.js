function setRating(rating, ratingLink) {
  var playlistId = ratingLink.id.match(/\d+/)[0];
  new Ajax.Request(ratingURL, {
    asynchronous : false,
    parameters : {
      "playlistId" : playlistId,
      "rating" : rating
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON && responseJSON.success) {
        removeSelectedClass(playlistId);
        $(rating+'RatingLink'+playlistId).addClassName('selectedRating');
//            $('numRatings'+songId).innerHTML = responseJSON.numRatings;
        if (responseJSON.rating) {
          $('needsRatings'+playlistId).hide();
          $('rating'+playlistId).update(responseJSON.rating);
          $('rating'+playlistId).show();
        } else if (responseJSON.numRatingsNeeded) {
          $('rating'+playlistId).hide()
          $('numRatingsNeeded'+playlistId).update(responseJSON.numRatingsNeeded);
          $('needsRatings'+playlistId).show();
        }
      } else {
        alert(responseJSON.error);
      }
    },
    onFailure : function() {
      alert('Could not save rating, please try again');
    }
  });
  return false;
}
function removeSelectedClass(playlistId) {
  $('goodRatingLink'+playlistId).removeClassName('selectedRating');
  $('okayRatingLink'+playlistId).removeClassName('selectedRating');
  $('badRatingLink'+playlistId).removeClassName('selectedRating');
  $('noneRatingLink'+playlistId).removeClassName('selectedRating');
}
/*
function saveSearchOpenHandler() {
  document.forms.saveSearchForm.searchName.focus();
}
function saveSearch() {
  var searchName = document.forms.saveSearchForm.searchName.value;
  new Ajax.Request(saveSearchURL, {
    asynchronous : false,
    parameters : {
      searchName : searchName
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON && responseJSON.success) {
        $('searchNameEl').innerHTML = searchName;
        $('saveSearchLink').hide();
        $('searchSavedMessage').show();
        searchModal.close()
        new Effect.Highlight('searchSavedMessage');
      } else {
        $('songSearchErrors').innerHTML = responseJSON.error;
        $('songSearchErrors').show();
      }
    },
    onFailure : function() {
      alert('Could not save search, please try again');
    }
  })
}
function cancelSaveSearch() {
  document.forms.saveSearchForm.searchName.value = '';
  searchModal.close();
}

*/
