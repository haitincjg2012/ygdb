<template>
   <div class="c-myTidings">
       <div class="c-myT-head">
           <div class="c-myT-h-ends-com" @click="back">
               <img src="../../assets/img/ret.png" class="c-myT-h-back-img"/>
           </div>
           <div class="c-myT-h-title">
                <h4>我的动态</h4>
           </div>
           <div class="c-myT-h-ends-com" @click="jumpPublsih">
              <img src="../../assets/img/dynamic.png" class="c-myT-h-publish-img" />
           </div>
       </div>
       <my-scroll :pullup="pullup" class="c-myT-scroll" :data="arrDynamic" @scrollToEnd="loadMore">
         <div class="c-myT-content">
           <div v-for="(item,index) in arrDynamic">
               <my-personHI :item = "item" class="c-myT-c-person"></my-personHI>
               <div class="c-myT-c-imageText" @click="jumpDetail(item)">
                 <div class="c-myT-c-image" v-if="item.url">
                   <img :src="item.url +'?x-oss-process=style/_list'"/>
                 </div>
                 <div class="c-myT-c-Text" >
                   <p class="c-myT-c-textstring">{{(item.content +"").length > 59?item.content.substring(0,59) +"...":item.content}}</p>
                 </div>
               </div>
               <div class="c-myT-c-data">
                 <div class="c-myT-c-d-read">
                   阅读&nbsp;<span>{{item.viewCount}}</span>
                 </div>
                 <div class="c-myT-c-d-praise">
                 <!--<img :src="item.likeUser?pN:pD" class="c-myT-c-d-praiseImg" /><span class="c-myT-c-d-praiseText">&nbsp;{{item.likeCount}}</span>-->
                 </div>
                 <!--<div class="c-myT-c-d-praise" @click="attention(item)" >-->
                   <!--<img :src="item.likeUser?pN:pD" class="c-myT-c-d-praiseImg" /><span class="c-myT-c-d-praiseText">&nbsp;{{item.likeCount}}</span>-->
                 <!--</div>-->
                 <div class="c-myT-c-d-more" @click="deleteItem(item, index)">
                   <img src="../../assets/img/round.png"/>
                 </div>
               </div>
               <div class="c-myT-c-step"></div>
           </div>
         </div>
       </my-scroll>
       <my-blank :isShow="(arrDynamic?(arrDynamic.length <= 0):true)"></my-blank>
   </div>
</template>
<style>
  @import "../../assets/css/myTidings.css";
</style>
<script>
  import blank from '../../components/blank.vue' //默认背景
  import scroll from '../../components/scroll/scroll.vue'//分页
  import circleFC from './circleFriendInfo.vue'//果圈中头像组件
  import praiseD from '../../assets/img/praiseD.png'//点赞的默认图
  import praiseN from '../../assets/img/praiseN.png' //点赞的变化图
  import userImgUrl from '../../assets/img/icon.png'//用户默认图像

  import {MessageBox} from 'mint-ui';
  import API from '../../api/api'
  const api = new API();
  export default{
      data(){
        return{
          arrDynamic:null,
          name:"",//我的姓名
          imgUrl:"",//我的图像
          pD:praiseD,//点赞默认图
          pN:praiseN,//点赞新图
          //下拉分页
          pullup:true,
          pageNumber:1,//当前页数
          pageCount:1000,//分页总数
        }
      },
      methods:{
        back(){
          this.$router.go(-1);
        },
        attention(data){
          var flag = data.likeUser;
          if(flag){

          }
        },
        deleteItem(item, index){
          var self = this;
            MessageBox.confirm("删除此次发布的行情?").then(action=>{
              var arr = [];
              var params = {
                api:"yg-society-service/societyPost/deleteSocietyPostById.apec",
                data:{
                  id:item.id
                }
              }
              self.post(params, function (data) {
                if(data.succeed){
                  self.arrDynamic.splice(index, 1);
                }else{

                }
              })
            },(cancel => {
              // console.log("您点了取消");
            }));

        },
        loadMore(){
            var self = this;

            if(self.pageNumber < self.pageCount){
                self.pageNumber ++;
                self.list();
            }
        },
        jumpPublsih(){
          //跳转到发布页面
          this.$router.push({name:"publishPost"})
        },
        jumpDetail(data){
            var postsId = data.id;
            var name = data.name;
          this.$router.push({name:"socialDetail",query:{id:postsId,commentName:name}});
        },
        list(){
            var self = this;
            var pageCurrent = arguments[0] || self.pageNumber;

           var params = {
               api:"yg-society-service/societyPost/mySocietyPostPage.apec",
               data:{
                 "pageNumber":pageCurrent,
                 "realm":"SOCIETYPOST",//列表帖子显示两条一级评论条数
               }
           }

           self.post(params, function (data) {
                if(data.succeed){
                    var dt = data.data;
                    self.pageCount = dt.pageCount;
                    var rows = dt.rows;
                    rows.forEach(function (current) {
                      current.createDate = new Date().format(current.createDate, "yyyy-MM-dd hh:mm")
                      current.name = self.name;
                      current.imgUrl = self.imgUrl;
                    });
                    if(rows.length > 0){
                      self.arrDynamic = self.arrDynamic?self.arrDynamic.concat(rows):[].concat(rows);
                    }

                }else{

                }
           });
        },
        getPersonInfo(){
          const self = this;
          var params = {
            api:"/_node_user/_info.apno"
          }

          this.post(params, function (data) {
            var item = data.data;
            var dt;
            if (data.succeed) {
              dt = JSON.parse(item);
              self.name = dt.name;
              self.imgUrl = (dt.imgUrl || userImgUrl);//用户头像

            } else {
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
          this.arrDynamic = null;
       this.getPersonInfo();
      this.list(1);
    },
    components:{
      'my-scroll': scroll,
      'my-personHI': circleFC,
      'my-blank':blank,
    }
  }
</script>
