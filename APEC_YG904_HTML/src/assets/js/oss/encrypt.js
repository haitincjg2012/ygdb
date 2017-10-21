/**
 * Created by gsy on 2017/10/11.
 * 阿里云上传图片相关
 */
import md5 from '~/assets/js/md5'
import apiUrl from '~/api/url'
import ax from '~/api/request'
import commonJs from '~/assets/js/common'
var accessKeyId="",
    accessKeySecret="",
    securityToken="",
    storeAs="",
    curUserid=commonJs.getValue('userNo');

var encrypOss={
    //加密方案 md5（param+时间戳）
    setEncryption:function(param){
        var timestamp=new Date().getTime();//时间戳
        var encryptResult=md5(param+timestamp);
        return encryptResult;
    },
    //aly相关参数

    //userid:commonJs.getValue('userNo'),
    //请求授权信息Api
    initialAurise:function(){
        var vm=this;
        let params={
            url:apiUrl.transaction.authoriseUrl,
            data:{}
        };
        ax.post(params,vm.authuriseCb);
    },
    authuriseCb:function(data){
        //将授权信息保存在cookie中
        commonJs.setCookie('accessKeyId',data.data.AccessKeyId,50);
        commonJs.setCookie('accessKeySecret',data.data.AccessKeySecret,50);
        commonJs.setCookie('securityToken',data.data.SecurityToken,50);
        accessKeyId=commonJs.getCookie('accessKeyId') || "";
        accessKeySecret=commonJs.getCookie('accessKeySecret') || "";
        securityToken=commonJs.getCookie('securityToken') || "";

    },

    //获取授权信息
    getAuthorise:function(){
        var vm=this;
        var cookieString=document.cookie;
        var flag=true;//调用授权Api标志（true：调用）
        if(cookieString.indexOf("accessKeyId")>-1){//缓存存在授权信息
            flag=false;
        }
        if(flag){//请求授权
            vm.initialAurise();
        }
        else{
            //授权信息赋值
            accessKeyId=commonJs.getCookie('accessKeyId') || "";
            accessKeySecret=commonJs.getCookie('accessKeySecret') || "";
            securityToken=commonJs.getCookie('securityToken') || "";
            //storeAs=vm.setEncryption(curUserid); //照片名字
        }
    },
    /**
     * OSS Function
     */
      ossUpload:function(file,callBack,errCallback){
        var vm=this;
        storeAs=vm.setEncryption(curUserid); //照片名字
        //console.log("accessKeyId:"+accessKeyId+"  accessKeySecret:"+accessKeySecret+"  securityToken:"+securityToken+"  storeAs:"+storeAs);
          console.log("endpoint:"+apiUrl.common.alyEndpoint+" bucket:"+apiUrl.common.alyBucket);
        vm.checkCookie();
         var client = new OSS.Wrapper({
             accessKeyId: accessKeyId,
             accessKeySecret: accessKeySecret,
             stsToken: securityToken,
             endpoint:apiUrl.common.alyEndpoint,
             //endpoint: 'https://oss-cn-qingdao.aliyuncs.com/',
             bucket:apiUrl.common.alyBucket

         });

         return  client.multipartUpload(storeAs, file).then(function (result) {
             return callBack(result);
         }).catch(function (err) {
             return errCallback(err);
         });
    },
    checkCookie:function(){
        var vm=this;
        var cookieString=document.cookie;
        var flag=true;//调用授权Api标志（true：调用）
        if(cookieString.indexOf("accessKeyId")>-1){//缓存存在授权信息
            flag=false;
        }

        if(flag){
            vm.initialAurise();
        }
    }

    //aliyun 上传图片jsdk(yd)

    // ossUpload:function(storeAs,file,accessKeyId,accessKeySecret,securityToken,callBack,errCallback){
    //     var client = new OSS.Wrapper({
    //         accessKeyId:  accessKeyId,
    //         accessKeySecret: accessKeySecret,
    //         stsToken: securityToken,
    //         endpoint: 'http://oss-cn-hangzhou.aliyuncs.com/',
    //         bucket: 'ygdb-pro'
    //     });
    //     return  client.multipartUpload(storeAs, file).then(function (result) {
    //         return callBack(result);
    //     }).catch(function (err) {
    //         return errCallback(err);
    //     });
    //}
};
export default encrypOss
