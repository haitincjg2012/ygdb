<template>
  <div>
      <!--<scrollbg class="c-endorsement" :data="arr" :pullup="pullup" ref="scrollBarS">-->
      <div class="c-endorsement">
        <div  class="c-endorsement-position">
          <div class="c-endorsement-endit">
            <div class="c-endorsement-f-style-com c-endorsement-charactier-name">
              <input type="text" class="c-endorsement-f-input-com" placeholder="请输入姓名" maxlength="6" v-model="name" required />
            </div>
            <div class="c-endorsement-f-style-com">
              <input type="tel" class="c-endorsement-f-input-com" placeholder="请输入手机号" maxlength="11" v-model="phone" required/>
            </div>
            <h6 class="c-endorsement-identify-tips">请选择你的身份:</h6>
            <div class="c-endorsement-identify-item">
              <div v-for="item in enumTagS" class="c-endorsement-id-item-content">
                <div class="c-endorsement-id-item-child"
                     :class="{'c-endorsement-identify-active':item.flag}"
                     @click="choiceIdentity(item)"
                >{{item.name}}</div>
              </div>
            </div>
            <div class="c-endorsement-f-style-com">
              <input type="text" class="c-endorsement-f-input-com" placeholder="请输入所在地" maxlength="10" v-model="address" required/>
            </div>
          </div>
          <label class="c-endorsement-btn" for="upId"></label>
          <input type="file" id="upId" class="c-endorsement-btn-file" @change="handlePicture">
          <!--<div class="c-endorsement-btn" @click="uploadPicture"></div>-->
        </div>
      </div>
      <div class="loading-wrapper" v-if="isLoading">
      	<div class="loading-content">
      		<div class="h-animation"></div>
      		<p class="loading-text">图片上传中...</p>
      	</div>
      </div>
      <!--</scrollbg>-->

      <div class="c-endorsement-compose-show" v-if="composeSrc" style="-webkit-user-select: none;">
         <img :src="composeSrc" class="c-endorsement-compose-show-picture">
      </div>
      <div class="home-tips" v-if="composeSrc">
      </div>
      <div class="c-endorsement-compose">
        <compose :headPortrait="headPortraitF" :name="name" :identity="identity"  :phone="phone" :address="address" @componse="getPicture"></compose>
      </div>
  </div>
</template>
<style scoped>
  @import "../../assets/css/operate/homeF.css";
</style>
<script>
  import scrollbg from '../../components/scroll/scrollbg.vue'
  import cPicture from './composePicture.vue'
  import ALIYUN from "../../components/aliyun.vue" //使用阿里云上传图片
  import compose from "./compose.vue"
  import p from "../../components/post.vue"
  import {Toast, Indicator} from 'mint-ui'
  export default{
      data(){
          return {
            enumTagS:[{name:"代办",
              type:"DB",
              flag:false,},
              {name:"客商",
                type:"KS",
                flag:false,},
              {name:"冷库",
                type:"LK",
                flag:false,},
              {name:"果农",
                type:"ZZH",
                flag:false,},
                {name:"合作社",
                  type:"HZS",
                  flag:false,},
 ],

            pullup:true,
            arr:['1'],
            headPortraitF:null,//头像的base64
            composeSrc:null,//合成图片

            name:null,
            address: '',
            identity:null,
            chosenType: null,//用于存储选中的类型
            phone:null,
            isLoading: false//图片加载
          }
      },
      methods:{
        getPicture(src){
            var that = this;
            that.composeSrc = src;
        		that.isLoading = false;//取消loading  在上传图片时候开启
        },
        choiceIdentity(item){
        	this.enumTagS.map(function(item){
        		item.flag = false;
        	})
        	this.chosenType = item.type;
          item.flag = true;
          this.identity = item.name;
        },
        uploadPicture(){
            this.$router.push({name:"revise"})
        },
        checkNull(){
        	var isNull = false;
        	this.enumTagS.map(function(item){
        		if(item.flag == false){
        			isNull = true;
        		}
        	})
        	if(!this.name || !this.phone){
        		isNull = true;
        	}
        	return isNull;
        },
        handlePicture(evt){
        	this.isLoading = true;
          var that = this;
          var files = evt.target.files || evt.dataTransfer.files;
          var file = files[0];
          if(!file){
            return;
          }
          var type = file.type;
          if(!type){
            Toast({
              message:"对不起，您上传的图片格式不对，请您重新上传图片",
              duration:1000
            })
        		that.isLoading = false;
            return;
          }

          var userIdTest = 123435;
          this.aliyunO(userIdTest,function (url) {
            that.headPortraitF = url + "?x-oss-process=style/_head_bg";
            //添加活动记录
	          let params = {
	            api: "/yg-user-service/userActiveInfo/addUserActiveInfo.apec",
	            data: {
	              "mobile": that.phone,
	              "userType": that.chosenType,
	              "name": that.name,
	              "imgUrl": url,
	              "activeUrl": "",
	              "address": that.address
	            }
	          }
	          p.post(params, function (data) {
	            Indicator.close();
	            if(data.succeed){
	            }else{
	              Toast("请求失败:"+data.errorMsg+" 稍后重试");
	            }
	          }, function (error) {
	            Indicator.close();
	            Toast("请求失败:"+error+"  稍后重试");
	          });
	          
          } , file);
          //读取图片
//          var reader = new FileReader();
//          reader.readAsDataURL(file);
//          reader.onload = function (e) {
//              that.headPortraitF
//            that.src = this.result;
//            that.setStyle();
//          };

        },
      },
      activated(){
        this.aliyunO = ALIYUN.aliyun();
      },
      components:{
        cPicture,
        compose,
        scrollbg
      }
  }
</script>
<style scoped>
.loading-wrapper {
	position: fixed;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
}
.loading-content {
	position: fixed;
	left: 50%;
	top: 50%;
	transform: translate(-50%, 50%);
	text-align: center;
}
.h-animation {
  display: inline-block;
  width: 2rem;
  height: 2rem;
  background: url("../../assets/img/pull-icon.png") 50% 100% no-repeat;
  /*background: url("../../../assets/img/pull-icon.png") 50% 100% no-repeat;*/
  background-size: 2rem 4rem;
  -webkit-transform: rotate(0deg) translateZ(0);
  -webkit-transition-duration: 0ms;
  animation: loading;
  -webkit-animation: loading;
  /* Safari 和 Chrome */
  -webkit-animation-duration: 2s;
  animation-iteration-count: infinite;
  -webkit-animation-iteration-count: infinite;
  /* Safari 和 Chrome */
  -webkit-animation-timing-function: linear;
  -webkit-transition-property: -webkit-transform;
  -webkit-transition-duration: 250ms;
}
.loading-text {
	color: #fff;
	font-size: 0.75rem;
}
@keyframes loading {
  from {
    transform: rotate(0deg) translateZ(0);
  }
  to {
    transform: rotate(360deg) translateZ(0);
  }
}
@-moz-keyframes loading {
  /* Firefox */
  from {
    -moz-transform: rotate(0deg) translateZ(0);
  }
  to {
    -moz-transform: rotate(360deg) translateZ(0);
  }
}
@-webkit-keyframes loading {
  /* Safari and Chrome */
  from {
    -webkit-transform: rotate(0deg) translateZ(0);
  }
  to {
    -webkit-transform: rotate(360deg) translateZ(0);
  }
}
.home-tips {
	position: fixed;
	left: 50%;
	bottom: 2.5rem;
	z-index: 999;
	transform: translateX(-50%);
	width: 11.65rem;
	height: 2.51rem;
	background: url(../../assets/img/tips.png);
	background-size: 100%;
}
</style>