// modified version of del.icio.us "playtagger"
// see http://del.icio.us/js/playtagger
if(typeof(Delicious) == 'undefined') Delicious = {}
Delicious.Mp3 = {
	playimg: null,
	player: null,
	go: function() {
		var all = document.getElementsByTagName('a')
    var spanId = 0;
    for (var i = 0, o; o = all[i]; i++) {
			if(o.href.match(/\.mp3$/i)) {
				var span = document.createElement('span')
        span.id = 'playLink' + spanId++;
        span.className = "playerSpan";
        span.style.marginLeft = '2px';
        span.style.paddingRight = '1px';
        span.innerHTML = "PLAY";
        span.onmouseover = function() { this.style.textDecoration = 'underline'; this.style.color = '#0000FF';};
        span.onmouseout = function() { this.style.textDecoration = 'none'; this.style.color = '#000055'};
        span.title = "Listen to song now"
        span.onclick = Delicious.Mp3.makeToggle(span, o.href)
        if (o.nextSibling) {
          o.parentNode.insertBefore(span.nextSibling, o)
        } else {
          o.parentNode.appendChild(span);
        }
  }}},
	toggle: function(span, url) {
		if (Delicious.Mp3.playimg == span) Delicious.Mp3.destroy()
		else {
			if (Delicious.Mp3.playimg) Delicious.Mp3.destroy()
			var a = span.previousSibling;
			span.innerHTML = 'STOP';
      Delicious.Mp3.playimg = span;
      span.style.paddingRight = '0px';
			Delicious.Mp3.player = document.createElement('span')
			Delicious.Mp3.player.innerHTML = '<object style="vertical-align:bottom;margin-right:0.2em;visibility:hidden;width:0px;height:0px;" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"' +
			'codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"' +
			'width="100" height="14" id="player" align="middle">' +
			'<param name="wmode" value="transparent" />' +
			'<param name="allowScriptAccess" value="sameDomain" />' +
			'<param name="flashVars" value="theLink='+url+'&amp;fontColor=000055" />' +
			'<param name="movie" value="http://images.del.icio.us/static/swf/playtagger.swf" /><param name="quality" value="high" />' +
			'<embed style="vertical-align:bottom;margin-right:0.2em;visibility:hidden;width:0px;height:0px;" src="http://images.del.icio.us/static/swf/playtagger.swf" flashVars="theLink='+url+'&amp;fontColor=000055"'+
			'quality="high" wmode="transparent" width="100" height="14" name="player"' +
			'align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash"' +
			' pluginspage="http://www.macromedia.com/go/getflashplayer" /></object>'
			span.parentNode.insertBefore(Delicious.Mp3.player, span.nextSibling)
	}},
	destroy: function() {
		Delicious.Mp3.playimg.innerHTML = 'PLAY';
    Delicious.Mp3.playimg.style.paddingRight = '1px';
    Delicious.Mp3.playimg = null;
		Delicious.Mp3.player.removeChild(Delicious.Mp3.player.firstChild);
    Delicious.Mp3.player.parentNode.removeChild(Delicious.Mp3.player);
    Delicious.Mp3.player = null
	},
	makeToggle: function(img, url) { return function(){ Delicious.Mp3.toggle(img, url) }}
}

Delicious.addLoadEvent = function(f) { var old = window.onload
	if (typeof old != 'function') window.onload = f
	else { window.onload = function() { old(); f() }}
}

Delicious.addLoadEvent(Delicious.Mp3.go)
