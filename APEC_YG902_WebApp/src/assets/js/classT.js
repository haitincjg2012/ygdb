/**
 * Created by Administrator on 2017/9/7.
 */

var utile = {
  hasClass:function (e,c) {
    var re = new RegExp("(^|\\s)" + e + "(\\s|$)");
    return re.test(c);
  },
  addClass:function (e,c) {
    var arg_arr = [].slice.call(arguments),
      i = 1,
      l = arg_arr.length,
      arr = [];
    for(;i < l; i++){
      var flag = test.hasClass(arg_arr[0],arg_arr[i]);
      if(!flag){
        arr.push(arg_arr[i]);
      }
    }

    var t_arr = arg_arr[0].split(" ");
    return t_arr.concat(arr).join(" ");
  },
  removeClass:function (e,c) {
    var arg_arr = [].slice.call(arguments),
      i = 1,
      l = arg_arr.length,
      arr = [];
    var t = arg_arr[0]
    for(;i < l; i++){
      var re =new RegExp("(^|\\s)"+arg_arr[i] + "(\\s|$)","g")
      t = t.replace(re," ");
    }
    return t;
  },
}

export default{utile};
