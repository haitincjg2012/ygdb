<template>
  <li :path = "item.path"
      :type = "item.type"
      :Characteristic = "item.keyword"
      >
    <div @click="search"  class='pitch' :class="{on:item.sh}" >
      <img src="../../../assets/img/hack.png">
      <span>{{item.keyword}}</span>
    </div>
  </li>
</template>
<script>
  export default{
      data(){
         return{}
      },
    methods:{
          search(e){
              var e = e || window.event;
              var target = e.toElement || e.srcElement,p;
              var tag = target.tagName.toUpperCase();
              if(tag == "LI"){
                  return;
              }else if(tag == "DIV"){
                  p = target.parentElement;
              }else{
                  p = target.parentElement.parentElement;
              }

              var obj = {};
              var path = p.getAttribute("path");
              var id = p.getAttribute("symbolId");
              var value = p.getAttribute("Characteristic");
              var type = p.getAttribute("type");
              obj.id = id;
              obj.path = path;
              obj.key = value;
              obj.type = type;
              obj.flag = this.item.sh;
              obj.code = this.item.code;
              this.$emit("rs", obj);
          }
    },
    props:{
          item:null
    }
  }
</script>
