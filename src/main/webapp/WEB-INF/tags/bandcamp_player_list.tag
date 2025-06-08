<%@ attribute name="song" type="com.ssj.model.song.Song" required="true" %>
<iframe width="46" height="23" style="position:relative;width:46px;height:23px;float:left"
        src="http://bandcamp.com/EmbeddedPlayer/v=2/track=${song.bandcampTrackId}/size=short/bgcol=FFFFFF/linkcol=4285BB/"
        allowtransparency="true" frameborder="0"><a href="${song.bandcampUrl}">${song.title} by ${song.artist.name}</a></iframe><div
        style="margin-left:50px;height:21px;width:46px;border:1px solid #ddd;background-color:white;text-align:center"><a style="display:block;margin-top:3px"
        href="${song.bandcampUrl}?action=download" title="Buy this song on Bandcamp" target="_blank">BUY</a></div>