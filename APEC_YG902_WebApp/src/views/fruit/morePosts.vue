<template>
<div class="c-postsMore">
   <div class="c-pM-head">
     <div class="c-pM-h-back" @click="back">
        <img src="../../assets/img/ret.png" class="c-pM-h-b-img">
     </div>
     <div class="c-pM-h-title"><h4>评价详情</h4></div>
   </div>
    <my-scroll class="c-pM-scroll" :data="commentInfo.societyLzlReplyViewVOS" :pullup="pullup" @scrollToEnd="loadMore">
      <div class="c-pM-content">
        <my-personHI :item="commentInfo" class="c-pM-c-portrait"></my-personHI>
        <p class="c-pM-c-t-text">{{commentInfo.content}}</p>
        <div class="c-pM-c-line"></div>
        <my-post :postsC = "commentInfo.societyLzlReplyViewVOS" class="c-pM-c-post"></my-post>
      </div>
    </my-scroll>
    <div class="c-pM-submit">
      <div class="c-pM-s-input">
        <input type="text" placeholder="写下你的评论..." v-model="postsComment" class="c-pM-s-input-text"/>
      </div>
      <div class="c-pM-s-btn" @click="replyComment">
        <div class="c-pM-s-text">发送</div>
      </div>
    </div>
</div>
</template>
<style>
@import "../../assets/css/postMore.css";
</style>
<script>
  import scroll from '../../components/scroll/scroll.vue'//分页
  import circleFC from './circleFriendInfo.vue'//果圈中头像组件
  import postC from './posts.vue' //帖子的组件

  import API from '../../api/api'
  const api = new API();
  export default{
    data(){
      return {
        arr: [],//评论的详情
        commentInfo:{
          societyLzlReplyViewVOS:[]
        },
        postsComment:'',//帖子评论的内容
        toUserName:'',//回复对象姓名
        commentName:'',//评论人姓名
        //下拉分页
        pullup: true,
        pageNumber:1,//关注果圈的当前页数
        pageCount:1000,//关注果圈的分页总数
      }
    },
    methods: {
      back(){
          this.$router.go(-1);
      },
      list(){
          //查询评论的数据
      },
      loadMore(){
      },
      createComment(comment){
          var self = this;
         var obj = {
             name:self.commentName,
             toUserName:self.toUserName,
             content:comment,
         }

         return obj;
      },
      getCommentDetail(id){
        //获取评论的详情
        var self = this;
        var param = {
            api:"yg-society-service/societyPostReply/queryReplyById.apec",
            data:{
              "id":id
            }
        }

        this.post(param, function (data) {
          if(data.succeed){
              self.toUserName = data.data.name;
              self.commentInfo = data.data;
              self.commentInfo.societyLzlReplyViewVOS = data.data.societyLzlReplyViewVOS;
              self.commentInfo.createDate = new Date().format(data.data.createDate, "yyyy-MM-dd hh:mm")
          }else{
              alert(data.errorMsg);
          }
        });
      },
      replyComment(){
          var self = this;
          var name = localStorage.name;

          if(!name){
            this.$router.push({name:"login"});
            return;
          };
          if(self.postsComment == ""){
             return;
          }

          var params = {
              api:"yg-society-service/societyLzlReply/addSocietyLzlReply.apec",
              data:{
                "toReplyId":self.commentInfo.id,
                "content":self.postsComment
              }
          }
          this.post(params, function (data) {
            if(data.succeed){
              self.commentInfo.societyLzlReplyViewVOS.push(self.createComment(self.postsComment));
              self.postsComment = "";
            }else{

            }
          });
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
        var commendId = this.$route.query.commentId;
        this.commentName = this.$route.query.commentName;
        this.getCommentDetail(commendId);
    },
    components: {
      'my-scroll': scroll,
      'my-personHI': circleFC,
      'my-post': postC
    }
  }
</script>
