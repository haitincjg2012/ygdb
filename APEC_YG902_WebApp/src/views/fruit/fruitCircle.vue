<template>
  <div class="c-goods">
    <div class="c-g-head">
        <div class="c-g-h-first" @click="jumpDynamic">
          <!--<img src="../../assets/img/socialmsg.png">-->
          我的动态
        </div>
        <div class="c-g-h-second">
             <div class="c-g-h-second-com " :class="{cghactive:headtitleFlag}" @click="attentionHome">
                  <span :class="{cghtextactive:headtitleFlag}">关注</span>
             </div>
             <div class="c-g-h-second-com" :class="{cghactive:!headtitleFlag}" @click="hot">
                  <span :class="{cghtextactive:!headtitleFlag}">果圈</span>
             </div>
        </div>
        <div class="c-g-h-three" :class="{CGHThree:msgFlag}" @click="msg">
           <img src="../../assets/img/fruitCMsg.png" >
        </div>
    </div>
    <my-scroll class="c-goods-scroll" :data="arr" :pullup="pullup" @pulldown = "refresh" :pulldown="pulldown" @scrollToEnd="loadMore" ref="scrollBar">
    <!--<my-scroll class="c-goods-scroll" :pullup="pullup" :pulldown="pulldown" @scrollToEnd="loadMore">-->
      <div class="c-g-content">
        <my-downRef ref="downLoading"></my-downRef>
        <my-threeFind></my-threeFind>
        <div v-for="(item,index) in arr">
          <my-personHI :item="item" attentionFlag="on_attention" @changeState="attentionState"></my-personHI>
          <div  @click=" jumpPostsDetail(item)">
            <div class="c-g-c-picture">
              <p class="c-g-c-p-text" >{{item.content}}</p>
              <my-picLayout :imgArr = "item.societyPostImagesViewVOS.length > 0?item.societyPostImagesViewVOS:null" :lengthN="item.lengthN" class="c-g-c-p-layout" type ="?x-oss-process=style/_stylist"></my-picLayout>
            </div>
            <div class="c-g-c-clientCommentN">
              阅读&nbsp;{{item.viewCount}}<span class="c-g-c-c-sp-com"></span>
              评论&nbsp;{{item.replyCount}}<span class="c-g-c-c-sp-com"></span>
              点赞&nbsp;{{item.likeCount}}
              <div class="c-g-c-c-line"></div>
            </div>
            <div class="c-g-c-msg" @click="postCommentMsg(item)" v-if="item.societyPostReplyViewVOS.length > 0">
              <my-posts :postsC="item.societyPostReplyViewVOS.length > 0?item.societyPostReplyViewVOS.slice(0, 3):null" class="c-g-c-posts" :class="{AddTempC:(item.societyPostReplyViewVOS.length < 3)}"></my-posts>
              <div class="c-g-c-m-more" v-if="item.societyPostReplyViewVOS.length > 2">展开更多评论></div>
              <div class="c-g-c-c-line" v-if="item.societyPostReplyNumberF"></div>
            </div>
          </div>
          <my-share class="c-g-c-operation" :pushInfo="item.posts" v-on:praiseCount="getPraiseCount(item)" interface="postHome" @getPostsId="getPId" @share="getShareState" :title="item.content" ></my-share>
        </div>
        <my-upload ref="floading"></my-upload>
        <!--<my-blank :isShow="(arr?(arr.length <= 0):true)"></my-blank>blankFlag-->
        <my-blank :isShow="blankFlag"></my-blank>
      </div>
    </my-scroll>
    <div class="c-g-publish"  @click="publishPosts">
       <img src="../../assets/img/publishComm.png" class="c-g-c-publish-img" />
    </div>
    <my-wx :showHideF="showHideF" @shareH="getShareState"></my-wx>
  </div>
</template>
<style>
@import "../../assets/css/fruitCircle.css";
</style>
<script>
  import scroll from '../../components/scroll/scrollbg.vue'
//  import scroll from '../../components/scroll/scroll.vue'//分页
//  import scroll from '../../components/scrollN.vue'
  import blank from '../../components/blank.vue' //默认背景
  import threeFind from '../businessV/threeFind.vue' //三个找的组件
  import imgLayout from '../../components/pictureLayout.vue' //图片布局
  import posts from './posts.vue'//发帖组件
  import shareC from './wxBusiness.vue' //分享，点赞，评论组件
  import test from '../../assets/img/test.png'  //测试用图
//  import uploadC from '../../components/loadingAnimation.vue' //上拉加载动画
  import uploadC from '../../components/loading.vue' //上拉加载动画
  import downRefreshC from '../../components/refresh.vue'   //刷新的动画组件
  import circleFC from './circleFriendInfo.vue' //个人信息头部组件
  import maskC from '../../components/mask.vue'
  import wxC from '../../components/share.vue' //微信分享页面
  import WX from '../../components/wx.vue'  //微信分享功能

  import {Toast} from 'mint-ui'

  import API from '../../api/api'
  const api = new API();
  var fn = {
      postList:function (data) {
        //果圈分页帖子的内容
        if(data.succeed){
           var self = this;
           var dt = data.data;
           this.pageCount = dt.pageCount;
//           this.pageNumber = dt.currentNo;
          var rows = dt.rows;
          var el = this.$refs.floading;
          var elRef = this.$refs.downLoading;
          if(this.refreshFlag){
            setTimeout(function () {
              elRef.end();
              if(rows.length > 0){
                el.start();
              }
            },1000);
            this.refreshFlag = false;
          }else{
              if(rows.length > 0){
                if(this.pageCount > this.pageNumber){
                  el.start();
                }else{
                  el.end();
                }
              }else{
                el.externalAction();
              }
          }
           rows.forEach(function (current) {
             current.content = current.content.length > 59?current.content.slice(0, 59) + "...":current.content;
             current.createDate = new Date().format(current.createDate, "yyyy-MM-dd hh:mm");
             current.lengthN = current.societyPostImagesViewVOS.length;
             current.societyPostReplyNumberF = current.societyPostReplyViewVOS.length > 0 ? true: false;
             current.posts = {
                 count:current.likeCount,
                 id:current.id,
                 title:current.title,
                 likeUser:current.likeUser,
                 name:current.name,
             };
             self.objfruit[current.id] = current;
             var userId = current.userOrgId;

             if(!self.objfruitAttention.hasOwnProperty(userId)){
               self.objfruitAttention[userId] = [];
             }
             self.objfruitAttention[userId].push(current);
           });

           if(rows.length > 0){
             this.arrfruit =this.arrfruit?this.arrfruit.concat(rows):[].concat(rows);
           }


           this.arr = this.arrfruit;
           if(!this.arr){
               this.blankFlag = true;
           }else{
               if(this.blankFlag){
                 this.blankFlag = false;
               }
           }

        }else{
          Toast(data.errorMsg);
        }
      },
      postAttentionList:function (data) {
        //关注分页帖子的内容
        if(data.succeed){
          var self = this;
          var dt = data.data;
          var rows = dt.rows;
          this.pageCountA = dt.pageCount;
          var el = this.$refs.floading;
          var elRef = this.$refs.downLoading;
          if(this.refreshFlag){
            setTimeout(function () {
              elRef.end();
              if(rows.length > 0){
                el.start();
              }
            },500);
            this.refreshFlag = false;
          }else{
            if(rows.length > 0){
              if(this.pageCountA > this.pageNumberA){
                el.start();
              }else{
                el.end();
              }
            }else{
              el.externalAction();
            }
          }


          rows.forEach(function (currentS) {
            currentS.content = currentS.content.length > 59?currentS.content.slice(0, 59) + "...":currentS.content;
            currentS.createDate = new Date().format(currentS.createDate, "yyyy-MM-dd hh:mm");
            currentS.lengthN = currentS.societyPostImagesViewVOS.length;
            currentS.societyPostReplyNumberF = currentS.societyPostReplyViewVOS.length > 0 ? true: false;
            currentS.posts = {
              count:currentS.likeCount,
              id:currentS.id,
              title:currentS.title,
              praise:currentS.likeUser,
              name:currentS.name,
            };
            self.objAttention[currentS.id] = currentS;
          });

          if(rows.length > 0){
            this.attentionF =this.attentionF?this.attentionF.concat(rows):[].concat(rows);
          }

          self.arr = self.attentionF;
          if(!this.arr){
            this.blankFlag = true;
          }else{
            if(this.blankFlag){
              this.blankFlag = false;
            }
          }
        }
    }
  }
export default{
    data(){
        return {
           sh2:true,
           name:"",//果圈中帖子的名字
           headtitleFlag:false,//false表示果圈，true表示关注
           objfruitAttention:{},//关注的所有数据，key为组织id,value值为组织下所有帖子id
           objfruit:{},//记录分页的每一条数据，key值为帖子id,value值为贴子对象
           fPostId:'',//果圈和关注中帖子的id
           arr:null,//下拉刷新的数据
           arrfruit:null,//果圈的数据

           attentionF:null,//关注行情的数据
           objAttention:{},//记录关注中分页的每一条数据，key值为帖子id,value值为贴子对象
          msgFlag:false,//消息标志

          showHideF:false,//分享页面的显示和隐藏标志位
          pushInfo:null,//分享，评论，点赞功能推送的信息
          //下拉分页
           pullup:true,
          pulldown:true,//下拉刷新
          imgUrl:null,
          pageNumber:1,//果圈的当前页数
          pageCount:1000,//果圈的分页总数
          pageNumberA:1,//关注果圈的当前页数
          pageCountA:1000,//关注果圈的分页总数

          postDetail:"postDetail",//调用点赞的标志位
          refreshFlag:false,//刷新的标志位
          blankFlag:false,//默认情况下是没有图片
        }
    },
    methods:{
      init(){
        this.headtitleFlag = false;//果圈初始化
        this.arrfruit = null;
        this.attentionF = null;
        this.pageNumber = 1,//果圈的当前页数
        this.pageCount = 1000,//果圈的分页总数
        this.pageNumberA = 1,//关注果圈的当前页数
        this.pageCountA = 1000;//关注果圈的分页总数
        this.arr = null,//下拉刷新的数据
        this.objAttention = {};//记录关注中分页的每一条数据，key值为帖子id,value值为贴子对象
        this.objfruitAttention = {};//关注的所有数据，key为组织id,value值为组织下所有帖子id
        this.objfruit = {};//记录分页的每一条数据，key值为帖子id,value值为贴子对象
        },
      attentionHome(){
          //首页关注
         this.headtitleFlag = true;
         if(this.pageNumberA == 1){
           this.attentionF = null;
           this.attentionPostList(1);
         }else{
           this.arr = this.attentionF;
           if(!this.arr){
             this.blankFlag = true;
           }else{
             if(this.blankFlag){
               this.blankFlag = false;
             }
           }
         }
      },
      hot(){
        //热门
        this.headtitleFlag = false;
        if(this.pageNumber == 1){
            this.arrfruit = [];
          this.list(1);
        }else{
          this.arr = this.arrfruit;
          if(!this.arr){
            this.blankFlag = true;
          }else{
            if(this.blankFlag){
              this.blankFlag = false;
            }
          }
        }
      },
      msg(){
          //消息
        var name = localStorage.name;
        if(!name){
          return;
        };
        this.$router.push({ name:'messageInfo'})
      },
      attention(data){
        //关注功能
        data.attentionUser = true;

      },
      getPId(obj){
          //获取帖子的id,为插入该帖子做准备
        this.fPostId = obj.id;
        this.setReadNumber(obj);
        this.setRelayNumber(obj);
        this.$store.state.fruitRouter.comment = true;
      },
      attentionState(id){
          var self = this;
         var rows = self.objfruitAttention[id];
         rows.forEach(function (current) {
           current.attentionUser = true;
         });
      },
      postCommentMsg(data){
          //分页查询帖子的评论消息
         var postsId = data.id;
      },
      getPraiseCount(data){
          //获取点赞的次数
        var flag = data.likeUser;
        if(flag){
            data.likeUser = false;
            data.likeCount --;
        }else{
          data.likeUser = true;
          data.likeCount ++;
        }
      },
      jumpPostsDetail(data){
          //查询帖子的具体内容
        var postsId = data.id;
        this.fPostId = postsId;
        var postsMainId = data.societyPostReplyViewVOS.id;// //评论主键id
        var useOrgId = data.userOrgId;
        this.$store.state.fruitRouter.detail = true;
        this.setReadNumber(data);
        this.$router.push({name:"socialDetail",query:{id:postsId,pmId:postsMainId,userOrgId:useOrgId,commentName:this.name}});
      },
      jumpDynamic(){
          //跳转到我的动态
        var name = localStorage.name;
        if(!name){
          this.$router.push({name:"login"});
          return;
        };
        this.$router.push({name:"myTidings"});
      },
      loadMore(){
          var self = this;
          if(this.headtitleFlag){
              //关注的刷新
            if(self.pageNumberA < self.pageCountA){
              self.pageNumberA ++;
              self.attentionPostList();
            }
          }else{
             //果圈分页
            if(self.pageNumber < self.pageCount){
              self.pageNumber ++;
              self.list();
            }
          }

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
      message:function (data) {
          //消息模块
        var self = this;
        let params = {
          api: "/_node_user/_message.apno",
        }
        this.post(params, function (data) {
          if(!data.succeed){
            return;
          }
          self.msgFlag = data.data.messageCount > 0 || data.data.praiseCount > 0  || data.data.replyCount > 0 ?true:false;
        });

      },
      list(param){
       //分页查询贴子的内容
        var self = this;
        var argument = param || self.pageNumber;
        let params = {
            api:"yg-society-service/societyPost/societyPostPage.apec",
            data:{
              "pageNumber":argument,
              "pageSize":10,
              "replyNum":3,//列表帖子显示两条一级评论条数
              "attentionUser":false,
              "realm":"SOCIETYPOST",
            }
        }
        self.post(params, fn.postList.bind(self));
      },
      attentionPostList(param){
          var self = this;
        var argument = param || self.pageNumberA;
        let params = {
          api:"yg-society-service/societyPost/societyPostPage.apec",
          data:{
            "pageNumber":argument,
            "pageSize":10,
            "replyNum":2,//列表帖子显示两条一级评论条数
            "attentionUser":true,
            "realm":"SOCIETYPOST",
          }
        }

        self.post(params, fn.postAttentionList.bind(self));
      },
      publishPosts(){
        var name = localStorage.name;
        if(!name){
          this.$router.push({name:"login"});
          return;
        };
          this.$router.push({name:"publishPost"})
      },
      createPosts(content){
          var self = this;
          var obj = {
            "name": self.name,
            "content": content,
          };

          return obj;
      },
      getUserName(){
        var self = this;
        var name = localStorage.name;

        if(name){
          self.name = name;
          return;
        }
      },
      getShareState(data){
          if(data.flag == 1){
              this.showHideF = true;
          }else{
            this.showHideF = false;
          }

          this.setReadNumber(data);
      },
      setReadNumber(dt){
          //增加阅读数
        var self = this;
        var params = {
           api:"yg-society-service/societyPost/addSocietyPostViewCount.apec" ,
           data:{
             "id":dt.id
           }
        }

        this.post(params, function (data) {
          if(data.succeed){

              self.objfruit[dt.id].viewCount ++;
          }
        })
      },
      setRelayNumber(dt){
          //增加评论的数量
        this.objfruit[dt.id].replyCount ++;
      },
      refresh(){
        var self = this;
        self.message();
        self.$refs.downLoading.init();
        this.$refs.floading.externalAction();//外部影响
        if(this.headtitleFlag){
          //关注的刷新，数据为空或者不为空
          self.attentionF = null;
          self.refreshFlag = true;
          self.attentionPostList(1);
        }else{
          //果圈的的刷新
          self.init();
          self.refreshFlag = true;
          self.list(1);
        }

      },
      postsMore(){
          //超过两条帖子的信息
      }
    },
    activated(){
        WX.wx("果来乐-果圈");
        var self = this;
        var state = self.$store.state;
        self.message();

        if(!state.fruitRouter.comment && !state.fruitRouter.detail){
            //不做评论或者不跳转详情
          self.init();
          self.list(1);
          self.getUserName();
        }else{
          //具有联动功能
          // 第一类：做评论
          //第一种：直接在果圈和关注做评论
          //第二种：详情做评论
          // 第二类：不做评论
          //第一种：在果圈和关注不做评论
          //第二种：在详情做评论
            if(state.fruitRouter.comment){
                if(state.fruitRouter.commentFlag.succeed){
                  if(self.headtitleFlag){
                    //关注里面的帖子评论
                    var item = self.objAttention[self.fPostId];
                    var temp = item.lengthN ;
                    temp ++;
                    item.societyPostReplyNumberF =  temp > 0 ? true: false;
                    item.societyPostReplyViewVOS.splice(1,0,self.createPosts(self.$store.state.fComment));
                  }else{
                    //果圈里面的帖子评论
                    console.log(this.objfruit,self.fPostId, 88888);
                    var item = this.objfruit[self.fPostId];
                    var temp = item.lengthN ;
                    temp ++;
                    item.societyPostReplyNumberF =  temp > 0 ? true: false;
                    item.societyPostReplyViewVOS.splice(1,0,self.createPosts(self.$store.state.fComment));
                  }
                }
            }

          state.fruitRouter.commentFlag.succeed = false;//不管评论是否成功都置为false
          state.fruitRouter.comment = false;//不管评论是否成功都置为false,为了果圈的入口
          state.fruitRouter.detail = false;//不管是否进入详情都置为false,为了果圈的入口
        }
    },
    mounted(){
      this.$refs.scrollBar.init();
      },
  components:{
    'my-scroll':scroll,
    'my-blank':blank,
    'my-picLayout':imgLayout,
    'my-posts':posts,
    'my-share':shareC,
    'my-downRef':downRefreshC,
    'my-upload':uploadC,
    'my-personHI':circleFC,
    'my-mask':maskC,
    'my-wx':wxC,
    'my-threeFind':threeFind,
  }
}
</script>
