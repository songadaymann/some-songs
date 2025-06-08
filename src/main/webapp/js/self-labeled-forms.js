if (Object.isUndefined(Axent)) { var Axent = { } }
Axent.SelfLabeledInput = Class.create({
	initialize: function() {
		var labelSelector = arguments[0] || 'label';
		$$(labelSelector).findAll(function(l) {return (l.readAttribute('for') !== null)}).each(function(l){
      if ('text' == $(l.readAttribute('for')).type) {
        l.hide();
  			$(l.readAttribute('for'))._value = l.innerHTML;
	  		if ($(l.readAttribute('for')).value.empty()) {
          $(l.readAttribute('for')).value = $(l.readAttribute('for'))._value
        }
		  	$(l.readAttribute('for')).observe('blur',function(e){if(Event.element(e).value == '') Event.element(e).value = Event.element(e)._value;});
			  $(l.readAttribute('for')).observe('focus',function(e){if(Event.element(e).value == Event.element(e)._value) Event.element(e).value = '';});
      }
    });
	}
});
