/**
* Created by gsy on 2017/10/26.
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
import header from '@/components/header/header'
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
      ntype:"",// comment:写评论    reply:回复
      myform:{
        content:""//内容
      },
      ctentSingleH:""//内容单行高度
    }
  },
  activated(){
    var vm=this;
    vm.id=vm.$route.query.id;
    vm.ntype=vm.$route.query.type;
    console.log("评论类型："+vm.ntype);
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
      // console.dir(getComputedStyle(textDom, null));
      // var fontDom=document.documentElement.style.fontSize.split("px")[0];
      // textDom.style.height=(para*fontDom)+'px';
      // console.log("textDom:"+textDom.style.height+" fontDom:"+fontDom);
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
      var api="";
      var obj={};
      if(vm.ntype == 'comment'){
        api="/yg-society-service/societyPostReply/addSocietyPostReply.apec";
        obj.content=vm.myform.content;
        obj.societyPostId=vm.id;
      }
      else if(vm.ntype == 'reply'){
        api="/yg-society-service/societyLzlReply/addSocietyLzlReply.apec";
        obj.content= vm.myform.content;
        obj.toReplyId = vm.id;
      }
      let params={
        api:api,
        data:obj
      };
      vm.post(params,vm.getsbmCommCb.bind(this),true);

    },
    getsbmCommCb(data){
      var vm=this;
      if(data.succeed){
        Toast({
          message:'评论发表成功！',
          iconClass:'icon icon-success',
          // duration:500
          duration:1000
        });
        vm.$store.state.commentBack = true;
        setTimeout(function(){
//
          /*vm.$router.push({
            name:"newsDetail",//我的行情
            query:{id:vm.id}
          });*/
          vm.$router.go(-1);
        },1000);

       vm.initform();

      }
    },
    //退出确认框
    goBack(){
      var vm=this;
      var noNullFlag=false;
      for(var i in vm.myform){
        console.log("vm.myform[i]:"+vm.myform[i]);
        if(vm.myform[i]){
          noNullFlag=true;
          break;
        }
      }
      if(noNullFlag){
        MessageBox.confirm('退出后无法保存,确定退出？').then(ok =>{
          vm.$router.go(-1);
        },(cancel => {
          // console.log("您点了取消");
        })
      );
      }
      else{
        vm.$router.go(-1);
      }

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
  @import '../../../assets/css/news.css';
</style>
