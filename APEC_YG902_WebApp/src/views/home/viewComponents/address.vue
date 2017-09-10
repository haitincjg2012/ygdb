<template>
  <div class="address-info-page-F">
    <top-bar title="选择地区"></top-bar>
    <scroller ref="my_scroller" :style="styleCal">
      <div class="main-page">
        <div class="z-history-address">
          <p class="z-history-r">历史浏览记录</p>
          <div v-for="item in historyArr" class="z-history-text" @click = "historyRecord" :cityId="item.cityId" :areaId="item.areaId" :townId="item.townId" :detailAddress = "item.detailAddress">
            <div class="z-history-name" :cityId="item.cityId" :areaId="item.areaId" :townId="item.townId" :detailAddress = "item.detailAddress">
              <p :cityId="item.cityId" :areaId="item.areaId" :townId="item.townId" :detailAddress = "item.detailAddress">{{item.detailAddress}}</p>
            </div>
          </div>
        </div>
        <div class="p-ad-form-cli">
          <div class="addLabel"><span>请选择市</span></div>
          <div @click.stop="selTopAddr($event,item)" v-for="item in addressTopList" :class="item.activeCls"><span>{{item.name}}</span></div>
        </div>
        <div class="p-ad-form-cli">
          <div class="addLabel"><span>请选择县区</span></div>
          <div @click.stop="selSecAddr($event,item)" v-for="item in addressSecList" :class="item.activeCls"><span>{{item.name}}</span></div>
        </div>
        <div class="p-ad-form-cli">
          <div class="addLabel"><span>请选择镇/街道</span></div>
          <div @click.stop="selThirAddr($event,item)" v-for="item in addressThirList" :class="item.activeCls"><span>{{item.name}}</span></div>
        </div>
        <div class="login-btn">
          <input class="btn-login-c login-confirm" type="submit" id="btn-login-code" value="保存地址" @click="saveBtn"></input>
        </div>
      </div>
    </scroller>
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
        countyName:'',
        cityName:'',
        countryName:'',
        styleCal:'',
        historyArr:null

      }
    },
    mounted(){
      this.gettopAddressList();
    },

    methods: {
      _initScroll(){
        const self = this;
        self.$nextTick(function () {
          var winHeight = window.innerHeight;
          var mainHeight = winHeight-50;
          self.styleCal='top:42px;height:'+mainHeight+'px';
        })
      },
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
                  self.fircityaddrCode = item.code;
                  self.city = item.code;//缓存市code
                  self.cityName = item.name;
                  self.province = item.parentId; //缓存省code
                }
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
            parentId:code
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
                });
                if(index == 0){
                  self.county = item.code;//缓存县code
                  self.countyName = item.name;
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
              console.log(error);
              Indicator.close();
            }
          )
        } catch (error) {
          console.log(error);
          Indicator.close();
        }
      },
      getthirAddrList(code){
        if(!code){
          Indicator.close();
          return;
        }
        const self = this;
        self.countryName = '';
        self.country = '';
        let params = {
          api: "/yg-systemConfig-service/regionLevel/listRegionLevel.apec",
          data: {
            parentId:code
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
                if(index ==0){
                  self.countryName = item.name;
                  self.country = item.code;
                }

              });
            } else {
            }
            self._initScroll();
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
        self.city = item.code;
        self.cityName = item.name;
        self.getsecAddrList(item.code);
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
        self.countyName = item.name;
        self.getthirAddrList(item.code);
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
        });
        self.country = item.code;
        self.countryName = item.name;
      },
      saveBtn(){
        const self = this;
        var data={
          cityId:self.city,
          countyId:self.county,
          townId:self.country,
          countyName:self.countyName,
          cityName:self.cityName,
          townName:self.countryName
        };
        var param = self.cityName +self.countyName+self.countryName;
        var obj = {
          cityId:self.city,
          areaId:self.county,
          townId:self.country,
          detailAddress:param
        }
        try {
          var flag = this.$store.state.address;
          if(flag == 1){
            //发布供应的消息
            this.$store.state.address = 0;
            this.$store.state.addrData = obj;
          }else if(flag == 2){
            //发布求购的消息
            this.$store.state.addrData = obj;
            this.$store.state.address = 0;
          }else{
            self.$store.commit("incrementUploadAddr", {'uploadAddr': data});
          }


          let params = {
              api:"/_node_user/_save_address.apno",
              data:{
                "detailAddress":obj
              }
          }

          try {
            api.post(params).then((res) =>{
              var dt = res.data;
               console.log(dt);
            }).catch((error) =>{
                console.log(error)
                Indicator.close();
              }
            )
          } catch (error) {
            console.log(error)
            Indicator.close();
          }


          Toast({
            message:"地址保存成功~",
            duration:1000
          })
          this.$router.go(-1);
        } catch (error) {
          Toast();
          Toast({
            message:"地址保存失败~",
            duration:1000
          })
          this.$router.go(-1);
          console.log(error)
        }
      },
      historyAdd(){
          var self = this;
          let params = {
              api:"_node_user/_address_his.apno",
              data:{}
          }
        try {
          api.post(params).then((res) =>{
            var dt = res.data;
            var address = dt.data.address;
            var obj = {
              cityId:address.cityId,
              areaId:address.areaId,
              townId:address.townId,
              detailAddress:address.detailAddress
            }
            var arr = [];
            arr.push(obj);
            self.historyArr = arr;
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
      historyRecord(evt){
          var e = evt || window.event;
          var target = e.toElement || e.srcElement;
          var cityId = "", countyId = '', townId = '',detailAddress = '';
          cityId = target.getAttribute("cityId");
        countyId= target.getAttribute("countyId");
        townId= target.getAttribute("townId");
        detailAddress= target.getAttribute("detailAddress");
        var self = this;
        var data={
          cityId:cityId,
          countyId:countyId,
          townId:townId,
          detailAddress:detailAddress
        };
        this.$store.state.addrData = data;
        this.$router.go(-1);
      }
    },
    activated(){
     this.historyAdd();
   },
    created() {
    },

    components: {
      topBar
    }
  }
</script>
<style scoped>
  @import "../../../assets/css/address.css";
</style>
<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .address-info-page-F
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    .p-ad-form-cli
      padding (15 /_rem)
      position relative
      .addLabel
        height (25/_rem)
        line-height (25/_rem)
        text-align center
        margin-bottom (10/_rem)
        span
          font-size (18/_rem)
      .m-v-tz:not(:first-child)
        margin-left (15/_rem)
        margin-bottom (10/_rem)
      .m-v-tz
        min-width (70 /_rem)
        background-color #929292
        border-radius 4px
        text-align center
        display inline-block
        cursor pointer
        height (25 /_rem)
        line-height (25 /_rem)
        font-size 0
        color #fff
        span
          font-size (14 /_rem)
      .active
        background-color #28cba7!important
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
        border: 1px solid #0bbe06;
</style>
