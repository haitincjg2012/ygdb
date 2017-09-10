<template>
    <div class="z-p-home">
      <div class="z-p-banner">
        <div class="z-header-person-h">
          <img src="../../../assets/img/ret.png" @click="back">
        </div>
         <div class="z-banner-img">
            <!--<img :src="person.bannerImgUrl">-->
           <img src="../../../assets/img/xqimg1.png">
         </div>
          <div class="z-canmer">
            <input type="file" class="z-imgInput" @change="modify">
          </div>
      </div>
      <div class="z-p-info">
         <span class="z-p-name">{{person.name}}</span>
         <span class="z-p-agency">{{person.useType}}</span>
         <span class="z-p-real">{{person.real}}</span>
        <!--<img src="../../../assets/img/Diamonds.png" class="z-p-level">-->
         <img :src="person.levelSrc" class="z-p-level">
      </div>
      <div class="z-p-concat">
         <div class="z-p-notice">
            <p class="z-common-t">289</p>
            <p class="z-common-t2">关注我的</p>
         </div>
        <div class="z-p-browse">
          <p class="z-common-t">123</p>
          <p class="z-common-t2">浏览我的</p>
        </div>
        <div class="z-p-c">
          <p class="z-common-t">123</p>
          <p class="z-common-t2">联系我的</p>
        </div>
      </div>
      <div class="z-space"></div>
      <div class="z-p-manage-info">
        <div class="z-p-area">
            <label>区域:</label>
            <span>{{person.address}}</span>
        </div>
        <div class="z-p-pz">
          <label>主营品种:</label>
          <span>{{person.pz}}</span>
        </div>
      </div>
      <div class="z-space"></div>
      <div class="z-description">
        <div class="z-p-edit-save">
          <p class="z-p-text">实力描述:</p>
          <!--<div class="z-save" v-if="saveFlag" @click="save">保存</div>-->
        </div>
         <textarea placeholder="还没有实力描述，赶快填写吧，让更多用户关注你" ref="edit" readonly="readonly"></textarea>
         <div class="z-icon" @click="edit">
         </div>
      </div>
      <!--<div class="z-des-img">-->
        <!--<p class="z-des-img-text">最多能上传5张</p>-->
        <!--<ul class="clear">-->
          <!--<li :is="item.SS" v-for="item in items" v-on:selecttype="delImage"></li>-->
          <!--<li class="z-add-upload">-->
            <!--<input type="file" accept="image/*"  @change="addImage">-->
          <!--</li>-->
        <!--</ul>-->
      <!--</div>-->
       <!--<div class="z-p-banner">-->
           <!--<img src="">-->
           <!--<input type="file" class="z-imgInput" @change="modify">-->
       <!--</div>-->
    </div>
</template>
<style scoped>
 @import "../../../assets/css/personH.css";
</style>
<script>
  import API from '../../../api/api'
  import DEL from '../../../components/del.vue'

  const api = new API();
  var fn = {
      img:[],
      bannerImg:function () {
      },
      init:function (data) {
        var isobj = data.data;
        var dt;

        if(typeof isobj == "string"){
            dt = JSON.parse(isobj);
        }
          this.person.name = dt.name;
          this.person.useType = dt.userTypeKey;
          this.person.real = dt.userRealAuth =="UNREALAUTH"?"":"实名认证";
          this.person.bannerImgUrl = dt.bannerImgUrl;
          this.person.address = dt.address.split("|").join("");
          this.person.pz = dt.mainOperating;
      },
      addImage:function (data) {
          console.log(data);
        var dt = data.data;

      }
  }
  export default{
      data(){
          return{
            saveFlag:false,
            editFlag:true,
            items:null,
            person:{
                  name:'',
                  address:'',
                  useType:'',
                  real:'',
              levelSrc:"#",
              pz:'',
              bannerImgUrl:"#"
            }
          }
      },
      methods:{
        modify(e){
              var e = e || window.event;
              var target = e.toElement || e.srcElement;
              var files = target.files;
              var type = files[0].type;
              var fd = new FormData();
             fd.append("file", files[0]);
            let params = {
              api:"/yg-user-service/user/uploadBanner/uploadFile.apec",
              data: fd
            }

            this.postImg(params, fn.bannerImg.bind(this))
          },
        back(){
              this.$router.go(-1);
          },
        postImg(params, fn){
          try {
            api.postImg(params).then((res) => {
              var data = res.data;
              fn(data);
            }).catch((error) => {
              console.log(error)
            })
          } catch (error) {
            console.log(error)
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
        edit(){
            this.editFlag = false;
            this.saveFlag = true;
            var el = this.$refs.edit;
            el.readOnly= false;
            this.$router.push({name: 'pcInfo'});
        },
        save(){
          this.editFlag = true;
          this.saveFlag = false;
          var el = this.$refs.edit;
          el.readOnly= true;
        },
        addImage(e){
            var e = e || window.event;
            var target = e.toElement || e.srcElement;
            var files = target.files;
            var fd = new FormData();
            fd.append("file",files[0]);
            let params = {
              api:"/yg-user-service/user/uploadBanner/uploadFile.apec",
              data: fd
            }

          this.postImg(params, fn.addImage.bind(this))
        }
      },
    activated(){
      let params = {
        api: "/_node_user/_info.apno",
        data: {}
      }
      this.post(params, fn.init.bind(this));
    }
  }
</script>
