<template>
  <div>
    <div class="z-p-follow" @click="notice">
      <div class="z-p-love" :class="{loveActive:attentionF}" v-if="mark== 1">
      </div>
      <div class="z-p-f" v-if="mark== 1">
        {{attentionF?"已收藏":"收藏"}}
      </div>
      <div class="z-p-star" :class="{startActive:attentionF}" v-if="mark== 2">
      </div>
      <div class="z-p-f" v-if="mark== 2">
        {{attentionF?"已关注":"关注"}}
      </div>
    </div>
    <div class="z-p-follow z-p-characteristic" v-if="mark== 1" @click="characteristicFirst">
      <div class="z-p-guidence">
      </div>
      <div class="z-p-f">
         分享
      </div>
    </div>
    <div class="z-p-phone" :class="{markFirst:mark == 1}">
      <a @click.stop="phoneClick" :href="`tel:${mobile}`" class="c-phone" >打电话</a>
    </div>
  </div>
</template>
<style>
@import "../../assets/css/phoneC.css";
</style>
<script>
  import API from '../../api/api'
  const api = new API();
export default{
    data(){
      return{
          attentionF:this.attentionFlag,//关注的标志位
      }
    },
    methods:{
      characteristicFirst(){
        //特性一 分享引导图
        this.$emit('share');
      },
      notice(){
        var flag = null;
        if(this.attentionF){
          this.attentionF = false;
          flag = false;
        }else{
          flag = this.attentionF = true;
        }

        this.$emit("attention",flag)

      },
      phoneClick(){
        this.$emit("phone");
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
    props:["mobile","attentionFlag","mark"],
    watch:{
      "attentionFlag":function (curValue) {
        this.attentionF = curValue;
      }
    }

}
</script>
