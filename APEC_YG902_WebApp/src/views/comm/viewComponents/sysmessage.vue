/**
* Created by gsy on 2017/11/10.
* 消息--系统消息
*/
<template lang="html">
  <div class="gsymesg sysmessage">
    <my-header :headTitle="title" v-on:initPage="initpage"></my-header>
    <div v-show="!blankFlag">
       <my-scroll class="megscroll" :data="sysmegData" :pullup="pullup" @scrollToEnd="loadMore">
         <div>
           <ul class="notifylist">
             <li v-for="item in sysmegData">
               <img class="bellIcon" src="../../../assets/img/notifyIcon.png" alt="notifyIcon">
               <div class="notifyR">
                 <h2 class="noRtitle">通知</h2>
                 <span class="noRtime">{{item.body.sendTime|dateFilter}}</span>
                 <span class="content" v-html="item.body.content"></span>
               </div>
             </li>
            </ul>
           <my-scrolltip ref="loadmoreTip" ></my-scrolltip>
         </div>
       </my-scroll>
    </div>
    <my-blankimg :isShow="blankFlag"></my-blankimg>
  </div>
</template>

<script>
import {Toast} from 'mint-ui'
import header from '@/components/header/header'
import scroll from '@/components/scroll/scroll'
import scrollTip from '@/components/downLoadAnimal' //滚动提示
import blankimg from '@/components/blank' //默认图
import API from '@/api/api'
const api=new API();
export default {
  data(){
    return {
      title:"系统消息",
       sysmegData:null,
       blankFlag:null,
        //分页参数
       pageNum:1,//页码
       pageSize:10,
        /*上拉加载更多*/
       pullup:true,
       pulldown:true,
       loadMflag:true
    }
  },
  activated(){
    var vm=this;
    vm.getsyslist();

  },
  methods:{
    //上拉加载更多
 loadMore(){
   var vm=this;
   if(vm.loadMflag){
     vm.$nextTick(function(){
       vm.$refs.loadmoreTip.loading();
     });
     vm.pageNum++;
     vm.getsyslist();
   }
 },
    //初始化页面
    initpage(){
      var vm=this;
       vm.pageNum=1;
       vm.sysmegData=null;
    },
    //获取列表list
    getsyslist(){
      var vm=this;
      let params={
        api:'/yg-message-service/message/getMessageByReceiver.apec',
        data:{
          pageNumber:vm.pageNum, //页码
          pageSize:vm.pageSize//页容量
        }
      };
      var failcb=function(vm){
        vm.$refs.loadmoreTip.end("网络异常...");
      }
      vm.post(params,vm.getsyslistCb,true,failcb.bind(this));
    },
    getsyslistCb(data){
      var vm=this;
      if(data.succeed){
         if(data.data.totalElements == 0){
           vm.blankFlag=true;
           vm.loadMflag=false;
         }
         else if(data.data.totalElements && data.data.currentNo == data.data.pageCount){//没有分页了
             vm.blankFlag=false;
             vm.loadMflag=false;
             vm.$refs.loadmoreTip.end("没有更多数据了");
             console.log("没数据啦~~");
         }
         else{//仍有分页
             vm.blankFlag=false;
             vm.loadMflag=true;
         }
       vm.sysmegData=vm.sysmegData?vm.sysmegData.concat(data.data.rows):[].concat(data.data.rows);
     }
     else{
       Toast(data.errorMsg);
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
    'my-scroll':scroll,
    'my-scrolltip':scrollTip,
    'my-blankimg':blankimg
  }
}
</script>

<style lang="css">
@import '../../../assets/css/messageInfo.css'
</style>
