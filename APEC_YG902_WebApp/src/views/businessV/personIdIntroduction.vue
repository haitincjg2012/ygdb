<template>
  <div class="z-personIdIntroduction">
    <div class="z-p-path">
      <div class="z-p-part">
        <p class="z-p-number">{{noticeNum}}</p>
        <p><span class="z-p-style">关注</span></p>
      </div>
      <div class="z-p-part">
        <p class="z-p-number">{{bsNum}}</p>
        <p><span class="z-p-style">浏览</span></p>
      </div>
      <div class="z-p-part">
        <p class="z-p-number">{{concatNum}}</p>
        <p><span class="z-p-style">联系</span></p>
      </div>
    </div>
    <div class="z-p-path-line"></div>
    <div class="c-p-introduce">
      <div class="c-p-intro-com">
        <div class="c-p-intro-label">
          <span class="c-p-label-color">身份&emsp;&emsp;</span>
        </div>
        <div class="c-p-intro-text" v-html="identity">
        </div>
      </div>
      <div class="c-p-intro-com">
        <div class="c-p-intro-label">
          <span class="c-p-label-color">签名&emsp;&emsp;</span>
        </div>
        <div class="c-p-intro-text">
          <span class="z-p-sign-text">{{!remarkText?'急卖货，急找货，果来乐都能做':remarkText}}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<style>
  @import "../../assets/css/personIdIntroduction.css";
</style>
<script>
  export default{
      data(){
        return {
          noticeNum:this.notice,
          bsNum:this.bs,
          concatNum:this.concat,
          identity:this.identityHtml,
          remarkText:this.remark
        }
      },
      props:[ 'notice', 'bs','concat','identityHtml','remark'],
      watch:{
          "notice":function (curVal, oldVal) {
            this.noticeNum = curVal
          },
        "bs":function (curVal) {
          this.bsNum = curVal
        },
        "concat":function (curVal) {
          this.concatNum = curVal
        },
        "identityHtml":function (curVal) {
          this.identity = curVal
        },
        "remark":function (curVal) {
          this.remarkText = curVal
        }
      },
  }
</script>
