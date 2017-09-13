<template>
  <li>
    <div @click="search"  class='pitch' :class="{on:item.sh}" >
      <img src="../../../assets/img/hack.png">
      <span>{{item.keyword}}</span>
    </div>
  </li>
</template>
<script>
  export default{
      methods:{
          search(e){
              var e = e || window.event;
              var target = e.toElement || e.srcElement;
              var tag = target.tagName.toUpperCase();
              if(tag == "LI"){
                  return;
              }
              if(this.item.sh){
                  return;
              }
              var obj = {};
              obj.path = this.item.path;
              obj.key = this.item.city + this.item.keyword;
              obj.code = this.item.code;


              this.$emit("rss",obj);
          }
      },
      props:{
          item:null
      }
  }
</script>
