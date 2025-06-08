function jumpPages(theLink, baseURL, totalResults, resultsPerPage) {
  var theDiv = $(theLink).next();
  if (theDiv == undefined) {
    $(theLink).insert({after:'<div class="popupMenu" style="display:none"></div>'});
    theDiv = $(theLink).next();
  }
  if (theDiv.style.display != 'none') {
    theDiv.hide();
  } else {
    if (!theDiv.innerHTML) {
      var a = [];
      var i = 0;
      var row = 0;
      a[i++] = '<ul>';
      for (var j = 0; j < totalResults; j += resultsPerPage) {
        a[i++] = '<li class="row' + row++ % 2 + '" onclick="location.href=\'';
        a[i++] = baseURL;
        a[i++] = (baseURL.indexOf('?') > -1 ? '&' : '?');
        a[i++] = 'start=' + (j + 1) + '\'">';
        var end = Math.min(totalResults, j + resultsPerPage);
        var start = Math.min(j + 1, end - resultsPerPage + 1);
        a[i++] =  start + ' - ' + end;
        a[i++] = '</li>';
      }
      a[i++] = '</ul>';
      theDiv.innerHTML = a.join('');

      theDiv.clonePosition(theLink, {
        setLeft: true,
        setTop : true,
        setWidth : false,
        setHeight: false,
        offsetTop : 20,
        offsetLeft : 0
      });
      // set initial height (height = 0 while hidden)
      theDiv.style.height = "140px";
      theDiv.style.width = (totalResults > 9999 ? 125 : (totalResults > 999 ? 100 : (totalResults > 99 ? 75 : 50))) + "px";
    }
    theDiv.show();
    // size the div to fit the content, if necessary
    theDiv.style.height = (Math.min(140, theDiv.firstDescendant().getHeight()) + "px");
  }
}
