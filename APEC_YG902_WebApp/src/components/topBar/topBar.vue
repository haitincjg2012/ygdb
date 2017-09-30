<template>
  <div data-spm="h.0.31.0" class="top-bar" :class="coClass">
    <div class="btn-back" data-spm="h.0.0.100" @click="back">
      <span v-if="isCancel"></span>
      <img  src="../../assets/img/ret.png" v-else>
    </div>
    <div class="top-title">
      <span class="text-title">{{title}}</span>
    </div>
    <div class="btn-menu" data-spm="h.0.0.100" @click="_leftClick">
      {{menuLeft}}
    </div>
  </div>
</template>

<script>

  export default {
    props: {
      title: {
        type: String
      },
      menuLeft: {
        type: String
      },
      isCancel: {
        type: Boolean,
        default: false
      },
      orClass: {
        type: String   //自定义样式
      }
    },
    methods: {
      back() {
          var login = this.$route.name;
          if(login && login == "login"){
              this.$router.push({name:"home"});
          }else{
            this.$router.go(-1)
          }

      },
      _leftClick() {
        this.$emit('rightBtn')
      }
    },
    computed: {
      coClass(){
        return this.orClass;
      }
    },
    watch: {
      isCancel: function (newVal) {
        this.isCancel = newVal
      }
    },
  }

</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import "../../assets/stylus/mixin.styl"
  _rem = 20rem;
  .top-bar
    border-1px(rgba(7, 17, 27, 0.1))
    background-color #fff
    .top-title
      height 42px
      width 100%
      font-size 0
      text-align center
      background-color #f4f4f4
      .text-title
        line-height 42px
        font-size 16px
        vertical-align top
    .btn-back
      position absolute
      top 0
      left 0
      width 42px
      height 42px
      line-height 42px
      text-align center
      z-index 99
      span
        font-size (16/_rem)
      .icon-arrow_lift
        font-size 20px
        color #28CBA7
      img
        width (11 / _rem)
        height (19.5 / _rem)
        vertical-align: middle;
    .btn-menu
      position absolute
      top 0
      right 0
      height 42px
      line-height 42px
      color: #28CBA7
      margin-right 10px
</style>
