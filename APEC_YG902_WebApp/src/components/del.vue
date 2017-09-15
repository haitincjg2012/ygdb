<template>
    <li v-if="yes" >
        <img :src="item.src" class="show-img" @click="del" :index = "item.index">
        <!--<div @click="del" :index = "index" class="del-img">-->
             <!--x-->
        <!--</div>-->
    </li>
</template>
<style>
  .show-img{
    height: 4.025rem;
  }
  .del-img{
    position: absolute;
    top: 0;
    right: 0;
  }
</style>
<script>

    export default{
        data(){
              return{
                yes:true,
              }
        },
        props:{
            src:'',
          index:'',
          item:null
        },
        methods:{
            del(event){
                var evt = event || window.event;
                var target = evt.toElement || evt.srcElement;
                var index = target.getAttribute("index");
                var self = this, name = '';
                if(this.name){
                    name = this.name;
                }
                var obj = {
                    index:index,
                    name:self.name
                }
                this.yes = false;
                this.$emit('selecttype', index)
            }
        }
    }
</script>
