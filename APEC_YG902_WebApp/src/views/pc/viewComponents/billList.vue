<template>
  <div class="billSel-info-page">
    <div ref="goodsListWrapper">
      <ul>
        <top-bar title="选择商品"></top-bar>
    <div v-show="hisGoodsList.length" class="ul-goods-list">
      <h6>历史记录(点击选择历史商品)</h6>
      <ul class="clearfix">
        <li @click.stop="selHisItem($event,item)" v-for="item in hisGoodsList" :class="item.class">{{item.attrValue}}
        </li>
      </ul>
      <split></split>
    </div>
    <div class="ul-goods-list">
      <h6>商品</h6>
      <ul class="clearfix">
        <li @click.stop="selXItem($event,item,'商品')" v-for="item in goodList" :class="item.class">{{item.attrValue}}
        </li>
      </ul>
    </div>
    <div class="ul-goods-list">
      <h6>规格</h6>
      <ul class="clearfix">
        <li @click.stop="selXItem($event,item,'规则')" v-for="item in sort" :class="item.class">{{item.attrValue}}</li>
      </ul>
    </div>
    <div class="ul-goods-list">
      <h6>等级</h6>
      <ul class="clearfix">
        <li @click.stop="selXItem($event,item,'等级')" v-for="item in level" :class="item.class">{{item.attrValue}}</li>
      </ul>
    </div>
    <div class="ul-goods-list">
      <h6>颜色</h6>
      <ul class="clearfix">
        <li @click.stop="selXItem($event,item,'颜色')" v-for="item in color" :class="item.class">{{item.attrValue}}</li>
      </ul>
    </div>
    <div class="login-btn">
      <input class="btn-login-c login-confirm" type="submit" id="btn-login-code" value="保存" @click="saveBtn"></input>
    </div>
      </ul>
    </div>
  </div>
</template>

<script>
  import BScroll from 'better-scroll'
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import keyBoard from '../../../components/keyboard/keyboard'
  import {Indicator, Toast} from 'mint-ui';


  const api = new API();

  export default {

    data() {
      return {
        goodList: [],//商品
        sort: [],//规格
        level: [],//等级
        color: [],//颜色
        hisGoodsList: [],//历史商品
        hisGoodsSel:{},
        goods: {},
        sorts: {},
        levels: {},
        colors: {},
        curGood: '',//id
        curGoodName: '',//名称
        curSort: '',
        curLevel: '',
        curColor: '',
        curSelData: {}
      }
    },
    mounted(){
      this.getGoodsList();

    },
    activated(){
      this.getHistoryList();
    },
    methods: {
      selXItem(e, item, ind){
        const self = this;
        switch (ind) {
          case '商品':
            self.goodList.forEach((i) => {
              if (item.id === i.id) {
                i.class = 'visited s-a-box-db';
                self.goods['attrValueId'] = i.id;
                self.goods['attrValue'] = i.attrValue;
              }
              else
                i.class = 's-a-box-db'
            });
            break;
          case '规则':
            self.sort.forEach((i) => {
              if (item.id === i.id) {
                i.class = 'visited s-a-box-db';
                self.sorts['attrValueId'] = i.id;
                self.sorts['attrValue'] = i.attrValue;
              }
              else
                i.class = 's-a-box-db'
            });
            break;
          case '等级':
            self.level.forEach((i) => {
              if (item.id === i.id) {
                i.class = 'visited s-a-box-db';
                self.levels['attrValueId'] = i.id;
                self.levels['attrValue'] = i.attrValue;
              }
              else
                i.class = 's-a-box-db'
            });
            break;
          case '颜色':
            self.color.forEach((i) => {
              if (item.id === i.id) {
                i.class = 'visited s-a-box-db';
                self.colors['attrValueId'] = i.id;
                self.colors['attrValue'] = i.attrValue;
              }
              else
                i.class = 's-a-box-db'
            });
            break;
          default:
            break;
        }

      },
      selHisItem(e,item){
        const self =this;
        self.hisGoodsList.forEach((i) => {
          if (item.id === i.id) {
            i.class = 'visited s-a-box-db-long';
            self.hisGoodsSel['attrValueId'] = i.id;
            self.hisGoodsSel['attrValue'] = i.attrValue;
          }
          else
            i.class = 's-a-box-db-long'
        });
        self.$store.commit("incrementuploadGoodList", {'uploadGoodList': self.hisGoodsSel});
        this.$router.go(-1);
      },
      getGoodsList(){//获取商品大类
        const self = this;
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-goods-service/goods/getGoods.apec",
          data: {}
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              var data = item.data.goodsAttrVOList;
              data.forEach((e) => {
                var e_data_title = e.goodsAttrName;
                var e_data = e.attributeValueVOS;
                if (e_data_title == '商品') {
                  self.goods = {
                    "attrId": e.attrId,
                    "attrName": e.goodsAttrName,
                    "attributeType": e.attributeType,
                    "attributeShowLevel": e.attributeShowLevel,
                    "sort": e.sort
                  };
                  e_data.forEach((p_e, index) => {
                    self.goodList.push({
                      'attrValue': p_e.attrValue,
                      'id': p_e.id,
                      'class': index == 0 ? 's-a-box-db visited' : 's-a-box-db',
                      'sort': p_e.sort
                    });
                    if (index == 0) {
                      self.goods['attrValueId'] = p_e.id;
                      self.goods['attrValue'] = p_e.attrValue;
                    }
                  });
                } else if (e_data_title == '规格') {
                  self.sorts = {
                    "attrId": e.attrId,
                    "attrName": e.goodsAttrName,
                    "attributeType": e.attributeType,
                    "attributeShowLevel": e.attributeShowLevel,
                    "sort": e.sort
                  };
                  e_data.forEach((p_e, index) => {
                    self.sort.push({
                      'attrValue': p_e.attrValue,
                      'id': p_e.id,
                      'class': index == 0 ? 's-a-box-db visited' : 's-a-box-db',
                      'sort': p_e.sort
                    });
                    if (index == 0) {
                      self.sorts['attrValueId'] = p_e.id;
                      self.sorts['attrValue'] = p_e.attrValue;
                    }
                  });
                } else if (e_data_title == '等级') {
                  self.levels = {
                    "attrId": e.attrId,
                    "attrName": e.goodsAttrName,
                    "attributeType": e.attributeType,
                    "attributeShowLevel": e.attributeShowLevel,
                    "sort": e.sort
                  };
                  e_data.forEach((p_e, index) => {
                    self.level.push({
                      'attrValue': p_e.attrValue,
                      'id': p_e.id,
                      'class': index == 0 ? 's-a-box-db visited' : 's-a-box-db',
                      'sort': p_e.sort
                    });
                    if (index == 0) {
                      self.levels['attrValueId'] = p_e.id;
                      self.levels['attrValue'] = p_e.attrValue;
                    }
                  });
                } else if (e_data_title == '颜色') {
                  self.colors = {
                    "attrId": e.attrId,
                    "attrName": e.goodsAttrName,
                    "attributeType": e.attributeType,
                    "attributeShowLevel": e.attributeShowLevel,
                    "sort": e.sort
                  };
                  e_data.forEach((p_e, index) => {
                    self.color.push({
                      'attrValue': p_e.attrValue,
                      'id': p_e.id,
                      'class': index == 0 ? 's-a-box-db visited' : 's-a-box-db',
                      'sort': p_e.sort
                    });
                    if (index == 0) {
                      self.colors['attrValueId'] = p_e.id;
                      self.colors['attrValue'] = p_e.attrValue;
                    }
                  });
                } else {
                }
              });
              self.$nextTick(() => {
                self._initScroll()
              })
              Indicator.close();
            } else {
            }
          }).catch((error) => {
              console.log(error)
              Indicator.close();
            }
          )
        } catch (error) {
          console.log(error)
          Indicator.close();
        }
      },
      getHistoryList(){
        const self = this;
        self.hisGoodsList = [];
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/_node_user/_product_his.apno",
          data: {}
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              let _data = (item.data);
              _data.forEach((e) => {
                self.hisGoodsList.push({
                  'attrValue': e.skuName,
                  'id': e.skuId,
                  'class': 's-a-box-db-long'
                })
              });
            } else {
            }
            Indicator.close();
          }).catch((error) => {
              console.log(error)
              Indicator.close();
            }
          )
        } catch (error) {
          console.log(error)
          Indicator.close();
        }
      },
      saveBtn(){
        const self = this;
        var productAttrs = [];
        productAttrs.push(self.goods);
        productAttrs.push(self.sorts);
        productAttrs.push(self.levels);
        productAttrs.push(self.colors);
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-product-service/product/addProductAttr.apec",
          data: {
            productAttrs: productAttrs
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              if(item.data.length){
                self.hisGoodsSel['attrValueId'] = item.data[0].skuId;
                self.hisGoodsSel['attrValue'] = item.data[0].skuName;
                self.$store.commit("incrementuploadGoodList", {'uploadGoodList': self.hisGoodsSel});
                this.$router.go(-1);
              }
            } else {
              Toast("地址保存失败~");
              this.$router.go(-1);
            }
            Indicator.close();
          }).catch((error) => {
              this.$router.go(-1);
              console.log(error)
              Indicator.close();
            }
          )
        } catch (error) {
          console.log(error)
          this.$router.go(-1);
          Indicator.close();
        }
      },
      _initScroll() {
        var winHeight = window.innerHeight;
        var mainHeight = winHeight-42;
        var ul = this.$refs.goodsListWrapper;
        ul.style.height = mainHeight + "px";
        if (!this.scroll) {
          this.scroll = new BScroll(this.$refs.goodsListWrapper, {
            click: true,
            probeType: 3
          })
        } else {
          this.scroll.refresh()
        }
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
  .billSel-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    font-size (15 /_rem)
    h6
      margin (5 /_rem) 0 (5 /_rem) (20 /_rem)
      font-size (20 /_rem)
    .ul-goods-list
      .clearfix
        .s-a-box-db-long
          min-width (50/_rem)
          border 1px solid #CCD0D1
          border-radius 4px
          text-align center
          display inline-block
          cursor pointer
          height (25 /_rem)
          line-height (25 /_rem)
          color #999999
          padding (1 /_rem)
          margin (5 /_rem) 0 (5 /_rem) (20 /_rem)
        .s-a-box-db
          min-width  (65 /_rem)
          border 1px solid #CCD0D1
          border-radius 4px
          text-align center
          display inline-block
          cursor pointer
          height (25 /_rem)
          line-height (25 /_rem)
          color #999999
          padding (1 /_rem)
          margin (5 /_rem) 0 (5 /_rem) (20 /_rem)
          span
            font-size (15 /_rem)
        .visited
          background-color #28CBA7
          color #ffffff !important
    .login-btn
      margin (35 /_rem) (15 /_rem) 0 (15 /_rem)
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
