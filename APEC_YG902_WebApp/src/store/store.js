
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);


const store = new Vuex.Store({
  state: {
    phone:'',//手机号
    token:window.localStorage.getItem('token'),//token
    imgUrl:'',//用户头像
    name:'',//名字
    userRealAuthName:'',//用户是否验证key
    userTypeName:'',//用户身份
    userRealAuthKey:'',//用户是否验证name
    navIndex:true,//底部导航是否显示
    id_code:'',//短信验证码，用户找回密码时候使用
    userId:'',//用户ID

    show:{
       home:false,
       add:false,
       user:false,
    },
    searchData:null,//搜索数据的保存
    searchInit:0,
    search:"",
    skuName:'',
    skuId:'',
    check:'',//用于选择是否是完善资料还是实名认证 0表示初始化 1表示完善资料 2表示实名认证
    xqInfo:0,
    xqInfoF:0,
    xqInfoS:0,
    address:0,//1表示发布供应信息 2表示求购信息 0表示初始话
    addrData:null,//地址消息
    addrSeekData:null,//求购的地址
    point:'',//用户积分
    userLevelKey:"",
    userLevelName:"",
    uploadAddr:{//上传单据用户地址
    },
    uploadGoodList:{//上传单据商品清单
    },
    nameN:"",//昵称的缓存
  },
  mutations: {
    incrementCheck(state,UT){
      state.check = UT.check
    },
    incrementuploadGoodList(state,UT){
      state.uploadGoodList = UT.uploadGoodList
    },
    incrementUploadAddr(state,UT){
      state.uploadAddr = UT.uploadAddr
    },
    incrementPoint(state,UT){
      state.point = UT.point
    },
    incrementUserLevelKey(state,UT){
      state.userLevelKey = UT.userLevelKey
    },
    incrementUserLevelName(state,UT){
      state.userLevelName = UT.userLevelName
    },
    incrementUserID(state,UT){
      state.userId = UT.userId
    },
    incrementUserData(state,UT){
      state.UserData = UT.UserData
    },
    incrementPhone(state,UT){
      state.phone = UT.phone
    },
    incrementToken(state,UT){
      state.token = UT.token
    },
    incrementNavIndex(state,UT){
      state.navIndex = UT.navIndex
    },
    incrementICode(state,UT){
      state.id_code = UT.id_code
    },
    incrementImgUrl(state,UT){
      state.imgUrl = UT.imgUrl
    },
    incrementName(state,UT){
      state.name = UT.name
    },
    incrementUType(state,UT){
      state.userTypeName = UT.userTypeName
    },
    incrementUTypeKey(state,UT){
      state.userTypeKey = UT.userTypeKey
    },
    incrementAurName(state,UT){
      state.userRealAuthName = UT.userRealAuthName
    },
    incrementAurKey(state,UT){
      state.userRealAuthKey = UT.userRealAuthKey
    },
    incrementInit(state, UT){
      console.log(UT, 123);
      let name = UT.name;

      if(UT.hasOwnProperty("flag")){

           let flag = UT["flag"] - 0;
           if(flag == 0){
               name = "home";
           }else if(flag == 1){
             name = "add";
           }
      }
      switch (name){
        case "home":
          state.show["home"] = true;
          break;
        case "add":
          state.show["add"] = true;
          break;
        case "pc":
          state.show["user"] = true;
          break;
      }
    }
  }
})

//输出store
export default  store;
