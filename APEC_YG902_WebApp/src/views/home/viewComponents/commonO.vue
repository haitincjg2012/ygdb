<template>
  <div class="z-person">
    <div class="z-p-head-t">
      <h1 class="z-p-h-text">{{person.title}}</h1>
      <div class="return" @click="back">
        <img src="../../../assets/img/ret.png">
      </div>
    </div>
    <div class="z-bannerImg">
      <img src="../../../assets/img/xqimg1.png">
      <img src="../../../assets/img/tool-1.png" class="z-head-portrait">
    </div>
    <div class="z-p-info">
      <div>
        <span class="z-p-name">{{person.name}}</span>
        <span class="z-p-real">{{person.real}}</span>
        <img :src="person.level" class="z-p-level">
      </div>
      <p class="z-p-x-agency" v-if="agenyFlag">{{person.ageny}}</p>
      <div class="z-p-path">
        <div class="z-p-part">
          <p class="z-p-number">{{person.notice}}</p>
          <p>关注我</p>
        </div>
        <div class="z-p-part">
          <p class="z-p-number">{{person.bs}}</p>
          <p>浏览我</p>
        </div>
        <div class="z-p-part">
          <p class="z-p-number">{{person.concat}}</p>
          <p>联系我</p>
        </div>
        <div class="z-p-follow">
          <div>
            关注
          </div>
        </div>
      </div>
    </div>
    <div class="z-space"></div>
    <div class="z-p-des">
      <div class="z-p-des-area">
        <img class="z-p-des-icon" src="../../../assets/img/pos.png">
        <label>所在区域:</label>
        <span>{{person.address}}</span>
      </div>
      <div class="z-p-des-pz">
        <img class="z-p-des-icon" src="../../../assets/img/pos.png">
        <label>主营品种:</label>
        <span>{{person.pz}}</span>
      </div>
      <div class="z-p-des-market">
        <img class="z-p-des-icon" src="../../../assets/img/pos.png">
        <label>主营市场:</label>
        <span>{{person.market}}</span>
      </div>
      <div class="z-p-des-text">
        <img class="z-p-des-icon" src="../../../assets/img/pos.png">
        <label>实力描述:</label>
        <p class="z-p-des-content">{{person.des}}</p>
      </div>
    </div>
    <div class="z-space"></div>
  </div>
</template>
<style scoped>
  @import "../../../assets/css/person.css";
</style>
<script>
  export default{
    data(){
      return{
        person:{
          title:"冷库详情",
          bannerImgUrl:"#",
          des:"11111111111111111111111111",
          level:"#",
          ageny:"烟台栖霞苹果合作社",
          notice:"19",
          bs:"222",
          concat:"55",
          name:"张三丰",
          real:'实名认证',
          market:'山东 湖北 广州',
          weight:"19万斤",
          rank:"19名"
        },
        agenyFlag:false
      }
    },
    methods:{
      back(){
        this.$router.go(-1);
      }
    },
    activated(){

    }
  }
</script>
