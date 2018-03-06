<template>
  <div class="c-quotes-message">
    <div class="c-q-m-head">
      <div class="c-q-m-h-back" @click="back">
        <img src="../../../assets/img/ret.png">
      </div>
      <div class="c-q-m-h-title">
        <h4>行情消息</h4>
      </div>
      <div class="c-q-m-h-delete" @click="msgDelete">
        <img src="../../../assets/img/quotesDel.png"/>
      </div>
    </div>
    <div v-if="showDataF">
        <my-scroll class="q-m-Scroll" :data="mainlistData" :pullup="pullup" @scrollToEnd="loadMore">
        <div class="c-q-m-test">
          <div class="c-q-m-content" v-for="item in mainlistData" @click="jump(item)">
            <div class="c-q-m-style">
              <div class="c-q-m-c-portrait">
                <!--<img src="../../../assets/img/thumb-up.png">-->
                <img :src="item.protrait">
              </div>
              <div class="c-q-m-c-operation">
                <div class="c-q-m-c-o-Info">
                  <div class="c-q-m-c-o-i-name">{{item.senderName}}</div>
                  <div class="c-q-m-c-o-i-text">{{item.tag}}</div>
                  <div class="c-q-m-c-o-i-time">{{item.time}}</div>
                </div>
              </div>
              <div class="c-q-m-c-title" v-html="item.html">
                <!--<img src="../../../assets/img/forward.png">-->
              </div>
            </div>
            <div class="c-q-m-c-line"></div>
          </div>
          <loading ref="load"></loading>
        </div>
      </my-scroll>
    </div>
    <blank :is-show="showHide"></blank>
  </div>
</template>
<style>
@import "../../../assets/css/quotesMessage.css";
</style>

<script>
  import API from '../../../api/api'
  import scroll from '@/components/scroll/scroll'
  import loading from '@/components/downLoadAnimal.vue'
  import blank from '@/components/blank.vue'
  import Default from "../../../assets/img/DBKS.png"
  import commentPic from '../../../assets/img/evaluate.png'//评论的图标

  const api = new API();
  export default{
      data(){
          return {
              showDataF:false,//默认是没有数据
              showHide:false,//默认为空白页
              mainlistData:null,
              clearMsgFlag:false,//用于记录是否清空数据
            //上拉加载更多
            pullup:true,
            pageCount:10000,//供求的总数
            pageNumber:1,//供求信息的页码
          }
      },
      methods:{
          back(){
              this.mainlistData = null;
              this.$router.go(-1);
          },
          loadMore(){
              var self = this;

              if(self.pageCount > self.pageNumber){
                self.$nextTick(function () {
                  self.$refs.load.loading();
                });
                self.pageNumber++;
                self.list();
              }
          },
          list(){
              var self = this;
              let params = {
                  api:"/yg-cms-service/comment/checkArticleMsg.apec",
                  data:{
                    "pageNumber":self.pageNumber,
                    "pageSize": 10
                  }
              }
              this.post(params, self.itemForamat)
          },
          itemForamat(data){
              if(data.succeed){
                var self = this;
                var dt = data.data;
                var rows = dt.rows;
                var arr = [];
                self.pageCount = dt.pageCount;
                if(self.pageCount == 0){
                   self.showHide = true;
                   self.showDataF = false;
                   self.clearMsgFlag = false;
                   return;
                }else{
                  self.showHide = false;
                  self.showDataF = true;
                   self.clearMsgFlag = true;
                   if(self.pageCount == self.pageNumber ){
                     self.$nextTick(function () {
                       self.$refs.load.end("没有更多数据");
                     });
                   }
                }
                rows.forEach(function (current) {
                    var obj = {};
                   obj.protrait = current.imgUrl? current.imgUrl+ "?x-oss-process=style/_list":Default;
                   obj.senderName = current.senderName;
                   obj.id = current.relativeArticleId;
                   obj.tag = "评论了你的行情";
                   obj.time = current.createDateStr;
                   obj.html = current.url == ""?current.title.substring(0, 8):'<img src='+ current.url+"?x-oss-process=style/_list" + '>';
                   arr.push(obj);
                });

                if(!self.mainlistData){
                    self.mainlistData = arr;
                }else{
                  self.mainlistData = self.mainlistData.concat(arr);
                }
              }

          },
          msgDelete(){
              //清空所有数据
              if(!this.clearMsgFlag){
                  return;
              }
            this.mainlistData = null;
            var params = {
              api:"/yg-cms-service/comment/clearArticleMsg.apec"
            };
            var self = this;
            this.post(params,function (data) {
                  if(data.succeed){
                    self.clearMsgFlag = false;
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
         jump(item){
           var id = item.id;
           this.$router.push({name:"newsDetail",query:{id:id}});
         }
      },
      activated(){
         this.mainlistData = null;
         this.list();
      },
      components:{
        'my-scroll':scroll,
        'loading':loading,
        'blank':blank
      }
  }
</script>
