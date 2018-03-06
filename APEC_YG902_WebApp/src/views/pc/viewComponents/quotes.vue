<template>
  <div class="c-z-quotes">
     <div class="c-z-q-head">
        <div class="c-z-q-h-back" @click="back">
           <img src="../../../assets/img/ret.png">
        </div>
        <div class="c-z-q-h-title" >
          <h4>管理我的行情</h4>
        </div>
       <div class="c-z-q-h-message">
        <!--<div class="c-z-q-h-message" @click="qMessage">-->
             <!--<img src="../../../assets/img/bellM.png"/>-->
             <!--<div class="c-z-q-h-m-number" v-if="quoteMsgNumber">-->
                <!--<div class="c-z-q-h-m-number-text">{{quoteMsgNumber}}</div>-->
             <!--</div>-->
        </div>
     </div>
     <!--<div v-if="quotesFlag">-->
       <my-scroll class="qScroll" :data="mainlistDataA" :pullup="pullup" @scrollToEnd="loadMore" ref="wrapper">
         <div>
           <div class="c-z-q-main" v-for="item in mainlistData">
             <div class="c-z-q-m-content" @click="jumpToNews(item)">
               <div class="c-z-q-m-c-personInfo">
                 <div class="c-z-q-m-c-p-portrait">
                   <img :src="imgUrl">
                 </div>
                 <div class="c-z-q-m-c-p-string">
                   <div class="c-z-q-m-c-p-s-name"><span class="sp1">{{author}}</span></div>
                   <div class="c-z-q-m-c-p-s-time"><span class="sp2">{{item.createDate}}</span></div>
                 </div>
                 <div class="c-z-q-m-c-p-examine">
                   <span class="c-z-q-m-c-p-e-text " :class="{exmainIng:item.auditExam,exmain:item.auditExamS,exmainFail:item.auditExamF}">{{item.auditText}}</span>
                 </div>
               </div>
               <div class="c-z-q-m-c-title">
                 <div class="c-z-q-m-c-t-img" v-if="item.hasImage">
                   <img :src="item.url">
                 </div>
                 <div class="c-z-q-m-c-t-p">
                   <!--<p>烟台地区8月19号红富士苹果的价格行情烟台地区8月19号红富士苹果的.....</p>-->
                   <p>{{item.title}}</p>
                 </div>
               </div>
               <div class="c-z-q-m-c-operation">
                 <div class="c-z-q-m-c-o-read">
                   <span class="sp1">阅读</span>
                   <span class="sp2">{{item.readNum}}</span>
                 </div>
                 <div class="c-z-q-m-c-o-care">
                   <!--<img src="../../../assets/img/attention.png">-->
                   <!--<span class="sp">{{item.attention}}</span>-->
                 </div>
                 <div class="c-z-q-m-c-o-more">
                   <span class="sp-radius"></span>
                   <span class="sp-radius"></span>
                   <span class="sp-radius"></span>
                 </div>
               </div>
             </div>
             <div class="c-z-q-m-blank"></div>
           </div>
           <loading ref="test"></loading>
         </div>
       </my-scroll>
     <!--</div>-->
    <blank :is-show="quotesFlagS"></blank>
  </div>
</template>
<style>
  @import "../../../assets/css/quotes.css";
</style>
<script>
  import API from '../../../api/api'
  import scroll from '@/components/scroll/scrollbg'
//import loading from '@/components/downLoadAnimal.vue' //分页提示

  import loading from '@/components/loading.vue' //分页提示
  import blank from '@/components/blank.vue'
  import userImgUrl from '../../../assets/img/icon.png'//用户默认图像

  const api = new API();
  export default{
      data(){
          return{
            author:'',//用户姓名
            imgUrl:null,//用户头像
            mainlistDataA:[],
            mainlistData:null,
            quotesFlag:false,//默认情况，我们认为是没有数据的，通过列表的判断来确认是存在数据，存在为true,否则为false
            quotesFlagS:false,//默认情况下，是为空的
            quoteMsgNumber:0,//行情消息的未读数
            //上拉加载更多
            pullup:true,
            loadFlag:true,//"没有更多信息了"文字
            loadMopen:false,//上拉加载标识
            pageCount:10000,//供求的总数
            pageNumber:1,//供求信息的页码
            load:"数据正在加载中...",
            arrive:false,//到底底部箭头切换
            loadFlag:false,//箭头切换以后，加载数据
            showVisableF:false,//默认显示的
          }
      },
      methods:{
         back(){
             this.init();
             this.$router.go(-1);
         },
        init(){
          this.mainlistDataA = [];
          this.mainlistData = null;
          this.quotesFlag = false;
          this.quotesFlagS = false;
        },
        loadMore(){
          var self = this;
          if(self.pageNumber < self.pageCount){
//            self.$refs.test.start();
            this.pageNumber ++;
            this.list();
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
        list(){
            var self = this;
            var params = {
                api:"/yg-society-service/societyPost/mySocietyPostPage.apec",
                data:{
                  "realm": "ARTICLE",
                  "pageNumber":self.pageNumber,
                  "pageSize": 10

                }
            }
            this.post(params, self.itemFormat)
        },
        itemFormat(data){
            var dt = data.data;
            var rows = dt.rows;
            var self = this;
            var arr = [];
          self.pageCount = dt.pageCount;
          if(dt.pageCount > 1){
            if(self.pageNumber == self.pageCount){
              self.$nextTick(function () {
                self.$refs.test.end();
              })
            }else{
              self.$refs.test.start();
            }
          }else{
            self.$refs.test.end();
          }

          if(dt.pageCount == 0){
            self.quotesFlagS = true;
          }else{
            self.quotesFlag = true;

          }

            rows.forEach(function (current) {
                var obj = {};
                obj.author = current.author;//发布人的姓名
                obj.createDate= new Date().format(current.createDate,"yyyy-MM-dd hh:mm");//发布时间
                 var auditState = current.auditState;// 审核状态，"0"为未审核，Y为审核通过，N为审核拒绝
                if(auditState == "0"){
                  obj.auditText= "审核中"
                  obj.auditExam = true;
                }else if(auditState == "Y"){
                  obj.auditText= "已通过"
                  obj.auditExamS = true;
                }else if(auditState == "N"){
                  obj.auditText= "未通过";
                  obj.auditExamF = true;
                }else{
                  obj.auditText= auditState;
                }
                var title = current.title;//文章标题
                obj.title= title.length > 25 ? title.substring(0, 25) + "..." :title;
                obj.hasImage = current.hasImage && current.url != "",//判断新闻是否带图片
                obj.url= current.url + "?x-oss-process=style/_list";//新闻配图路径
                obj.readNum= current.viewCount,//阅读数量
                obj.attention= current.likeCount,//关注数
                obj.id= current.id;//新闻id
                arr.push(obj);
            })

            if(arr.length > 0){
              self.mainlistData = self.mainlistData?self.mainlistData.concat(arr):[].concat(arr);
              self.mainlistDataA = self.mainlistData;
            }
        },
        msgNumber(){
//            未读行情消息的数量
          var self = this;
          var params = {
              api:"/_node_user/_article_message.apno",
              data:{}
          };
          this.post(params, function (data) {
             if(data.succeed){
                if(data.data*1 != 0){
                   self.quoteMsgNumber = data.data;
                }else{
                  self.quoteMsgNumber = 0;
                }
             }
          });

        },
        clearMsg(){
            var params = {
                api:"/_node_user/_clear_msgcount.apno"
            }

          this.post(params, function (data) {
          });
        },
        qMessage(){
             this.init();
            this.clearMsg();
            this.$router.push({"name":"quotesMessage"});
        },//跳转到行情消息
        identify(){
          const self = this;
          var params = {
            api:"/_node_user/_info.apno"
          }
          try {
            api.post(params).then((res) => {
              var item = res.data;
              var dt;
              if (item.succeed) {
                dt = JSON.parse(item.data);
                this.imgUrl = (dt.imgUrl || userImgUrl) + "?x-oss-process=style/_head";//用户头像
                this.author = dt.name;//发布人的姓名
              } else {
              }
            }).catch((error) => {
            })
          } catch (error) {
            console.log(error)
          }
        },
        jumpToNews(data){
           var id = data.id;
           this.$router.push({name:"newsDetail",query:{id:id}});
        },//跳转到新闻行情
      },
      activated(){
          this.init();
          this.list();
          this.identify();
          this.msgNumber();
          this.$refs.wrapper.init();
      },
      components:{
        'my-scroll':scroll,
        'loading':loading,
        'blank':blank
      }
  }
</script>
