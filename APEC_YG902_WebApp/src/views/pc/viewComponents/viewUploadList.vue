<template>
  <div class="upload-view-info-page">
      <top-bar title="我的单据"></top-bar>
      <scroller ref="my_scroller" style="top:42px;" :on-infinite="infinite">
      <div class="main-page">
        <div class="m-u-v-total"><span>合计：{{totalCount}}&nbsp;斤</span></div>
        <div v-for="item in upViewList" class="m-u-v-form-cli">
          <div class="m-u-v-table">
            <div class="m-u-v-cell">
              <span>卖货方：{{item.saleMarket}}</span>
            </div>
            <div class="m-u-v-cell">
              <span>买货方：{{item.name}}</span>
            </div>
            <div class="m-u-v-cell">
              <span>交收时间：{{item.deliveryTime}}</span>
            </div>
          </div>
          <div v-for="e in item.voucherGoodsVVO" class="m-u-v-content">
            <span>{{e.skuName}}</span>
            <span class="con-cell">{{e.number}}斤</span>
            <span class="con-cell">{{e.amount}}斤/元</span>

            <span class="con-cell">{{e.totalAmount}}元</span>
          </div>
          <split></split>
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
  import {MessageBox, Indicator} from 'mint-ui';

  const api = new API();

  export default {

    data() {
      return {
        upViewList: [],
        totalCount: 0,//总上传斤数
        isActivated:true,
        currentPageNo:1,
      }
    },
    activated () {
      this.isActivated = true;
      this.currentPageNo = 1;
      this.GetViewList();
    },
    deactivated () {
      this.isActivated = false
    },

    mounted(){
      this.$refs.my_scroller.finishInfinite(true);
    },

    methods: {
      GetViewList(){//获取信息
        const self = this;
        self.upViewList = [];
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-voucher-service/voucher/getVoucherInfo.apec",
          data: {
            pageNumber: self.currentPageNo
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              self.totalCount = item.data.totalNumber || 0;
              item.data.voucherVVOs.rows.forEach((item) => {
                self.upViewList.push({
                  'saleMarket': item.saleMarket,
                  'name': item.name,
                  'deliveryTime': item.deliveryTime,
                  'voucherGoodsVVO': item.voucherGoodsVVO
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
          api: "/yg-voucher-service/voucher/getVoucherInfo.apec",
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
      infinite (done) {
        if (!this.isActivated) return done(true);
        const self = this;
        self.currentPageNo++;
        self.busy = true;
        setTimeout(()=>{
          self.refresh_infinite_dt(function (data) {
            if (data.succeed && data.data.voucherVVOs.rows.length) {
              self.totalCount = data.data.totalNumber;
              data.data.voucherVVOs.rows.forEach((item) => {
                self.upViewList.push({
                'saleMarket': item.saleMarket,
                'name': item.name,
                'deliveryTime': item.deliveryTime,
                'voucherGoodsVVO': item.voucherGoodsVVO
              })
            });
            } else {
            }
          });
          done(true);
        }, 1500);
      }
    },

    created() {
    },

    components: {
      topBar,
      split
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .upload-view-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    font-size (16/_rem)
    .m-u-v-total
      height (30/_rem)
      line-height (30/_rem)
      border-bottom 1px solid #D7D7D7
      padding 5px
      text-align center
      color #28cba7
    .m-u-v-form-cli
      position relative
      border-bottom 1px solid #D7D7D7
      .m-u-v-table
        padding  0 10px 10px 10px
        border-bottom 1px solid #D7D7D7
        .m-u-v-cell
          margin-top 10px
      .m-u-v-content
        padding  10px 10px 10px 10px
        font-size (14/_rem)
        span:not(:first-child)
          margin-left (12/_rem)
        .con-cell
          color:#28cba7

</style>
