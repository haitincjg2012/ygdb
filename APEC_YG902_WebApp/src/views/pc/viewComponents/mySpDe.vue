<!--我的供求-->
<template>
  <div class="Jhds">
    <scroller ref="my_scroller_spde" :on-refresh="refresh" :on-infinite="infinite">
    <!--<top-bar title="我的供求"></top-bar>-->
      <my-header>
         <h1 slot="pack" class="my-title">我的供求</h1>
      </my-header>
    <input checked class="radio" type="radio" name="Jhds" id="Jhds1">
    <input class="radio" type="radio" name="Jhds" id="Jhds2">
    <div class="tab-title">
      <label for="Jhds1">上市中</label>
      <label for="Jhds2">已下架</label>
    </div>
      <div class="tab-outer">
        <ul class="tab-inner">
          <li v-for="item in onlineList">
            <!--<div v-if="!item.isEdit" class="list" @click="xq(item)" :data-id = "item.id">-->
            <div class="list" @click="xq(item)" :data-id = "item.id">
              <div class="left-img" :data-id = "item.id">
                <img :src="item.firstImageUrl" :data-id = "item.id">
                <div :class="item.class" :data-id = "item.id">
                  <span :data-id = "item.id">{{item.productTypeName}}</span>
                </div>
              </div>
              <div class="right-content" :data-id = "item.id">
                <h4 :data-id = "item.id">
                  {{item.skuName}}
                </h4>
                <div v-if="item.productTypeName==='求购'" class="price" :data-id = "item.id">
                  <span class="dj" :data-id = "item.id">{{item.startAmount}} -</span>
                  <span class="dj" :data-id = "item.id">{{item.endAmount}}</span>
                  <span class="djdw" :data-id = "item.id">{{item.priceUnit}}</span>
                  <span class="num" :data-id = "item.id">{{item.weight}}</span><span class="numdw" :data-id = "item.id">{{item.weightUnit}}</span>
                </div>
                <div v-else="" class="price" :data-id = "item.id">
                  <span class="dj" :data-id = "item.id">{{item.amount}}</span>
                  <span class="djdw" :data-id = "item.id">{{item.priceUnit}}</span>
                  <span class="num" :data-id = "item.id">{{item.weight}}</span><span class="numdw" :data-id = "item.id">{{item.weightUnit}}</span>
                </div>
                <div class="time-up" :data-id = "item.id">
                  <span class="time" :data-id = "item.id">{{item.showCredateTime}}</span>
                </div>
              </div>
            </div>
            <!--<div v-else class="list-edit">-->
              <!--<div class="left-img">-->
                <!--<img :src="item.firstImageUrl">-->
                <!--<div :class="item.class">-->
                  <!--<span>{{item.productTypeName}}</span>-->
                <!--</div>-->
              <!--</div>-->
              <!--<div class="right-content">-->
                <!--<div v-if="item.productTypeName==='求购'" @click.stop="updatePrice($event,item)" class="price">-->
                  <!--<i class="fa fa-minus cut" aria-hidden="true"></i><span class="dj">{{item.startAmount}} -</span>-->
                  <!--<span style="color: #2fcaa7;font-size: 0.9rem;">&nbsp;{{item.endAmount}}</span><i-->
                  <!--class="fa fa-plus add" aria-hidden="true"></i><span class="djdw">{{item.priceUnit}}</span>-->
                <!--</div>-->
                <!--<div v-else @click.stop="updatePrice($event,item)" class="price">-->
                  <!--<i class="fa fa-minus cut" aria-hidden="true"></i><span class="dj">{{item.amount}}</span><i-->
                  <!--class="fa fa-plus add" aria-hidden="true"></i><span class="djdw">{{item.priceUnit}}</span>-->
                <!--</div>-->
                <!--<div @click.stop="updateWeight($event,item)" class="price">-->
                  <!--<i class="fa fa-minus cut" aria-hidden="true"></i><span class="num">{{item.weight}}</span><i-->
                  <!--class="fa fa-plus add" aria-hidden="true"></i><span class="numdw">{{item.weightUnit}}</span>-->
                <!--</div>-->
              <!--</div>-->
              <!--<div @click.stop="offSell(item.id)" class="xj">-->
                <!--<a class="btn-xj">下架</a>-->
              <!--</div>-->
            <!--</div>-->
            <div class="edit">
              <a @click.stop.prevent="edit($event,item)" class="editL">{{item.editData}}</a>
              <a @click.stop.prevent="offSell(item.id)" class="editR">{{item.offShelf}}</a>
            </div>
            <div class="split"></div>
          </li>
        </ul>
        <ul class="tab-inner">
          <li v-for="item in offlinelist">
            <div class="list" :data-id = "item.id">
              <div class="left-img" :data-id = "item.id">
                <img :src="item.firstImageUrl" :data-id = "item.id">
                <div :class="item.class" :data-id = "item.id">
                  <span :data-id = "item.id">{{item.productTypeName}}</span>
                </div>
              </div>
              <div class="right-content" :data-id = "item.id">
                <h4 :data-id = "item.id">
                  {{item.skuName}}
                </h4>
                <div v-if="item.productTypeName==='求购'" class="price" :data-id = "item.id">
                  <span class="dj" :data-id = "item.id">{{item.startAmount}} -</span>
                  <span class="dj" :data-id = "item.id">{{item.endAmount}}</span>
                  <span class="djdw" :data-id = "item.id">{{item.priceUnit}}</span>
                  <span class="num" :data-id = "item.id">{{item.weight}}</span><span class="numdw" :data-id = "item.id">{{item.weightUnit}}</span>
                </div>
                <div v-else="" class="price" :data-id = "item.id">
                  <span class="dj" :data-id = "item.id">{{item.amount}}</span>
                  <span class="djdw" :data-id = "item.id">{{item.priceUnit}}</span>
                  <span class="num" :data-id = "item.id">{{item.weight}}</span><span class="numdw">{{item.weightUnit}}</span>
                </div>
                <div class="time-up" :data-id = "item.id">
                  <span class="time" :data-id = "item.id">{{item.showCredateTime}}</span>
                </div>
              </div>
            </div>
            <div class="split"></div>
          </li>
        </ul>
      </div>
    </scroller>
    <!--<puw></puw>-->
    <!--<wuw></wuw>-->
  </div>
</template>

<script>
//  import topBar from '../../../components/topBar/topBar'
   import header from '../../../components/header/headerTwo.vue'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import puw from './priceUpdatWindow.vue'
  import wuw from './weightUpdatWindow.vue'
  import {MessageBox, Indicator, Toast} from 'mint-ui';
  const api = new API();

  export default {

    data() {
      return {
        onlineList: [],
        offlinelist: [],
        updatePriceId: '',
        currentPageNo: 1,
        isActivated: true,
        editIndex:'0'//0是编辑  1  完成
      }
    },
    activated () {
      this.isActivated = true;
      this.currentPageNo = 1;
      this.GetGQList();
      this.GetdownList();
    },
    deactivated () {
      this.isActivated = false;
    },

    mounted(){
      this.$refs.my_scroller_spde.finishInfinite(true);
    },

    methods: {
      updatePrice(e, item){
        this.updatePriceId = item.id;
        switch (item.productTypeName){
          case '求购':
            this.$root.eventHub.$emit('puw.Event',{'priceUnit':item.priceUnit,'index':'1'});
            break;
          case '供应':
            this.$root.eventHub.$emit('puw.Event',{'priceUnit':item.priceUnit,'index':'0'});
            break;
          default:
              break;
        }
      },
      updateWeight(e, item){
        this.updatePriceId = item.id;
        this.$root.eventHub.$emit('wuw.Event',{'weightUnit':item.weightUnit});
      },
      edit(e, item){
        const self = this;
        if(item.editData==='编辑'){
//          self.onlineList.forEach((i) => {
//              if (item.id === i.id) {
//                i.isEdit = true;
//                i.editData ='完成';
//              }
//              else
//                i.isEdit = false;
//            }
//          )
          this.$router.push({name:"edit",query:{id:item.id,priceUnit:item.priceUnit,weightUnit:item.weightUnit,gq:item.productTypeName}});
        }else{
          self.onlineList.forEach((i) => {
              if (item.id === i.id) {
                i.isEdit = false;
                i.editData ='编辑'
              }
            }
          )
        }
      },
      GetGQList(){//获取上市中的供求
        const self = this;
        self.onlineList = [];
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/_node_product/_user_push_list.apno",
          data: {
            pageNumber: self.currentPageNo
          }
        };
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              item.data.rows.forEach((item) => {
                self.onlineList.push({
                  'skuName': item.skuName.length > 20 ? item.skuName.substring(0, 20) + '...' : item.skuName,
                  'id': item.id,
                  'firstImageUrl': item.firstImageUrl + "?x-oss-process=style/_list",
                  'productTypeName': item.productTypeName,
                  'address': item.address,
                  'showUserName': item.showUserName,
                  'class': item.productTypeName == '求购' ? 'r-c-label red' : 'r-c-label blue',
                  'priceUnit': item.priceUnit,
                  'weightUnit': item.weightUnit,
                  'amount': item.amount,
                  'weight': item.weight,
                  'isEdit': false,
                  'endAmount':item.endAmount,
                  'startAmount':item.startAmount,
                  'showCredateTime':item.showCredateTime,
                  'editData':'编辑',
                  'offShelf':'下架'
                })
              });
            } else {
            }
            Indicator.close();
          }).catch((error) => {
            Indicator.close();
            console.log(error)
          })
        } catch (error) {
          Indicator.close();
          console.log(error)
        }
      },
      refresh_infinite_dt(fn){
        const self = this;
        let params = {
          api: "/_node_product/_user_push_list.apno",
          data: {
            pageNumber: self.currentPageNo
          }
        };
        try {
          api.post(params).then((res) => {
            var item = res.data;
            fn(item);
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      GetdownList(){//获取下架中的供求
        const self = this;
        self.offlinelist = [];
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/_node_product/_user_off_list.apno",
          data: {
            pageNumber: '1'
          }
        };
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              item.data.rows.forEach((item) => {
                self.offlinelist.push({
                  'skuName': item.skuName.length > 20 ? item.skuName.substring(0, 20) + '...' : item.skuName,
                  'id': item.id,
                  'firstImageUrl': item.firstImageUrl+"?x-oss-process=style/_list",
                  'productTypeName': item.productTypeName,
                  'address': item.address,
                  'showUserName': item.showUserName,
                  'class': item.productTypeName == '求购' ? 'r-c-label red' : 'r-c-label blue',
                  'priceUnit': item.priceUnit,
                  'weightUnit': item.weightUnit,
                  'amount': item.amount,
                  'weight': item.weight,
                  'isEdit': false,
                  'endAmount':item.endAmount,
                  'startAmount':item.startAmount,
//                  'showCredateTime':item.showCredateTime
                  'showCredateTime':new Date().format(item.offsellDate, 'yyyy-MM-dd'),
                })
              });
            } else {
            }
            Indicator.close();
          }).catch((error) => {
            Indicator.close();
            console.log(error)
          })
        } catch (error) {
          Indicator.close();
          console.log(error)
        }
      },
      offSell(id){//下架
        if (!id)
          return;
        Indicator.open();
        const self = this;
        var params = {
          api: "/yg-product-service/product/offSellProduct.apec",
          data: {
            "elasticId": id,
          }
        };
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              Toast("下架成功~");
              self.slDt(id);
            } else {
            }
            Indicator.close();
          }).catch((error) => {
            console.log(error);
            Indicator.close();
          })
        } catch (error) {
          console.log(error);
          Indicator.close();
        }
      },
      updateGYPriceNum(id, weight = 0, num = 0){
        const self = this;
        var params = {
          api: "/yg-product-service/product/updateProductByES.apec",
          data: {
            "elasticId": id,
          }
        };
        if (num && !weight) {
          params.data['amount'] = num;
        } else if(!num && weight){
          params.data['weight'] = weight;
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              Toast("修改成功~");
              self.sortDt(id, weight, num,0,0);
            } else {
              Toast("修改失败~");
            }
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      updateQGPriceNum(id, weight = 0,startAmount=0,endAmount=0){
        const self = this;
        var params = {
          api: "/yg-product-service/product/updateBuyProductByES.apec",
          data: {
            "elasticId": id,
          }
        };
        if (startAmount && endAmount && !weight) {
          params.data['startAmount'] = startAmount;
          params.data['endAmount'] = endAmount;
        } else if(!startAmount && !endAmount && weight){
          params.data['weight'] = weight;
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              Toast("修改成功~");
              self.sortDt(id, weight,0,startAmount,endAmount);
            } else {
              Toast("修改失败~");
            }
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      slDt(id){
        const self = this;
        var i = self.onlineList.findIndex((i) => {
            return id == i.id
          }
        );

//        var item = self.onlineList[i];
//        item.offsellDate = new Date().getTime();
        self.offlinelist.push(self.onlineList[i]);

        if (i > -1) {
          self.onlineList.splice(i, 1);
        }

      },
      sortDt(id, weight = 0, num = 0,startAmount=0,endAmount=0){
        const self = this;
        self.onlineList.forEach((item) => {
          if (item.id == id) {
            item.isEdit = false;
            item.editData='编辑';
            if (weight)
              item.weight = weight;
            if (num)
              item.amount = num;
            if (startAmount)
              item.startAmount = startAmount;
            if (endAmount)
              item.endAmount = endAmount;
          }
        });
      },
      refresh (done) {
        const self = this;
        self.currentPageNo++;
        setTimeout(() => {
          self.refresh_infinite_dt(function (data) {
            if (data.succeed) {
              data.data.rows.forEach((item) => {
                self.onlineList.splice(0, 0, ({
                  'skuName': item.skuName.length > 20 ? item.skuName.substring(0, 20) + '...' : item.skuName,
                  'id': item.id,
                  'firstImageUrl': item.firstImageUrl+"?x-oss-process=style/_list",
                  'productTypeName': item.productTypeName,
                  'address': item.address,
                  'showUserName': item.showUserName,
                  'class': item.productTypeName == '求购' ? 'r-c-label red' : 'r-c-label blue',
                  'priceUnit': item.priceUnit,
                  'weightUnit': item.weightUnit,
                  'amount': item.amount,
                  'weight': item.weight,
                  'isEdit': false
                }));
              });
            } else {
            }
          });
          done()
        }, 1500);
      },
      infinite (done) {
        if (!this.isActivated) return done(true)
        const self = this;
        self.currentPageNo++;
        setTimeout(() => {
          self.refresh_infinite_dt(function (data) {
            if (data.succeed) {
              data.data.rows.forEach((item) => {
                self.onlineList.push({
                  'skuName': item.skuName.length > 20 ? item.skuName.substring(0, 20) + '...' : item.skuName,
                  'id': item.id,
                  'firstImageUrl': item.firstImageUrl +"?x-oss-process=style/_list",
                  'productTypeName': item.productTypeName,
                  'address': item.address,
                  'showUserName': item.showUserName,
                  'class': item.productTypeName == '求购' ? 'r-c-label red' : 'r-c-label blue',
                  'priceUnit': item.priceUnit,
                  'weightUnit': item.weightUnit,
                  'amount': item.amount,
                  'weight': item.weight,
                  'isEdit': false
                });
              });
            } else {
            }
          });
          done(true)
        }, 500);
      },
      xq(item){
        var id = item.id;
        this.$router.push({path: 'detail',query:{id:id}});
      }
    },

    created() {
      let self = this;
      self.$root.eventHub.$on('puwUpdate.confirm', function (item) {
          console.log(item, 8888);
        //调用供应接口
        if(item.priceSwitch==='0')
        {
          self.updateGYPriceNum(self.id, 0, item.num);
        }else{
          self.updateQGPriceNum(self.id, 0, item.startNum,item.endNum);
        }
      });
      self.$root.eventHub.$on('wuwUpdate.confirm', function (weight) {
        self.updateGYPriceNum(self.id, weight, 0)
      });
    },

    components: {
//      topBar,
      "my-header":header,
      puw,
      wuw
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  _rem = 20rem;
  .Jhds
    font-size: (14 /_rem);
    margin: 0;
    color: #252525;
    position: relative;
    background-color #f4f4f4
    .radio
      opacity: 0;
      position: absolute;
    .radio:nth-of-type(1):checked ~ .tab-title label:nth-of-type(1)
      color: #fff;
      background-color: #28CBA7;
    .radio:nth-of-type(2):checked ~ .tab-title label:nth-of-type(2)
      color: #fff;
      background-color: #28CBA7;
    .radio:nth-of-type(1):checked ~ .tab-outer .tab-inner:nth-of-type(1)
      display block;
    .radio:nth-of-type(2):checked ~ .tab-outer .tab-inner:nth-of-type(2)
      display block;
    .tab-title
      font-size: 0;
      padding: 15px;
      text-align: center;
      border-bottom (1/_rem) solid #f4f4f4;
      background-color #fff;
      label
        display: inline-block;
        color: #28CBA7;
        text-align: center;
        background-color: #fff;
        border: 1px solid #28CBA7;
        font-size: 14px;
        width: 45%;
        padding: 5px 0;
      label:first-child
        border-right: 0 none;
        border-top-left-radius: 5px;
        border-bottom-left-radius: 5px;
      label:last-child
        border-left: 0 none;
        border-top-right-radius: 5px;
        border-bottom-right-radius: 5px;
    .tab-outer
      .tab-inner
        display: none;
        li
          position: relative;
          background-color: #fff;
          .list
            padding (10 /_rem);
            display flex;
            .left-img
              position relative;
              flex 2
              .r-c-label
                width (36 /_rem)
                border-radius 4px
                text-align center
                display inline-block
                cursor pointer
                height (16 /_rem)
                line-height (16 /_rem)
                color #fff
                position absolute
                left 0
                bottom 0
                font-size (12 /_rem)
              .blue
                background-color #18ACA5
              .red
                background-color #F5493E
                span
                  color #ffffff
              img
                height (85 /_rem)
                display block
                width (85 /_rem)
            .right-content
              flex 5
              h4
                font-size (15 /_rem)
                line-height (30 /_rem)
                height (30 /_rem)
                font-weight bold
                .r-c-label
                  width (40 /_rem)
                  border 1px solid #28cba7
                  border-radius 4px
                  text-align center
                  display inline-block
                  cursor pointer
                  height (20 /_rem)
                  line-height (20 /_rem)
                  color #fff
              .price
                height (25 /_rem)
                line-height (25 /_rem)
                .dj, .num
                  color #2FCAA7
                  font-size (18 /_rem)
                .djdw, .numdw
                  color #B0BBCA
                  margin-left (7 /_rem)
                  font-size (12 /_rem)
                .num
                  margin-left (20 /_rem)
              .time-up
                height (30 /_rem)
                line-height (30 /_rem)
                color #AAB6C6
                font-size (12 /_rem)
          .list-edit
            display flex;
            .left-img
              position relative;
              flex 2
              margin (10 /_rem) 0 (10 /_rem) (10 /_rem)
              .r-c-label
                width (36 /_rem)
                border-radius 4px
                text-align center
                display inline-block
                cursor pointer
                height (16 /_rem)
                line-height (16 /_rem)
                color #fff
                position absolute
                left 0
                bottom 0
                font-size (12 /_rem)
              .blue
                background-color #18ACA5
              .red
                background-color #F5493E
                span
                  color #ffffff
              img
                height (85 /_rem)
                display block
                width (85 /_rem)
            .right-content
              flex 3.8
              padding (10 /_rem) 0 (10 /_rem) 0;
              line-height: (40 /_rem)
              align-items: center;
              justify-content: center;
              display: flex;
              flex-direction: column;
              .price
                font-size 0;
                width (195 /_rem)
                display: flex;
                flex-direction: row;
                align-items: center;
                position relative
                .cut, .add
                  color #717375
                  font-size (24 /_rem)
                .cut
                  margin-left (7 /_rem)
                .add
                  position absolute
                  top 50%
                  transform translateY(-50%)
                  right (50 /_rem)
                .dj, .num
                  color #2FCAA7
                  font-size (18 /_rem)
                  margin-left (10 /_rem)
                .djdw, .numdw
                  color #B0BBCA
                  font-size (12 /_rem)
                  position absolute
                  top 50%
                  transform translateY(-50%)
                  right (10 /_rem)
                .num
                  margin-left (10 /_rem)
              .time-up
                height (30 /_rem)
                line-height (30 /_rem)
                color #AAB6C6
                font-size (12 /_rem)
            .xj
              flex 1.2
              text-align center
              background-color #F5493E
              color #ffffff
              line-height (105 /_rem)
          .edit
            height (44 /_rem)
            border-top 1px solid #f4f4f4
            position relative
            .editL
               display inline-block
               height (25/_rem)
               line-height (25/_rem)
               margin-left (255/_rem)
               margin-top (10/_rem)
               border-radius (4/_rem)
            .editR
               display inline-block
               height (25/_rem)
               line-height (25/_rem)
               margin-left (20/_rem)
               margin-top (10/_rem)
               border-radius (4/_rem)
            a
              color #323232
              padding 0 (5/_rem)
              border (1/_rem) solid #BFBFBF
              font-size (15 /_rem)
          top: 0
    .title:after
      position: absolute;
      left: 0;
      content: '';
      width: 100%;
      height: 1px;
      font-size: 0;
      background-color: #d9d9d9;
      -webkit-transform: scaleY(0.5);
      transform: scaleY(0.5);
      text-decoration: none;
    .title:after, .tab-inner li:last-child:after
      bottom 0
    .title
      padding: 10px;
      position: relative;
      overflow: hidden;
      .Jhds-num
        color: #498fee;
        white-space: nowrap;
      .time
        float: right;
        display: inline-block;
    .split
      height (10/_rem)
      background-color #f4f4f4
    .my-title
      width (325/_rem)
      text-align center
      color #222222
      font-size (18/_rem)

</style>
