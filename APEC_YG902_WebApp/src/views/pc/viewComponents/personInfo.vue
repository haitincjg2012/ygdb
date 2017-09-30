<template>
  <div class="person-info-page" style="background-color: #fff">
    <div class="z-p-info-header">
      <div class="z-title-T">编辑资料</div>
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
          <input type="file" accept="image/*"  class="p-v-info-upload" @change="handImg"/>
        </div>
      </div>
      <!--<split></split>-->
      <!--<div class="p-v-form-cli" @click.stop.prevent="updateName">-->
        <div class="p-v-form-cli">
          <span class="label">昵称</span>
          <input type="text" placeholder="请您输入姓名" v-model="name" @input="updtName" class="c-z-t-name"/>
        <!--<span class="c-com-w">{{name}}</span>-->
        <!--<a class="updateBtn" @click.stop.prevent="updateName">修改昵称</a>-->
        <!--<img class="arrow-com" src="../../../assets/img/back.png">-->
      </div>
      <div class="p-v-form-cli" @click.stop.prevent="updatePhone">
        <span class="label">电话</span>
        <span class="c-com-w">{{mobile}}</span>
        <!--<a class="updateBtn" @click.stop.prevent="updatePhone">更换手机号码</a>-->
        <img class="arrow-com" src="../../../assets/img/back.png">
      </div>
      <div v-if="!disableM">
        <div class="p-v-form-cli" @click.stop.prevent="updateAur" >
          <span class="label">身份</span>
          <div class="s-a-box-db"><span>{{userTypeKey}}</span>
            <!--<a class="updateBtn" @click.stop.prevent="updateAur">修改身份</a>-->
          </div>
          <img class="arrow-com" src="../../../assets/img/back.png">
        </div>
      </div>
      <div class="z-edit-disable" v-if="disableM">
        <!--pushFlag显示出来-->
        <div class="p-v-form-cli">
          <span class="label">身份</span>
          <div class="s-a-box-db"><span>{{userTypeKey}}</span>
          </div>
          <img class="arrow-com" src="../../../assets/img/back.png">
        </div>
        <!--<div class="z-p-identity">-->
          <!--<p class="z-p-identity-text">请选择身份</p>-->
          <!--<ul class="clear z-ul">-->
            <!--<li v-for="item in itemId"-->
                <!--class="z-p-id-li-com"-->
            <!--&gt;-->
              <!--<span class="z-p-id-li-ct" :class="{activeL:item.sh}" :data-id="item.id" :data-path="item.path">{{item.keyword}}</span>-->
            <!--</li>-->
          <!--</ul>-->
        <!--</div>-->
      </div>
      <div class="z-p-identity" v-if="Identity.pIDF">
        <p class="z-p-identity-text">请选择身份</p>
        <ul class="clear z-ul">
          <li v-for="item in itemId"
              class="z-p-id-li-com"
          >
            <span class="z-p-id-li-ct" :class="{activeL:item.sh}" @click="selectID" :data-id="item.id" :data-path="item.path">{{item.keyword}}</span>
            <!--<span class="z-p-id-li-ct" :class="{activeL:item.sh}" :data-id="item.id" :data-path="item.path">{{item.keyword}}</span>-->
          </li>
        </ul>
      </div>
      <div class="z-p-warehouse" v-if="Identity.warehouse">
         <div v-if="ckFlag">
           <label for="warehouse" class="label">仓库名称</label>
           <input type="text" placeholder="填写仓库的名称" id="warehouse" v-model="organiza.name" class="c-pos-r">
         </div>
        <div v-if="!ckFlag">
          <label for="warehouse" class="label">仓库名称</label>
          <input type="text" placeholder="填写仓库的名称" id="warehouse1" disabled v-model="organiza.name" class="c-pos-r">
        </div>

      </div>
      <div class="z-p-storageCapacity" v-if="Identity.storage">
        <div v-if="!ckFlag">
          <label for="storage" class="label">仓库库容</label>
          <input type="text" placeholder="填写仓库的库容" id="storage" disabled v-model="organiza.storage" class="c-pos-r">
        </div>
        <div v-if="ckFlag">
          <label for="storage" class="label">仓库库容</label>
          <input type="text" placeholder="填写仓库的库容" id="storage1"  v-model="organiza.storage" class="c-pos-r">
        </div>
      </div>
      <div class="z-p-warehouse" v-if="coopF">
        <div v-if="!coopFlag">
          <label for="coop" class="label">合 作 社</label>
          <input type="text" placeholder="填写仓库的名称" id="coop" v-model="organiza.name" class="c-pos-r">
        </div>
        <div v-if="coopFlag">
          <label for="coop" class="label">合 作 社</label>
          <input type="text" placeholder="填写仓库的名称" id="coop1" v-model="organiza.name" disabled class="c-pos-r">
        </div>
      </div>



      <div class="p-v-form-cli c-p-sale" v-if="coldBG">
        <div v-if="!comRoleFlag">
          <span class="label">销售区域</span>
          <input type="text" placeholder="销售区域"  v-model="organiza.saleAddr" disabled class="c-pos-r">
        </div>
        <div v-if="comRoleFlag">
          <span class="label">销售区域</span>
          <input type="text" placeholder="销售区域"  v-model="organiza.saleAddr" class="c-pos-r">
        </div>
      </div>
      <!--<div @click.stop="saleArea" class="p-v-form-cli" v-if="!coldBG">-->
      <div class="p-v-form-cli c-p-sale" v-if="!coldBG && GNFlag">
        <div v-if="!comRoleFlag">
          <span class="label">销售区域</span>
          <input type="text" placeholder="销售区域"  v-model="organiza.saleAddr" disabled class="c-pos-r">
        </div>
        <div v-if="comRoleFlag">
          <span class="label">销售区域</span>
          <input type="text" placeholder="销售区域"  v-model="organiza.saleAddr" class="c-pos-r">
        </div>
      </div>
      <div v-if="!comRoleFlag" class="p-v-form-cli">
        <!--<div  v-if="coldBG">-->
        <span class="label">所在地区</span>
        <span class="c-sp-area">{{organiza.addr}}</span>
        <img class="arrow-com" src="../../../assets/img/back.png">
        <!--</div>-->
      </div>

      <div v-if="comRoleFlag" @click.stop="addSelect" class="p-v-form-cli">
        <!--<div  v-if="!coldBG">-->
        <span class="label">所在地区</span>
        <span class="c-sp-area">{{organiza.addr}}</span>
        <img class="arrow-com" src="../../../assets/img/back.png">
        <!--</div>-->
      </div>
      <div class="p-v-form-cli c-p-sale" v-if="coldBG">
        <div v-if="!comRoleFlag">
          <span class="label">详细地址</span>
          <input type="text" placeholder="详细地址" disabled v-model="organiza.addrDetail" class="c-pos-r">
        </div>
        <div v-if="comRoleFlag">
          <span class="label">详细地址</span>
          <input type="text" placeholder="详细地址" v-model="organiza.addrDetail" class="c-pos-r">
        </div>
      </div>
      <div class="p-v-form-cli c-p-sale" v-if="!coldBG ">
        <div v-if="!comRoleFlag">
          <span class="label">详细地址</span>
          <input type="text" placeholder="详细地址"  v-model="organiza.addrDetail" disabled class="c-pos-r">
        </div>
        <div v-if="comRoleFlag">
          <span class="label">详细地址</span>
          <input type="text" placeholder="详细地址"  v-model="organiza.addrDetail" class="c-pos-r">
        </div>
      </div>
      <div class="p-v-form-cli c-p-manage" v-if="coldBG">
        <div v-if="!comRoleFlag">
          <span class="label">主营品种</span>
          <input type="text" placeholder="主营品种"  v-model="organiza.pz" disabled class="c-pos-r">
        </div>
        <div v-if="comRoleFlag">
          <span class="label">主营品种</span>
          <input type="text" placeholder="主营品种"  v-model="organiza.pz" class="c-pos-r">
        </div>
      </div>
      <div class="p-v-form-cli c-p-manage" v-if="!coldBG">
        <div v-if="!comRoleFlag">
          <span class="label">主营品种</span>
          <input type="text" placeholder="主营品种"  v-model="organiza.pz" disabled class="c-pos-r">
        </div>
        <div v-if="comRoleFlag">
          <span class="label">主营品种</span>
          <input type="text" placeholder="主营品种"  v-model="organiza.pz" class="c-pos-r">
        </div>
      </div>
      <div v-if="comRoleFlag">
        <div class="z-p-description">
          <p class="z-p-text">实力描述:</p>
          <textarea placeholder="还没有实力描述，赶快填写吧，让更多用户关注你" ref="edit" v-model="organiza.remark" ></textarea>
        </div>
      </div>
      <div v-if="!comRoleFlag">
        <div class="z-p-description">
          <p class="z-p-text">实力描述:</p>
          <textarea placeholder="还没有实力描述，赶快填写吧，让更多用户关注你" ref="edit" v-model="organiza.remark" disabled ></textarea>
        </div>
      </div>
      <div class="c-z-space-RT"></div>
      <!--<div class="z-des-img">-->
        <!--<p class="z-des-img-text">最多能上传5张</p>-->
        <!--<ul class="clear">-->
          <!--<li :is="item.SS" v-for="item in items" :item="item" v-on:selecttype="delImage"></li>-->
          <!--<li class="z-add-upload">-->
            <!--<input type="file" accept="image/*" multiple @change="addImage">-->
          <!--</li>-->
        <!--</ul>-->
      <!--</div>-->
    </div>
    <div class="c-commit" @click="submit">
        提交
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

  import userImgUrl from '../../../assets/img/icon.png'

  const api = new API();
  var fn = {
    img:[],
    DB:[{id:"10000",path:0,keyword:"调果代办",sh:false},{id:"10000",path:1,keyword:"收果代办",sh:false}],
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
      var value = dt[0][0].imagePath;
      var name = dt[0][0].imageName;
      var obj = {
        SS:DEL,
//        srcM:dt[1].imagePath,
        src:value,
//        srcT:dt[2].imagePath,
        name:name,
        index:0
      };

      this.items.push(obj);

    },
    submit:function(data){
        if(data.succeed){
          sessionStorage.clear();
          var name = data.data.name;
          var level = data.data.userType;

          window.sessionStorage.setItem("newName",name);
          window.sessionStorage.setItem("level", level);

          this.$router.push({name:"pc"});
        }
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
        userDetailType:"",
        userDetailTypeKey:"",
        organiza:{
          saleAddr:'',
          addr:"",
          addrDetail:'',
          pz:'',
          name:'',
          storage:'',
          remark:''
        },
        items:[],
        itemId:null,
        Identity:{
          pIDF:false,
          warehouse:false,
          storage:false
        },
        coldBG:true,//冷库-仓库保管员
        coopF:false,
        recordImg:{},
        disableM:false,//默认为false,
        ckFlag:false,//默认冷库不可以修改
        coopFlag:false,//默认合作社不可以修改
        GNFlag:true,//默认果农不可以修改
        comRoleFlag:false,//默认共同角色不可以修改
        isFlag:true,//省份是否可以修改
      }
    },
    activated(){
        var flag = this.$route.query.Flag;
        if(!this.$store.state.addr && !this.$store.state.saleAddr){
          this.getUserInfo();
        }else{
            if(this.$store.state.nameN){
                this.name = this.$store.state.nameN;
            }
            if(this.$store.state.addr){
              this.organiza.addr = this.$store.state.addr;
            }
            if(this.$store.state.saleAddr){
              this.organiza.saleAddr = this.$store.state.saleAddr;
            }

        }
    },
    methods: {
        back(){
           this.reset();
          this.$router.push({name: 'pc'})
        },
        reset(){
            var self = this;
          self.Identity.pIDF = false;
          self.Identity.warehouse = false;
          self.Identity.storage = false;
          self.coldBG = false;
          self.coopF = false;
          self.userDetailType = "";
          self.ckFlag = false;
          self.coopFlag = false;
          self.comRoleFlag = false;
          self.GNFlag = true;
        },
      updtName(){
            var name = this.name + "";
            var num = name.length;
            if(num > 15){
                this.name = name.substring(0, 15);
            }
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

           if(value == "保管"){
               this.userDetailType = "LK_BG";
               this.coldBG = false;
           }else{
             this.userDetailType = "LK_LB";
             this.coldBG = false;
           }
        }else if(id == "10000"){

          if(value == "调果代办"){
            this.userDetailType = "DB_DG";
          }else{
            this.userDetailType = "DB_SG";
          }
        }

      },
      initCold(){
          this.coldBG = false;
      },
      updateName(){

        MessageBox.prompt('请输入新的姓名').then(({value, action}) => {
          var self = this;
          var dl = {
            name: value
          };
          this.$store.state.nameN = value;
          self.name = value;
//          self.setUserInfo(dl, value, function () {
//            self.name = value;
//            self.$store.commit("incrementName", {'name': value});//用户名
//            c_js.setLocalValue('name',value);
//          });
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
        var self = this;
        MessageBox.prompt('如xx村xx街道xx号','请输入详细地址').then(({value, action}) => {

          var dl = {
            addressDetail: value
          };
          self.organiza.addrDetail = value;
//          self.setUserInfo(dl, value, function () {
//            self.addrDetail = value;
//          });
        });
      },
      updatezySort(){
        MessageBox.prompt('如红富士80#一二级，红富士70#一二级','请输入主营品种').then(({value, action}) => {
          var self = this;
          var dl = {
            mainOperating: value
          };
          self.organiza.pz = value;
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
        var type = db.aurKey;
        this.userType = db.aurKey;
        this.userTypeKey = db.aurName;

        if( self.isFlag  == "ORG_CHILD_ACCOUNT"){
          this.comRoleFlag = false;
          this.ckFlag = false;
        }else{
          this.comRoleFlag = true;
          this.ckFlag = true;
        }

        if(type == "LK"){
          self.itemId = fn.LK;
            self.Identity.pIDF = true;
            self.Identity.warehouse = true;
            self.Identity.storage = true;
            this.coldBG = true;
          this.coopF = false;
//          this.ckFlag = false;
        }else if(type == "DB"){
          self.itemId = fn.DB;

          self.Identity.pIDF = true;
          self.Identity.warehouse = false;
          self.Identity.storage = false;
          this.coopF = false;
          this.initCold();
        }else{
            var flag = false;
            if(this.comRoleFlag){
              flag = true;
            }else{
                flag = false;
            }
          self.reset();
          this.initCold();

          if(type == "HZS"){
              this.coopF = true;
              if(flag){
                self.comRoleFlag = true;
              }
          }else if(type == "ZZH"){
            self.GNFlag = false ;
            if(flag){
              self.comRoleFlag = true;
            }
          }else if(type == "KS"){
            if(flag){
              self.comRoleFlag = true;
            }
          }
        }

      },
      addSelect(){
        this.$router.push({name: 'addressInfo',query:{flag:"addr"}});
      },
      saleArea(){
        this.$router.push({name: 'addressInfo',query:{flag:"saleAddr"}});
      },
      getUserInfo(){
        const self = this;

        let params = {
          api:"/yg-user-service/user/findSelfInfo.apec",
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              var data = item.data;

              self.addr = data.address || '请修改地区';
              self.addrDetail = data.addressDetail || '请修改详细地址';
              self.zySort = data.mainOperating || '请修改主营品种';
              self.workYear = data.workOfYear || '请修改工作年限';
              self.name = data.name;
              self.imgUrl = data.imgUrl || userImgUrl;

              self.userTypeKey = data.userTypeKey || this.$store.state.userTypeKey || c_js.getLocalValue('userTypeKey');

              self.userType = data.userType;
              self.mobile = data.mobile;
              if(data.userOrgClientVO){
                self.organiza.addr = data.userOrgClientVO.address;
                self.organiza.saleAddr = data.userOrgClientVO.saleAddress;
                self.organiza.addrDetail = data.userOrgClientVO.addressDetail;
                self.organiza.pz = data.userOrgClientVO.mainOperating;
                self.organiza.name = data.userOrgClientVO.orgName;
                self.organiza.storage = data.userOrgClientVO.orgStockCap;
                self.organiza.remark = data.userOrgClientVO.remark;

                var arr = [];
                data.userOrgClientVO.userOrgImageVOS.forEach(function (current,index) {
                    var name = "";
                  var obj = {
                    SS:DEL,
                    src:current.imageUrl,
                    name:current.createDate,
                    index:index
                  };

                  arr.push(obj);
                })
                self.items = arr;

                if(data.userOrgClientVO.pushFlag){
                    //用户不能修改自己的身份
                    self.disableM = true;
                }
              }

              var userDetailType = data.userDetailType;
              self.isFlag =  data.userDetailType;
              if(data.userAccountType == "ORG_CHILD_ACCOUNT"){
                   this.comRoleFlag = false;
                   this.ckFlag = false;
              }else{
                this.comRoleFlag = true;
                this.ckFlag = true;
              }
                self.coopFlag = false;
                self.GNFlag = true;
              if(self.userTypeKey == "代办"){
                fn.DB.forEach(function (current,index) {
                  current.sh = false;
                })
                self.Identity.pIDF = true;
                self.Identity.warehouse = false;
                self.Identity.storage = false;
                self.initCold();
                if(userDetailType == "DB_DG"){
//                   调果代办
                   fn.DB[0].sh = true;
                  self.userDetailType = userDetailType;
                }else if(userDetailType == "DB_SG"){
                  fn.DB[1].sh = true;
                  self.userDetailType = userDetailType;
                }
                self.itemId = fn.DB;
                return;
              }else if(self.userTypeKey == "冷库"){
                fn.LK.forEach(function (current,index) {
                  current.sh = false;
                })
                self.Identity.pIDF = true;
                self.Identity.warehouse = true;
                self.Identity.storage = true;
                this.initCold();
                if(userDetailType == "LK_LB"){
                  fn.LK[0].sh = true;
                  self.userDetailType = userDetailType;
                }else if(userDetailType == "LK_BG"){
                    this.coldBG = true;
                  fn.LK[1].sh = true;
                  self.userDetailType = userDetailType;
                }
                self.itemId =fn.LK ;
                return;
              }else if(self.userTypeKey == "合作社"){
                  self.coopFlag == true;
                this.initCold();
              }else if(self.userTypeKey == "果农"){
                self.GNFlag = false ;
                this.initCold();
              }
              else{
                  this.initCold();
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
      delImage(data){
         var name = data.name;
         this.items.forEach(function (current, index) {
           if(current.name == name){
               this.items.splice(index, 1);
           }else{
               return;
           }
         });

      },
      addImage(e){
        var e = e || window.event;
        var target = e.toElement || e.srcElement;
        var files = target.files;
        var size = files.size/(1000*1000);
        if(size > 10){
          Toast({
            message:"对不起，您上传的图片过大，请您重新上传图片",
            duration:1000
          })
          return;
        }
//        var name = file.name;
//        var nameA = name.split(".");
//        var n = encodeURI(nameA[0]);
//        var obj =this.recordImg ;
//        if(obj.hasOwnProperty(n)){
//          return;
//        }

        if(this.items.length > 5){
            return;
        }
        var fd = new FormData();
        fd.append("file",files[0]);
        let params = {
          api:"/common/uploadImg.apec",
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
      submit(){
        var el = this.$refs.edit;
        var remark = el.value;
        var self = this;
        if(self.userTypeKey == "代办"){
          if(self.name == ""){
            Toast("对不起，请您填写用户名称");
            return;
          }

          if(self.userDetailType == ""){
            Toast("对不起，请您选择身份");
            return;
          }
        }else if(self.userTypeKey == "冷库"){
            if(self.name == ""){
              Toast("对不起，请您填写用户名称");
              return;
            }
          console.log(self.userDetailType);
          if(self.userDetailType == ""){
            Toast("对不起，请您选择身份");
            return;
          }
        }

        var arrT = [];
        this.items.forEach(function (current, index) {
          var obj = {};
          obj.imageUrl = current.src;
          arrT.push(obj)
        });

          var data = {
            name:self.name,//用户名称
            userType:self.userType,//用户身份类型
            userDetailType:self.userDetailType,//用户的详细身份
            userOrgClientVO:{
              remark:remark,//用户评价
              address:self.organiza.addr,//所在区域
              saleAddress:self.organiza.saleAddr,//销售区域
              addressDetail:self.organiza.addrDetail,//详细地址
              mainOperating:self.organiza.pz,//主营品种
              orgStockCap:self.organiza.storage,//库容量
              orgName:self.organiza.name,//库容名称
              userOrgImageVOS:arrT
            }
          };

        let params = {
          api: "/yg-user-service/user/updateUserInfo.apec",
          data: data
        }

        this.post(params, fn.submit.bind(this));
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

    created() {
      this.$root.eventHub.$on('aurUpdate.confirm', this.updateAurCall)
    },

    components: {
//      split,
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
      border-bottom (1/_rem) solid #f4f4f4
      position relative
      .p-v-info-main-F
        position relative
        line-height (90 / _rem)
        .p-v-info-person
          height (20 / _rem)
          font-size (16/_rem)
          color #323232
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
      border-bottom (1/_rem) solid #f4f4f4;
      .arrow
        width (10 /_rem)
        height (10 /_rem)
        position absolute
        right (20 /_rem)
        top 50%
        transform translateY(-50%)
      .s-a-box-db
        width (60 /_rem)
        border 1px solid #2fcaa7
        border-radius 4px
        text-align center
        display inline-block
        cursor pointer
        height (20 /_rem)
        line-height (20 /_rem)
        font-size 0
        color #2fcaa7
        margin-left (30 /_rem)
      .updateBtn
        position absolute
        top 50%
        transform translateY(-50%)
        right (24 /_rem)
        font-size (15 /_rem)
        color #2fcaa7
      .label
        color #323232
      .value
        color #111111
        margin-left (10 /_rem)
        display inline-block
        width (250/_rem)
      span
        font-size (16 /_rem)

</style>
