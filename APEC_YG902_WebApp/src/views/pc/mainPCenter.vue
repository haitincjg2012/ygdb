<template>
  <div class="pc-page">
    <scroller ref="my_scroller" :style="styleCal">
    <div ref="main_page" class="main-page">
      <div class="p-info">
        <div class="p-info-main">
          <img @click.stop="routerInfo('pcInfo')" class="p-info-user" :src="imgUrl">
          <div class="p-info-user-form">
            <div class="p-info-user-l">
              <span class="p-info-name">{{name}}</span>
              <img :src="userTypeName" class="p-info-aur">
            </div>
            <div class="p-info-user-k">
              <img :src="userLevelName">
              <span>{{userLevelKey}}会员</span>
            </div>
          </div>
        </div>
        <!--<img src="../../assets/img/card.png" class="p-info-logo">-->
        <a @click.stop.prevent="messagePush" class="p-info-user-message-click">
          <img src="../../assets/img/xx.png" class="p-info-user-message">
          <span v-show="messageCount">{{messageCount}}</span>
        </a>
      </div>
      <div class="p-line-top">
        <div @click.stop="routerInfo('myP')" class="line-one">
          <img src="../../assets/img/integral.png">
          <span>积分</span>
          <span>{{point}}</span>
        </div>
        <div @click.stop="routerInfo('pD')" class="line-two">
          <img src="../../assets/img/EarnPoints.png">
          <span>如何赚积分</span>
        </div>
        <div class="line-three">
          <img src="../../assets/img/Jurisdiction.png">
          <span>查看等级权限</span>
        </div>
      </div>
      <split></split>
      <div @click.stop="routerInfo('person')" class="p-form-cli">
        <img class="label" src="../../assets/img/Personal.png">
        <span>个人主页</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
      <div @click.stop="routerInfo('upList')" class="p-form-cli">
        <img class="label" src="../../assets/img/GoPro.png">
        <span>上传单据</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
      <div @click.stop="routerInfo('viewUpload')" class="p-form-cli">
        <img class="label" src="../../assets/img/Bill.png">
        <span>查看单据</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
      <div @click.stop="routerInfo('ranking')" class="p-form-cli">
        <img class="label" src="../../assets/img/rankG.png">
        <span>调果排行榜</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
       <div @click.stop="routerInfo('myAttention')" class="p-form-cli">
        <img class="label" src="../../assets/img/Bill.png">
        <span>我的关注</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <split></split>
      <div @click.stop="routerInfo('myCo')" class="p-form-cli">
        <img class="label" src="../../assets/img/Group0.png">
        <span>我的收藏</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
      <div @click.stop="routerInfo('mySP')" class="p-form-cli">
        <img class="label" src="../../assets/img/gq.png">
        <span>我的供求</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
      <div @click.stop="routerInfo('rnAur')" class="p-form-cli">
        <img class="label" src="../../assets/img/Visa_Card.png">
        <span>实名认证</span>
        <span class="aur-is-not">{{userRealAuthKey}}</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
      <!--<div  class="p-form-cli">-->
        <!--<img class="label" src="../../assets/img/Visa_Card.png">-->
        <!--<span>投诉反馈</span>-->
        <!--<span class="aur-is-not">{{userRealAuthKey}}</span>-->
        <!--<img class="arrow" src="../../assets/img/back.png">-->
      <!--</div>-->
      <!--<div class="dash-line"></div>-->

      <a href="tel:0535-3143298" class="p-form-cli">
        <img class="label" src="../../assets/img/kf.png">
        <span>客服电话</span>
        <img class="arrow" src="../../assets/img/back.png">
      </a>
      <div class="dash-line"></div>
      <div @click.stop="routerInfo('upSecret')" class="p-form-cli">
        <img class="label" src="../../assets/img/script.png">
        <span>修改密码</span>
        <img class="arrow" src="../../assets/img/back.png">
      </div>
      <div class="dash-line"></div>
      <div class="login-btn">
        <input class="btn-login-c login-confirm" type="submit" id="btn-login-code" value="退出"
               @click="confirmBtn">
        <!--</input>-->
      </div>
    </div>
    </scroller>
  </div>
</template>

<script>
  import split from '../../components/split/split';
  import API from '../../api/api';
  import c_js from '../../assets/js/common';
  import {MessageBox, Indicator, Toast} from 'mint-ui';



  import userImgUrl from '../../assets/img/icon.png'//用户默认图像
  import daiban from '../../assets/img/AgencyTips.png'//代办
  import zzHu from '../../assets/img/AgencyTipsG.png'//种植户
  import keshang from '../../assets/img/AgencyTipsK.png'//客商
  import lengku from '../../assets/img/AgencyTipsL.png'//冷库
  import hezuoshang from  '../../assets/img/hezuoshang.png'//合作商


  import QT from '../../assets/img/copper@3x.png'//铜牌
  import BY from '../../assets/img/silver@3x.png'//银牌
  import HJ from '../../assets/img/gold@3x.png'//金牌
  import BJ from '../../assets/img/Pt@3x.png'//铂金
  import ZS from '../../assets/img/Diamonds.png'//砖石
  import DS from '../../assets/img/Ancrown@3x.png'//大师

  import IMG from '../../components/gqimg.vue'
  import backgroudImg from '../../assets/img/bj.png'

  const api = new API();

  export default {
     mounted(){
//       this.$refs.main_page.style.height = window.innerHeight-50 +'px';
     },

    data() {
      return {
        imgUrl: this.$store.state.imgUrl || c_js.getLocalValue('imgUrl') || userImgUrl,//用户头像
//        name: this.$store.state.name || c_js.getLocalValue('name') || '',//用户名
        name: '',//用户名
        userTypeName: this.userTypeNameSwitch(this.$store.state.userTypeName || c_js.getLocalValue('userTypeName')),//用户身份
        userRealAuthKey: '',//用户是否验证
        userRealAuthName:'',
        point: "",//积分
        userLevelKey: '',//用户等级
        userLevelName: '',//
        messageCount:'',//未读消息条数,
        styleCal:"0"

      }
    },

    methods: {
      messagePush(){
        this.$router.push({name: 'messageList'});
      },
      _initScroll(){
        const self = this;
        self.$nextTick(function () {
//            console.dir();
            var el =this.$refs.main_page;
//          var winHeight = window.innerHeight;
          var mainHeight = el.offsetHeight + 50;
//          self.styleCal='top:42px;height:'+mainHeight+'px';
          this.$refs.main_page.style.height = mainHeight+'px';
//                    self.styleCal=mainHeight+'px';
        })
      },
      routerInfo(e){
          switch(e){
            case 'upList'://上传单据
              this._validateAur();
            break;
            case 'pcInfo'://个人信息
              this.$router.push({name: 'pcInfo'});
              break;
            case 'viewUpload'://查看单据
              this.$router.push({name: 'viewUploadList'});
              break;
            case 'ranking'://调果排行榜
              this.$router.push({name:'ranking'});
              break;
            case 'myAttention'://我的关注
              this.$router.push({name:'myAttention'});
              break;
            case 'upSecret'://修改密码
              this.$router.push({name: 'updateSecret'});
              break;
            case 'rnAur'://实名认证
              if(this.userRealAuthName=='AUDITING' || this.userRealAuthName=='NORMAL')
                  return;
              this.$router.push({name: 'realNameAur'});
              break;
            case 'myP'://个人积分详情
              this.$router.push({name: 'myPoint'});
              break;
            case 'mySP'://我的供求
              this.$router.push({name: 'mySpDe'});
              break;
            case 'pD'://如何赚钱积分
//              this.$router.push({name: 'pointDoc'});
              this.$router.push({ name: 'activePage_three'});
              break;
            case 'myCo'://我的收藏
              this.$router.push({name: 'myCollect'});
              break;
            case 'person'://个人主页
              this.$router.push({ name: 'personH'});
              break;
            default:
              this.$router.push({name: 'home'});
              break;
          }
      },
      userLevelKeySwitch(key){//用户积分转换
        switch (key) {
          case 'QT':
            return QT;
            break;
          case 'BY':
            return BY;
            break;
          case 'HJ':
            return HJ;
            break;
          case 'BJ':
            return BJ;
            break;
          case 'ZS':
            return ZS;
            break;
          case 'DS':
            return DS;
            break;
          default:
            return '';
            break;
        }
      },
      userTypeNameSwitch(key){//用户身份转换
        switch (key) {
          case 'DB':
            return daiban;
            break;
          case 'ZZH':
            return zzHu;
            break;
          case 'LK':
            return lengku;
            break;
          case 'KS':
            return keshang;
            break;
          case 'HZS':
            return hezuoshang;
            break;
          default:
            return '';
            break;
        }
      },
      GetUserLevel(){//获取用户等级、积分等资料
        const self = this;
        let params = {
          api: "/_node_user/_view.apno",
          data: {}
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed && item.data) {
              var data = JSON.parse(item.data);
              self.point = data.point;
              self.userLevelKey = data.userLevelKey;
//              self.userLevelName = self.userLevelKeySwitch(data.userLevelName);
              self.userLevelName = IMG.methods.userLevel(data.userLevelName);
              self.userRealAuthKey = data.userRealAuthKey;
              self.userRealAuthName = data.userRealAuthName;

              self.$store.commit("incrementPoint", {'point': data.point || 0});//积分
              c_js.setLocalValue('point',data.point || 0);

              self.$store.commit("incrementUserLevelName", {'userLevelName': data.userLevelName || ''});//等级积分 'QT'
              c_js.setLocalValue('userLevelName',data.userLevelName || '');

              self.$store.commit("incrementUserLevelKey", {'userLevelKey': data.userLevelKey || ''});//等级积分 '青铜'
              c_js.setLocalValue('userLevelKey',data.userLevelKey || '');

              self.$store.commit("incrementAurKey", {'userRealAuthKey': data.userRealAuthKey || ''});//验证情况
              c_js.setLocalValue('userRealAuthKey',data.userRealAuthKey || '');

            } else {
                self.point = this.$store.state.point || c_js.getLocalValue('point');
                self.userLevelKey = this.$store.state.userLevelKey || c_js.getLocalValue('userLevelKey');
                self.userLevelName = self.userLevelKeySwitch(this.$store.state.userLevelName || c_js.getLocalValue('userLevelName'));
                self.userRealAuthKey = this.$store.state.userRealAuthKey || c_js.getLocalValue('userRealAuthKey');
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      },
      GetMessageCount(){//获取用户等级、积分等资料
        const self = this;
        let params = {
          api: "/_node_user/_message.apno",
          data: {}
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed && item.data) {
              self.messageCount = item.data==='0'?'':item.data;
            } else {
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      },
      _validateAur(){
        const self = this;
        Indicator.open();
        let params = {
          api: "/_node_user/_check_pronum.apno"
        };
        try {
          api.post(params).then((res)=>{
            Indicator.close();
            var item = res.data;
            if (item.succeed){
              var checkStatus = item.data.checkStatus;
              var realAuth = item.data.realAuth;//是否实名验证
              var realInfo = item.data.realInfo;//是否填写资料
              if(!checkStatus && !realInfo){
                //self.$store.commit("incrementCheck",{'check':'1'});
                self.$store.state.check = "1";
                self.$router.push({name:'validate'});
              }
              else
                self.$router.push({name: 'uploadList'});
            } else {
            }
          }).catch((error)=> {

          }
        )
        } catch (error) {
          console.log(error)
        }
      },
      confirmBtn(){
        const self = this;
        sessionStorage.clear();

        let params = {
          api: "/yg-user-service/login/loginOut.apec",
          data: {}
        }

        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
                self.$router.push({name:'login'})
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      },
      identify(){

        const self = this;

        var params = {
          api:"/_node_user/_info.apno"
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            var dt;

            if (item.succeed) {

                dt = JSON.parse(item.data);

                this.name = dt.name;
                window.localStorage.useId = dt.userOrgId;
                this.userTypeName = IMG.methods.userTypeNameSwitch(dt.userTypeKey);
              this.imgUrl = dt.imgUrl || userImgUrl;//用户头像
              this.userRealAuthName = dt.userRealAuth;

            } else {
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      },
      init(data){
//          console.log(data);

      },
      post(params, fn){
        try {
          api.post(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
    },

    activated(){
      this.identify();
      this.GetUserLevel();
      this.GetMessageCount();
      this._initScroll();
//      var name =  window.sessionStorage.getItem("newName") || this.$store.state.name || c_js.getLocalValue('name') || '';
//      var level = window.sessionStorage.getItem("level") || this.$store.state.userTypeName || c_js.getLocalValue('userTypeName');
//      this.userTypeName=this.userTypeNameSwitch(this.$store.state.userTypeName || c_js.getLocalValue('userTypeName'));



    },

    created() {
    },

    beforeRouteEnter(to, form, next) {
      next(vm => {
      })
    },

    components: {
      split
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .pc-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    .p-info
      background url('../../assets/img/mybg.png') center 0 no-repeat;
      background-size cover;
      width 100%
      height (150 /_rem)
      position relative
      color #ffffff
      .p-info-main
        position absolute
        top 50%
        transform translateY(-50%)
        margin-left (25 /_rem)
        -moz-box-align: stretch;
        -webkit-box-align: stretch;
        -o-box-align: stretch;
        box-align: stretch;
        display flex
        .p-info-user
          border-radius 50%
          height (60 /_rem)
          width  (60 /_rem)
        .p-info-user-form
          line-height (30 /_rem)
          margin (10 /_rem) 0 (10 /_rem) (10 /_rem)
          .p-info-user-l
            line-height (30 /_rem)
            .p-info-name
              font-size (20 /_rem)
              vertical-align middle
            .p-info-aur
              width (20 /_rem)
              height (20 /_rem)
              vertical-align middle
          .p-info-user-k
            img
              width (16 /_rem)
              height (16 /_rem)
              vertical-align middle
            span
              font-size (10 /_rem)
              vertical-align middle

      .person-info
        position absolute
        top 50%
        transform translateY(-50%)
        font-size (14/_rem)
        right 5px
      .p-info-logo
        position absolute
        right 0
        bottom 0;
        width (100 /_rem)
        height (70 /_rem)
      .p-info-user-message-click
        position absolute
        right 20px
        top 10px;
        span
          position: absolute;
          display: inline-block;
          width (13 /_rem)
          height (13 /_rem)
          top: 0
          right: 0
          text-align: center;
          font-size: (10 /_rem);
          color: #fff;
          background-color: #ff4e4e;
          border-radius: 10px;
          white-space: nowrap;
      .p-info-user-message
        width (30 /_rem)
        height (30 /_rem)

    .p-line-top
      height (50 /_rem);
      display flex;
      .line-three
        opacity 0.4
      .line-one, .line-two, .line-three
        flex 1
        vertical-align middle
        line-height (40 /_rem)
        text-align center
        margin (5 /_rem) 0
        img
          width (20 /_rem)
          height (20 /_rem)
          vertical-align middle
        span
          font-size (12 /_rem)
      .line-one, .line-two
        border-right 1px solid #f4f4f4

    .p-form-cli
      height (45 /_rem)
      line-height (45 /_rem)
      margin-left (15 /_rem)
      position relative
      display block
      color: #323232
      img
        vertical-align: middle
      .label
        width (20 /_rem)
        height (20 /_rem)
      .arrow
        width (6 /_rem)
        height (11 /_rem)
        position absolute
        right (20 /_rem)
        top 50%
        transform translateY(-50%)
      span
        font-size (16 /_rem)
        margin-left (10 /_rem)
      .aur-is-not
        position absolute
        right (40 /_rem)
        top 50%
        transform translateY(-50%)
        color #59d7bc
    .dash-line
      margin 0 0 0 (50 /_rem)
      height 1px
      background-color #f4f4f4
    .login-btn
      margin (35 /_rem) (15 /_rem) (15 /_rem) (15 /_rem)
      .btn-login-c
        color #ffffff
        line-height (15 /_rem)
        height (40 /_rem)
        border-radius: 5px;
        display: inline-block;
        width: 100%;
        font-size (16/_rem)
      .login-confirm
        background-color: #28CBA7;
        color #FFFFFF;
</style>
