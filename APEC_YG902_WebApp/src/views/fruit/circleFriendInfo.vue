<!--此组件现在只有多种状态,需要扩展请注意-->
<template>
  <div class="c-circleFriend">
    <div class="c-cf-portrait" @click="personal($event)">
      <img :src="item.imgUrl ?item.imgUrl+'?x-oss-process=style/_head':dBg" data-flag="protrait" :data-src ="item.imgUrl">
    </div>
    <div class="c-cf-string" @click="personal($event)">
      <div class="c-cf-s-name"><span class="sp1">{{item.name}}</span></div>
      <div class="c-cf-s-time"><span class="sp2">{{item.createDate}}</span></div>
    </div>
    <div class="c-cf-state" @click="attention(item)" v-if="hasAttention">
      <span class="c-cf-s-text" :class="{cAttentionActive:item.attentionUser}">{{item.attentionUser?"已关注":"+&nbsp;关注"}}</span>
    </div>
    <div class="c-cf-picture" v-if="state">
       <div class="c-cf-p-comment" @click="comment">
           <img src="../../assets/img/comment.png" class="c-cf-p-com c-cf-p-t">
       </div>
       <div class="c-cf-p-praise" @click="praiseF" :data="dealWith">
         <img :src="praise" class="c-cf-p-com">
         <span class="c-cf-p-sp">{{item.likeCount}}</span>
       </div>
    </div>
    <div class="c-cf-pic-comment" v-if="stateComment" @click="comment">
      <img src="../../assets/img/comment.png" class="c-cf-p-com c-cf-p-t">
    </div>
  </div>
</template>
<style>
@import "../../assets/css/circleFriendInfo.css";
</style>
<script>

  import dbks from '../../assets/img/DBKS.png'
  import praiseD from '../../assets/img/praiseD.png'//点赞的默认图
  import praiseN from '../../assets/img/praiseN.png' //点赞的变化图
  import API from '../../api/api'
  const api = new API();
  export default{
      data(){
          return{
              dBg:dbks,
              praise:praiseD
          }
      },
      methods:{
        personal(event){
            //个人中心页面
          var el = event.toElement || event.srcElement;
          var flag = el.dataset.flag;

          if(flag == "protrait"){
              var src = el.dataset.src;
              if(src){
                this.$router.push({name:"pictureOriginal",query:{src:src}});
                return;
              }

          }
          var id = this.item.userId,
               orgId = this.item.userOrgId;
          this.$router.push({name:"personXq",query:{userId:id,orgId:orgId}});
        },
        attention(data){
          //关注查询
          var name = localStorage.name;
          if(!name){
              this.$router.push({name:"login"});
              return;
          }
          var flag = data.attentionUser;
          if(flag){
              return;
          }else{
            data.attentionUser = true;
          }
          var self = this;
          var userOrgId = self.item.userOrgId;
          var params = {
            api:"_node_user/_save_user_org.apno",
            data:{
              orgId:data.userOrgId,
              "saveFlag":data.attentionUser
            }
          }
          self.post(params, function (data) {
            if(data.succeed){
              self.$emit("changeState",userOrgId);
              data.attentionUser = data.attenFlag;
            }
          });
        },
        praiseF(){
            //取消点赞和赞成点赞
          var self = this;
          var name = localStorage.name;
          if(!name){
            this.$router.push({name:"login"})
          }
          var flag = self.item.likeUser;
          if(flag){
            this.praise = praiseD;
            self.item.likeUser = false;
          }else{
            this.praise = praiseN;
            self.item.likeUser = true;
          }
          var params = {
            api:"yg-society-service/societyPostReply/likeSocietyPostOrNot.apec",
            data:{
              "id":self.item.id,
              "likePostReply":self.item.likeUser
            }
          };

          self.post(params, function (data) {
            if(data.succeed){
              if(flag){
                self.item.likeCount --;
              }else{
                self.item.likeCount ++;
              }
            }
          });
        },
        comment(){
          //圈友详情的评论
          var name = localStorage.name;
          if(!name){
            this.$router.push({name:"login"});
            return;
          }
          var self = this;
          var id = self.item.id;
          var obj = {
            name:self.item.name,
            id:self.item.id,
          }
          this.$emit("getPostsId",obj);
          this.$router.push({name:"newsfruitComment",query:{id:id,flag:self.interface}});
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
    props:['item','state','attentionFlag','interface','stateComment'],
    computed:{
      dealWith(){
        if(this.item.likeUser){
          this.praise = praiseN;
        }else{
          this.praise = praiseD;
        }
      },
      hasAttention(){
          if(this.attentionFlag =="on_attention"){
              return true;
          }else{
              return false;
          }
      }
    }
  }
</script>
