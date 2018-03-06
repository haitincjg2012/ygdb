<template>
  <div class="c-warn">
    <my-header class="c-warn-header" @receive="back">
       <h1 slot="pack" class="c-warn-header-title">举报</h1>
    </my-header>
    <div class="c-warn-blank"></div>
    <div>
      <h6 class="c-warn-subtitle">请选择举报类别</h6>
      <div class="c-warn-subtitle-content">
        <div class="c-warn-classify-item"
           v-for="item in enumItem">
            <div class="c-warn-classify-text" :class="{'c-warn-active':item.flag}" @click="content(item)">
              {{item.designation}}
            </div>
        </div>
      </div>
      <div class="c-warn-reason">
          <textarea placeholder="其他举报理由" v-model="reason"></textarea>
      </div>
    </div>
    <div class="c-warn-blank"></div>
    <div class="c-submit-btn" @click="report">举报</div>
  </div>
</template>
<style>
  @import "../../../assets/css/warn.css";
</style>
<script>
  import header from '../../../components/header/headerTwo.vue'
  import {Toast} from 'mint-ui'
  import API from '../../../api/api'
  const api = new API();

  export default{
      data(){
          return {
              enumItem:[{designation:"货源不真实",flag:false},{designation:"联系不上",flag:false},{designation:"价格虚高",flag:false},{designation:"价格虚低",flag:false}],
              reason:"",
              realm:null,
              articalId:'',
              articalName:"",
          }
      },
    methods:{
      back(){
        var self = this;
        self.enumItem.forEach(function (current) {
          current.flag = false;
        })
        self.reason="";
        self.raalm=null;
      },
      report(){
          var self = this;
          var id = window.localStorage.useId,
               name = window.localStorage.name,
               feedBackInfo = '';

            if(!name){
              Toast("请您先登录，再进行举报!");
              self.enumItem.forEach(function (current) {
                current.flag = false;
              })
              self.reason="";
              self.raalm=null;

              return;
            }


            this.enumItem.forEach(function (current) {
            if(current.flag){
                feedBackInfo += (current.designation + " ");
            }
          })
           if(!feedBackInfo){
             feedBackInfo = "其他";
           }
          if(!feedBackInfo && !self.reason){
               Toast("举报不能为空!");
               return;
          }
          var params ={
              api:"yg-systemConfig-service/feedBack/saveFeedBackInfo.apec",
              data:{
                informantUser:name,
                supplementary:self.reason,
                realm:"PRODUCT",//单据的来源
                feedBackInfo:feedBackInfo,//用户反馈信息
                feedBackType:'JB',
                certificateUrl:"",//凭证(图片url)
                reportedUserId:id,//被举报人id
                reportedUser:this.articalName,//被举报人姓名
                relatedIds:this.articalId,
              }
          }

          this.post(params, function(data){
              if(data.succeed){
                self.enumItem.forEach(function (current) {
                  current.flag = false;
                })
                self.reason="";
                self.raalm=null;
                self.$router.go(-1);
              }else{
                  Toast(data.errorMsg);
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
      content(item){
        if(item.flag){
          item.flag = false;
        }else{
          item.flag = true;
        }
      },
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
              window.localStorage.useId = dt.userOrgId;
            } else {
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      },
    },

    activated(){
      var that = this;
      this.articalId =  this.$route.query.articalId;
      this.articalName =  this.$route.query.articalName;
      that.realm = that.$route.query.realm;
      that.back();
      if(!window.localStorage.useId){
        that.identify();
      }

    },
    components:{
          "my-header":header
    }
  }
</script>
