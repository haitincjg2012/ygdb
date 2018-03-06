<template>
<div class="c-edit">
   <my-header>
      <h1 class="c-header-edit" slot="pack">编辑</h1>
   </my-header>
   <div class="c-edit-content-com c-edit-price">
      <div class="c-edit-c-first-com">{{this.gq == '供应'?"供应价格":"求购价格"}}</div>
      <div class="c-edit-p-number" v-if="this.gq != '供应'">
        <input type="text" placeholder="请填写单价" @input="priceHandle($event,1)" v-model="behindP" maxlength="4">
         <span>-</span>
        <input type="text" placeholder="请填写单价" @input="priceHandle($event,0)" v-model="frontP" maxlength="4" class="c-edit-front">
      </div>
      <div v-else class="c-edit-w-number">
        <input type="text" placeholder="请填写单价" @input="priceHandle($event,2)" v-model="signleP" maxlength="4" class="c-edit-front">
      </div>
      <div class="c-edit-c-second-com">{{priceUnit}}</div>
      <div class="c-edit-line"></div>
   </div>
   <div class="c-edit-content-com c-edit-weight">
     <div class="c-edit-c-first-com">{{this.gq == '供应'?"供应量":"求购量"}}</div>
     <div class="c-edit-w-number">
       <input type="text" placeholder="请填写单价" @input="priceHandle($event,2)" @blur="weightResult" v-model="weight"  maxlength="5">
     </div>
     <div class="c-edit-c-second-com">{{weightUnit}}</div>
   </div>
   <div class="c-edit-ok" @click = "submit">
     确认
   </div>
</div>
</template>
<style>
@import "../../../assets/css/edit.css";
</style>
<script>
  import header from '../../../components/header/headerTwo.vue'
  import {Toast} from 'mint-ui';
  import API from '../../../api/api'
  const api = new API();
  export default{
      data(){
          return {
            frontP:"",
            behindP:"",
            signleP:'',
            weight:"",
            gq:'',//供应和求购的标准位
            id:'',//修改的条目id
            priceUnit:'',//价格单位
            weightUnit:"",//重量单位
          }
      },
      methods:{
        priceHandle(event,mark){
          //小数
          var self = this;
          var value = (event.target || event.srcElement).value;
          var pattern1 = /^[0-9]/g;//处理>0 <1的数据
          var pattern2 =/^0(\.)[0-9]*$/g;
          var pattern3 = /^[1-9]+(\.)?[0-9]*$/g;
          var flag1, flag;


          var len = value.length;

          if(len == 1){
            flag1 = pattern1.test(value);
          }else{
            flag1 = pattern2.test(value) || pattern3.test(value);
          }

          if(!flag1){
            numberHandle(mark);
            return;
          }



          var frontP = this.frontP;
          var behideP = this.behindP;

          if(behideP&&frontP){
              //小数相比较
              var behidePStr = behideP.split(".").join("");
              var frontPstr = frontP.split(".").join("");

              var len = behideP.length;

              if(mark == 0){
                  if(frontP > behideP){
                    this.frontP = "";
                  }
              }
              if(mark == 1){

                  if(behideP < frontP.substring(0, len)){
                    this.behindP = "";
                  }
              }

          }

          function numberHandle(mark) {
            switch (mark){
              case 0:
                self.frontP = "";
                break;
              case 1:
                self.behindP = "";
                break;
              case 2:
                self.frontP = "";
                break
            }
          }

        },
        weightResult(){
          if(this.weight <= 0){
            this.weight = "";
          }
        },
        updateGYPriceNum(id, weight = 0, num = 0){
          const self = this;
          var params = {
            api: "/yg-product-service/product/updateProductByES.apec",
            data: {
              "elasticId": id,
            }
          };


          if (num) {
            params.data['amount'] = num;
          }
          if(weight){
            params.data['weight'] = weight;
          }
          try {
            api.post(params).then((res) => {
              var item = res.data;
              if (item.succeed) {
                Toast("修改成功~");
                self.$router.push({name:"mySpDe"});
//                self.sortDt(id, weight, num,0,0);
              } else {
                Toast("修改失败~");
              }
            }).catch((error) => {
              console.log(error)
            })
          } catch (error) {
            console.log(error)
          }
        },
        updateQGPriceNum(id, weight = 0,startAmount=0,endAmount=0){
          const self = this;
          var params = {
            api: "/yg-product-service/product/updateBuyProductByES.apec",
            data: {
              "elasticId": id,
            }
          };
          if (startAmount && endAmount) {
            params.data['startAmount'] = startAmount;
            params.data['endAmount'] = endAmount;
          }
          if(weight){
            params.data['weight'] = weight;
          }
          try {
            api.post(params).then((res) => {
              var item = res.data;
              if (item.succeed) {
                Toast("修改成功~");
                self.$router.push({name:"mySpDe"});
//                self.sortDt(id, weight,0,startAmount,endAmount);
              } else {
                Toast("修改失败~");
              }
            }).catch((error) => {
              console.log(error)
            })
          } catch (error) {
            console.log(error)
          }
        },
        submit(){

          if(this.gq == "供应"){
              //供应
            this.updateGYPriceNum(this.id, this.weight, this.signleP);
//            this.$root.eventHub.$emit('puwUpdate.confirm', {'priceSwitch':this.gq,id:this.id,'startNum':this.frontP,'endNum':this.behindP});
          }else{
              //求购
            this.updateQGPriceNum(this.id,this.weight, this.frontP, this.behindP);

          }

      }
      },
      activated(){
        this.id = this.$route.query.id;
        this.gq = this.$route.query.gq;
        this.priceUnit = this.$route.query.priceUnit;
        this.weightUnit = this.$route.query.weightUnit;
        this.frontP = "",
        this.behindP = "",
          this.signleP= "",
          this.weight= "";
      },
      components:{
          "my-header":header
      }
  }
</script>
