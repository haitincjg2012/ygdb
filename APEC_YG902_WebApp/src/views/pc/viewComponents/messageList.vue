<template>
  <div class="message-info-page">
    <scroller>
    <top-bar title="我的消息"></top-bar>
      <div class="main-page">
        <div @click.stop="messageReaded($event,item)" v-for="item in messageList" class="m-v-form-cli">
          <div :class="item.activeCls"><span>通知</span></div>
          <div class="m-v-content"><span v-html="item.content"></span></div>
          <div class="m-v-time"><span>{{item.sendTime}}</span></div>
        </div>
      </div>
      <div class="c-empty-show"  v-if="messageFlag">
          <img src="../../../assets/img/noMessage.png"/>
          <p class="c-z-message">暂无消息</p>
      </div>
    </scroller>
  </div>
</template>
<style>
@import "../../../assets/css/message.css";
</style>
<script>
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import {MessageBox,Indicator} from 'mint-ui';

  const api = new API();

  export default {

    data() {
      return {
        messageList: [],//消息列表
        activeCls:'m-v-tz',//通知样式
        vuegConfig:{
          forwardAnim:'touchPoint',
          backAnim: 'touchPoint',  //options所有配置可以写在这个对象里，会覆盖全局的配置
        },
        messageFlag:false,//是否有消息存在
      }
    },

    mounted(){

    },
    activated () {
      this.messageFlag = false;
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

            self.GetMesList( dt.useId);
          } else {
          }
        }).catch((error) => {
        })
      } catch (error) {
        console.log(error)
      }


    },

    methods: {
      GetMesList(userId){//获取信息
        const self = this;

//        const userId = self.$store.state.userId || c_js.getLocalValue('userId') || window.localStorage.getItem("userId");
//        if (!userId)
//          return;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        self.messageList = []
        let params = {
          api: "/yg-message-service/message/getMessageByReceiver.apec",
          data: {
            receiver: userId
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              item.data.rows.forEach((item) => {
                self.messageList.push({
                  "content": item.body.content,
                  //"sendTime":newDate.toDateString()
                  "sendTime": new Date().format(item.body.sendTime, 'yyyy-MM-dd'),
                  "activeCls":item.messageStatus==='NEW'?"m-v-tz active":'m-v-tz',
                  "id":item.body.id
                })
              });
              if(self.messageList.length == 0){
                  self.messageFlag = true;
              }
            } else {
            }
            Indicator.close();
          }).catch((error) => {
            Indicator.close();
          })
        } catch (error) {
          Indicator.close();
          console.log(error)
        }
      },
      messageReaded(e,_item){

      if(_item.activeCls != 'm-v-tz active')
      return;

        if(!_item.id)
            return;
        const self = this;
        let params = {
          api: "/yg-message-service/message/setMessageRead.apec",
          data: {
            bodyId: _item.id
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              var index = self.messageList.findIndex(function (e) {
                return e.id ==_item.id;
              });
              self.$set(self.messageList[index],'activeCls','m-v-tz');
            } else {
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      }
    },

    created() {
    },

    components: {
      topBar
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .message-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    .m-v-form-cli
      padding (10 /_rem) (15 /_rem) (10 /_rem) (15 /_rem)
      position relative
      border-bottom 1px solid #D7D7D7
      font-size 0
      img
        float right
        margin-top (10 /_rem)
      .m-v-tz
        width (60 /_rem)
        background-color #929292
        border-radius 4px
        text-align center
        display inline-block
        cursor pointer
        height (20 /_rem)
        line-height (20 /_rem)
        font-size 0
        color #fff
        span
          font-size (16 /_rem)
      .active
        background-color #00c850!important
      .m-v-content
        font-size (16 /_rem)
        margin (10 /_rem) 0 (10 /_rem) 0
        line-height (25 /_rem)
      .m-v-time
        color #929292
        height (20/_rem)
        span
          font-size (14 /_rem)

</style>
