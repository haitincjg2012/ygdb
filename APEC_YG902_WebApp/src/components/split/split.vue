<template>
  <div class="split"></div>
</template>

<script>
  export default {
    props: {
      height:'',
      background:''
    },
    computed:{
        sty(){
            return `height:${this.height};background:${this.background};`
        }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .split
    width: 100%
    height: (10/_rem)
    background: #f4f4f4
</style>
