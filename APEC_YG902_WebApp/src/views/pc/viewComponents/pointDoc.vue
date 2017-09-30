<template>
  <div class="point-doc-info-page">
    <scroller>
      <top-bar title="积分文档"></top-bar>
      <div class="main-page">
        <!--<div v-html="pointDocContent" class="p-form-content"></div>-->
        <div v-html="pointDocContent" class="p-form-content">
          <!--<h3>一．会员等级：</h3>-->
          <!--<p>-->
            <!--1.铜牌阶段：0—10级，每级50分，累计500分。-->
          <!--</p>-->
          <!--<p>-->
            <!--2.银牌阶段：11—20级，每级100分，累计1000分。-->
          <!--</p>-->
          <!--<p>-->
            <!--3.金牌阶段：21—30级，每级150分，累计1500分。-->
          <!--</p>-->
          <!--<p>-->
            <!--4.铂金阶段：31—40级，每级200分，累计2000分。-->
          <!--</p>-->
          <!--<p>-->
            <!--5.钻石阶段：41—49级，每级250分，累计2250分。-->
          <!--</p>-->
          <!--<p>-->
            <!--6.大师阶段：50级，每级2750分，累计2750分。-->
          <!--</p>-->
          <!--<p>-->
            <!--合计：等级50级，分数10000分。-->
          <!--</p>-->
          <!--<h3>-->
            <!--二. 积分规则-->
          <!--</h3>-->
          <!--<p class="Zadd">-->
            <!--“+”分项-->
          <!--</p>-->
         <!--<p>-->
           <!--1.实名认证（+300分）-->
         <!--</p>-->
          <!--<p>-->
            <!--2.交收单达到一定数量（每填写100吨+50分）-->
          <!--</p>-->
          <!--<p>-->
            <!--3.发布供求信息（每发布一条+2分）-->
          <!--</p>-->
          <!--<p>-->
            <!--4.好评累积（每1条好评+2分）-->
          <!--</p>-->
          <!--<p>-->
            <!--5.在线时长（一小时+1分，不足一小时按一小时计算）-->
          <!--</p>-->
          <!--<p>-->
            <!--6.每日首次登陆（1分）-->
          <!--</p>-->
          <!--<p>-->
            <!--7.签到抽积分（1   2  5   8   10   12  其中5概率最大40%，2占30%，1占20%，其他合占10%）-->
          <!--</p>-->
          <!--<p>-->
            <!--备注：积分可用于年底抽奖写进积分规则，具体规则产品用一段时间再给出（预测200积分抽一次，暂定）-->
          <!--</p>-->
          <!--<p class="Zsub">-->
            <!--“-”分项-->
          <!--</p>-->
          <!--<p>-->
            <!--1.货物品质与价格描述跟事实严重不符（后台人工判定，1次警告，后每次1次-20分）-->
          <!--</p>-->
          <!--<p>-->
            <!--2.盗用其他用户资质，货品介绍，货品图片等图片，视频信息（后台人工判定，1次警告，后每次次-20分）-->
          <!--</p>-->
          <!--<p>-->
            <!--3.恶意诋毁他人货品（后台人工判定，1次警告，2次-50分）-->
          <!--</p>-->
          <!--<p>-->
            <!--4.差评累积（每达到50条-50分）-->
          <!--</p>-->
          <!--<p>-->
            <!--5.举报（累计3条不同ID举报-50分）-->
          <!--</p>-->
          <!--<p>-->
            <!--6.恶意发布无关信息、虚假信息、虚假单据等（后台人工判定，一次警告，两次-4,3次-16,4次-64,以此类推，直至积分归零，情节严重者考虑注销号码）-->
          <!--</p>-->
        </div>
      </div>
    </scroller>
  </div>
</template>
<style>
  @import "../../../assets/css/js.css";
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
        pointDocTitle:'',
        pointDocContent:''
      }
    },

    mounted(){
      this.GetPointDoc();
    },

    methods: {
      GetPointDoc(){//获取信息
        const self = this;
        self.pointDoc = [];
        Indicator.open({
          text: '加载中...',
          spinnerType: 'fading-circle'
        });
        let params = {
          api: "/yg-cms-service/cms/articleList.apec",
          data: {
            category:'PRODUCT',
            channelCode:'POINT_RULE'
          }
        }
        try {
          api.post(params).then((res) => {
            var item = res.data;
            if (item.succeed) {
                var data = item.data[0];
                self.pointDocTitle = data.title;
                self.pointDocContent = data.content;
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
  .point-doc-info-page
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    font-size (16/_rem)
    .p-form-title
      text-align center
      padding (10/_rem)
      font-weight bold
    .p-form-content
      padding (20/_rem)
      text-align left
      line-height (25/_rem)

</style>
