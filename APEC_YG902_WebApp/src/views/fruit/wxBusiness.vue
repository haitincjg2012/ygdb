<template>
  <div>
    <div class="c-wx">
      <div class="c-wx-share c-wx-child"  @click="share" v-if="shareMark">
        <img src="../../assets/img/share.png"  class="c-wx-child-imgC">
        分享
      </div>
      <div class="c-wx-comment c-wx-child" @click="comment">
        <img src="../../assets/img/comment.png"  class="c-wx-child-imgC">
        评论
      </div>
      <div class="c-wx-parse c-wx-child" @click="praise">
        <img :data="praiseF" :src = "praiseP"  class="c-wx-child-imgC">
        点赞
      </div>
    </div>
  </div>
</template>
<style>
  @import "../../assets/css/wxBusiness.css";
</style>
<script>
  import praiseD from '../../assets/img/praiseD.png'//点赞的默认图
  import praiseN from '../../assets/img/praiseN.png' //点赞的变化图
  import commentC from '../../components/comment.vue' //评论组件

  import API from '../../api/api'
  import {Toast} from 'mint-ui';
  const api = new API();
  export default{
      data(){
        return {
          sh2:true,//遮罩里面的显示
          praiseP:praiseD,//点赞图片
          isShowF:false,//默认是不显示的
        }
      },
      methods:{
        share(){
          //分享
          var that = this;
          var obj = {
              flag:"socialDetail",
              data:true,
          }
          that.$emit('share', obj);
        },
        comment(){
          //评论
          var name = localStorage.name;
          if(!name){
            this.$router.push({name:"login"});
            return;
          }
          var id = this.pushInfo.id;
          var self = this;
          var obj = {
              name:self.pushInfo.name,
              id:self.pushInfo.id,
          }

          this.$emit("getPostsId",obj);
          this.$router.push({name:"newsfruitComment",query:{id:id,flag:self.interface}});
        },
        praise(){
          //点赞
          var name = localStorage.name;
          if(!name){
            this.$router.push({name:"login"})
            return
          }
          var self = this;
          if(this.pushInfo.likeUser){
              //取消点赞
            this.pushInfo.likeUser = false;
            this.praiseP = praiseD;
          }else{
            this.pushInfo.likeUser = true;
            this.praiseP = praiseN;
          }

          var params = {
            api:"yg-society-service/societyPost/likeSocietyPostOrNot.apec",
            data:{
              "id":self.pushInfo.id,
              "likeSocietyPost":self.pushInfo.likeUser
            }
          }

          this.post(params,function (data) {
              if(!data.succeed){
                Toast(data.errorMsg);
              }else{
                  console.log("点赞成功");
                  self.$emit("praiseCount");
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
      props:['pushInfo','interface','shareMark'],
      computed:{
        praiseF(){
          if(this.pushInfo.likeUser){
            this.praiseP = praiseN;
          }else{
            this.praiseP = praiseD;
          }
        }
      },
    components:{
    }

  }
</script>
