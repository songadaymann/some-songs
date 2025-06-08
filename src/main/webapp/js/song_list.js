function setRating(rating, ratingLink) {
  var songId = ratingLink.id.match(/\d+/)[0];
  new Ajax.Request(ratingURL, {
    asynchronous : false,
    parameters : {
      "songId" : songId,
      "rating" : rating
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON && responseJSON.success) {
        removeSelectedClass(songId);
        $(rating+'RatingLink'+songId).addClassName('selectedRating');
//            $('numRatings'+songId).innerHTML = responseJSON.numRatings;
        if (responseJSON.rating) {
          $('needsRatings'+songId).hide();
          $('rating'+songId).update(responseJSON.rating);
          $('rating'+songId).show();
        } else if (responseJSON.numRatingsNeeded) {
          $('rating'+songId).hide()
          $('numRatingsNeeded'+songId).update(responseJSON.numRatingsNeeded);
          $('needsRatings'+songId).show();
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
function removeSelectedClass(songId) {
  $('goodRatingLink'+songId).removeClassName('selectedRating');
  $('okayRatingLink'+songId).removeClassName('selectedRating');
  $('badRatingLink'+songId).removeClassName('selectedRating');
  $('noneRatingLink'+songId).removeClassName('selectedRating');
}
function saveSearchOpenHandler() {
  document.forms.saveSearchForm.searchName.focus();
}
var searchModal;
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
var saveModals = [];
document.observe('dom:loaded', function() {
  var mainContent = $('yui-main').down().down();
  var listNames = mainContent.select('div.songListName');
  for (var i = 0; i < listNames.length; i++) {
    var listName = listNames[i];
    var saveLinks = listName.select('a.saveSearchLink');
    if (saveLinks && saveLinks.length > 0 && saveLinks[0] && saveLinks[0].href) {
      searchModal = new Control.Modal(saveLinks[0], {
        requestOptions:{method:'get'},
//        fade:false,
        fadeDuration:0.25,
        onSuccess: saveSearchOpenHandler,
        width:300
      });
      saveModals[i] = searchModal;
//      var saveSearchURL = '<c:url value="/user/save_search.do"/>';
    }
  }
});