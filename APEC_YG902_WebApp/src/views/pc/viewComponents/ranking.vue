/**
* Created by gsy on 2017/9/8.
* 积分排行榜
*/
<template>
  <div id="rankBox">
    <div class="rankFix">
      <div class="rankBanner">
        <span @click="back">
          <img src="../../../assets/img/backIcon.png"/>
        </span>
        <h1>龙虎榜</h1>
        <h2>最具实力调果排行榜</h2>
      </div>
      <!--tab切换-->
      <ul class="rankTab">
        <li v-for="item in tabItem" :key="item.id" class="ranktab-item" @click="tabToggle(item)">
          <a>{{item.name}}</a>
          <i class="triangle" :class="{white:item.isActive}"></i>
        </li>
      </ul>
    </div>
    <!--列表-->
    <my-scroll class="scrollWrapper" :pullup="pullup" @scrollToEnd="loadMore">
      <dl class="rankContent">
        <dd v-for="(item,index) in rateData">
        <span class="orderNum">
          <img :src="index|imgFilter" v-if="index<3"/>
          <i v-else="index>=3">{{index}}</i>
        </span>
          <div class="rContent">
          <span class="headPhoto">
            <img src="../../../assets/img/pokeNeg.png"/>
          </span>
            <span class="name">张易</span>
            <span class="totalNum">2000<i>斤</i></span>
          </div>
        </dd>
      </dl>
    </my-scroll>
  </div>
</template>

<script>
    import c_js from '../../../assets/js/common'
    import {Toast,Indicator,MessageBox} from 'mint-ui'
    import API from '../../../api/api'
    import scroll from '@/components/scroll/scroll'
    //徽章图片
    import rateFirst from '../../../assets/img/rateFirst.png'
    import rateSed from '../../../assets/img/rateSed.png'
    import rateThird from '../../../assets/img/rateThird.png'
    const api=new API();
    export default{
        data(){
            return {
              //tab切换
              tabid:1,
              tabItem:[
                {id:1,name:"月榜（"+(new Date().getMonth()+1)+"月）",isActive:true},
                {id:2,name:"总榜（"+new Date().getFullYear()+"年）",isActive:false}
              ],
              //上拉加载更多
              pullup:true,
              loadMflag:true,
              pageNum:1,//页码
              pageSize:10,
              //列表数据
              rateData:[
                {id:1,name:4545},
                {id:2,name:445},
                {id:3,name:455},
                {id:4,name:45451},
                {id:5,name:45425},
                {id:1,name:4545},
                {id:2,name:445},
                {id:3,name:455},
                {id:4,name:45451},
                {id:5,name:45425},
                {id:2,name:445},
                {id:3,name:455},
                {id:4,name:45451},
                {id:5,name:45425}
              ]

            }
        },
        methods: {
          //返回
          back(){
            this.$router.go(-1);
          },
          //tab切换
          tabToggle(item){
            var vm=this;
            this.tabId=item.id;
            vm.activeClass();
          },
          activeClass(){
            var vm=this;
            vm.tabItem.forEach(item =>{
              if(item.id==vm.tabId){
              item.isActive=true;
             }
             else{
               item.isActive=false;
              }
            });
          },
          //上拉加载更多
          loadMore(){
            if(this.loadMflag){
              this.pageNum++;
              console.log("上拉加载更多："+this.pageNum);
//              this.initRecordList(this.timeId);
            }
          }
        },
        filters:{
          imgFilter(val){//等级图标
            switch(val){
              case 0:
                    return rateFirst;
                    break;
              case 1:
                    return rateSed;
                    break;
              case 2:
                    return rateThird;
                    break;
            }

          }
        },
        components: {
          'my-scroll':scroll
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import '../../../assets/css/ranking.css';

</style>
