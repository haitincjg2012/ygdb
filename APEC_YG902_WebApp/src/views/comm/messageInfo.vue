/**
* Created by gsy on 2017/11/10.
* 消息模块-主页面
*/
<template lang="html">
  <div class="gsymesg messageInfo">
    <my-header :headTitle="title" v-on:initpage="initpage"></my-header>
    <ul class="mesglist">
      <li @click="goDetail('praise')">
        <div class="listCotent">
          <div class="listL">
            <img src="../../assets/img/praiseIcon.png"/>
          </div>
          <div class="listR">
              <span class="listText">赞</span>
              <span class="listarrow"><img src="../../assets/img/back.png"/></span>
          </div>
        </div>
        <span class="unread" v-if="(unreads.praiseCount && unreads.praiseCount!='0')">{{unreads.praiseCount}}</span>
      </li>
      <li @click="goDetail('comments')">
        <div class="listCotent">
          <div class="listL">
            <img src="../../assets/img/comentIcon.png"/>
          </div>
          <div class="listR">
              <span class="listText">评论</span>
              <span class="listarrow"><img src="../../assets/img/back.png"/></span>
          </div>
        </div>
        <span class="unread" v-if="(unreads.replyCount && unreads.replyCount!='0')">{{unreads.replyCount}}</span>
      </li>
      <li @click="goDetail('sysmesg')">
        <div class="listCotent">
          <div class="listL">
            <img src="../../assets/img/sysmegIcon.png"/>
          </div>
          <div class="listR">
              <span class="listText">系统消息</span>
              <span class="listarrow"><img src="../../assets/img/back.png"/></span>
          </div>
        </div>
        <span class="unread" v-if="(unreads.messageCount && unreads.messageCount!='0')">{{unreads.messageCount}}</span>
      </li>
    </ul>
  </div>
</template>

<script>
import {Toast} from 'mint-ui'
import header from '@/components/header/header'
import scroll from '@/components/scroll/scroll'
import scrollTip from '@/components/downLoadAnimal' //滚动提示
import API from '@/api/api'
const api=new API();
export default {
  data(){
    return {
      title:"消息",
      unreads:[]
    }
  },
  activated(){
    var vm=this;
    vm.initpage();
  },
  deactivated(){
    var vm=this;

  },
  methods:{
    //初始化页面
    initpage(){
      var vm=this;
      vm.getUnreads();
    },
    //进入详情时调用 清空消息数
    clearUnreads(clearType){
      var vm=this;
      let params={
        api:'/_node_user/_clear_msgcount.apno',
        data:{
          articleMsgType:clearType //GIVE_THE_THUMBS_UP  点赞; GIVE_THE_COMMENT 评论; SYSRTEM 系统消息;
        }
      };
      vm.post(params,vm.clearUnreadCb);
    },
    clearUnreadCb(){
    },
    //获取未读消息数
    getUnreads(){
      var vm=this;
      let params={
        api:"/_node_user/_message.apno",
        data:{}
      };
      vm.post(params,vm.getUnreadsCb);
    },
    getUnreadsCb(data){
      var vm=this;
      vm.unreads=data.data;
    },
    //进入详情页
    goDetail(type){
      var vm=this;
      var name="";//路由名
      var clearType="";//清空参数
      var detailInfo="";
      detailInfo=vm.switchRouter(type);
      name=detailInfo.split('|')[0];
      clearType=detailInfo.split('|')[1];
      console.log("name:"+name+" clearType:"+clearType);
      vm.$router.push({
        name:name
      });
      vm.clearUnreads(clearType);
    },
    switchRouter(type){
      switch (type) {
          case 'praise':
          return 'praise|GIVE_THE_THUMBS_UP';
          break;
          case 'comments':
          return 'comments|GIVE_THE_COMMENT';
          break;
          case 'sysmesg':
          return 'sysmessage|SYSRTEM';
          break;
      }
    },


    //封装post请求
    post(params, fn, flag, failcb){
     var vm=this;
     try {
       api.post(params).then((res) => {
         var data = res.data;
         fn(data);
       }).catch((error) => {
         console.log(error);
         Toast(error);
         if(flag){
           failcb();
         }
       })
     } catch (error) {
       console.log(error);
       Toast(error);
       if(flag){
         failcb();
       }
  }
}
  },
  components:{
    'my-header':header,
    'my-scroll':scroll
  }
}
</script>

<style lang="css">
  @import '../../assets/css/messageInfo.css'
</style>
