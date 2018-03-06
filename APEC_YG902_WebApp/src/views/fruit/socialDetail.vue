<template>
  <div class="c-postsDetail">
    <div class="c-p-head">
       <div class="c-p-h-back" @click="back">
          <img src="../../assets/img/ret.png"/>
       </div>
       <div class="c-p-h-title">
         <h4>圈友动态</h4>
       </div>
       <div class="c-p-h-more">
       </div>
    </div>
    <!--<my-scroll class="c-posts-content" :pullup="pullup" :data="postsdetail" @scrollToEnd="loadMore" ref="wrapper">-->
    <div class="c-posts-content">
      <div class="c-postsC">
        <my-personHI :item="postsPInfo" attentionFlag="on_attention"></my-personHI>
        <div class="c-p-c-article"><p class="c-p-c-style">{{postsPInfo.content}}</p></div>
        <my-picture :imgArr = "postsPInfo.societyPostImagesViewVOS" :lengthN="lengthN" class="c-p-c-picture" type="?x-oss-process=style/_stydetail" @receivedImg="loadedImg"></my-picture>
        <div class="c-p-c-clientCommentN">
          阅读&nbsp;{{postsPInfo.viewCount}}<span class="c-p-c-c-sp-com"></span>
          评论&nbsp;{{postsPInfo.replyCount}}<span class="c-p-c-c-sp-com"></span>
          点赞&nbsp;{{postsPInfo.likeCount}}
          <div class="c-g-c-c-line"></div>
        </div>
        <div v-if="hotPosts.flag">
          <my-personHI :state="stateF" :item="hotPosts" class="c-p-c-person" @getPostsId="getPId" interface="postLZL" ></my-personHI>
          <p class="c-p-c-contentText">{{hotPosts.content}}</p>
          <my-posts class="c-posts-c-msg" :postsC="hotPosts.societyLzlReplyViewVOS?(hotPosts.societyLzlReplyViewVOS.length > 0 ? hotPosts.societyLzlReplyViewVOS.slice(0, 3):null):null" :postNumber ="hotPosts.societyLzlReplyViewVOS?hotPosts.societyLzlReplyViewVOS.length > 2:false"  :commentId="hotPosts.id" :commentName="commentName" @postDetail="setDetailPos"></my-posts>
          <div class="c-g-c-c-line"></div>
        </div>
        <div v-for="item in societyPostReplyViewVOS">
          <my-personHI :state="stateF" :item="item" class="c-p-c-person" @getPostsId="getPId" interface="postLZL"></my-personHI>
          <p class="c-p-c-contentText">{{item.content}}</p>
          <!--<my-posts :postsC="item.societyLzlReplyViewVOS" :postNumber ="item.postsNumber > 3" class="c-posts-c-msg" :commentId="item.id" :commentName="commentName"></my-posts>-->
          <my-posts :postsC="item.societyLzlReplyViewVOS?(item.societyLzlReplyViewVOS.length > 0 ? item.societyLzlReplyViewVOS.slice(0, 3):null):null" :postNumber ="item.societyLzlReplyViewVOS?item.societyLzlReplyViewVOS.length > 2:false" class="c-posts-c-msg" :commentId="item.id" :commentName="commentName" @postDetail="setDetailPos"></my-posts>
          <div class="c-g-c-c-line"></div>
        </div>
      </div>
      <div class="c-posts-blank"></div>
    </div>
    <!--</my-scroll>-->
    <my-share class="c-pD-operation" :pushInfo="postsPInfo" v-on:praiseCount="getPraiseCount(postsPInfo)" interface="postDetail" @getPostsId="getPostId" @share="getShareState" :title="postsPInfo.content" shareMark = "1"></my-share>
    <my-shareWx :wxflag="wxflag" @getWx="weChatGuidance"></my-shareWx>
    <my-wx :showHideF="showHideF" @shareH="getShareState"></my-wx>
  </div>
</template>
<style>
  @import "../../assets/css/postsDetail.css";
</style>
<script>
//  import scroll from '../../components/scroll/scroll.vue'//第三方分页组件
//  import scroll from '../../components/scroll/scrollN.vue'//自定义分页组件
  import scroll from '../../components/scroll/scrollbg.vue'//第三方分页组件
  import circleFC from './circleFriendInfo.vue' //个人信息头部组件
  import imgLayout from '../../components/pictureLayout.vue' //图片布局
  import postsC from './posts.vue'//发帖组件
  import WX from '../../components/wx.vue'  //微信分享功能
  import wxC from '../../components/share.vue' //微信分享
  import shareWx from '../../components/shareWx.vue'  //微信分享引导
  import shareC from './wxBusiness.vue'
  import API from '../../api/api'
  const api = new API();

  export default{
      data(){
          return{
            recordN:0,//0代表没有添加评论，1.代表添加评论
            postId:'',//帖子的id
            commId:'',//获取楼中楼的评论的id
            toUserName:'',//评论回复人姓名
            commentName:'',//评论人姓名
            imgUrl:"",//评论人的头像

            hotId:null,//热门评论的id
            showHideF:false,//分享页面的显示和隐藏标志位
            //下拉分页
            pullup:true,
            pageNumber:1,//果圈的当前页数
            pageCount:1000,//果圈的分页总数
            el:null,//因为是全局的滚动条，所以要判断是否该页面的滚动
            dataFlag:false,//加载更多数据的标志位

            tempY:0,//记录滚动的位置
            y:0,//滚动条悬浮的位置

            postsdetail:null,//帖子的具体内容
            postsPInfo:{},//帖子的个人信息
            lengthN:1,//九宫格组件的变化
            hotPosts:{},//最热门的帖子

            stateF:true,//评论，点赞的状态
            societyPostReplyViewVOS:null,//帖子里面的内容
            arrPosts:{},//所有评论的集合

            imgFlag:false,//图片的标志位

            wxflag:false,//微信引导图
          }
      },

      methods:{
        weChatGuidance(){
          this.wxflag = false;
        },
        back(){
             if(localStorage.wx){
               this.$router.push({name:"home"});
                 this.init();
                 localStorage.removeItem("wx");
                return;
             }
              if(this.postsPInfo.societyPostImagesViewVOS){
                  if(this.imgFlag){
                    this.$router.go(-1);
                    this.init();
                    this.imgFlag = false;
                  }
              }else{
                this.$router.go(-1);
                this.init();
              }
        },
        loadedImg(flag){
          //判断图片是否加载完成
          if(flag){
              //true说明图片加载完成
             this.imgFlag = true;
          }else{
            this.imgFlag = false;
          }
        },
        init(){
           this.pageNumber =1,//果圈的当前页数
            this.pageCount =1000,//果圈的分页总数
           this.postsdetail = null;//帖子的具体内容
           this.hotPosts ={},//最热门的帖子
           this.postsPInfo ={},//帖子的个人信息
           this.societyPostReplyViewVOS =null,//帖子里面的内容
           this.arrPosts ={};//所有评论的集合
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
        getPostsDetail(id){
            //获取贴子的具体内容
          var self = this;
            var params = {
                api:"yg-society-service/societyPost/findSocietyPostById.apec",
                data:{
                  "id":id
                }
            }

            this.post(params,function (data) {
               if(data.succeed){
                   var dt = data.data;

                   var title = dt.content.length > 20 ? dt.content.substring(0, 20)+"...":dt.content;
                   WX.wx("果来乐-" +title,undefined,self.closeGuidence);//微信分享功能
                   dt.createDate = new Date().format(dt.createDate, "yyyy-MM-dd hh:mm");
                   self.postsPInfo =  dt;
                   self.postsPInfo.societyPostImagesViewVOS = self.postsPInfo.societyPostImagesViewVOS.length > 0?self.postsPInfo.societyPostImagesViewVOS:null;
                   self.postsdetail = dt.societyPostImagesViewVOS;
                   self.postId = dt.id;//帖子id
                   if(dt.replyCount > 0){
                     self.getHotPost(self.postId);
                   }
               }
            })
        },
        closeGuidence(){
            var that = this;
          that.wxflag = false;
        },
        getHotPost(id){
            var self = this;
            var params = {
                api:"yg-society-service/societyPostReply/findMaxReplyNum.apec",
                data:{
                    id:id
                }
            }

            self.post(params, function (data) {
              if(data.succeed){
                var dt = data.data;
                if(!dt.id || dt.id == 0){
                  self.hotPosts.flag = false;//判断是否存在热门的帖子
                   return;
                }
                self.hotPosts = dt;
                self.hotId = dt.id;
                self.imgUrl = dt.imgUrl;
                self.hotPosts.flag = true;//判断是否存在热门的帖子
                dt.createDate = new Date().format(dt.createDate, "yyyy-MM-dd hh:mm");
                self.hotPosts.hostPlstsNumber = data.data.societyLzlReplyViewVOS.length;
//                self.hotPosts.societyLzlReplyViewVOS =data.data.societyLzlReplyViewVOS.length > 0 ? data.data.societyLzlReplyViewVOS.slice(0, 3):null;
                self.hotPosts.societyLzlReplyViewVOS =data.data.societyLzlReplyViewVOS;

                self.arrPosts[data.data.id] = data.data;

                //当帖子至少有2的时候，调用该接口
                if(self.postsPInfo.replyCount){
                  self.getPostsContent(self.postId);
                }
              }
            });
        },
        getPostsContent(id){
          //除开第一条数据的其他帖子的内容
          var self = this;
          var params = {
            api:"yg-society-service/societyPostReply/findSocietyPostReplyPage.apec",
            data:{
              "societyPostId":id,  //帖子id
              "pageNumber":self.pageNumber,
              "pageSize":"5",
              "ids":self.hotId,
            }
          }

          self.post(params, function (data) {
            if(data.succeed){
                self.pageCount = data.data.pageCount;

                var dt = data.data.rows;
//                if(self.recordN == 1){
//                    var n = self.recordN;
//                    console.log(self, 8888);
//                    var index = self.societyPostReplyViewVOS.length - n;
//                    self.societyPostReplyViewVOS.splice(index,n);
//                    self.recordN  = 0;
//                }
                dt.forEach(function (current){
                  current.postsNumber = current.societyLzlReplyViewVOS.length;
//                  current.societyLzlReplyViewVOS =current.societyLzlReplyViewVOS.length > 0 ? current.societyLzlReplyViewVOS.slice(0, 3):null;
                  current.societyLzlReplyViewVOS =current.societyLzlReplyViewVOS;
                  current.createDate = new Date().format(current.createDate, "yyyy-MM-dd hh:mm");
                  self.arrPosts[current.id] = current;
                });

                if(dt.length > 0){
                  self.societyPostReplyViewVOS = self.societyPostReplyViewVOS? self.societyPostReplyViewVOS.concat(dt):[].concat(dt);
                }
                setTimeout(function () {
                  self.dataFlag = true;
                },200)
//              self.postsdetail =self.postsdetail?self.postsdetail.concat(self.societyPostReplyViewVOS):[].concat(self.societyPostReplyViewVOS);
            }
          });
        },
        getPraiseCount(data){
          //获取点赞的次数
          var flag = data.likeUser;
          if(flag){
            data.likeCount ++;
          }else{
            data.likeCount --;
          }

        },
        getPId(data){
          //获取评论后的帖子
          this.y = this.tempY;
          this.$store.state.fruitDetail.LZL = true;
          var self = this;
          self.commId=data.id;
          self.toUserName = data.name;//评论回复人姓名
        },
        getPostId(data){
          //帖子评论
          var self = this;
          this.y = this.tempY;
          self.recordN ++;
          self.postId = data.id;
          this.setRelayNumber();
          this.$store.state.fruitDetail.LZL = true;
        },
        setRelayNumber(){
            //增加评论的数量
          this.postsPInfo.replyCount ++;
        },
        createPostComment(content){

          var self = this;
          var obj = {
            name:self.commentName,
            toUserName:self.toUserName,
            content:content,
          };
          return obj;
        },
        getShareState(data){
          if(data.flag == "socialDetail"){
            this.wxflag = data.data;
          }
        },
        createPost(content){
          var self = this;
          var date =new Date();
          var time = date.getFullYear() +"-" + (date.getMonth()*1 + 1) + "-" + date.getDate() + " " + date.getHours()+":"+date.getMinutes();

          var obj = {
            name:self.commentName,
            createDate:time,
            likeUser:false,//默认是没有点赞的
            imgUrl:self.imgUrl,
            content:content,
            id:self.$store.state.newPosts.id,
            likeCount:0,
            societyLzlReplyViewVOS:null,
          };
//         var tarr = [];
//          tarr.push(obj);

//          self.arrPosts[self.$store.state.newPosts.id] = {societyLzlReplyViewVOS :tarr};//新增加的帖子评论添加到容器中
          self.arrPosts[self.$store.state.newPosts.id] = obj;//新增加的帖子评论添加到容器中

          return obj;
        },
        loadMore(){
          var self = this;
          if(self.pageNumber < self.pageCount){
            self.pageNumber ++;
            self.getPostsContent(self.postId);
          }

        },
        nextFrame:function() {
          return window.requestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.oRequestAnimationFrame ||
            window.msRequestAnimationFrame ||
            function(callback) { return setTimeout(callback, 1); };
        },
        scroll(evt){
           var self = this;
          if(self.el){
              var e = evt.srcElement || evt.toElement;//兼容性
              var height = document.querySelector(".page").clientHeight;
              var scrollHeight = e.body.scrollHeight;
              var scrollTop = e.body.scrollTop;
              this.tempY = scrollTop;
              if(scrollHeight - scrollTop -height == 0){
                  if(self.dataFlag){
                    self.loadMore();
                    self.dataFlag = false;
                  }
              }
          }
        },
        setDetailPos(){
          this.y = this.tempY;
           this.$store.state.fruitDetail.commentDetail = true;
        }
      },
      activated(){
        var self = this;
        self.el = document.querySelector(".c-postsDetail");
        var postId = this.$route.query.id;//帖子的id
        var pmId = this.$route.query.pmId;//评论主键id
        var useOrgId = this.$route.query.userOrgId;//组织id
        window.postId = postId;
        self.commentName = this.$route.query.commentName;
        self.postId = postId;

//详情里面的评论
// 第一类：楼中楼的评论
// 第二类：详情的评论
// 楼中楼的评论 :
//              评论成功
//              评论不成功
// 详情的评论 :
//              评论成功
//              评论不成功

//评论里面的更多的返回
        var xq = this.$store.state.fruitDetail;

        if(!xq.comment && !xq.LZL && !xq.commentDetail){
           self.init();
           self.getPostsDetail(postId);
        }else{
            if(xq.LzlFlag.succeed){
                //是否是已有评论，还是临时添加的评论
                //有楼中楼和没有楼中楼
                var item = self.arrPosts[this.commId].societyLzlReplyViewVOS;
                if(!item){
                  self.arrPosts[this.commId].societyLzlReplyViewVOS = [];
                }
              item = self.arrPosts[this.commId].societyLzlReplyViewVOS;
              item.push(self.createPostComment(self.$store.state.fDLzlContent));

            }else if(xq.commentFlag.succeed){
               var item =self.createPost(self.$store.state.fComment) ;
               self.societyPostReplyViewVOS = self.societyPostReplyViewVOS?self.societyPostReplyViewVOS:[];
               self.societyPostReplyViewVOS.unshift(item);
            }

          //更多评论的返回、楼中楼的返回、帖子的评论返回
          window.scrollTo(0, this.y);
          xq.comment = false;
          xq.LZL = false;
          xq.commentFlag.succeed = false;
          xq.LzlFlag.succeed = false;
          xq.commentDetail = false;
        }

      },
      mounted(){
          var that = this;
          window.addEventListener("scroll",this.scroll,false);
         document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
             //微信又上角的按钮
           console.log(33333333);
        });
      },
      components:{
//        "my-scroll":scroll,
        "my-share":shareC,
        "my-personHI":circleFC,
        "my-picture":imgLayout,
        "my-posts":postsC,
        'my-wx':wxC,
        "my-shareWx":shareWx
      },
    computed:{
          test(){
              console.log("zy");
              return 13;
          }
      }
  }
</script>
