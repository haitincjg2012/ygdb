/*
    # @pargam option{
      designWidth: 设计稿宽度，必须
      designHeight: 设计稿高度，不传的话则比例按照宽度来计算，可选
      designFontSize: 设计稿宽高下用于计算的字体大小，默认20，可选
      callback: 字体计算之后的回调函数，可选
    }
*/
!function(win, option) {
  var count = 0,
      designWidth = option.designWidth,
      designHeight = option.designHeight || 0,
      designFontSize = option.designFontSize || 40,
      callback = option.callback || null,
      root = document.documentElement,
      body = document.body,
      rootWidth, newSize, t, self;
      root.style.width = '100%';
  function _getNewFontSize() {
    var scale = designHeight !== 0 ? Math.min(win.innerWidth / designWidth, win.innerHeight / designHeight) : win.innerWidth / designWidth;
    return parseInt( scale * 10000 * designFontSize ) / 10000;

  }

  !function () {
    rootWidth = root.getBoundingClientRect().width ? root.getBoundingClientRect().width : root.getBoundingClientRect().right - root.getBoundingClientRect().left;
    self = self ? self : arguments.callee;
    if( rootWidth !== win.innerWidth &&  count < 20 ) {
      win.setTimeout(function () {
        count++;
        self();
      }, 0);
    } else {
      newSize = _getNewFontSize();

      if( newSize + 'px' !== getComputedStyle(root)['font-size'] ) {
        root.style.fontSize = newSize + "px";
        return callback && callback(newSize);
      };
    };
  }();
  win.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", function() {
    clearTimeout(t);
    t = setTimeout(function () {
      self();
    }, 300);
  }, false);
}(window, {
  designWidth: 750,
  // designHeight: 1136,
  designFontSize: 40,
  callback: function (argument) {
  }
});


if(window.devicePixelRatio && devicePixelRatio == 2){
    document.querySelector('html').className = 'dpr2';
}
if(window.devicePixelRatio && devicePixelRatio == 3){
    document.querySelector('html').className = 'dpr3';
}
