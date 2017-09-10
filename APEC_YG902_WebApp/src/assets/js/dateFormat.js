+function (Date) {
  Date.prototype.format = function(dateStr,format) {
    var timestamp3 = dateStr;
    var newDate = new Date();
    newDate.setTime(timestamp3);
    var date = {
      "M+": newDate.getMonth() + 1,
      "d+": newDate.getDate(),
      "h+": newDate.getHours(),
      "m+": newDate.getMinutes(),
      "s+": newDate.getSeconds(),
      "q+": Math.floor((newDate.getMonth() + 3) / 3),
      "S+": newDate.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
      format = format.replace(RegExp.$1, (newDate.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
      if (new RegExp("(" + k + ")").test(format)) {
        format = format.replace(RegExp.$1, RegExp.$1.length == 1
          ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
      }
    }
    return format;
  }
}(Date);
