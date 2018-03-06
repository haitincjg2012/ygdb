<template>
    <li v-if="yes" >
        <img :src="item.src" class="show-img" :index = "item.index">
        <div @click="del" :index = "item.index" class="del-img">
          <img src="../assets/img/delIcon.png" class="c-del-picture-t" />
        </div>
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

  .c-del-picture-t{
    width: 1.05rem;
    height: 1.05rem;
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
