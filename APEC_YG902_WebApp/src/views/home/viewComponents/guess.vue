<template>
<div class="c-guess" v-if="guessFlag" @click="comment">
   <div class="c-g-subheading">
      <div class="c-g-subheading-text"><h4>{{title}}</h4></div>
      <div class="c-g-subheading-number">{{totalPeople}}</div>
      <div class="c-g-subheading-content"> 人正在参与</div>
   </div>
  <div class="c-performance-one" v-if="mark== 1" >
     <div class="c-performance-l" ref="lineLeft"></div>
     <div class="c-performance-center" ref="lineCenter"></div>
     <div class="c-performance-r" ref="lineRight">
     </div>
      <div class="c-guess-data">
        <div class="c-guess-data-com">
          {{highPeopleP}}人看涨
        </div>
        <div class="c-guess-data-com c-guess-data-r">
          {{lowerPeopleP}}人看跌
        </div>
      </div>
  </div>
   <div v-else>
     <div class="c-g-pricefluctuation">
       <div class="c-g-pricef-text">
         <p class="c-g-pT-com c-g-pT-r">{{highPeopleP}}人看涨</p>
         <p class="c-g-pT-com c-g-pT-l">{{lowerPeopleP}}人看跌</p>
       </div>
       <div class="c-g-pricef-draw">
         <div class="c-g-p-line c-linebg-l"></div>
         <div class="c-g-p-center"></div>
         <div class="c-g-p-line c-linebg-r"></div>
       </div>
     </div>
     <div class="c-g-operate">
       <div class="c-g-o-com c-g-o-comment" v-if="commentFlag">
         <img src="../../../assets/img/whiteMsg.png" class="c-g-o-comment-img">
         {{replyCount}}
       </div>
       <div class="c-g-o-com c-g-o-comment" @click="comment" v-else>
         <img src="../../../assets/img/fruitMsg.png" class="c-g-o-comment-img">
         {{replyCount}}
       </div>
       <div class="c-g-o-com c-g-o-center" :class="{ractive:raiseflag}" @click="price('up')">
         <img :src="praise" class="c-g-o-img">
         {{raiseflag?"已看涨":"看涨"}}
       </div>
       <div class="c-g-o-com" :class="{factive:fallflag}" @click="price('down')">
         <img :src="pfall" class="c-g-o-img">
         {{fallflag?"已看跌":"看跌"}}
       </div>
     </div>
   </div>
</div>
</template>
<style>
@import "../../../assets/css/guess.css";
</style>
<script>
  import fall from '../../../assets/img/fall.png'//看跌的默认图
  import raise from '../../../assets/img/raise.png'//看涨的默认图
  import hasRaise from '../../../assets/img/hasRaise.png'//以看涨的图
  import hasFall from '../../../assets/img/hasFall.png'//已看跌的图
  import API from '../../../api/api'
  const api = new API();
  import {Toast} from 'mint-ui'
  export default{
      data(){
          return{
            pfall:fall,
            praise:raise,
            pflag:false,//默认是都可以有点击事件
            fallflag:false,//看跌的标志位
            raiseflag:false,//看涨的标志位
            highPeopleP:0,//默认看涨人数百分比
            lowerPeopleP:0,//默认看涨人数百分比
            totalPeople:0,//默认参与总人数
            replyCount:0,//默认回复数
            id:0,//帖子的id
            title:'',//标题头
            guessFlag:true,//是否存在
          }
      },
    methods:{
      price(flag){
          var name = localStorage.name;
          if(name){
            if(!this.pflag){
              var self = this;
              var f = "";
              switch (flag){
                case "up":
                  this.praise = hasRaise;
                  this.raiseflag = true;
                  f = true;
                  break;
                case "down":
                  this.pfall = hasFall;
                  this.fallflag = true;
                  f = false;
                  break;
              }
              this.pflag = true;

              var params = {
                  api:"_node_quotation/_join_quotation.apno",
                  data:{
                    quotationId:self.id,
                    quotationFlag:f,
                  }
              }
              self.post(params, function () {
                self.getData();
              });
            }
          }else{
            Toast('请您先登录，再进行价格竞猜!');
          }
      },
      comment(){
          var self = this;
          if(self.mark== 1){
            this.$router.push({name:"priveMovement",query:{useId:self.id}});
          }
      },
      post(params, fn){
        try {
          api.post(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
      format(data){
          if(data.succeed){

              if(!data.data){
                  this.guessFlag = false;
                  return;
              }
              var dt = data.data;
              this.totalPeople = dt.totalUser;
              this.id = dt.id;
              this.title = dt.title + "(" + dt.content +")";
              this.replyCount = dt.replyCount;
              if(this.totalPeople > 0){
                var hp = (dt.quotationHigh / this.totalPeople).toFixed(2);
                var pV = Math.round(hp*100);
                this.highPeopleP = pV + "%";
                this.lowerPeopleP = (100 - pV) + "%";
              }else{
                this.highPeopleP = "50%";
                this.lowerPeopleP = "50%";
              }

              var name = localStorage.name;
              if(name){
                  if(dt.isHigh){
                    this.praise = hasRaise;
                    this.raiseflag = true;
                    this.pflag = true;
                  }
                  if(dt.isLight){
                    this.pfall = hasFall;
                    this.fallflag = true;
                    this.pflag = true;
                  }
              }
              this.setDraw(hp);
          }
      },
      setDraw(hP){
        var el = this.$refs.lineLeft;
        if(el){
          var elT = this.$refs.lineRight;
          var widthO = getComputedStyle(el, null),
              widthT = getComputedStyle(elT, null);
             var x = widthO.width.split("px")[0] * 1 + widthT.width.split("px")[0] * 1;
             el.style.width = x * hP + "px";
             elT.style.width = x * (1 - hP) + "px";
        }else{
          var elH = document.querySelector(".c-linebg-l"),
            elL = document.querySelector(".c-linebg-r"),
            lineStyleR = getComputedStyle(elH, null),
            lineStyleL = getComputedStyle(elL, null);
          var x = lineStyleR.width.split("px")[0] * 1 + lineStyleL.width.split("px")[0] * 1;

          elH.style.width = x * hP + "px";
          elL.style.width = x * (1 - hP) + "px";
        }

      },
      getData(){
        var params = {
          api:"yg-society-service/societyPost/newsQuo.apec",
        }
        this.post(params, this.format);
      },
      initFruitGuess(){
        this.pflag = false;//初始化
        this.fallflag = false;//看跌的标志位
        this.raiseflag = false;//看涨的标志位
        this.pfall = fall;
        this.praise = raise;
      },
    },
    activated(){
         this.initFruitGuess();
        this.getData();
    },
    props:['commentFlag','mark']
  }
</script>
