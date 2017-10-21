<template>
  <div class="person-info-page-del">
    <top-bar title="供应商信息"></top-bar>
    <div class="main-page">
      <div class="p-v-info">
        <div class="p-v-info-main">
          <span class="p-v-info-pS">头像</span>
          <img class="p-v-info-user" :src="supplyInfo.imgT">
        </div>
      </div>
      <split></split>
      <div class="p-v-form-cli">
        <span class="label">昵称</span>
        <span class="value">{{supplyInfo.name}}</span>
      </div>
      <div class="solid-line">
      </div>
      <div class="p-v-form-cli">
        <span class="label">电话</span>
        <span class="value">{{supplyInfo.mobile}}</span>
      </div>
      <div class="solid-line">
      </div>
      <div class="p-v-form-cli">
        <span class="label">身份</span>
        <div class="s-a-box-db"><span>{{supplyInfo.userTypeKey}}</span>
        </div>
      </div>
      <split></split>
      <div class="p-v-form-cli">
        <span class="label">所在地区</span>
        <span class="value">{{supplyInfo.address}}</span>
      </div>
      <div class="solid-line-p">
      </div>
      <div class="p-v-form-cli">
        <span class="label">详细地址</span>
        <span class="value">{{supplyInfo.addressDetail}}</span>
      </div>
      <div class="solid-line-p">
      </div>
      <div class="p-v-form-cli">
        <span class="label">主营品种</span>
        <span class="value">{{supplyInfo.mainOperating}}</span>
      </div>
      <div class="solid-line-p">
      </div>
      <div class="p-v-form-cli">
        <span class="label">从业年限</span>
        <span class="value">{{supplyInfo.workOfYear}}</span>
      </div>
      <div class="solid-line-p">
      </div>
    </div>
  </div>
</template>

<script>
  import split from '../../../components/split/split'
  import topBar from '../../../components/topBar/topBar'
  import API from '../../../api/api'
  import c_js from '../../../assets/js/common'
  import {MessageBox, Toast} from 'mint-ui';

//  import userImgUrl from '../../../assets/img/p.png'

  const api = new API();

  var fn = {
    supplyInfoFn:function (data) {
      if(data.succeed){
          data = data.data;
          console.log();
        this.supplyInfo={
            "imgT":data.imgUrl,
          "name":data.name,
          "addressDetail":data.addressDetail,
          "userTypeKey":data.userTypeKey,
          "address":data.address,
          "workOfYear":data.workOfYear,
          "mainOperating":data.mainOperating,
          "mobile":data.mobile
        }
      }
    }
  };

  export default {
    data() {
      return {
        imgUrl:"",
        supplyInfo:{
        },
        UserId: this.$route.params.id
      }
    },
    activated(){
      this.UserId=this.$route.params.id;
      this.getSupplyInfo();
    },
    methods: {
      getSupplyInfo(){//供应商个人信息
        var that= this ;
        if(!that.UserId)
          return;
        console.log(that.UserId);
        let params = {
          api:"/yg-user-service/user/findUserInfo.apec",
          data:{
            id:that.UserId
          }
        };
        this.post(params, fn.supplyInfoFn.bind(that));
      },
      post(params, fn){
        try {
          api.post(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
    },

    created() {
    },

    components: {
      split,
      topBar
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .person-info-page-del
    position: fixed;
    top: 0;
    left: 0;
    height 100%;
    width 100%;
    .p-v-info
      height (70 /_rem)
      position relative
      .p-v-info-main
        line-height  (70 /_rem)
        .p-v-info-pS
          margin-left (15/_rem)
          font-size (15/_rem)
          color #7e7e7e
          line-height  (70 /_rem)
        .p-v-info-user
          border-radius 50%
          height (60 /_rem)
          width (60 /_rem)
          position relative
          vertical-align middle
    .p-line-top
      height (50 /_rem);
      display flex;
      .line-one, .line-two, .line-three
        flex 1
        vertical-align middle
        line-height (40 /_rem)
        text-align center
        margin (5 /_rem) 0
        img
          width (20 /_rem)
          height (20 /_rem)
          vertical-align middle
        span
          font-size (12 /_rem)
      .line-one, .line-two
        border-right 1px solid #f4f4f4

    .p-v-form-cli:last-of-type
      margin-bottom 10px
    .p-v-form-cli
      height (45 /_rem)
      line-height (45 /_rem)
      margin-left (15 /_rem)
      position relative
      .arrow
        width (10 /_rem)
        height (10 /_rem)
        position absolute
        right (20 /_rem)
        top 50%
        transform translateY(-50%)
      .s-a-box-db
        width (60 /_rem)
        border 1px solid #28cba7
        border-radius 4px
        text-align center
        display inline-block
        cursor pointer
        height (20 /_rem)
        line-height (20 /_rem)
        font-size 0
        color #28cba7
        margin-left (30 /_rem)
      .updateBtn
        position absolute
        top 50%
        transform translateY(-50%)
        right (20 /_rem)
        font-size (14 /_rem)
        color #28cba7
      .label
        color #7e7e7e
      .value
        color #111111
        margin-left (30 /_rem)
      span
        font-size (15 /_rem)
    .solid-line
      margin 0 0 0(50 /_rem)
      height 1px
      background-color #D7D7D7
    .solid-line-p
      margin 0 0 0(75 /_rem)
      height 1px
      background-color #D7D7D7

</style>
