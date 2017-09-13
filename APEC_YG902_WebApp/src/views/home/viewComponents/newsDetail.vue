/**
* Created by gsy on 2017/9/12.
* 新闻详情
*/
<template>
  <div class="newsDetail">
    <div class="myheader">
      <my-header :headTitle="title"></my-header>
    </div>
    <my-scroll class="newsDetailScroll" :pullup="pullup" @scrollToEnd="loadMore">
      <div class="newsDetailMain">
        <div class="newsDtop">
          <h1>{{detailData.title}}</h1>
          <p>{{detailData.websit}}<span>{{detailData.time}}</span></p>
        </div>
        <div class="newsDbtm">
          <p v-for="item in detailData.content">{{item}}</p>
        </div>
      </div>
    </my-scroll>
  </div>
</template>

<script>
    import {Indicator,Toast} from 'mint-ui'
    import header from '@/components/header/header'
    import scroll from '@/components/scroll/scroll'
    import API from '@/api/api'
    const api=new API();
    export default{
        data(){
            return {
              title:'市场行情',
              id:"",
              detailData:{
                 id:1,
                 title:"2018年红富士苹果走货令人担忧",
                 time:"2017-8-25",
                 websit:"海外网",
                 content:[
                    '2016年膜袋红富士苹果走货情况是这样走货较慢，中后期存膜袋红富士的客商明显增多价格保持稳定，但是去年走现货量不是很大冷库现在存的货根本走不动，因为陆地货现在还多，陆地现在膜袋红富士苹果走货价格是70以上装好袋子的货0.5元左右，75以上0.7元左右客商少。',
                    '现在冷库纸袋红富士苹果价格在75以上，装好框子的货1.85-2元，有包点的货价格在1.4-1.5元，冷库纸夹膜红富士苹果现在走货价格在1.6-1.75元，颜色好的很。',
                    '装好框子的货1.85-2元，有包点的货价格在1.4-1.5元，冷库纸夹膜红富士苹果现在走货价格在1.6-1.75元，颜色好的很。大家想一想现在冷库纸袋和纸夹膜红富士苹果价格就那么低，谁还会要膜袋红富士苹果到市场去买？  　　在这里我心里现在还是为存苹果的客商和代办捏了一把汗，不管价格咋样，主要是客商今年少的很，看2017年红富士苹果走货还要看市场走货情况了，望存货老板看好时机及时出货。',
                    '冷库纸夹膜红富士苹果现在走货价格在1.6-1.75元，颜色好的很。在这里我心里现在还是为存苹果的客商和代办捏了一把汗，不管价格咋样，主要是客商今年少的很，看2017年红富士苹果走货还要看市场走货情况了，望存货老板看好时机及时出货。',
                    '现在走货价格在1.6-1.75元，颜色好的很。在这里我心里现在还是为存苹果的客商和代办捏了一把汗，不管价格咋样，主要是客商今年少的很，看2017年红富士苹果走货还要看市场走货情况了，望存货老板看好时机及时出货。'
                 ]
              },
              /*分页参数--上拉加载更多*/
              pullup:true,
              loadMflag:true,
              pageNum:1,//页码
              pageSize:10
            }
        },
        mounted(){
          var vm=this;
          vm.id=vm.$route.query.id;
          console.log("id:"+vm.id);
        },
        methods: {
          //上拉加载更多
          loadMore(){
            if(this.loadMflag){
              this.pageNum++;
              console.log("上拉加载更多："+this.pageNum);
            }
          }
        },
        components: {
          'my-header':header,
          'my-scroll':scroll
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import '../../../assets/css/news.css';
</style>
