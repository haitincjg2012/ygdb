/**
* Created by gsy on 2017/9/12.
* 新闻详情
*/
<template>
  <div class="newsDetail">
    <div class="myheader">
      <my-header :headTitle="title"></my-header>
    </div>
    <my-scroll class="newsDetailScroll" :data="postsdetail" :pullup="pullup" @scrollToEnd="loadMore" ref="wrapper">
      <div>
        <div class="newsDetailMain">
          <div class="newsDtop">
            <h1>{{detailData.title}}</h1>
            <!-- <p>{{detailData.author}}<span>{{detailData.createDate|ymdFilter}}</span></p> -->
            <div class="newsDtmid">
              <div class="newsDmInfo">
                <div class="newsDmIleft">
                  <!-- <img src="../../../assets/img/icon.png"/> -->
                  <img :src="detailData.imgUrl+'?x-oss-process=style/_head'" v-if="detailData.personPub && detailData.imgUrl"/>   <!-- 显示用户头像 -->
                  <img src="../../../assets/img/icon.png" v-if="detailData.personPub && !detailData.imgUrl"/>   <!-- 显示用户默认头像 -->
                  <img src="../../../assets/img/systemhead.png" v-if="!detailData.personPub"/>   <!-- 显示系统logo -->
                </div>
                <div class="newsDmImid">
                  <p>{{detailData.author}}</p>
                  <span>{{detailData.createDate|dateFilter}}</span>
                </div>
              </div>
              <div v-if="detailData.personPub">
                <div class="newsDmICare green" @click="changeAttention" v-if="!detailData.attentionUser">关注</div>
                <div class="newsDmICare" @click="changeAttention" v-if="detailData.attentionUser">已关注</div>
              </div>

            </div>
          </div>
          <div class="newsImg" v-if="detailData.url">
            <img :src="detailData.url+'?x-oss-process=style/_detail_hq'" />
          </div>
          <div class="newsDbtm">
            <!--<p>{{detailData.content}}</p>-->
            <p v-for="item in contentArray">{{item}}</p>
            <!-- <p>{{contentArray}}</p> -->
          </div>
        </div>
        <div class="newsDetailInfo">
          <div>
            <span class="read">阅读</span>
            <span>{{detailData.viewCount}}</span><i class="circleDot"></i>
            <span class="read">评论</span>
            <span>{{detailData.replyCount}}</span>
            <!--暂时不做点赞
            <span class="likeIcon" @click="changePraise" v-if="!praiseFlag"><img src="../../../assets/img/unlike.png"></span>
            <span class="likeIcon" @click="changePraise" v-if="praiseFlag"><img src="../../../assets/img/like.png"></span>
            <span>0</span>
          -->
          </div>
          <span class="doComm" @click="goComm('comment',detailData.id)">写评论</span>
        </div>
        <!-- 评论列表 -->
        <div class="comments" v-show="commFlag">
          <div class="commHead">
            <span class="lineGrey"></span>
            <span class="lineText">用户评价</span>
            <span class="lineGrey"></span>
          </div>
          <ul class="commList">
              <li v-if="hotPosts.flag" >
                 <div class="commentL">
                   <div class="commHimg">
                      <img src="../../../assets/img/icon.png?x-oss-process=style/_head" v-if="!hotPosts.imgUrl"/>
                      <img :src="hotPosts.imgUrl + '?x-oss-process=style/_head'" v-if="hotPosts.imgUrl"/>
                   </div>
                   <div class="rContent">
                     <h1>{{hotPosts.name}}</h1>
                     <p>{{hotPosts.content}}</p>
                     <span>{{hotPosts.createDateStr}}</span>
                     <my-posts :postsC="hotPosts.societyLzlReplyViewVOS" :postNumber="hotPosts.hostPlstsNumber  > 3?true:false" :commentId="hotPosts.id" :commentName='username'></my-posts>

                   </div>

                 </div>

                  <!-- 回复icon -->
                 <div class="commentR" @click="goComm('reply',hotPosts.id)">
                   <img src="../../../assets/img/comment.png"/>
                 </div>

              </li>
              <li v-for="item in societyPostReplyViewVOS" :key="item.id">
                 <div class="commentL">
                   <div class="commHimg">
                      <img src="../../../assets/img/icon.png?x-oss-process=style/_head" v-if="!item.imgUrl"/>
                      <img :src="item.imgUrl + '?x-oss-process=style/_head'" v-if="item.imgUrl"/>
                   </div>
                   <div class="rContent">
                     <h1>{{item.name}}</h1>
                     <p>{{item.content}}</p>
                     <span>{{item.createDateStr}}</span>
                     <my-posts :postsC="item.societyLzlReplyViewVOS" :postNumber="item.postsNumber > 3?true:false" :commentId="item.id" :commentName='username'></my-posts>

                   </div>
                 </div>
                 <!-- 回复 -->
                <div class="commentR" @click="goComm('reply',item.id)">
                  <!-- 点赞，暂不需要 隐藏 <img src="../../../assets/img/unlike.png"/> -->
                  <img src="../../../assets/img/comment.png"/>
                </div>
              </li>
          </ul>
          <my-scrolltip ref="loadmoreTip"></my-scrolltip>
        </div>
     </div>

    </my-scroll>

<!-- 因ipone不兼容，暂改成新页面方式 -->
<!--
    <form class="subComm" onsubmit="return false;" style="display:none;">
      <div class="subCommL">
        <textarea class="subtext" id="textarea"  v-model="commText" placeholder="说点什么吧..."></textarea>
        <div class="white"></div>
      </div>
        <a class="subBtn" id="subBtn" :class="{green:greenflag}" @click="submitComm">提交</a>
    </form>
-->
  </div>
</template>

<script>
    import postsC from '@/views/fruit/posts.vue'//发帖组件
    import {Indicator,Toast} from 'mint-ui'
    import header from '@/components/header/header'
//    import scroll from '@/components/scroll/scroll'//分页
    import scroll from '@/components/scroll/scrollbg'//分页

    import scrollTip from '@/components/downLoadAnimal' //滚动提示
    import commJs from '@/assets/js/common.js'
    import autoHeight from '@/assets/js/autoHeight'
    import WX from '../../../components/wx.vue'
    import API from '@/api/api'
    const api=new API();
    export default{
        data(){
            return {
              title:'市场行情',
              backflag:false, //是否执行返回路由
              username:"",//当前登录人
//              id:"",
              detailData:[],//详情对象
              contentArray:null,//新闻文章
              attentionData:[],//用户是否关注了该文章的作者，是否点赞了
              commData:[],//评论对象
              // scrollData:[], //滚动高度数据集合
              id:"",//文章id
              commid:"",//评论id(回复用)
              orgid:"",//组织ID
              isAttentionFlag:null,//关注标志
              saveflag:null, //是否关注的标志
              praiseFlag:null,//点赞标志
              // greenflag:false,//"提交"按钮是否启用
              commFlag:false,//是否有评论
              commText:"", //评论内容
              hotPostsInfo:{},//热门评论的对象的个人信息
              hotPosts:{},//最热门的帖子
              arrPosts:{},//所有评论的集合
              hotId:"",//最热帖子id
              postsdetail:[],//帖子的具体内容
              societyPostReplyViewVOS:null,//热门评论的楼中楼
              LzlReplyViewData:[],//hotcomment 回复 用scrolldata合并
              /*分页参数--上拉加载更多*/
              pullup:true,
              pulldown:true,
              loadMflag:true,
              pageNum:1,//页码
              pageSize:5

            }
        },
        activated(){
          var vm=this;
          this.$refs.wrapper.init();
          vm.id=vm.$route.query.id;
          window.postId = vm.id;
          vm.newsDetailist(vm.id);
          vm.username=vm.$store.state.name ||commJs.getLocalValue('name');
          console.log(window.location.href,"home-news");
          //textarea 内容增加高度自适应
          /*
          vm.getAutoHeight();
          vm.initInput();
          */
        },
        deactivated(){
          var vm=this;
          vm.initpage();
          vm.readArticle();//增加阅读数
          vm.commText="";

        },
        computed:{
        /*  scrollData(){
            // console.log("计算数据啦");
            var vm=this;

            // console.log("长度："+vm.commData.concat(vm.contentArray).length);
            // return vm.commData.concat(vm.contentArray);
            return vm.societyPostReplyViewVOS?vm.societyPostReplyViewVOS.concat(vm.contentArray):[].concat(vm.contentArray);
          }*/
        },
        methods: {
          //初始化页面数据
          initpage(){
            var vm=this;
            vm.pageNum=1;
            vm.societyPostReplyViewVOS=null;//评论
            vm.detailData=[];//详情对象
            vm.contentArray=null;//新闻文章
            vm.attentionData=[];//用户是否关注了该文章的作者，是否点赞了
            vm.commData=[];//评论对象
            vm.hotPostsInfo={};//热门评论的对象的个人信息
            vm.hotPosts={};//最热门的帖子
            vm.arrPosts={};//所有评论的集合
            vm.postsdetail=[];//帖子的具体内容
            vm.LzlReplyViewData=[];//hotcomment 回复 用scrolldata合并
            vm.commFlag=false;
            vm.isAttentionFlag=null;//关注标志
            vm.saveflag=null;//是否关注的标志
            vm.praiseFlag=null;//点赞标志
            vm.commid="";//评论id(回复用)
            vm.orgid="";//组织ID
            vm.hotId="",//最热帖子id
            /*分页参数--上拉加载更多*/
            vm.pullup=true;
            vm.pulldown=true;
            vm.loadMflag=true;

          },
          //获取行情详情
          newsDetailist(){
            var vm=this;
            let params={
              api:'/yg-society-service/societyPost/findSocietyPostById.apec',
              data:{
                id:vm.id //用户id
              }
            };
            vm.post(params,vm.newsDetailistCb);
          },
          newsDetailistCb(data){
            var vm=this;
            if(data.succeed){
              vm.detailData=data.data;
              var content=vm.detailData.content;
              // console.log(content.replace(/\s+/g,"").split("\n"));
              //不可行方式 vm.contentArray=commJs.trimStr(content.replace(" ","")).split("\n");
              var contentPage=content.split("\n");
              //for循环方法
              for(var i=0;i<contentPage.length;i++){
                contentPage[i]=commJs.trimStr(contentPage[i]);
              }
                vm.contentArray=contentPage;
              /* forEach方法:
              contentPage.forEach(function(item,index){
                    item=commJs.trimStr(item);
                    vm.contentArray.push(item);
                  });*/
              vm.orgid=data.data.userOrgId;
              vm.isAttentionFlag=data.data.attentionUser;
              vm.praiseFlag=data.data.likeUser;
              vm.postsdetail.push(vm.detailData);
              if(vm.detailData.replyCount > 0){
                 vm.commFlag=true;
                 vm.getHotPost(vm.id);
              }
//              console.log("新闻内容："+vm.contentArray);
              WX.wx(vm.detailData.title)
            }
          },

          //最大评论数
          getHotPost(id){
              var vm = this;
              var params = {
                  api:"yg-society-service/societyPostReply/findMaxReplyNum.apec",
                  data:{
                      id:id
                  }
              }

              vm.post(params, function (data) {
                if(data.succeed){
                  var dt = data.data;
                  if(!dt.id || dt.id == 0){
                    vm.hotPosts.flag = false;//判断是否存在热门的帖子
                     return;
                  }

                  vm.hotPosts = dt;
                  vm.hotId = dt.id;
                  vm.hotPosts.flag = true;//判断是否存在热门的帖子
                  dt.createDate = new Date().format(dt.createDate, "yyyy-MM-dd hh:mm");
                  vm.hotPosts.hostPlstsNumber = data.data.societyLzlReplyViewVOS.length;
                  vm.hotPosts.societyLzlReplyViewVOS =data.data.societyLzlReplyViewVOS.length > 0 ? data.data.societyLzlReplyViewVOS.slice(0, 3):null;

                  vm.arrPosts[data.data.id] = data.data;

                  vm.postsdetail = vm.postsdetail.concat(dt);
                  vm.postsdetail = vm.postsdetail.concat(vm.hotPosts.societyLzlReplyViewVOS);

                  //当帖子有的时候，调用该接口
                  if(vm.detailData.replyCount>1){
                    vm.getPostsContent(dt.id);
                  }
                  else{
                    vm.$refs.loadmoreTip.end("没有更多评论了");
                  }
                }
              });
          },
            //获取评论list
          getPostsContent(){
            //除开第一条数据的其他帖子的内容
            var vm = this;
            var params = {
              api:"yg-society-service/societyPostReply/findSocietyPostReplyPage.apec",
              data:{
                "societyPostId":vm.id,  //帖子id
                "pageNumber":vm.pageNum,
                "pageSize":vm.pageSize,
                "ids":vm.hotId,
              }
            }

            var failcb=function(vm){
                vm.$refs.loadmoreTip.end("网络异常...");
            }
            vm.post(params,vm.getPostsContentCb,true,failcb.bind(this));
          },
          getPostsContentCb(data){
            var vm=this;
            if(data.succeed){
                vm.pageCount = data.data.pageCount;

                var dt = data.data.rows;
//                if(vm.recordN == 1){
//                    var n = vm.recordN;
//                    console.log(vm, 8888);
//                    var index = vm.societyPostReplyViewVOS.length - n;
//                    vm.societyPostReplyViewVOS.splice(index,n);
//                    vm.recordN  = 0;
//                }
                if(data.data.totalElements == 0){//数据为0
                  vm.commFlag=false;
                  vm.loadMflag=false;
                }
                else if(data.data.totalElements && data.data.currentNo == data.data.pageCount) {//没有分页了
                  vm.commFlag=true;
                  vm.loadMflag=false;
                  vm.$refs.loadmoreTip.end("没有更多评论了");
                }
                else{//仍有分页
                  // vm.blankFlag=false;
                  vm.commFlag=true;
                  vm.loadMflag=true;
                  // vm.$refs.loadmoreTip.hide();
              }
                dt.forEach(function (current){
                  current.postsNumber = current.societyLzlReplyViewVOS.length;
                  current.societyLzlReplyViewVOS =current.societyLzlReplyViewVOS.length > 0 ? current.societyLzlReplyViewVOS.slice(0, 3):null;
                  vm.LzlReplyViewData=current.societyLzlReplyViewVOS;
                  current.createDate = new Date().format(current.createDate, "yyyy-MM-dd hh:mm");
                  vm.arrPosts[current.id] = current;
                });

                if(dt.length > 0){
                  vm.societyPostReplyViewVOS = vm.societyPostReplyViewVOS? vm.societyPostReplyViewVOS.concat(dt):[].concat(dt);
                }

              vm.postsdetail = vm.postsdetail.concat(vm.societyPostReplyViewVOS);
              vm.postsdetail = vm.postsdetail.concat(vm.LzlReplyViewData);

            }
            else{
                vm.$refs.loadmoreTip.end("网络异常...");
            }
          },

          //获取评论list
      /*    getCommlist(){
            var vm=this;
            let params={
              api:"/yg-society-service/societyPostReply/findSocietyPostReplyPage.apec",
              data:{
                //分页参数
                pageNumber:vm.pageNum,//页码
                pageSize:vm.pageSize,
                societyPostId:vm.id

              }
            };
            vm.post(params,vm.getCommlistCb,true);

          },

          getCommlistCb(data){
            var vm=this;
            if(data.succeed){
              var dt = data.data;
              if(data.data.totalElements == 0){//数据为0
                  // vm.blankFlag=true;
                  vm.commFlag=false;
                  vm.loadMflag=false;
                }
                else if(data.data.totalElements && data.data.currentNo == data.data.pageCount){//没有分页了
                    // vm.blankFlag=false;
                    vm.commFlag=true;
                    vm.loadMflag=false;
                    vm.$refs.loadmoreTip.end("没有更多评论了");
                }
                else{//仍有分页
                    // vm.blankFlag=false;
                    vm.commFlag=true;
                    vm.loadMflag=true;
                    // vm.$refs.loadmoreTip.hide();
                }
                vm.commData=vm.commData.concat(data.data.rows);

            }
            else{
              Toast(data.errorMsg);
            }
          },*/
          //去写评论
          goComm(type,id){
            var vm=this;
            vm.$store.state.commentBack = false;
//            console.log("id:"+id);
            vm.$router.push({name:'newsComm',query:{id:id,type:type}});
          },
          //init输入框
          initInput(){
            var vm=this;
            var textDom = document.getElementById("textarea");
            var fontDom=document.documentElement.style.fontSize.split("px")[0];
            textDom.style.height=Math.ceil(1.15*fontDom)+'px';

            // console.log("textDom:"+textDom.style.height+" fontDom:"+fontDom);
          },
          //textarea 内容增加高度自适应
          getAutoHeight(){
            var vm=this;
            var textDom = document.getElementById("textarea");
            autoHeight.autoTextarea(textDom,'80');// 调用
          },
          //上拉加载更多
         loadMore(){
           var vm=this;
           // vm.loadMopen=true;
           if(vm.loadMflag && vm.detailData.replyCount>1){
             vm.$nextTick(function(){
               vm.$refs.loadmoreTip.loading();
             });
             vm.pageNum++;
             // vm.getCommlist();
             vm.getPostsContent();
             // console.log("上拉加载更多："+this.pageNum);
           }

         },

          //提交评论
          submitComm(){
            var vm=this;
            if(!vm.commText){
              Toast("请输入评论内容!");
            }
            else{
              vm.pageNum=1,//页码
              // vm.commData=[];
              vm.commData=[];
              vm.getsbmComm();
            }
          },
          //提交评论api
          getsbmComm(){
            var vm=this;
            let params={
              api:"/yg-society-service/societyPostReply/addSocietyPostReply.apec",
              data:{
                content:vm.commText,
                societyPostId:vm.id
              }
            };
            vm.post(params,vm.getsbmCommCb);

          },
          getsbmCommCb(data){
            var vm=this;
            if(data.succeed){
              Toast("评论发表成功！");
              vm.commText="";
              setTimeout(function(){
                vm.getCommlist();
              },500);

            }
          },
          //评论框文本改变
          textChange(){
            var vm=this;
            //iphone
            /*
            var screenH=window.screen.availHeight;
            var contentDom=document.getElementById("newsDetailScroll");
            var textDomH = document.getElementById("textarea").style.height;
            contentDom.style.top
            */
            if(vm.commText){
              vm.greenflag=true;
            }
            else{
              vm.greenflag=false;
            }

          },
          //阅读数
          readArticle(){
            var vm=this;
            let params={
              api:"/yg-society-service/societyPost/addSocietyPostViewCount.apec",
              data:{
                id:vm.id
              }
            };
            vm.post(params,vm.readArticleCb);
          },
          readArticleCb(data){
             console.log(data);
          },
          //点赞/取消点赞
          changePraise(){
            var vm=this;
            var praiseflag=!vm.praiseFlag;
            let params={
              api:'/yg-society-service/societyPost/likeSocietyPostOrNot.apec',
              data:{
                id:vm.id,
                likeSocietyPost:praiseflag
              }
            };
            vm.post(params,vm.changePraiseCb);
          },
          changePraiseCb(data){
            var vm=this;
            if(data.succeed){
              //vm.getAttention();
              vm.praiseFlag=praiseFlag;

            }
          },
          //关注、取消关注
          changeAttention(){
            var vm=this;
            vm.saveflag=!vm.isAttentionFlag;
            let params={
              api:'/_node_user/_save_user_org.apno',
              data:{
                 orgId:vm.orgid,
                 saveFlag:vm.saveflag
              }
            };
            vm.post(params,vm.changeAttentionCb);
          },
          changeAttentionCb(data){
            var vm=this;
            if(data.succeed){
              //vm.getAttention();
              //将关注按钮置为相应的状态
              vm.isAttentionFlag= vm.saveflag;
              vm.detailData.attentionUser = vm.saveflag;
              if(vm.saveflag){
                Toast("您关注成功！");
              }
              else{
                Toast("您取消了关注！");
              }

            }
          },
          //用户是否关注了该文章的作者，是否点赞了
          /** getAttention(){
            var vm=this;
            let params={
              api:'/yg-cms-service/cms/isAttentionArticleUser.apec',
              data:{
                  id:vm.id //用户id
              }
            };
            vm.post(params,vm.getAttentoinCb,true);
          },
          getAttentionCb(data){
            var vm=this;
            if(data.succeed){
              vm.attentionData=data.data;
              vm.orgid=data.data.orgId;
              vm.isAttentionFlag=data.data.attentionArticleUser;
              vm.praiseFlag=data.data.praiseArticle;
            }
          },*/


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
        components: {
          'my-header':header,
          'my-scroll':scroll,
          'my-scrolltip':scrollTip,
          'my-posts':postsC,
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import '../../../assets/css/news.css';
</style>
