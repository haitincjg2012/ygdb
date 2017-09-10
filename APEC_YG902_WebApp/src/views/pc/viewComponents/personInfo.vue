<template>
  <div class="person-info-page">
    <div class="z-p-info-header">
      <div class="z-title">编辑资料</div>
      <div class="return" @click="back">
        <img src="../../../assets/img/ret.png">
      </div>
    </div>
    <div class="main-page">
      <div class="p-v-info-F">
        <div class="p-v-info-main-F">
          <span class="p-v-info-person">头像</span>
          <img class="p-v-info-user" :src="imgUrl">
          <img class="arrow-img" src="../../../assets/img/back.png">
          <input type="file" accept="image/*" capture="camera" class="p-v-info-upload" @change="handImg"/>
        </div>
      </div>
      <split></split>
      <div class="p-v-form-cli">
        <span class="label">昵称</span>
        <span class="value">{{name}}</span>
        <a class="updateBtn" @click.stop.prevent="updateName">修改昵称</a>
        <img class="arrow-com" src="../../../assets/img/back.png">
      </div>
      <div class="solid-line">
      </div>
      <div class="p-v-form-cli">
        <span class="label">电话</span>
        <span class="value">{{mobile}}</span>
        <a class="updateBtn" @click.stop.prevent="updatePhone">更换手机号码</a>
        <img class="arrow-com" src="../../../assets/img/back.png">
      </div>
      <div class="solid-line">
      </div>
      <div class="p-v-form-cli">
        <span class="label">身份</span>
        <div class="s-a-box-db"><span>{{userTypeKey}}</span>
          <a class="updateBtn" @click.stop.prevent="updateAur">修改身份</a>
        </div>
        <img class="arrow-com" src="../../../assets/img/back.png">
      </div>
      <div class="z-p-identity" v-if="Identity.pIDF">
        <p class="z-p-identity-text">请选择身份</p>
        <ul class="clear z-ul">
           <li v-for="item in itemId"
                 class="z-p-id-li-com"
                >
               <span class="z-p-id-li-ct" :class="{activeL:item.sh}" @click="selectID" :data-id="item.id" :data-path="item.path">{{item.keyword}}</span>
           </li>
        </ul>
      </div>
      <!--<div class="p-v-bg">-->
          <!--<p class="p-v-bg-text">背景图片</p>-->
          <!--<input class="p-v-bg-upload" type="file" accept="image/*">-->
          <!--<img class="arrow-com" src="../../../assets/img/back.png">-->
      <!--</div>-->
      <split></split>
      <div class="z-p-warehouse" v-if="Identity.warehouse">
         <label for="warehouse">仓库名称</label>
         <input type="text" placeholder="填写仓库的名称" id="warehouse">
      </div>
      <div class="z-p-storageCapacity" v-if="Identity.storage">
        <label for="storage">仓库库容</label>
        <input type="text" placeholder="填写仓库的库容" id="storage">
      </div>
      <div @click.stop="addSelect" class="p-v-form-cli">
        <span class="label">所在地区</span>
        <span class="value">{{addr}}</span>
        <img class="arrow-com" src="../../../assets/img/back.png">
      </div>
      <div class="solid-line-p">
      </div>
      <div class="p-v-form-cli">
        <span class="label">详细地址</span>
        <span class="value">{{addrDetail}}</span>
        <a class="updateBtn" @click.stop.prevent="updateAddrDetail">修改详细地址</a>
        <img class="arrow-com" src="../../../assets/img/back.png">
      </div>
      <div class="solid-line-p">
      </div>
      <div class="p-v-form-cli">
        <span class="label">主营品种</span>
        <span class="value">{{zySort}}</span>
        <a class="updateBtn" @click.stop.prevent="updatezySort">修改主营品种</a>
        <img class="arrow-com" src="../../../assets/img/back.png">
      </div>
      <div class="solid-line-p">
      </div>
      <!--<div class="p-v-form-cli">-->
        <!--<span class="label">从业年限</span>-->
        <!--<span class="value">{{workYear}}</span>-->
        <!--<a class="updateBtn" @click.stop.prevent="updateWorkY">修改从业年限</a>-->
        <!--<img class="arrow-com" src="../../../assets/img/back.png">-->
      <!--</div>-->
      <div class="solid-line-p">
      </div>
      <div class="z-p-description">
         <p class="z-p-text">实力描述:</p>
         <textarea placeholder="还没有实力描述，赶快填写吧，让更多用户关注你" ref="edit"></textarea>
      </div>
      <div class="z-des-img">
        <p class="z-des-img-text">最多能上传5张</p>
        <ul class="clear">
          <li :is="item.SS" v-for="item in items" v-on:selecttype="delImage"></li>
          <li class="z-add-upload">
            <input type="file" accept="image/*"  @change="addImage">
          </li>
        </ul>
      </div>
    </div>
    <key-board></key-board>
  </div>
</template>
<style scoped>
  @import "../../../assets/css/personInfo.css";
</style>
<script>
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import keyBoard from '../../../components/keyboard/keyboard'
  import {MessageBox, Toast} from 'mint-ui';
  import DEL from '../../../components/del.vue'

  import userImgUrl from '../../../assets/img/Headportrait.png'

  const api = new API();
  var fn = {
    img:[],
    DB:[{id:"10000",path:0,keyword:"调果员A",sh:false},{id:"1000",path:1,keyword:"调果员B",sh:false}],
    LK:[{id:"80000",path:0,keyword:"老板",sh:false},{id:"80000",path:1,keyword:"保管",sh:false}],
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
      var dt = data.data;

    }
  }
  export default {

    data() {
      return {
        imgUrl: '',//用户头像
        name: '',//用户名
        userTypeKey: '',//用户身份
        mobile: '',
        addr: '',//市县区镇
        addrDetail: '',//详细地址
        zySort: '',//主营品种
        workYear: '',
        userType:'',
        items:null,
        itemId:null,
        Identity:{
          pIDF:false,
          warehouse:false,
          storage:false
        }
      }
    },
    activated(){
      this.getUserInfo();
    },
    methods: {
        back(){
           this.reset();
           this.$router.go(-1);
        },
        reset(){
            var self = this;
          self.Identity.pIDF = false;
          self.Identity.warehouse = false;
          self.Identity.storage = false;
        },
      selectID(e){
            var e = e || window.event;
            var target = e.toElement || e.srcElement;
            var id = target.dataset.id,
              path = target.dataset.path;
            var value = target.innerHTML;

        this.itemId.forEach(function (current,index) {
          if(index == path){
            current.sh = true;
          }else{
            current.sh = false;
          }
        })
        if(id == "80000"){
          var el = document.getElementById("warehouse");
          var e2 = document.getElementById("storage");
           if(value == "保管"){
               el.readOnly = true;
               e2.readOnly = true;
           }else{
             el.readOnly = false;
             e2.readOnly = false;
           }
        }
      },
      updateName(){
        MessageBox.prompt('请输入新的姓名').then(({value, action}) => {
          var self = this;
          var dl = {
            name: value
          };
          self.setUserInfo(dl, value, function () {
            self.name = value;
            self.$store.commit("incrementName", {'name': value});//用户名
            c_js.setLocalValue('name',value);
          });
        });
      },
      handImg(evt){
          const self = this;
          var evt = evt || window.event;
          var files = evt.target.files || evt.dataTransfer.files;

          var file = files[0];

          var type = file.type;
          if(!type){
            Toast("对不起，您上传的图片格式不对，请您重新上传图片");
          }
          var tpattern = /image\/(png|jpeg)/g;
          if(!tpattern.test(type)){
            return;
          }
          var that = this;
          var fd = new FormData();

          fd.append("file",file)
          let params = {
              api:"/yg-user-service/user/uploadImage/uploadFile.apec",
            data:fd
            }
            console.log(fd);
            try {
              api.postImg(params).then((res) => {
                var data = res.data;
                if(data.succeed){
                  self.$store.commit("incrementImgUrl", {'imgUrl': data.data.imgUrl});//用户头像
                  c_js.setLocalValue('imgUrl',data.data.imgUrl);
                  var fr = new FileReader();
                      fr.onload = function (evt) {
                        var t = evt.target.result;
                        var el = document.querySelector(".p-v-info-user");
                        el.src = t;
                      };
                  fr.readAsDataURL(file);
                }
              }).catch((error) => {
                console.log(error)
              })
            } catch (error) {
              console.log(error)
            }

      },
      updateAddrDetail(){
        MessageBox.prompt('如xx村xx街道xx号','请输入详细地址').then(({value, action}) => {
          var self = this;
          var dl = {
            addressDetail: value
          };
          self.setUserInfo(dl, value, function () {
            self.addrDetail = value;
          });
        });
      },
      updatezySort(){
        MessageBox.prompt('如红富士80#一二级，红富士70#一二级','请输入主营品种').then(({value, action}) => {
          var self = this;
          var dl = {
            mainOperating: value
          };
          self.setUserInfo(dl, value, function () {
            self.zySort = value;
          });
        });
      },
      updateWorkY(){
        MessageBox.prompt('如3个月、1年、5年','请输入工作年限').then(({value, action}) => {
          var self = this;
          var dl = {
            workOfYear: value
          };
          self.setUserInfo(dl, value, function () {
            self.workYear = value;
          });
        });
      },
      updatePhone(){
        this.$router.push({name: 'upPhone'});
      },
      updateAur(){
        this.$root.eventHub.$emit('aurUpdate.open', this.userType)
      },
      updateAurCall(db){
        var self = this;
        var ID = db.aurKey;
        if(ID == "LK"){
          self.itemId = fn.LK;
            self.Identity.pIDF = true;
            self.Identity.warehouse = true;
            self.Identity.storage = true;
        }else if(ID == "DB"){
          self.itemId = fn.DB;
          self.Identity.pIDF = true;
          self.Identity.warehouse = false;
          self.Identity.storage = false;
        }else{
          self.reset();
        }
        var dl = {
          userType: db.aurKey
        };
        self.setUserInfo(dl, '', function () {
          self.userTypeKey = db.aurName;
          self.$store.commit("incrementUType", {'userTypeName': db.aurKey});//用户身份 'DB'
          c_js.setLocalValue('userTypeName',db.aurKey);
        });
      },
      addSelect(){
        this.$router.push({name: 'addressInfo'});
      },
      getUserInfo(){
        const self = this;
        let params = {
          api: "/_node_user/_info.apno",
          data: {}
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              var data = JSON.parse(item.data);
              self.addr = data.address || '请修改地区';
              self.addrDetail = data.addressDetail || '请修改详细地址';
              self.zySort = data.mainOperating || '请修改主营品种';
              self.workYear = data.workOfYear || '请修改工作年限';
              self.name = data.name || '请修改昵称';
              self.imgUrl = data.imgUrl || userImgUrl;
              self.userTypeKey = data.userTypeKey || this.$store.state.userTypeKey || c_js.getLocalValue('userTypeKey');
              self.userType = data.userType;
              self.mobile = data.mobile;
              if(self.userTypeKey == "代办"){
                self.itemId = fn.DB;
                self.Identity.pIDF = true;
                self.Identity.warehouse = false;
                self.Identity.storage = false;
              }else if(self.userTypeKey == "冷库"){
                  self.itemId = fn.LK;
                self.Identity.pIDF = true;
                self.Identity.warehouse = true;
                self.Identity.storage = true;
              }
              self.itemId.forEach(function (current,index) {
                current.sh = false;
              })
            } else {
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      },
      setUserInfo(item, value, fn){
        const self = this;
        let params = {
          api: "/yg-user-service/user/updateUserInfo.apec",
          data: item
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              Toast("修改成功~");
              fn();
            } else {
              Toast("修改失败，请稍后重试");
            }
          }).catch((error) => {
          })
        } catch (error) {
          console.log(error)
        }
      },
      delImage(){

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
    },

    created() {
      this.$root.eventHub.$on('aurUpdate.confirm', this.updateAurCall)
    },

    components: {
      split,
      topBar,
      keyBoard
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .person-info-page
    .p-v-info-F
      height (90/_rem)
      line-height (90 / _rem)
      margin-left (15 /_rem)
      position relative
      .p-v-info-main-F
        position relative
        line-height (90 / _rem)
        .p-v-info-person
          height (20 / _rem)
          font-size (15/_rem)
          color #7e7e7e
        .p-v-info-user
          height (60 /_rem)
          width (60 /_rem)
          border-radius (50%)
          position relative
          margin-left (234/_rem)
          vertical-align middle
        .p-v-info-upload
          position absolute
          top 0
          left 0
          z-index 99
          background transparent
          opacity 0
          height (80 /_rem)
          width 100%
   .p-line-top
      height (50 /_rem);
      display flex;
      .line-one, .line-two, .line-three
        flex 1
        vertical-align middle
        line-height (40 /_rem)
        text-align center
        margin (5 /_rem) 0
        img
          width (20 /_rem)
          height (20 /_rem)
          vertical-align middle
        span
          font-size (12 /_rem)
      .line-one, .line-two
        border-right 1px solid #f4f4f4

    .p-v-form-cli:last-of-type
      margin-bottom 10px
    .p-v-form-cli
      height (45 /_rem)
      line-height (45 /_rem)
      margin-left (15 /_rem)
      position relative
      .arrow
        width (10 /_rem)
        height (10 /_rem)
        position absolute
        right (20 /_rem)
        top 50%
        transform translateY(-50%)
      .s-a-box-db
        width (60 /_rem)
        border 1px solid #28cba7
        border-radius 4px
        text-align center
        display inline-block
        cursor pointer
        height (20 /_rem)
        line-height (20 /_rem)
        font-size 0
        color #28cba7
        margin-left (30 /_rem)
      .updateBtn
        position absolute
        top 50%
        transform translateY(-50%)
        right (24 /_rem)
        font-size (15 /_rem)
        color #28cba7
      .label
        color #7e7e7e
      .value
        color #111111
        margin-left (30 /_rem)
      span
        font-size (15 /_rem)
    .solid-line
      margin 0 0 0(15 /_rem)
      height 1px
      background-color #D7D7D7
    .solid-line-p
      margin 0 0 0(15 /_rem)
      height 1px
      background-color #D7D7D7

</style>
