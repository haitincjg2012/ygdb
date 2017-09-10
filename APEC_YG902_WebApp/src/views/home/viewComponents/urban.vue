<template>
  <li :symbolId="symbolId"
      :path = "path"
      :Characteristic = "CharacteristicId"
      :vt = "value"
  >
    <div @click="search"  class='pitch' :class="{on:sh}">
      <img src="../../../assets/img/hack.png">
      <span>{{CharacteristicId}}{{tt}}</span>
    </div>
  </li>
</template>
<script>
  export default{
    methods:{
      search(event){
        var evt = event || window.event;
        var target = evt.toElement || evt.srcElement;
        var tag = target.tagName.toUpperCase(),classlist,parent;

        if(tag == "DIV"){
          parent = target.parentElement;
          classlist = target.classList;
        }else if(tag == "LI"){
          return;
        }else{
          parent = target.parentElement.parentElement;
          classlist = target.parentElement.classList;
        }
        var obj = {};
        var text = parent.getAttribute("Characteristic");
        var path = parent.getAttribute("path");
        var id = parent.getAttribute("symbolId");
        var vt = parent.getAttribute("vt");
        this.$store.state.urban = {
            path:path,
            flag:false
        }
        obj.text = text;
        obj.id = id;
        obj.value = vt;
        obj.path = path;
//
        var p = parent.parentElement;
        var child = p.children;
        for(var ik = 0; ik < child.length; ik ++){
          if(ik != path){
            child[ik].children[0].classList.remove("on")
          }else{
            child[ik].children[0].classList.add("on");
          }
        }
        this.$emit("rsss",obj)
      }
    },
    props:{
      symbolId:'',
      CharacteristicId:'',
      path:'',
      sh:false,
      value:''
    },
    computed:{
        tt:function () {
          var URBAN = this.$store.state.urban;
          if(!URBAN){
            var el = document.querySelector(".pitch");
            if(el){
              var childArr = el.parentElement.children;
              var len = childArr.length;
              for (var i = 0; i < len; i ++){
                childArr[i].classList.remove("on");
              }
            }

          }
        }
    },
    activated(){
    },
    updated(){

    }
  }
</script>
