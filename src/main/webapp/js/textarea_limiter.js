var TextareaLimiter = {
  LIMITER_DATA : {},
  addLimiter : function(textareaId, maxChars, showRemainingChars) {
    var textareaEl = $(textareaId);
    if (textareaEl) {
      var charsUsedSpanId = textareaId + 'CharsUsed';
      this.LIMITER_DATA[textareaId] = {
        'charsUsedSpanId' : charsUsedSpanId,
        'maxChars' : maxChars,
        'showRemainingChars' : showRemainingChars
      };
      textareaEl.insert({after : '<div style="font-size:75%"><b id="'+charsUsedSpanId+'">0</b> of <b>'+maxChars+'</b> characters used.</div>'});
      // prototype event observe stuff not working for some reason, use old school approach
      textareaEl.onkeydown = this.updateCharacterCount.bind(textareaEl);
      textareaEl.onkeyup = this.updateCharacterCount.bind(textareaEl);
      textareaEl.onblur = this.updateCharacterCount.bind(textareaEl);
      textareaEl.onblur();
    }
  },
  updateCharacterCount : function() {
    var limiter = TextareaLimiter.LIMITER_DATA[this.id];
    if (limiter) {
      if (this.value.length >= limiter.maxChars) {
        this.value = this.value.substring(0, limiter.maxChars);
      }
      var charsUsedSpan = $(limiter.charsUsedSpanId);
      if (charsUsedSpan) {
        if (limiter.showRemaining) {
          charsUsedSpan.innerHTML = limiter.maxCharCount - this.value.length;
        } else {
          charsUsedSpan.innerHTML = this.value.length;
        }
      }
    }
  }
};
