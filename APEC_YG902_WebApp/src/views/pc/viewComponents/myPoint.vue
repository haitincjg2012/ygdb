<template>
  <div class="m-point-view-info-page">
      <top-bar title="我的积分"></top-bar>
    <!--<scroller ref="my_scroller" style="top:42px;" :on-infinite="infinite">-->
    <div class="wrapper" ref="wrapper">
      <div>
        <div class="top-tip">
          <span class="refresh-hook">下拉刷新</span>
        </div>
        <div class="main-page">
          <div class="m-u-v-form-cli">
            <div class="m-p-form">
              <div class="m-p-label">积分汇总</div>
              <div class="m-p-point">{{point}}</div>
            </div>
            <split></split>
            <div class="m-p-table">
              <div class="m-p-cell-p">
                <span>我的等级：</span>
                <img :src='userLevelName'>
              </div>
            </div>
            <split></split>
            <div class="m-p-content-label">积分明细</div>
            <div  v-for="e in pointViewList" class="m-p-content">
              <span class="time">{{e.createDate}}</span>
              <span class="remark">{{e.remark}}</span>
              <span :class="e.class">+{{e.pointsChanged}}</span>
            </div>
          </div>
        </div>
        <div class="bottom-tip">
          <span class="loading-hook">查看更多</span>
        </div>
      </div>

    </div>
    <!--</scroller>-->
  </div>
</template>

<style scoped>
   @import "../../../assets/css/scroll.css";
</style>
<script>
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import {MessageBox, Indicator} from 'mint-ui';


  import QT from '../../../assets/img/t.png'//铜牌
  import BY from '../../../assets/img/y.png'//银牌
  import HJ from '../../../assets/img/j.png'//金牌
  import BJ from '../../../assets/img/bj-1.png'//铂金
  import ZS from '../../../assets/img/zs.png'//砖石
  import DS from '../../../assets/img/Ancrown@3x.png'//大师

  import BScroll from 'better-scroll';
  const api = new API();

  var fn = {
      dt:function () {
        this.$nextTick(() => {
          if (!this.scroll) {
            this.currentPageNo ++;
            this.reloadData(this.currentPageNo);
            this.scroll = new BScroll(this.$refs.wrapper)
            this.scroll.on('touchend', (pos) => {
//                   下拉动作
              if (pos.y > 50) {
                  //下拉刷新
//                      console.log();
//                    this.getPoint()
              }else if(pos.y < (this.scroll.maxScrollY - 30)) {
                const eleM = document.querySelector(".loading-hook");
                if(this.pageCount <= this.currentPageNo){
                  eleM.innerHTML = '数据加载完成...';
                }else{
                  eleM.innerHTML = '加载中...';
                  setTimeout(function () {
                    this.currentPageNo ++;
                    // 恢复文本值
                    eleM.innerHTML = '查看更多';
                    // 向列表添加数据
                    this.reloadData(this.currentPageNo);
                    // 加载更多后,重新计算滚动区域高度
                    this.scroll.refresh();
                  }.bind(this), 1000);
                }

              }
            })
          } else {

            this.scroll.refresh()
          }
        })
      }
  }
  export default {

    data() {
      return {
        pointViewList: [],
        userLevelName:'',//积分牌子
        point:'',
        currentPageNo:1,
        isActivated:true,
        pageCount:0
      }
    },
    activated () {
      this.isActivated = true;
      this.currentPageNo=1;
//      this.GetPointList();
      this.getPoint();
      this.userLevelName = this.userLevelKeySwitch(this.$store.state.userLevelName || c_js.getLocalValue('userLevelName') || '');
      console.log(this.$store.state.userLevelName, c_js.getLocalValue('userLevelName'), 888888);
      this.point = this.$store.state.point || c_js.getLocalValue('point') || 0;
    },
    deactivated () {
      this.isActivated = false
    },
    up(){},
    move(){},
    down(){},
    transition(){},
    mounted(){
//      this.$refs.my_scroller.finishInfinite(true);
    },

    methods: {
      userLevelKeySwitch(key){//用户积分转换
        switch (key) {
          case 'QT':
            return QT;
            break;
          case 'BY':
            return BY;
            break;
          case 'HJ':
            return HJ;
            break;
          case 'BJ':
            return BJ;
            break;
          case 'ZS':
            return ZS;
            break;
          case 'DS':
            return DS;
            break;
          default:
            return '';
            break;
        }
      },
      GetPointList(){//获取信息
        const self = this;
        self.pointViewList = [];
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-user-service/userpoint/pageUserPointRecords.apec",
          data: {
            pageNumber: self.currentPageNo
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              item.data.rows.forEach((item) => {
                self.pointViewList.push({
                  'createDate': new Date().format(item.createDate, 'yyyy-MM-dd'),
                  'pointsChanged': item.pointsChanged,
                  'remark': item.remark,
                  'class':item.pointsChanged-0>0?'blue':'red'
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
          api: "/yg-user-service/userpoint/pageUserPointRecords.apec",
          data: {
            pageNumber: self.currentPageNo
          }
        };
        try {
          api.post(params).then((res) => {
            var item = res.data;
//            fn(item);
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
              data.data.rows.forEach((item)=>{
                self.pointViewList.push({
                'createDate': new Date().format(item.createDate, 'yyyy-MM-dd'),
                'pointsChanged': item.pointsChanged,
                'remark': item.remark,
                'class':item.pointsChanged-0>0?'blue':'red'
              })
              });
            } else {
            }
          });
        done(true);
        }, 1500);
      },
      getPoint(){
        this.list();
      },
      list(){
        const self = this;
        self.pointViewList = [];
        let params = {
          api: "/yg-user-service/userpoint/pageUserPointRecords.apec",
          data: {
            pageNumber: self.currentPageNo
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              self.pageCount = item.data.pageCount;
              item.data.rows.forEach((item) => {
                self.pointViewList.push({
                  'createDate': new Date().format(item.createDate, 'yyyy-MM-dd'),
                  'pointsChanged': item.pointsChanged,
                  'remark': item.remark,
                  'class':item.pointsChanged-0>0?'blue':'red'
                })
              });
              fn.dt.bind(this)();
            } else {
            }
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      reloadData(){
          var arg = arguments[0];
          var pageNumber = arg? arg:self.currentPageNo;
        const self = this;
//        self.pointViewList = [];
        let params = {
          api: "/yg-user-service/userpoint/pageUserPointRecords.apec",
          data: {
            pageNumber: self.currentPageNo
          }
        }

        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
              item.data.rows.forEach((item) => {
                self.pointViewList.push({
                  'createDate': new Date().format(item.createDate, 'yyyy-MM-dd'),
                  'pointsChanged': item.pointsChanged,
                  'remark': item.remark,
                  'class':item.pointsChanged-0>0?'blue':'red'
                })
              });
            } else {
            }
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
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
  .m-point-view-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    font-size (15/_rem)
    .m-p-form
      height (113/_rem)
      background-color #2FCAA7
      text-align center
      .m-p-label
        color #ffffff;
        opacity:0.5;
        font-size (15/_rem)
        padding-top (28/_rem)
      .m-p-point
        color #ffffff;
        font-size (50/_rem)
    .m-p-table
      height (45/_rem)
      line-height (45/_rem)
      margin-left (5/_rem)
      .m-p-cell-p
        span
          font-size (14/_rem)
          vertical-align middle
        img
          width (25/_rem)
          height:(25/_rem)
          vertical-align middle
      .m-p-cell-l
        text-align center
    .m-p-content-label
      height (45/_rem)
      line-height (45/_rem)
      padding-left  (5/_rem)
      border-bottom 1px solid #D7D7D7
    .m-p-content
      height (45/_rem)
      line-height (45/_rem)
      border-bottom 1px solid #D7D7D7
      position relative
      .time
        color #717375
        margin-left (10/_rem)
      .remark
        margin-left (15/_rem)
      .blue
        color #222328
        position absolute
        top 50%
        transform translateY(-50%)
        right (20/_rem)
      .red
        color red
        top 50%
        transform translateY(-50%)
        right (20/_rem)
    .m-p-content-nr
      height (45/_rem)
      line-height (45/_rem)
      background-color #f4f4f4
    .m-u-v-content
      width 100%
      margin (10/_rem)
      font-size (14/_rem)
      tr
        height 30px
        line-height 30px
        text-align left

</style>
