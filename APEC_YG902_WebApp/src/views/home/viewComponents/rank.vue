<template>
  <li :symbolId="symbolId"
      :path = "path"
      :Characteristic = "CharacteristicId"
      :vt = "value"
      :recordF = "recordF"
     >
      <div @click="search"  class='pitch' :class="{on:sh}" >
      <img src="../../../assets/img/hack.png">
      <span>{{CharacteristicId}}</span>
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
             var obj = {};
             var text = parent.getAttribute("Characteristic");
             var path = parent.getAttribute("path");
             id = parent.getAttribute("symbolId");
             var vt = parent.getAttribute("vt");
             var sh = this.sh;
             if(id == "10000"){
               this.$store.state.GQF = {
                   id:id,
                   path:path,
                   sh:(!sh)
               }
             }else{
               this.$store.state.recordPZ = {
                 id:id,
                 path:path,
                 sh:(!sh)
               }
             }

             obj.text = text;
             obj.id = id;
             if(sh){
               obj.text = "";
             }else{
               obj.text = text;
             }
             if(id == "1003"){
               obj.childFlag = true;
               obj.path = path;
               if(vt){
                   obj.value = vt;
               }
               var p = parent.parentElement;
               var child = p.children;
               for(var ik = 0; ik < child.length; ik ++){
                 if(ik != path){
                   child[ik].children[0].classList.remove("on")
                 }else{
                    child[ik].children[0].classList.add("on");
                 }
               }
             }else{
               obj.childFlag = false;
             }

             this.$emit("rs",obj)
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
         symbolId:'',
         CharacteristicId:'',
         path:'',
         sh:false,
         value:'',
         recordF:''
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
//          var p = document.querySelector(".pitch").parentElement;
//          var
//          for(){
//
//          }
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
