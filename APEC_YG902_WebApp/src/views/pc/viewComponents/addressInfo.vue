<template>
  <div class="address-info-page">
    <top-bar title="选择地区"></top-bar>
    <div class="main-page">
      <div class="p-ad-form-cli">
        <div class="addLabel"><span>请选择市</span></div>
        <div @click.stop="selTopAddr($event,item)" v-for="item in addressTopList" :class="item.activeCls"><span>{{item.name}}</span></div>
      </div>
      <div class="p-ad-form-cli" v-if="!otherCity">
        <div class="addLabel"><span>请选择县区</span></div>
        <div @click.stop="selSecAddr($event,item)" v-for="item in addressSecList" :class="item.activeCls"><span>{{item.name}}</span></div>
      </div>
      <div class="p-ad-form-cli" v-if="!otherCity">
      <div class="addLabel"><span>请选择镇/街道</span></div>
      <div @click.stop="selThirAddr($event,item)" v-for="item in addressThirList" :class="item.activeCls"><span>{{item.name}}</span></div>
      </div>
      <div v-if="otherCity" class="c-address-edit">
        <input type="text" placeholder="请您填写所在地址" v-model="addressEdit" class="c-address-edit-input" maxlength="20">
      </div>
      <div class="login-btn">
        <input class="btn-login-c login-confirm" type="submit" id="btn-login-code" value="保存地址" @click="saveBtn"></input>
      </div>
    </div>
  </div>
</template>

<script>
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import keyBoard from '../../../components/keyboard/keyboard'
  import {Indicator,Toast} from 'mint-ui';


  const api = new API();

  export default {

    data() {
      return {
          addressTopList:[],//一级市
          addressSecList:[],//二级市县
          addressThirList:[],//三级镇
          fircityaddrCode:'',//序号为0 的第一级地址数组的code 默认为烟台市
          fircountyaddrCode:'',//序号为0 的第一级地址数组的code 默认为烟台市
          province:'',//省
          county:'', //县
          city:'',//市
          country:'',//镇
        countyS:'', //县
        cityS:'',//市
        countryS:'',//镇
        otherCity:false,//外地客商的选择
        addressEdit:'',//外地客商填写的地址
      }
    },
    mounted(){
        this.gettopAddressList();
    },

    methods: {
      gettopAddressList(){//获取一级市
        const self = this;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-systemConfig-service/regionLevel/listRegionLevel.apec",
          data: {
          }
        }
        try {
          api.post(params).then((res) =>{
            var item = res.data;
            var flag = false;
          if (item.succeed) {
            item.data.forEach((item,index) =>{
              self.addressTopList.push({
              "code": item.code,
              "name": item.name,
               "parentId":item.parentId,
              "activeCls": index ==0 ? "m-v-tz active" : 'm-v-tz',
              "id": item.id
            });

              if(index == 0){
                self.cityS = item.name;
                self.fircityaddrCode = item.code;
                self.city = item.code;//缓存市code
                self.province = item.parentId; //缓存省code
              }
          });
            self.addressTopList.push({
              "code": "9999",
              "name": "其他",
              "parentId":"10000",
              "activeCls": 'm-v-tz',
              "id": "100010"
            });
            flag = true;
          } else {
          }
          return {"flag":flag,"code":self.fircityaddrCode};
          }).then((res)=>{
              if(res.flag){
                  self.getsecAddrList(res.code)
              }else{
                Indicator.close();
                Toast("请求失败:地区选择失败，请稍后重试");
              }
          }).catch((error) =>{
            console.log(error)
            Indicator.close();
          }
        )
        } catch (error) {
          console.log(error)
          Indicator.close();
        }
      },
      getsecAddrList(code){
        if(!code)
        {
          Indicator.close();
          return;
        }
        const self = this;
        let params = {
          api: "/yg-systemConfig-service/regionLevel/listRegionLevel.apec",
          data: {
            parentId:code,
          }
        }
        try {
          api.post(params).then((res) =>{
            var item = res.data;
            var flag = false;
            if (item.succeed) {
                self.addressSecList = [];
              item.data.forEach((item,index) =>{
                self.addressSecList.push({
                  "code": item.code,
                  "name": item.name,
                  "parentId":item.parentId,
                  "activeCls": index ==0 ? "m-v-tz active" : 'm-v-tz',
                  "id": item.id
                })
                if(index == 0){
                  self.countyS = item.name;
                  self.county = item.code;//缓存县code
                  self.fircountyaddrCode = item.code;
                }

              });
              flag = true;
            } else {
            }
            return {"flag":flag,"code":self.fircountyaddrCode};
          }).then((res)=>{
            if(res.flag){
              self.getthirAddrList(res.code)
            }else{
              Indicator.close();
              Toast("请求失败:县选择失败，请稍后重试");
            }
          }).catch((error) =>{
              console.log(error)
            Indicator.close();
            }
          )
        } catch (error) {
          console.log(error)
          Indicator.close();
        }
      },
      getthirAddrList(code){
        if(!code){
          Indicator.close();
          return;
        }
        const self = this;
        let params = {
          api: "/yg-systemConfig-service/regionLevel/listRegionLevel.apec",
          data: {
            parentId:code,
          }
        }
        try {
          api.post(params).then((res) =>{
            var item = res.data;
            if (item.succeed) {
              self.addressThirList=[];
              item.data.forEach((item,index) =>{
                self.addressThirList.push({
                  "code": item.code,
                  "name": item.name,
                  "parentId":item.parentId,
                  "activeCls": index ==0 ? "m-v-tz active" : 'm-v-tz',
                  "id": item.id
                })
                if(index ==0)
                {
                  self.countryS = item.name;
                  self.country = item.code;
                }

              });

              if(item.data.length <= 0){
                self.addressThirList = null;
              }
            } else {
            }
            Indicator.close();
          }).catch((error) =>{
              console.log(error)
            Indicator.close();
            }
          )
        } catch (error) {
          console.log(error)
          Indicator.close();
        }
      },
      selTopAddr(e,item){
        const self = this;

        self.addressTopList.forEach((i) => {
          if (item.id === i.id) {
            i.activeCls = 'm-v-tz active';
            self.aur = item.aur;
          }
          else
            i.activeCls = 'm-v-tz'
        });
        if(item.name == "其他"){
            self.addressSecList = null;
            self.addressThirList = null;
            self.otherCity = true;
            return;
        }
        self.otherCity = false;
        self.city = item.code;
        self.cityS = item.name;
        self.getsecAddrList(item.code, item.name);
      },
      selSecAddr(e,item){
        const self = this;
        self.addressSecList.forEach((i) => {
          if (item.id === i.id) {
            i.activeCls = 'm-v-tz active';
            self.aur = item.aur;
          }
          else
            i.activeCls = 'm-v-tz'
        });
        self.county = item.code;
        self.countyS = item.name;

        self.getthirAddrList(item.code, item.name);
      },
      selThirAddr(e,item){
        const self = this;
        self.addressThirList.forEach((i) => {
          if (item.id === i.id) {
            i.activeCls = 'm-v-tz active';
            self.aur = item.aur;
          }
          else
            i.activeCls = 'm-v-tz'
        })
        self.country = item.code;
        self.countryS = item.name;

      },
      saveBtn(){
        const self = this;
//        var address = self.cityS + self.countyS + self.countryS;
        if(self.otherCity){
          var address = self.addressEdit;
        }else{
          var address = self.cityS + self.countyS + self.countryS;
          console.log(address, 8888888);
        }

//        let params = {
//          api: "/yg-user-service/user/updateUserInfo.apec",
//          data: data
//        }
         var flag = this.$route.query.flag;
         if(flag == "addr"){
           this.$store.state.addr = address;
         }else if(flag == "saleAddr"){
           this.$store.state.saleAddr = address;
         }

        this.$router.push({name:"pcInfo",query:{Flag:1}});
//        try {
//          api.post(params).then((res) =>{
//            var item = res.data;
//            if (item.succeed) {
//              Toast("地址保存成功~");
//              this.$router.go(-1);
//            } else {
//              Toast("地址保存失败，请稍后重试");
//              this.$router.go(-1);
//            }
//          }).catch((error) =>{
//              console.log(error)
//            }
//          )
//        } catch (error) {
//          console.log(error)
//        }
      }
    },

    created() {
    },

    components: {
      topBar
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .address-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    .p-ad-form-cli
      padding (15 /_rem) (15 /_rem) 0
      position relative
      .addLabel
        height (25/_rem)
        line-height (25/_rem)
        margin-bottom (10/_rem)
        span
          font-size (16/_rem)
      .m-v-tz:not(:first-child)
        margin-left (15/_rem)
        margin-bottom (10/_rem)
      .m-v-tz
        width (70 /_rem)
        background-color #f1f2f6
        border-radius 4px
        text-align center
        display inline-block
        cursor pointer
        height (25 /_rem)
        line-height (25 /_rem)
        font-size 0

        span
          font-size (14 /_rem)
      .active
        background-color #28cba7!important
        color #fff
      span
        font-size (14 /_rem)
    .login-btn
      margin (25 /_rem) (15 /_rem) 0 (15 /_rem)
      .btn-login-c
        color #ffffff
        line-height (15 /_rem)
        height (40 /_rem)
        font-size (15 /_rem)

        border-radius: 0;
        display: inline-block;
        width: 100%;

      .login-confirm
        background-color: #28CBA7;
        color #FFFFFF;
        border-radius (5/_rem)
    .c-address-edit
      padding (15 /_rem) (15 /_rem) 0
      position relative
      .c-address-edit-input
        width (340/_rem)
        height (40/_rem)
        line-height (40/_rem)
        font-size (16/_rem)
        color #323232
        background-color transparent
</style>
