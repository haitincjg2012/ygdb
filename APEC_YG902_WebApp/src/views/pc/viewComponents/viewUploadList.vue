<template>
  <div class="upload-view-info-page">
      <top-bar title="我的单据"></top-bar>
      <scroller ref="my_scroller" style="top:42px;" :on-infinite="infinite">
      <div class="main-page">
        <!--<div class="m-u-v-total"><span>合计：{{totalCount}}&nbsp;斤</span></div>-->
        <div class="c-z-total">
            <p class="c-z-w-title">合计交收数量(斤)</p>
            <p class="c-z-w-number">{{totalCount}}</p>
        </div>
        <div  v-for="(item,index) in upViewList" class="z-m-u-v-form-cli">
             <div class="c-z-space-viewmenu"></div>
            <div class="c-z-m-p">
              <div class="c-z-viewmenu-bg">
                <p class="c-z-viewmunu-bg-T">{{item.createTime}}</p>
              </div>
              <div class="c-z-del-menu">
                 <span class="c-z-p-text" @click.stop="delMenu(index,item.voucherId)">删除</span>
              </div>
            </div>

             <div class="c-z-viewmenu">
                 <div class="c-z-viewmenu-s">
                    <div class="c-z-flex-com-One">卖货方：</div>
                    <div class="c-z-flex-com-Two">{{item.saleName}}</div>
                 </div>
                 <div class="c-z-viewmenu-b">
                    <div class="c-z-flex-com-One">买货方：</div>
                    <div class="c-z-flex-com-Two">{{item.name}}</div>
                 </div>
               <div class="c-z-viewmenu-b">
                 <div class="c-z-flex-com-One">交收时间：</div>
                 <div class="c-z-flex-com-Two">{{item.deliveryTime}}</div>
               </div>
                 <div class="c-z-viewmenu-info-title">
                    <div>商品信息</div>
                 </div>
               <div v-for="e in item.voucherGoodsVVO" class="c-m-u-v-content">
                 <div class="c-z-viewmenu-info-l">
                    <p class="c-z-v-info-t">{{e.number}}</p>
                    <p class="c-z-v-info-n">数量(斤)</p>
                 </div>
                 <div class="c-z-viewmenu-info-r">
                    <p  class="c-z-v-info-r-t">{{e.skuName}}</p>
                    <div class="c-z-v-info-r-price">
                       <div class="c-z-v-i-r-p-l">
                          <p>单价：{{e.amount}}斤/元</p>
                       </div>
                       <div class="c-z-v-i-r-p-r">
                           <p>总金额：{{e.totalAmount}}元</p>
                       </div>
                    </div>
                 </div>
               </div>
             </div>
        </div>
        <!--<div v-for="item in upViewList" class="m-u-v-form-cli">-->
          <!--<div class="m-u-v-table">-->
            <!--<div class="m-u-v-cell">-->
              <!--<span>卖货方：{{item.saleMarket}}</span>-->
            <!--</div>-->
            <!--<div class="m-u-v-cell">-->
              <!--<span>买货方：{{item.name}}</span>-->
            <!--</div>-->
            <!--<div class="m-u-v-cell">-->
              <!--<span>交收时间：{{item.deliveryTime}}</span>-->
            <!--</div>-->
          <!--</div>-->
          <!--<div v-for="e in item.voucherGoodsVVO" class="m-u-v-content">-->
            <!--<span>{{e.skuName}}</span>-->
            <!--<span class="con-cell">{{e.number}}斤</span>-->
            <!--<span class="con-cell">{{e.amount}}斤/元</span>-->

            <!--<span class="con-cell">{{e.totalAmount}}元</span>-->
          <!--</div>-->
          <!--<split></split>-->
        <!--</div>-->
      </div>
      </scroller>
  </div>
</template>
<style>
@import "../../../assets/css/viewMenu.css";
</style>
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
      this.upViewList = [];
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
                  "saleName":item.shipWarehouse,
                  'name': item.name,
                  "voucherId":item.voucherId,
                  "createTime":new Date().format(item.createDate,"yyyy/MM/dd"),
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
      },
      delMenu(index, id){
          var self = this;
          console.log(index, id);
        MessageBox.confirm('您确定要删除该单据吗?', "提示").then(action => {
            let param = {
                api:"/yg-voucher-service/voucher/deleteVoucherInfo.apec",
                data:{
                  voucherId:id,
                }
            }
          try {
            api.post(param).then((res) => {
              var item = res.data;
              if (item.succeed) {
                self.upViewList.splice(index, 1);
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
      });
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
