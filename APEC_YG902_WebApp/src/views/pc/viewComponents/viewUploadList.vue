<template>
  <div class="upload-view-info-page">
      <!--<top-bar title="我的单据"></top-bar>-->
      <div class="c-upload-head">
         <div class="c-u-h-back" @click="back">
           <img src="../../../assets/img/ret.png">
         </div>
         <div class="c-u-h-title"><h4>我的单据</h4></div>
         <div class="c-u-h-newMenu" @click="jumpMenu"><span>上传</span></div>
      </div>
     <my-scroll class="c-upload-Scroll" :data="upViewList" :pullup="pullup" @scrollToEnd="loadMore" :pulldown="pulldown" @pulldown="downRefresh">
       <div class="mainc-page">
         <div class="downRefresh" v-if="dnfreshFlag">
           <my-scrollTip ref="refreshTip"></my-scrollTip>
         </div>
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
             <div class="c-z-m-check">
               {{item.auditState}}
             </div>
             <div class="c-z-del-menu">
               <span class="c-z-p-text" @click.stop="delMenu(index,item.voucherId,item)">删除</span>
             </div>
           </div>

           <div class="c-z-viewmenu">
             <div class="c-z-viewmenu-s">
               <div class="c-z-flex-com-One">卖货方：</div>
               <div class="c-z-flex-com-Two">{{item.saleName}}</div>
             </div>
             <div class="c-z-viewmenu-b">
               <div class="c-z-flex-com-One">买货方：</div>
               <div class="c-z-flex-com-Two">{{item.saleMarket}}</div>
             </div>
             <div class="c-z-viewmenu-b">
               <div class="c-z-flex-com-One">买货姓名：</div>
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
         <my-scrollTip ref="loadmoreTip" ></my-scrollTip>
       </div>
     </my-scroll>
      <!--<scroller ref="my_scroller" style="top:42px;" :on-infinite="infinite">-->

      <!--</scroller>-->
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
  import scroll from '@/components/scroll/scroll'//分页
//  import scrollTip from '@/components/downLoadAnimal' //滚动提示
  import scrollTip from '../../../components/loading.vue' //分页提示


  const api = new API();

  export default {
    data() {
      return {
        upViewList: null,
        totalCount: 0,//总上传斤数
        isActivated:true,
        currentPageNo:1,
        pageCount:1000,
        //上拉分页
        pullup:true,
        //下拉刷新
        pulldown:true,
        dnfreshFlag:false //下拉刷新标志
      }
    },
    activated () {
        this.dnfreshFlag = false;
       this.upViewList = null;
      this.isActivated = true;
      this.currentPageNo = 1;
      this.GetViewList();
    },
    deactivated () {
      this.isActivated = false
    },

    mounted(){
//      this.$refs.my_scroller.finishInfinite(true);
    },

    methods: {
      GetViewList(){//获取信息
        const self = this;
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
              self.pageCount = item.data.voucherVVOs.pageCount;
              self.totalCount = item.data.totalNumber || 0;

              if(self.pageCount > 1){
                self.$refs.loadmoreTip.start();
              }else{
                self.$refs.loadmoreTip.end();
              }
              var arr = [];
              item.data.voucherVVOs.rows.forEach((item) => {
                arr.push({
                  'saleMarket': item.saleMarket,
                  "saleName":item.shipWarehouse,
                  'name': item.name,
                  "voucherId":item.voucherId,
                  "createTime":new Date().format(item.createDate,"yyyy/MM/dd"),
                  "auditState":item.auditState,
                  'deliveryTime': item.deliveryTime,
                  'voucherGoodsVVO': item.voucherGoodsVVO,
                  'weight':item.voucherGoodsVVO.number,
                })
              });
              if(arr.length){
                self.upViewList = arr;
              }

            }else {
//              Indicator.close();
//              Indicator.open({
//                text: item.errorMsg,
//                spinnerType: 'fading-circle'
//              });
//               return;
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

//        if (!this.isActivated) return done(true);
        const self = this;
        self.currentPageNo = 1;
        self.busy = true;
        setTimeout(()=>{
          self.refresh_infinite_dt(function (data) {
            if (data.succeed && data.data.voucherVVOs.rows.length) {
              self.totalCount = data.data.totalNumber;
              data.data.voucherVVOs.rows.forEach((item) => {
                self.upViewList.push({
                'saleMarket': item.saleMarket,
                 "saleName":item.shipWarehouse,
                'name': item.name,
                "voucherId":item.voucherId,
                "createTime":new Date().format(item.createDate,"yyyy/MM/dd"),
                  "auditState":item.auditState,
                'deliveryTime': item.deliveryTime,
                'voucherGoodsVVO': item.voucherGoodsVVO,
              })
            });
            } else {
            }
          });
          done(true);
        }, 1500);
      },
      delMenu(index, id,data){
          var self = this;
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
//                  self.totalCount = self.totalCount - data.voucherGoodsVVO[0].number;
//                self.upViewList.splice(index, 1);
                self.upViewList = null;
                self.currentPageNo = 1;
                self.GetViewList();

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
      },
      back(){
          self.dnfreshFlag=false;
          this.$router.go(-1);
      },
      jumpMenu(){
        const self = this;
        let params = {
          api: "/_node_user/_check_pronum.apno"
        };
        try {
          api.post(params).then((res)=>{
            Indicator.close();
            var item = res.data;
            if (item.succeed){
              var checkStatus = item.data.checkStatus;
              var realAuth = item.data.realAuth;//是否实名验证
              var realInfo = item.data.realInfo;//是否填写资料
              if(!checkStatus && !realInfo){
                //self.$store.commit("incrementCheck",{'check':'1'});
                self.$store.state.check = "1";
                self.$router.push({name:'validate'});
              }
              else
                self.$router.push({name: 'uploadList'});
            } else {
            }
          }).catch((error)=> {

            }
          )
        } catch (error) {
          console.log(error)
        }
      },
      downRefresh(){
          //上拉刷新
        var self = this;
        self.dnfreshFlag=true;
        self.$nextTick(function(){
          self.$refs.refreshTip.refresh("刷新中...");
        });

        let params = {
          api: "/yg-voucher-service/voucher/getVoucherInfo.apec",
          data: {
            pageNumber: 1
          }
        };
        try {
          api.post(params).then((res) => {
            var data = res.data;
            if (data.succeed && data.data.voucherVVOs.rows.length){
              self.totalCount = data.data.totalNumber;
              setTimeout(function(){
                self.dnfreshFlag=false;
              },1000);
            }
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      loadMore(){
        var self = this;
        if(self.pageCount > self.currentPageNo){
          self.$nextTick(function(){
            self.$refs.loadmoreTip.start();
          });
          self.currentPageNo ++;
          self.refresh_infinite_dt(function (data) {
            if (data.succeed && data.data.voucherVVOs.rows.length) {
              self.totalCount = data.data.totalNumber;
              data.data.voucherVVOs.rows.forEach((item) => {
                self.upViewList.push({
                  'saleMarket': item.saleMarket,
                  "saleName":item.shipWarehouse,
                  'name': item.name,
                  "voucherId":item.voucherId,
                  "createTime":new Date().format(item.createDate,"yyyy/MM/dd"),
                  "auditState":item.auditState,
                  'weight':item.voucherGoodsVVO.number,
                  'deliveryTime': item.deliveryTime,
                  'voucherGoodsVVO': item.voucherGoodsVVO
                })
              });
            } else {
            }
          });
        }else{
          self.$refs.loadmoreTip.end();
        }
      },
    },

    created() {
    },

    components: {
      topBar,
      split,
      'my-scroll':scroll,
      'my-scrollTip':scrollTip,
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
