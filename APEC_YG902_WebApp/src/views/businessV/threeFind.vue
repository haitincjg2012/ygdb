<template>
  <div class="c-threeFind">
    <div class="c-tF-com c-agencybg" @click.stop="agency">
      <p class="c-tF-text-com">找代办</p>
    </div>
    <div class="c-tF-com c-coldbg" @click.stop="cold">
      <p class="c-tF-text-com">找冷库</p>
    </div>
    <div class="c-tF-com c-traderbg" @click.stop="trade">
      <p class="c-tF-text-com">找客商</p>
    </div>
  </div>
</template>
<style>
  @import "../../assets/css/threeFind.css";
</style>
<script>
export default{
    data(){
        return {

        }
    },
    methods:{
      cold(){
        this.$router.push({name: 'cold',query:{name:"COLD"}});
      },
      agency(){
        this.$router.push({name: 'agency',query:{name:"AG"}});
      },
      trade(){
        this.$router.push({name: 'trader',query:{name:"TR"}});
      },
    }
}
</script>
