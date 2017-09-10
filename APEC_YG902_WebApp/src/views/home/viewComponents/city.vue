<template>
  <li :path = "child.path"
      :Characteristic = "child.keyWord"
      :city = "child.city"
      :sign = "child.id"
     >
    <div @click="search"  class='pitch' :class="{on:child.sh}">
      <img src="../../../assets/img/hack.png">
      <span>{{child.keyWord}}</span>
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
        var id = "";
        if(tag == "DIV"){
          parent = target.parentElement;
          classlist = target.classList;

        }else if(tag == "LI"){
          return;
        }else{
          parent = target.parentElement.parentElement;
          classlist = target.parentElement.classList;

        }
        var text = parent.getAttribute("Characteristic");
        var city = parent.getAttribute("city")
        var path = parent.getAttribute("path");
        var id = parent.getAttribute("sign");
        var flag = parent.getAttribute("flag");
        var record = parent.getAttribute("record");
        var tt = "";
        var mm = "";
        if(path == 0){
           tt = city;
        }else{
          tt = city + text;
        }
        this.$store.state.recordArea = {
          id:id,
          path:path,
        }

        this.$emit("rs",tt)
      },
      sign(){
        var id = this.symbolId;
        var obj = this.$store.state.recordPZ;
        if(obj){
          if(id == obj.id){
            var idx = obj.path;
            if(idx == this.path){
              var parent = document.querySelector(".pitch").parentElement.parentElement;
              var children = parent.children;
              var child = children[this.path];
              var divEle = child.children[0];
              divEle.classList.add("on");
            }
          }
        }
      }
    },
    props:{
         child:""
    },
//        mounted(){
//            this.sign();
//            console.log(1111111);
//        },
//       updated(){
//            console.log(123456);
//         this.sign();
//       },
//       created(){
//             console.log(this);
//             this.nextTick(function () {
//               console.log(document.querySelector(".pitch"));
//             });
//       },
//       activated(){
//           alert(123456);
//       },
//       watch:{
//         symbolId:function () {
//           alert(123);
//         }
//       }
//       activated(){
//              alert(123);
//              var id = this.symbolId;
//              var obj = this.$store.state.recordPZ;
//              if(obj){
//                  if(id = obj.id){
//                      var idx = obj.path;
//
//                      var parent = document.querySelector(".pitch").parentElement;
//                      var child = parent.children;
//                      [].forEach.call(function (current, index) {
//                         current.classList.remove("on");
//                         if(index == idx){
//                           current.classList.add("on");
//                         }
//                      });
//                  }
//              }
//       }
//       mounted(){
//             console.log(2111111111111);
//       }
  }
</script>
