<!--
c-personInfo:这一个块的样式
c-pI：c-personHot简写
sy：style的简写
-->
<template>
  <div class="c-personInfo">
    <div class="c-pI-com">
      <div class="c-pI-label">
        <span>身份&emsp;&emsp;</span>
      </div>
      <div class="c-pI-Img" v-if="picture">
         <img :src="picture" class="c-pI-levelPicture" />
      </div>
      <div class="c-pI-identify" v-html="identityHtml">
      </div>
    </div>
    <div class="c-pI-com">
      <div class="c-pI-label c-pI-signatureName">
        <span>签名&emsp;&emsp;</span>
      </div>
      <div class="c-pI-signature" v-html="signatureHtml">
        <!--<span class="c-pI-sC-textsy">{{person.userOrgClientVO.remark == "" ? '在果来乐,挣钱不重要,开心最重要' : person.userOrgClientVO.remark}}</span>-->
      </div>
    </div>
  </div>
</template>
<style>
  @import "../../assets/css/personInfoC.css";
</style>
<script>
  export default{
    data(){
      return{
        html:this.personInfo,
      }
    },
    methods:{},
    props:['identityHtml','signatureHtml','picture'],
    mounted(){
    },
    watch:{
    }
  }
</script>
