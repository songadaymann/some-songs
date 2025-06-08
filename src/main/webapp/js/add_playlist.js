$('addPlaylistLink').onclick = '';
var addPlaylistModal = new Control.Modal('addPlaylistLink', {
  fade:true,
  fadeDuration:0.25,
  width:400,
  requestOptions : {
    method : 'get',
    evalScripts:true
  }
});
function savePlaylist() {
  $('addPlaylistForm').request({
  onSuccess : function (transport) {
    var responseJSON = transport.responseJSON;
    if (responseJSON && responseJSON.success) {
      window.location.href = playlistURL+'?id='+responseJSON.id;
    } else {
      $('modal_container').update(transport.responseText);
    }
  }
});
}
function cancelPlaylist() {
  addPlaylistModal.close();
}
