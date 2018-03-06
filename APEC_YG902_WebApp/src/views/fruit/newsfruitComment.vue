/**
* Created by gsy,zy on 2017/10/26.
* 发表评论
*/
<template>
  <div class="newsPublish" id="formContainer">
    <div class="myheader">
      <my-header :headTitle="title" :backFlag="backflag" v-on:initPage="goBack"></my-header>
    </div>
    <div class="publicate" @click="submitComm">
      <span>提交</span>
    </div>
    <form class="myform" >
      <div class="content">
        <textarea id="textarea" placeholder="说点什么吧..."  v-model="myform.content"></textarea>
      </div>
    </form>

  </div>

</template>

<script>
  import {Indicator,Toast,MessageBox} from 'mint-ui'
  import header from '@/components/header/headerT'//头部组件
  import autoHeight from '@/assets/js/autoHeight'
  import API from '@/api/api'
  const api=new API();
  export default{
    data(){
      return{
        title:'评论',
        backflag:false, //是否执行返回路由
        id:null,//文章id
        areaRows:2,
        myform:{
          content:""//内容
        },
        ctentSingleH:""//内容单行高度
      }
    },
    activated(){
      var vm=this;
      vm.id=vm.$route.query.id;
      vm.initform();
      //初始化title、content高度为单行高度
      vm.initArea("textarea",vm.ctentSingleH);
      //textarea 内容增加高度自适应
      vm.getAutoHeight();

    },
    mounted(){
      var vm=this;
      var contentDom = document.getElementById("textarea");
      vm.ctentSingleH = getComputedStyle(contentDom, null)["height"];
    },
    watch:{
      /**
       //监听textarea
       content(){
        var vm=this;
        vm.textareaChange();
      }
       **/
    },
    methods:{
      //init textarea输入框
      initArea(id,height){
        var vm=this;
        var textDom = document.getElementById(id);
        textDom.style.height=height;
      },
      //提交评论
      submitComm(){
        var vm=this;
        if(!vm.myform.content){
          Toast("请输入评论内容!");
        }
        else{
          vm.getsbmComm();
        }
      },
      //提交评论api
      getsbmComm(){
        var vm=this;
        var id = this.$route.query.id;
        var flag = this.$route.query.flag;//postLZL代表楼中楼，postHome代表果圈，postDetail代表帖子的详情,price果价猜猜的
        var obj = {};
        var url = "";
        switch (flag){
          case "postLZL":
            obj.toReplyId = id;
            obj.content= vm.myform.content;
            url ="yg-society-service/societyLzlReply/addSocietyLzlReply.apec"
            break;
          case "postHome":
            obj.societyPostId = id;
            obj.content= vm.myform.content;
            url ="yg-society-service/societyPostReply/addSocietyPostReply.apec"
            break;
          case "postDetail":
            obj.societyPostId = id;
            obj.content= vm.myform.content;
            url ="yg-society-service/societyPostReply/addSocietyPostReply.apec"
            break;
            case "price":
            obj.societyPostId = id;
            obj.content= vm.myform.content;
            url ="yg-society-service/societyPostReply/addSocietyPostReply.apec"
            break;
        }

        let params={
          api:url,
          data:obj
        };
        vm.post(params,vm.getsbmCommCb.bind(this, flag),true);

      },
      getsbmCommCb(flag, data){

        var vm=this;
        if(data.succeed){
          Toast({
            message:'评论发表成功！',
            iconClass:'icon icon-success',
            duration:1000
          });
          var router = vm.$store.state.fruitRouter;//路由的由来，响应相应的数据
          switch (flag){
            case "postLZL":
               vm.$store.state.fDLzlContent = vm.myform.content;
               this.$store.state.fruitDetail.LzlFlag.succeed = true;
               this.$store.state.postId = data.data.id;
                break;
            case "postHome":
              vm.$store.state.fruitRouter.commentFlag.succeed  = true;
              vm.$store.state.fComment = vm.myform.content;
              router.comment = true;
              break;
            case "postDetail":
              router.comment = true;
              this.$store.state.fruitDetail.commentFlag.succeed = true;//果圈详情的评论二者是相同的
              vm.$store.state.fruitRouter.commentFlag.succeed  = true;//果圈的评论
              vm.$store.state.fComment = vm.myform.content;
              vm.$store.state.newPosts = data.data;
              break;
          }

          setTimeout(function(){
              vm.$router.go(-1);
          },1100);

          vm.initform();

        }
      },
      //退出确认框
      goBack(){
        var vm=this;
        vm.$store.state.fruitRouter.commentFlag.succeed = false;
        vm.$router.go(-1);
      },
      //textarea 内容增加高度自适应
      getAutoHeight(){
        var vm=this;
        var textDom = document.getElementById("textarea");
        autoHeight.autoTextarea(textDom);// 调用
      },

      //init 表单
      initform(){
        var vm=this;
        for(var i in vm.myform){
          vm.myform[i]="";
        }
      },
      //封装post请求
      post(params,fn,indifalg){
        if(!indifalg){
          Indicator.open({
            text:'加载中...',
            spinnerType:'fading-circle'
          });
        }
        try{
          api.post(params).then((res)=>{
            Indicator.close();
            var data=res.data;
            fn(data);
          }).catch((error)=>{
            Indicator.close();
            console.log(error);
          })
        }
        catch(error){
          Indicator.close();
          console.log(error);
        }

      }
    },
    components:{
      'my-header':header
    }

  }
</script>

<style scoped>
  @import '../../assets/css/news.css';
</style>
