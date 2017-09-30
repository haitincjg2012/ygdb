<!--我的收藏-->

<template>
  <div class="Jhds_co">
    <scroller ref="my_scroller" :on-infinite="infinite">
      <top-bar title="我的收藏"></top-bar>
      <div class="tab-outer">
        <ul class="tab-inner">
          <li v-for="item in myCollectList">
            <div class="list" @click="xq" :data-id = "item.id">
              <div class="left-img" :data-id = "item.id">
                <img :src="item.firstImageUrl" :data-id = "item.id">
                <img class="imglabel" :src="item.imgLabel" :data-id = "item.id">
              </div>
              <div class="right-content" :data-id = "item.id">
                <h4 :data-id = "item.id">
                  {{item.skuName}}
                </h4>
                <div class="address" :data-id = "item.id">
                  {{item.address}}
                </div>
                <div class="people" :data-id = "item.id">{{item.showUserName}}
                  <div @click.stop="cancleCollect($event,item.id)" class="r-click">
                    <span>取消收藏</span>
                  </div>
                </div>
              </div>
            </div>
            <split></split>
          </li>
        </ul>
      </div>
      <div class="c-empty-collect-show" v-if="collectFlag" >
        <img src="../../../assets/img/noCollect.png"/>
        <!--<p class="c-z-collect">暂无收藏</p>-->
      </div>
    </scroller>
  </div>
</template>
<style>
@import "../../../assets/css/collect.css";
</style>
<script>
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import {MessageBox, Indicator} from 'mint-ui';

  import buy from '../../../assets/img/b.png'
  import sell from '../../../assets/img/s.png'


  const api = new API();

  export default {

    data() {
      return {
          myCollectList:[],
        isActivated:true,
        currentPageNo:1,
        collectFlag:false,//是否有收藏
      }
    },

    mounted(){
      this.$refs.my_scroller.finishInfinite(true);
    },

    activated () {
        this.collectFlag = false;
      this.GetMyCollect();
      this.isActivated = true
    },
    deactivated () {
      this.isActivated = false
    },

    methods: {
      GetMyCollect(){//获取收藏信息
        const self = this;
        self.myCollectList = [];
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/_node_product/_user_list.apno",
          data: {
            pageNumber: self.currentPageNo
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              item.data.rows.forEach((item) => {
                self.myCollectList.push({
                  'skuName': item.skuName.length>15?item.skuName.substring(0,15)+'...':item.skuName,
                  'id':item.id,
                  'firstImageUrl':item.firstImageUrl,
                  'productTypeName':item.productTypeName,
                  'address':item.address,
                  'showUserName':item.showUserName,
                  'imgLabel':item.productTypeName =='求购'?buy:sell
                })
              });
              if(self.myCollectList.length == 0){
                  self.collectFlag = true;
              }
            } else {
            }
            console.log(self.myCollectList);
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
          api: "/_node_product/_user_list.apno",
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
            if (data.succeed && data.data.rows.length) {
              data.data.rows.forEach((item) => {
                self.myCollectList.push({
                  'skuName': item.skuName.length>15?item.skuName.substring(0,15)+'...':item.skuName,
                  'id':item.id,
                  'firstImageUrl':item.firstImageUrl,
                  'productTypeName':item.productTypeName,
                  'address':item.address,
                  'showUserName':item.showUserName,
                  'class':item.productTypeName =='求购'?'r-c-label red':'r-c-label blue'
                })
              });
            } else {
            }
          });
          done(true);
        }, 1500);
      },
      cancleCollect(e,id){
        if(!id)
            return;
        const self = this;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/_node_user/_save_product.apno",
          data: {
            elasticId:id,
            salveFlag:false
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              self._outItem(id);
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
      _outItem(id){
        const self = this;
        var i = self.myCollectList.findIndex((i)=>
          {
            return id == i.id
          }
        );
        if (i > -1) {
          self.myCollectList.splice(i, 1);
        }
      },
      xq(evt){
        var e = evt || window.event;
        var target = e.toElement || e.srcElement;
        var id = target.dataset.id;
        this.$router.push({path: '/detail/' + id});
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

<style lang="stylus" rel="stylesheet/stylus" scoped>
  _rem = 20rem;
  .Jhds_co
    font-size: (14/_rem);
    margin: 0;
    color: #252525;
    position: relative;
    .no-more-list
      height (25/_rem)
      line-height (25/_rem)
      text-align center
      padding (10/_rem)
    .tab-title
      font-size: 0;
      padding: 15px;
      text-align: center;
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
        li
          position: relative;
          background-color: #fff;
          .list
            padding (10/_rem)
            display:-moz-box;
            display:-webkit-box;
            display:box;
            .left-img
              position relative
              -moz-box-flex:1;
              -webkit-box-flex:1;
              box-flex:1;
              .imglabel
                width (36 /_rem)
                border-radius 4px
                text-align center
                display inline-block
                height (16 /_rem)
                line-height (16 /_rem)
                color #fff
                position absolute
                left 0
                bottom 0
              img
                height (85/_rem)
                display block
                width (85/_rem)
            .right-content
              -moz-box-flex:3;
              -webkit-box-flex:3;
              box-flex:3;
              h4
                font-size (15/_rem)
                line-height (30/_rem)
                height (30/_rem)
                font-weight bold
              .address
                height (25/_rem)
                line-height (25/_rem)
                font-size (15/_rem)
              .people
                height (25/_rem)
                line-height (25/_rem)
                font-size (15/_rem)
                position relative
                .r-click
                  width (80 /_rem)
                  border 1px solid #28cba7
                  border-radius 4px
                  text-align center
                  display inline-block
                  cursor pointer
                  height (20 /_rem)
                  line-height (20 /_rem)
                  color #28cba7
                  position absolute
                  top 50%
                  transform translateY(-50%)
                  right (20/_rem)
        li:before
          top:0
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

</style>
