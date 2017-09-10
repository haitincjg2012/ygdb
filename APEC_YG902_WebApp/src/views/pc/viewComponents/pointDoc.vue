<template>
  <div class="point-doc-info-page">
    <scroller>
      <top-bar title="积分文档"></top-bar>
      <div class="main-page">
        <div v-html="pointDocContent" class="p-form-content"></div>
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
