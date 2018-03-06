<template>
   <div>
        <img :src="src">
   </div>
</template>
<style></style>
<script>
  import {Toast} from 'mint-ui'
  export default{
      data(){
          return {
              originsrc:this.src,
          }
      },
      props:['src'],
  }
</script>
