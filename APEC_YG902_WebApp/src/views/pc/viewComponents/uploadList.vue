<template>
  <div class="uploadList-info-page">
      <top-bar title="上传单据" v-on:rightBtn="_goSelGoods" :menuLeft="menuLeft"></top-bar>
      <scroller ref="my_scroller" :style="styleCal">
      <div class="main-page">
        <div class="u-v-form-cli">
          <div class="sell-l"><span>卖货方</span></div>
          <div class="sell-f">
            <div @click.stop="addrSel" class="sell-f-sz">
              <span class="sell-f-sz-shi">{{cityName}}</span>
              <span class="sell-f-sz-xian">{{countyName}}</span>
              <span class="sell-f-sz-zhen">{{townName}}</span>
              <img class="arrow" src="../../../assets/img/Right_Arrow_Icon.png">
            </div>
            <div class="sell-f-chc">

                <input v-model="shipWarehouse" type="text" placeholder="请填写出货仓库">
                <!--<span>123</span>-->
                <!--<span  v-for="item in dataAur" @click.stop="AurSelect($event,item)" :class="item.class"-->
                <!--&gt;{{item.name}}</span>-->


              <div v-for="item in dataAur" @click.stop="AurSelect($event,item)" :class="item.class">
                <span>{{item.name}}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="u-v-form-cli">
          <div class="sell-l"><span>买货方</span></div>
          <div class="sell-f">
            <div class="buy-f-chc">
              <input v-model="saleMarket" type="text" placeholder="请填写销货市场">
            </div>
            <div class="buy-f-name">
              <input v-model="name" type="text" placeholder="请填写买方姓名">
            </div>
          </div>
        </div>
        <split></split>
        <div class="u-v-form-time">
          <span class="time-select">交收时间：</span>
          <input type="date" v-model="deliveryTime" placeholder="请选择交收时间">
        </div>
        <split></split>
        <div v-for="item in goodsList" class="u-v-good-form">
          <div class="u-v-lab-form">
            <span class="u-v-c-lab">当前商品</span>
            <span v-html="item.skuName" class="u-v-c-good"></span>
            <a style="color: red;float: right;" @click.stop.prevent="_delGood($event,item)">删除</a>
          </div>
          <ul class="u-v-lab-zw">
            <li>
              <div class="num">数量(斤)</div>
              <div class="inputVal"><input v-model="item.number" placeholder="请填写数量" @input="amountQ($event,item)" maxlength="5"/></div>
              <!--<div class="inputVal"><input v-model="item.number" type="number" placeholder="请填写数量" @input="amountQ($event, item)" maxlength="5"></div>-->
            </li>
            <li>
              <div class="num">单价(元/斤)</div>
              <div class="inputVal"><input v-model="item.amount" placeholder="请填写单价" @input="priceU($event,item)" maxlength="5"></div>
              <!--<div class="inputVal"><input v-model="item.amount" type="number" placeholder="请填写单价" @input="priceU($event,item)" maxlength="5"></div>-->
            </li>
            <li>
              <div class="num">金额(元)</div>
              <div class="inputVal"><input v-model="item.totalAmount" type="number" placeholder="总金额" disabled></div>
            </li>
          </ul>
        </div>
        <!--<div style="margin:10px auto;width:50%;color: #ffffff;height: 30px;line-height: 30px;text-align: center;background-color:rgba(0,0,0,.86);opacity:0.5;font-size: 0.7rem;"><i class="fa fa-exclamation-triangle" aria-hidden="true"></i><span>请填写数据</span></div>-->
        <div v-show="_btnshow" class="login-btn">
          <input class="btn-login-c login-confirm" type="submit" id="btn-login-code" value="提交单据"
                 @click="confirmBtn"></input>
        </div>
      </div>
      </scroller>
  </div>
</template>

<script>
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import keyBoard from '../../../components/keyboard/keyboard'
  import {MessageBox, Toast, Indicator} from 'mint-ui';

  const api = new API();

  export default {
    data() {
      return {
        warnInfo:[],
        goodInfo: {
          'skuName': '',
          'skuId': '',
          'number': '',
          'amount': '',
          'totalAmount': ''
        },
        number: '',//数量
        amount: '',//单价
        totalAmount: '',//总价
        cityId: '',//城市id
        countyId: '',//县ID
        townId: '',//镇id
        cityName: '市',//城市
        countyName: '县',//县
        townName: '镇',//镇
        styleCal:'',

        shipWarehouse: '',//出货仓库
        saleMarket: '',//售货市场
        name: '',//买方姓名
        deliveryTime: '',//
        voucherUrl: '',//凭据图片路径
        voucherGoodsVO: '',//上传凭据商品信息
        goodsList: [],//商品集合
        goodCom: '',//用于显示的商品
        menuLeft: '新增单据',
        dataAur: [
          {
            aur: 'KS',
            name: '客商',
            class: 's-a-box-db visited'
          },
          {
            aur: 'ZZH',
            name: '果农',
            class: 's-a-box-db'
          }
        ],
        type: 'KS',//Y	KS:客商；ZZH:果农	卖货方类型
        mm:''
      }
    },
    mounted(){
        this._initScroll();
    },
    activated(){
    },
    computed: {
      _btnshow(){
        return this.goodsList.length && this.cityId && this.countyId && this.townId && this.shipWarehouse && this.saleMarket && this.name && this.deliveryTime
      }
    },
    methods: {
      confirmBtn(){
        const self = this;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        var data = {
          'cityId': self.cityId,
          'countyId': self.countyId,
          'townId': self.townId,
          'type': self.type,
          'shipWarehouse': self.shipWarehouse,
          'saleMarket': self.saleMarket,
          'name': self.name,
          'deliveryTime': self.deliveryTime,
          'voucherGoodsVO': self.goodsList
        };
        var formData = new FormData();
        data = JSON.stringify(data);
        formData.append(data,'');
        formData.append('voucherUrl', '');

        let params = {
          api: "/yg-voucher-service/voucher/addVoucherInfo/uploadPicture.apec",
          data: formData
        };
        try {
          api.postImg(params).then((res)=>{
            var item = res.data;
          if (item.succeed) {
            Toast("提交单据成功~");
            self.$router.push({name:'pc'});
          } else {
            Toast(item.errorMsg);
          }
          Indicator.close();
        }).catch((error)=>{
            console.log(error)
          Indicator.close();
        }
        )
        } catch (error) {
          console.log(error)
          Indicator.close();
        }
      },
      _delGood(e, item){
        const self = this;
        var i = self.goodsList.findIndex((i)=>
        {
          return item.skuId == i.skuId
        }
      );
        if (i > -1) {
          self.goodsList.splice(i, 1);
        }
      },
      _goSelGoods(){
        this.$router.push({name: 'billList'})
      },
      addrSel(){
        this.$router.push({name: 'addressSel'})
      },
      AurSelect(event, item){
        const self = this;
        self.dataAur.forEach((i)=>
        {
          if (item.aur === i.aur) {
            i.class = 's-a-box-db visited'
            self.type = item.aur;
          }
          else
            i.class = 's-a-box-db'
        }
      )
      },
      addSelect(){
        this.$router.push({name: 'addressInfo'});
      },
      setUserInfo(item, value, fn){
        const self = this;
        let params = {
          api: "/yg-user-service/user/updateUserInfo.apec",
          data: item
        }
        try {
          api.post(params).then((res)=>
          {
            var item = res.data;
            if (item.succeed) {
              Toast("修改成功~");
              fn();
            } else {
              Toast("修改失败，请稍后重试");
            }
          }
        ).
          catch((error)=>
          {
          }
        )
        } catch (error) {
          console.log(error)
        }
      },
      _initScroll(){
        const self = this;
        self.$nextTick(function () {
          var winHeight = window.innerHeight;
          var mainHeight = winHeight-50;
          self.styleCal='top:42px;height:'+mainHeight+'px';
        })
      },
      amountQ(e, item){
//          var e = e || window.event;
//          var target = e.srcElement || e.toElement;
//          var val = target.value.toString();
//          var val = item.number + "";
        var value = item.number;
        var len = value.length;
        var pattern =/^([1-9]*(\.)?\d*)$/;
        var pattern1 = /^[0-9]$/;
        var pattern2 = /^0(\.)\d*$/;
        var flag;
        if(len == 1){
          flag = pattern1.test(value)
        }else{
          if(value[0] == 0){
            flag = pattern2.test(value)
          }else{
            flag = pattern.test(value)
          }
        }

        if(!flag){
          item.number= value.slice(0, -1);
        }

        if(item.amount != ""){
            item.totalAmount = item.number * item.amount;
        }

      },
      priceU(e,item){
        var value = item.amount;
        var len = value.length;
        var pattern =/^([1-9]*(\.)?\d*)$/;
        var pattern1 = /^[0-9]$/;
        var pattern2 = /^0(\.)\d*$/;
        var flag;
        if(len == 1){
          flag = pattern1.test(value)
        }else{
          if(value[0] == 0){
            flag = pattern2.test(value)
          }else{
            flag = pattern.test(value)
          }
        }

        if(!flag){
          item.amount= value.slice(0, -1);
        }

        if(item.number != ""){
          item.totalAmount = item.number * item.amount;
        }
      }
    },

    created() {
    },
    beforeRouteEnter(to, from, next) {
      next(vm=>
      {
        if (from.name === "addressSel") {
          var data = vm.$store.state.uploadAddr;
          if (data.cityId) {
            vm.cityId = data.cityId || '';
            vm.countyId = data.countyId || '';
            vm.townId = data.townId || '';
            vm.cityName = data.cityName || '';
            vm.countyName = data.countyName || '';
            vm.townName = data.townName || '';
            vm.$store.commit("incrementUploadAddr", {'uploadAddr': {}});
          }
        }
        if (from.name === "billList") {
          var data = vm.$store.state.uploadGoodList;
          if (data.attrValueId) {
            vm.goodInfo['skuName'] = data.attrValue;
            vm.goodInfo['skuId'] = data.attrValueId;
            vm.goodsList.push(vm.goodInfo);
            vm.$store.commit("incrementuploadGoodList", {'uploadGoodList': {}});
          }
          vm.goodInfo= {
            'skuName': '',
              'skuId': '',
              'number': '',
              'amount': '',
              'totalAmount': ''
          };
        }
        if(from.name !== "billList" && from.name !== "addressSel"){
          vm.shipWarehouse = '';//出货仓库
          vm.saleMarket = '';//售货市场
          vm.name = '';//买方姓名
          vm.deliveryTime = '';//
          vm.voucherUrl = '';//凭据图片路径
          vm.voucherGoodsVO = '';//上传凭据商品信息
          vm.goodsList = [];
          vm.cityId = '';
          vm.countyId = '';
          vm.townId =  '';
          vm.cityName =  '市';
          vm.countyName = '县';
          vm.townName =  '镇';
        }
      }
    )
    },

    components: {
      split,
      topBar
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .uploadList-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    font-size (15 /_rem)
    .arrow
      width (10 /_rem)
      height (10 /_rem)
      position absolute
      right (20 /_rem)
      top 50%
      transform translateY(-50%)
    .u-v-form-cli:first-child
      border-bottom 1px solid #D7D7D7
    .u-v-form-cli
      height (80 /_rem)
      display: -webkit-flex
      display -ms-flex
      display flex
      -webkit-flex-flow: row wrap;
      -moz-flex-flow: row wrap;
      flex-flow: row wrap;
      .sell-l
        text-align center
        border-right 1px solid #D7D7D7
        line-height (80 /_rem)
        width (100 / _rem)
        span
          font-size (16 /_rem)
      .sell-f
        -webkit-flex: 3;
        -ms-flex: 3;
        flex 3
        .sell-f-sz
          height 50%
          line-height (40 /_rem)
          position relative
          border-bottom 1px solid #D7D7D7
          span
            font-size (15 /_rem)
            margin-left (15 /_rem)
        .sell-f-chc
          height 50%
          line-height (40 /_rem)
          input
            height 20px
            margin-left (15 /_rem)
            width (120 /_rem)
            font-size (15 /_rem)
          .s-a-box-db
            width (55 /_rem)
            border 1px solid #CCD0D1
            border-radius 4px
            text-align center
            display inline-block
            cursor pointer
            height (25 /_rem)
            line-height (25 /_rem)
            font-size 0
            color #999999
            span
              font-size (15 /_rem)
          .visited
            background-color #28CBA7
            color #ffffff !important
          .s-a-box-db:not(:first-child)
            margin-left (5 /_rem)
        .buy-f-chc
          border-bottom 1px solid #D7D7D7
        .buy-f-chc, .buy-f-name
          height 50%
          line-height (40 /_rem)
          input
            height 30px
            margin-left (15 /_rem)
            width 70%
            font-size (15 /_rem)
    .u-v-form-time
      height (30 /_rem)
      line-height (30 /_rem)
      position relative
      .time-select
        margin-left (20 /_rem)
      input
        margin-left (15 /_rem)
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        right (10/_rem)
        font-size (15/_rem)
    .u-v-good-form
      margin 0 (10 /_rem) (10 /_rem) (10 /_rem)
      .u-v-lab-form
        border-bottom 1px solid #D7D7D7
        height (40 /_rem)
        line-height (40 /_rem)
        .u-v-c-lab
          margin-left (10 /_rem)
        .u-v-c-good
          margin-left (30 /_rem)
          color #28CBA7
    .u-v-lab-zw
      height (60 /_rem)
      display: -moz-box;
      display: -webkit-box;
      display: box;
      justify-content center
      width 100%
      margin (10 /_rem) 0
      border-bottom 1px solid #D7D7D7
      li:not(:last-child)
        border-right 1px solid #D7D7D7
      li
        -moz-box-flex: 1;
        -webkit-box-flex: 1;
        box-flex: 1;
        line-height (30 /_rem)

        .num
          text-align center
        .inputVal
          input
            height 20px
            width (117 /_rem)
            text-align center
            background transparent;
    .solid-line
      margin 0 0 0(50 /_rem)
      height 1px
      background-color #D7D7D7
    .login-btn
      margin (20 /_rem) (15 /_rem) 0 (15 /_rem)
      .btn-login-c
        color #ffffff
        line-height (15 /_rem)
        height (40 /_rem)
        border-radius: 5px;
        display: inline-block;
        width: 100%;
      .login-confirm
        background-color: #28CBA7;
        color #FFFFFF;
        border: 1px solid #0bbe06;

</style>
