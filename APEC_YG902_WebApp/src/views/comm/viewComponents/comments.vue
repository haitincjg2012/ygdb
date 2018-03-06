
/**
* Created by gsy on 2017/11/10.
* 消息--评论
*/
<template lang="html">
  <div class="gsymesg praise comments">
    <my-header :headTitle="title" v-on:initPage="initpage" :backFlag="backflag"></my-header>
    <div v-show="!blankFlag">
       <my-scroll class="megscroll" :data="commentData" :pullup="pullup" @scrollToEnd="loadMore">
         <div>
           <ul class="praiselist">
              <li v-for="item in commentData">
                <div class="pralFirst">
                  <div class="praFl">
                    <img v-if="item.sender && item.senderImgUrl" :src="item.senderImgUrl+'?x-oss-process=style/_head'" alt="headimg">
                    <!-- 显示默认用户头像 -->
                    <img v-if="item.sender && !item.senderImgUrl" src="../../../assets/img/icon.png" alt="defaultimg">
                    <!-- 显示默认系统logo -->
                    <img v-if="!item.sender" src="../../../assets/img/systemhead.png" alt="sysimg">
                  </div>
                  <div class="praFr">
                    <div class="praFrl">
                      <span class="title">{{item.senderName}}</span>
                      <span class="time">{{item.createDate|dateFilter}}</span>
                    </div>
                    <!-- <div class="praFrr" @click="goReplay(item.relativeId,item.realm)">回复</div> -->
                    <div class="praFrr" @click="goReplay(item.relativeId,item.realm)">
                      <span>回复</span>
                    </div>


                  </div>
                </div>
                <div class="pralSed">{{item.message}}</div>
                <div class="pralThird" @click="goDetail(item.relativeMainId,item.receiverName,item.realm)">
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
        title:"评论",
        commentData:null,
        refershFlag:true,//通过返回icon离开页面再进入页面需要刷新，通过点击进入详情再进入页面不需要刷新
        blankFlag:null,
        //分页参数
        pageNum:1,//页码
        pageSize:10,
        /*上拉加载更多*/
         pullup:true,
         pulldown:true,
         loadMflag:true,
         backflag:true  //false 不能返回上一级  true 可以返回上一级
    }
  },
  activated(){
    var vm=this;
    if(vm.refershFlag){
     vm.getCommlist();
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
      vm.getCommlist();
    }
  },
    //回复 source:SOCIETYPOST 果友圈 /ARTICLE 行情
    goReplay(commid,source){
      var vm=this;
       vm.changereflag();//详情返回不刷新
      if(source=='SOCIETYPOST'){
        vm.$router.push({
          name:'newsfruitComment',
          query:{id:commid,flag:'postLZL'}
        });
      }
      else if(source=='ARTICLE'){
        // Toast("行情回复开发ing...");
        vm.$router.push({
          name:'newsComm',
          query:{id:commid,type:'reply'}
        });
      }else if(source=='QUOTATION'){
        // Toast("行情回复开发ing...");
        vm.$router.push({
          name:'newsComm',
          query:{id:commid,type:'reply'}
        });
      }
    },
    //去果友圈帖子详情页  source:SOCIETYPOST 果友圈 /ARTICLE 行情/ARTICLE 行情
    goDetail(id,name,source){
      var vm=this;
      vm.changereflag();//详情返回不刷新
      if(source=='SOCIETYPOST'){
        vm.$router.push({
          name:'socialDetail',
          query:{id:id,commentName:name}
        });
      }
      else if(source=='ARTICLE'){
        vm.$router.push({
          name:'newsDetail',
          query:{id:id}
        });
      }else if(source=='QUOTATION'){
        vm.$router.push({
          name:'priveMovement',
          query:{useId:id}
        });
      }


    },
    //初始化页面
    initpage(){
      var vm=this;
      if(vm.backflag){
        vm.pageNum=1;
        vm.commentData=null;
        vm.refershFlag=true;
      }

    },
    //改变refershFlag状态为false
    changereflag(){
      var vm=this;
      vm.refershFlag=false;
    },
    //赞列表list
    getCommlist(){
      var vm=this;
      vm.backflag=false;
      let params={
        api:'/yg-society-service/societyPostReplyMsg/societyPostReplyMsgPage.apec',
        data:{
          pageNumber:vm.pageNum, //页码
          pageSize:vm.pageSize,//页容量
          articleMsgType:"GIVE_THE_COMMENT"  //GIVE_THE_THUMBS_UP("点赞"),GIVE_THE_COMMENT("发表评论");

        }
      };
      var failcb=function(vm){
        vm.backflag=true;
        vm.$refs.loadmoreTip.end("网络异常...");
      }
      vm.post(params,vm.getCommlistCb,true,failcb.bind(this));
    },
   getCommlistCb(data){
     var vm=this;
     vm.backflag=true;
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
     vm.commentData=vm.commentData?vm.commentData.concat(data.data.rows):[].concat(data.data.rows);
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
