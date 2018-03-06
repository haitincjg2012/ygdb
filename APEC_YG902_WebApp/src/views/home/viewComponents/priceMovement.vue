<template>
   <div class="c-price">
       <div class="c-price-head">
         <div class="c-p-head-title"><h3>果价猜猜猜</h3></div>
         <div class="return" @click="back">
           <img src="../../../assets/img/ret.png">
         </div>
       </div>
       <my-scroll class="c-price-content" :data="societyPostReplyViewVOS?societyPostReplyViewVOS:[]" :pullup="pullup" @scrollToEnd="loadMore" ref="wrapper">
          <div>
           <div class="c-p-c-blank"></div>
           <my-guess></my-guess>
           <div>
             <h4 class="c-p-content-title">全部评论</h4>
           </div>
           <div v-for="item in societyPostReplyViewVOS">
             <my-personHI :state="stateF" :stateComment="stateComment" :item="item" class="c-p-c-person"
                          @getPostsId="getPId" interface="postLZL"></my-personHI>
             <p class="c-p-c-contentText">{{item.content}}</p>
             <my-posts
               :postsC="item.societyLzlReplyViewVOS?(item.societyLzlReplyViewVOS.length > 0 ? item.societyLzlReplyViewVOS.slice(0, 3):null):null"
               :postNumber="item.societyLzlReplyViewVOS?item.societyLzlReplyViewVOS.length > 2:false"
               class="c-posts-c-msg" :commentId="item.id" :commentName="commentName"
               @postDetail="setDetailPos"></my-posts>
             <div class="c-g-c-c-line"></div>
           </div>
         </div>
       </my-scroll>
       <div class="c-price-footer" @click="comment">
          <img src="../../../assets/img/fruitMsg.png" class="c-p-footer-img">
          评论
       </div>
   </div>
</template>
<style>
@import "../../../assets/css/priceMovement.css";
</style>
<script>
  import guess from './guess.vue'//果价猜猜猜
  import circleFC from '../../fruit/circleFriendInfo.vue' //个人信息头部组件
  import postsC from '../../fruit/posts.vue'//发帖组件
  import scroll from '@/components/scroll/scrollbg'//分页
  import API from '../../../api/api'
  const api = new API();
export default{
    data(){
        return {
          societyPostReplyViewVOS:null,//
          arrPosts:[],
          stateF:false,
          stateComment:true,
          id:0,//用户的id
          commentName:"",//评论人的姓名
          pageNumber:1,//

          pullup:true,
        }
    },
    methods:{
        back(){
            this.$router.go(-1);
        },
        getPId(){},
        setDetailPos(){},
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
        getPostsContent(id){
        var self = this;
        var params = {
          api:"yg-society-service/societyPostReply/findSocietyPostReplyPage.apec",
          data:{
            "societyPostId":self.id,  //帖子id
            "pageNumber":self.pageNumber,
            "pageSize":"5",
            "ids":null,
          }
        }

        self.post(params, function (data) {
          if(data.succeed){
            self.pageCount = data.data.pageCount;

            var dt = data.data.rows;
            dt.forEach(function (current){
              current.postsNumber = current.societyLzlReplyViewVOS.length;
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
          }
        });
      },
        comment(){
            var name = localStorage.name;
            if(!name){
              this.$router.push({name:"login"});
              return;
            }
            var self = this;
            var id = this.id;
            self.commentName = name;
            var obj = {
              name:name,
              id:id,
            }

            this.$router.push({name:"newsfruitComment",query:{id:id,flag:'price'}});
        },
        loadMore(){
            if(this.pageNumber < this.pageCount){
              this.pageNumber ++;
              this.getPostsContent(this.id);
            }
        }
    },
    activated(){
        var self = this;
        self.arrPosts = [];
        this.$refs.wrapper.init();
        self.id = self.$route.query.useId;
        self.societyPostReplyViewVOS = null;
        self.pageNumber = 1;
        this.getPostsContent(self.id);
    },
    components:{
      'my-guess':guess,
      "my-personHI":circleFC,
      'my-posts':postsC,
      'my-scroll':scroll
    }
}
</script>
