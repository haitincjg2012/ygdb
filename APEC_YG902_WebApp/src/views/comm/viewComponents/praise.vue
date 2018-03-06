/**
* Created by gsy on 2017/11/10.
* 消息--赞
*/
<template lang="html">
  <div class="gsymesg praise">
    <my-header :headTitle="title" v-on:initPage="initpage"></my-header>
    <div v-show="!blankFlag">
      <my-scroll class="megscroll" :data="praiseData" :pullup="pullup" @scrollToEnd="loadMore">
        <div>
          <ul class="praiselist">
            <li v-for="item in praiseData">
              <div class="pralFirst">
                <div class="praFl">
                  <img v-if="item.sender && item.senderImgUrl" :src="item.senderImgUrl+'?x-oss-process=style/_head'" alt="headimg">
                  <!-- 显示默认用户头像 -->
                  <img v-if="item.sender && !item.senderImgUrl" src="../../../assets/img/icon.png" alt="defaultimg">
                   <!-- 显示默认系统logo -->
                  <img v-if="!item.sender" src="../../../assets/img/systemhead.png" alt="sysimg">
                </div>
                <div class="praFr">
                  <span class="title">{{item.senderName}}</span>
                  <span class="time">{{item.createDate|dateFilter}}</span>
                </div>
              </div>
              <!-- <div class="pralSed">赞了这条动态</div> -->
              <div class="pralSed">{{item.message}}</div>
              <div class="pralThird" @click="goDetail(item.relativeMainId,item.receiverName)">
                <span class="pralTimg" v-if="item.url"><img :src="item.url+'?x-oss-process=style/_list_hq'"/></span>
                <span class="content">{{item.societyPostContent}}</span>
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
        title:"赞",
        praiseData:null,
        refershFlag:true,//通过返回icon离开页面再进入页面需要刷新，通过点击进入详情再进入页面不需要刷新
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
    if(vm.refershFlag){
      vm.getPraiselist();
    }


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
       vm.getPraiselist();
     }
   },
    //去果友圈帖子详情页
    goDetail(id,name){
      var vm=this;
      vm.changereflag();//详情返回不刷新
      vm.$router.push({
        name:'socialDetail',
        query:{id:id,commentName:name}
      });

    },
    //初始化页面
    initpage(){
      var vm=this;
      vm.pageNum=1;
      vm.praiseData=null;
      vm.refershFlag=true;
    },
    //改变refershFlag状态为false
   changereflag(){
     var vm=this;
     vm.refershFlag=false;
   },
    //赞列表list
    getPraiselist(){
      var vm=this;
      let params={
        api:'/yg-society-service/societyPostReplyMsg/societyPostReplyMsgPage.apec',
        data:{
          pageNumber:vm.pageNum, //页码
          pageSize:vm.pageSize,//页容量
          articleMsgType:"GIVE_THE_THUMBS_UP"  //GIVE_THE_THUMBS_UP("点赞"),GIVE_THE_COMMENT("发表评论");

        }
      };
      var failcb=function(vm){
        vm.$refs.loadmoreTip.end("网络异常...");
      }
      vm.post(params,vm.getPraiselistCb,true,failcb.bind(this));
    },
   getPraiselistCb(data){
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
         }
         else{//仍有分页
             vm.blankFlag=false;
             vm.loadMflag=true;
         }
       vm.praiseData=vm.praiseData?vm.praiseData.concat(data.data.rows):[].concat(data.data.rows);
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
