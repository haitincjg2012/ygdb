import base64 from './base64'
import sha1 from './sha1'

/*前端加密方案*/
var setToken = function(token){
    var currentTime = Math.round(new Date().getTime());//获取毫秒数
    let token_head = base64(token+'.'+currentTime);
    let token_body_string = "secret=APEC2017&time="+currentTime+"&token="+token;
    var token_body = sha1.SHA1(token_body_string, { asString: true });
    return token_head+'.'+token_body;
  }

  export default setToken
