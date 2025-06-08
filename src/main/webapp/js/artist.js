var oldArtistInfoHTML;
function editArtist() {
  oldArtistInfoHTML = $('artistInfoDiv').innerHTML;
  new Ajax.Request(editURL, {
    asynchronous : false,
    parameters : {
      artistId : artistId
    },
    method : 'get',
    onSuccess : function(transport) {
      $('artistInfoDiv').update(transport.responseText);
    },
    onFailure : function() {
      alert('Could not edit artist, please try again');
    }
  });
}
function cancelEdit() {
  $('artistInfoDiv').update(oldArtistInfoHTML);
}
function saveArtist() {
  $('editArtistForm').request({
    onSuccess : function(transport) {
      $('artistInfoDiv').update(transport.responseText);
    }
  });
}
// onclick starts out as "return false" so that clicks on the link before the Control.Modal code has
// been attached to it don't do anything
$('addLink').onclick = '';
var addLinkModal = new Control.Modal('addLink', {
  fade:true,
  fadeDuration:0.25,
  width:350,
  requestOptions:{method:'get', evalScripts:true}
//      ,
//      onSuccess:bigCommentOpenHandler
});
function saveLink() {
  $('editLinkForm').request({
    asynchronous : false,
    onSuccess : function(transport) {
      if (transport.responseText.indexOf('editLinkForm') > -1) {
        $('linkFormDiv').update(transport.responseText);
      } else {
        $('linksDiv').update(transport.responseText);
        addLinkModal.close();
      }
    },
    onFailure : function() {
      alert('Could not save artist link, please try again');
    }
  });
}
function cancelLinkEdit() {
  Control.Modal.close();
}
var editLinkModal;
function editLink(id) {
  $('editLink').href = editLinkURL + id;
  editLinkModal = new Control.Modal('editLink', {
    fade:true,
    fadeDuration:0.25,
    width:350,
    requestOptions:{method:'get', evalScripts:true}
  });
  editLinkModal.open();
}
function deleteArtist() {
  if (confirm('Are you sure you want to delete this artist?\nThis will delete all songs for this artist\nand all song ratings, comments, etc.\nThis cannot be undone.')) {
    new Ajax.Request(deleteURL, {
      asynchronous:false,
      parameters: {
        id : artistId
      },
      method:'post',
      onSuccess:function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          location.href = myInfoURL;
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not delete aritst, please try again');
      }
    })
  }
}
function deleteLink(artistLinkId) {
  if (confirm('Are you sure you want to delete this artist link?\nThis cannot be undone.')) {
    new Ajax.Request(deleteLinkURL, {
      asynchronous:false,
      parameters: {
        id : artistLinkId
      },
      method:'post',
      onSuccess:function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          new Ajax.Updater('linksDiv', artistLinksURL, {
            asynchronous:false,
            parameters:{
              id : artistId
            },
            method:'get'
          });
          editLinkModal.close();
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure : function() {
        alert('Could not delete artist, please try again');
      }
    })
  }
}
/*
function reloadOtherUsers() {
  removeEventHandlers($('otherUsersDiv'));
  new Ajax.Updater('otherUsersDiv', otherUsersURL, {
    parameters : {
      id : artistId
    },
    onSuccess:function(transport) {
      addEventHandlers(null, $('otherUsersDiv'));
    }
  });
}
function reloadRelatedArtists() {
  removeEventHandlers($('relatedArtistsDiv'));
  new Ajax.Updater('relatedArtistsDiv', relatedArtistsURL, {
    parameters : {
      id : artistId
    },
    onSuccess:function(transport) {
      addEventHandlers(null, $('relatedArtistsDiv'));
    }
  });
}
*/
/*var otherUsersURL = '<c:url value="/include/other_users.do"/>';*/
var updateOtherUsers = function(selectedUser) {
  new Ajax.Request(addOtherUserURL, {
    asynchronous:false,
    parameters:{
      id : artistId,
      userId : selectedUser.id
    },
    method:'post',
    onSuccess:function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON && responseJSON.success) {
        location.reload();
        // TODO just reload changed section, but event handlers not working after that...
//            reloadOtherUsers();
      } else {
        alert(responseJSON.error);
      }
    },
    onFailure:function() {
      alert('Unable to share artist with user, please try again');
    }
  });
}
/*var relatedArtistsURL = '<c:url value="/include/related_artists.do"/>';*/
var updateRelatedArtists = function(selectedArtist) {
  new Ajax.Request(addRelatedArtistURL, {
    asynchronous:false,
    parameters:{
      id : artistId,
      artistId : selectedArtist.id
    },
    method:'post',
    onSuccess:function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON && responseJSON.success) {
        location.reload();
        // TODO just reload changed section, but event handlers not working after that...
//            reloadRelatedArtists();
      } else {
        alert(responseJSON.error);
      }
    },
    onFailure:function() {
      alert('Unable to add related artist, please try again');
    }
  });
}
new Ajax.Autocompleter('otherUserName', 'otherUsersMenu', searchUsersURL, {
  paramName:'name',
  minChars:2,
  indicator:'otherUserSpinner',
  updateElement:updateOtherUsers
});
new Ajax.Autocompleter('relatedArtistName', 'relatedArtistsMenu', searchArtistsURL, {
  paramName:'name',
  minChars:2,
  indicator:'relatedArtistSpinner',
  updateElement:updateRelatedArtists
});
var addEventHandlers = function() {
  var myInfoLists = $('rightNavBox').select('ul.myInfoList');
  myInfoLists.each(function(myInfoList) {
    var listItems = myInfoList.childElements();
    listItems.each(function(listItem) {
      listItem.observe('mouseover', listItemMouseoverHandler);
    });
  });
}
/*
var removeEventHandlers = function(element) {
  displayedId = null;
  var myInfoLists = element.select('ul.myInfoList');
  myInfoLists.each(function(myInfoList) {
    var listItems = myInfoList.childElements();
    listItems.each(function(listItem) {
      listItem.stopObserving('mouseover', listItemMouseoverHandler);
    });
  });
}
*/
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
    element.insert({top:'<span id="delete'+element.id+'" style="float:right"><a class="myInfoDelete" title="Delete" href="javascript:deleteArtistInfo(\''+element.id+'\')">X</a></span>'});
  } else {
    $('delete'+element.id).show();
  }
  displayedId = element.id;
}
Event.observe(window,'load',addEventHandlers);
function deleteArtistInfo(itemId) {
  new Ajax.Request(deleteItemURL, {
    asynchronous : false,
    parameters : {
      id : artistId,
      itemId : itemId
    },
    method : 'post',
    onSuccess : function(transport) {
      var responseJSON = transport.responseJSON;
      if (responseJSON && responseJSON.success) {
        location.reload();
        // TODO just reload changed section, though couldn't get the event handlers to work again after that...
/*
        if (itemId.match(/^ou/)) {
          reloadOtherUsers();
        } else {
          reloadRelatedArtists();
        }
*/
      } else {
        alert(responseJSON.error);
      }
    },
    onFailure : function() {
      alert('Could not delete info, please try again');
    }
  });
}
