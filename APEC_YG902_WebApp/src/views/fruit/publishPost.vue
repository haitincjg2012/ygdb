/**
* Created by gsy,zy on 2017/10/17.
* 新闻发布
*/
<template>
  <div class="newsPublish" id="formContainer">
    <div class="myheader">
      <!--<my-header :headTitle="title" :backFlag="backflag" v-on:initPage="goBack"></my-header>-->
      <my-header>
         <h3 slot="pack" class="c-publish-title">发布帖子</h3>
      </my-header>
    </div>
    <div class="publicate" @click="goIssue">
      <span>发布</span>
    </div>
    <form class="myform" >
      <!--<div class="title">-->
        <!--&lt;!&ndash; <input id="title"  type="text" placeholder="添加标题" v-model="myform.title" /> &ndash;&gt;-->
        <!--<textarea id="titlearea" rows="1" placeholder="添加标题" v-model="myform.title"></textarea>-->
      <!--</div>-->
      <div class="c-post-picture">
        <div v-for="(item,index) in arrImg">
          <div class="c-p-p-HW">
            <span class="c-p-p-HW-delBtn" @click="delImg(index)">
               <img src="../../assets/img/delIcon.png" />
            </span>
            <img :src="item.imgUrl+'?x-oss-process=style/_stylist'" class="c-p-p-HW-img" />
          </div>
        </div>
        <div class="c-p-p-addHW" v-show="pictureFlag">
          <i class="plus"></i>
          <span class="plusText">添加图片</span>
          <input type="file" accept="image/*"  @change="handleimg"/>
        </div>
        <div class="c-p-p-blank"></div>
      </div>
      <!--<div class="uploadImg" v-if="uploadflag">-->
        <!--<i class="plus"></i>-->
        <!--<span class="plusText">添加图片</span>-->
        <!--<input type="file" accept="image/*"  @change="handleimg"/>-->
      <!--</div>-->
      <!--<div class="newsImg" v-if="!uploadflag">-->
        <!--&lt;!&ndash; <div class="newsImg" v-if="true" > &ndash;&gt;-->
        <!--<span class="delBtn" @click="delImg">-->
            <!--<img src="../../assets/img/delIcon.png" />-->
          <!--</span>-->
        <!--<img :src="myform.url+'?x-oss-process=style/_detail_hq'" />-->
        <!--&lt;!&ndash; <img src="https://yg-pro.oss-cn-qingdao.aliyuncs.com/4cd0ddf770bd87516553fc659e5f56e7?x-oss-process=style/_detail_hq" /> &ndash;&gt;-->
      <!--</div>-->

      <div class="content">
        <textarea id="textarea" placeholder="添加内容"  v-model="myform.content"></textarea>
      </div>
    </form>

  </div>

</template>

<script>
  import {Indicator,Toast,MessageBox} from 'mint-ui'
//  import header from '@/components/header/header'
  import header from '../../components/header/headerTwo.vue'
  import ALIYUN from '@/components/aliyun.vue'
  import autoHeight from '@/assets/js/autoHeight'
  import API from '@/api/api'
  const api=new API();
  export default{
    data(){
      return{
        arrImg:null,//上传到阿里云服务器的图片路径,数组[]
        lengthN:0,//图片数量
        title:'发布帖子',
        backflag:false, //是否执行返回路由
        content:"",
        areaRows:2,
        aliyun0:null,//阿里云调用函数
        uploadflag:true, //true show上传图片，false show已上传的图片
        myform:{
          title:"",//标题
          url:"", //上传图片地址
          content:""//内容
        },
        titSingleH:"",//标题单行高度
        ctentSingleH:"",//内容单行高度

        pictureFlag:true,//添加图片的按钮
      }
    },
    activated(){
      var vm=this;
      vm.arrImg = null;//初始化
      vm.pictureFlag = true;
      vm.aliyunO = ALIYUN.aliyun();
      vm.initform();
//      var titleDom = document.getElementById("titlearea");
      //初始化title、content高度为单行高度
//      vm.initArea("titlearea",vm.titSingleH);
//      vm.initArea("textarea",vm.ctentSingleH);
      //textarea 内容增加高度自适应
      vm.getAutoHeight();

    },
    watch:{
    },
    mounted(){
      var vm=this;
//      var textDom = document.getElementById("titlearea");
      var contentDom = document.getElementById("textarea");
//      vm.titSingleH = getComputedStyle(textDom, null)["height"];
      vm.ctentSingleH = getComputedStyle(contentDom, null)["height"];
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
      //退出确认框
      goBack(){
        var vm=this;
        var noNullFlag=false;
        for(var i in vm.myform){
          // console.log("vm.myform[i]:"+vm.myform[i]);
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
//        var titleDom = document.getElementById("titlearea");
//        autoHeight.autoTextarea(titleDom);// 调用
        autoHeight.autoTextarea(textDom);// 调用

      },
      //发布内容
      getContent(){
        var vm=this;
        // console.log("content:"+vm.myform.content);
      },
      //校验
      validate(){
        var vm=this;
       if(!vm.myform.content){
          Toast("请填写内容！");
          return false;
        }
        else{
          return true;
        }
      },
      //发布
      goIssue(){
        var vm=this;
        if(vm.validate()){
          vm.issueNews();
        }
      },
      //发布新闻
      issueNews(){
        var vm=this;
        var hasimage=vm.myform.imgUrl?true:false;
        // console.log("hasimage:"+hasimage);
        let params={
          api:'yg-society-service/societyPost/addSocietyPost.apec',
          data:{
            category:"NEWS",
            channelCode:"NEWS",
            realm:"SOCIETYPOST",
            hasImage:hasimage,
            title:vm.myform.title,
            url:vm.arrImg&&vm.arrImg[0].imgUrl,
            content:vm.myform.content,
            personPub:true, //true || false，默认为false（代办后台发布），web app发布为true
//            societyPostImagesVOS:[{
//              "imgUrl":vm.myform.url,
//              "sort":"0"
//            }]
            societyPostImagesVOS:vm.arrImg
          }
        };
        vm.post(params,vm.issueNewsCb,true);
      },
      issueNewsCb(data){
        var vm=this;
        Toast({
          message:'发布成功！',
          iconClass:'icon icon-success',
          duration:1000
        });

        setTimeout(function(){
          vm.$router.push({
            name:"fruitCircle",
            query:{flag:2}//返回到果圈首页
          });
        },1000);

        vm.pictureFlag = false;//提交成功后，上传按钮消失
        vm.initform();
      },
      //init 表单
      initform(){
        var vm=this;
        for(var i in vm.myform){
          vm.myform[i]="";
        }

        vm.uploadflag=true;
      },
      //删除已上传图片
      delImg(index){
        var vm=this;
//        vm.myform.url="";
        vm.arrImg.splice(index, 1);
        if(vm.arrImg.length < 6){
            vm.pictureFlag = true;
        }
      },
      //图片上传
      handleimg(evt){
        var vm = this;
        var files = evt.target.files || evt.dataTransfer.files;
        var file = files[0];
        var type = file.type;
        if(!type){
          Toast({
            message:"对不起，您上传的图片格式不对，请您重新上传图片",
            duration:1000
          })
        }
        var name = file.name;
        var userId = vm.$store.state.userId;
        this.aliyunO(userId,vm.uploadPicture, file);
      },
      uploadPicture(url){
        var vm=this;
        vm.arrImg = vm.arrImg || [];
        //  console.log("图片上传成功！"+url);
        vm.arrImg.push({imgUrl:url});
        vm.myform.imgUrl=url;

        if(vm.arrImg.length > 5){
            vm.pictureFlag = false;
        }

//        vm.uploadflag=false;
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
  @import '../../assets/css/publishPost.css';
</style>
