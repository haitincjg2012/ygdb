/**
* Created by gsy on 2017/9/8.
* 积分排行榜
*/
<template>
  <div id="rankBox">
    <my-scroll class="scrollWrapper" :data="rateData" :pullup="pullup" @scrollToEnd="loadMore">
    <div>
      <!--<div class="rankFix">-->
      <div class="rankBanner">
        <span @click="back" >
          <img src="../../../assets/img/backIcon.png"/>
        </span>
        <h1>龙虎榜</h1>
        <h2>最具实力调果排行榜</h2>
      </div>
      <!--tab切换-->
      <ul class="rankTab">
        <li v-for="key in tabItem" :key="key.id" class="ranktab-item" @click="tabToggle(key)">
          <a>{{key.name}}</a>
          <i class="triangle" :class="{white:key.isActive}"></i>
        </li>
      </ul>
      <!--</div>-->
      <!--列表-->
      <dl class="rankContent">
        <dd v-for="(item,index) in rateData">
        <span class="orderNum">
          <img :src="index|imgFilter" v-if="index<3"/>
          <i v-else>{{index+1}}</i>
        </span>
          <div class="rContent">
          <span class="headPhoto">
            <img :src="item.imgUrl +'?x-oss-process=style/_head'" v-if="item.imgUrl!='null'"/>
            <img src="../../../assets/img/icon.png" v-else />
          </span>
            <span class="name">{{item.name}}</span>
            <span class="totalNum">{{item.totalNumber}} <i>斤</i></span>
          </div>
        </dd>
        <dd v-if="!loadFlag" class="ddLast">
          <div>没有更多信息了</div>
        </dd>
      </dl>
    </div>
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
              loadFlag:true,//"没有更多信息了"文字
              loadMopen:false,//上拉加载标识
              //月榜
              mPgNum:1,//页码
              mPgSize:10,//页容量
              //年榜
              yPgNum:1,//页码
              yPgSize:10,//页容量
              //列表数据
              rateData:[]
              /*rateData:[
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
              ]*/

            }
        },
        activated(){
          var vm=this;
          vm.rateData=[];
          vm.initTab();
          vm.initMthlist();
        },
        methods: {
          //初始化tab
          initTab(){
            var vm=this;
            vm.tabid=1;
            vm.tabItem[0].isActive=true;
            vm.tabItem[1].isActive=false;
          },
          //初始化月榜
          initMthlist(){
            var vm=this;
            vm.tabid=1;
            vm.loadFlag=true;
            vm.loadMopen=false;
            //月榜
            vm.mPgNum=1;//页码
            vm.getMonthlist();
          },
          //初始化总榜
          initYearlist(){
            var vm=this;
            vm.tabid=2;
            vm.loadFlag=true;
            vm.loadMopen=false;
            //月榜
            vm.yPgNum=1;//页码
            vm.getYearlist();
          },
          //返回
          back(){
            this.$router.go(-1);
          },
          //tab切换
          tabToggle(item){
            var vm=this;
            vm.tabid=item.id;
//            console.log("toggle vm.tabid:"+vm.tabid);
            vm.rateData=[];
            vm.activeClass();
//            console.log("vm.rateData.length:"+vm.rateData.length);
            if(vm.tabid==1){//月榜
              vm.initMthlist();
            }
            else if(vm.tabid==2){//年榜
              vm.initYearlist();
            }
          },
          activeClass(){
            var vm=this;
            vm.tabItem.forEach(item =>{
              if(item.id==vm.tabid){
              item.isActive=true;
             }
             else{
               item.isActive=false;
              }
            });
          },

          //上拉加载更多
          loadMore(){
            var vm=this;
            vm.loadMopen=true;
//            console.log("上拉加载："+" vm.mloadFlag:"+vm.mloadFlag+" vm.loadFlag:"+vm.loadFlag+" vm.tabid:"+vm.tabid);
            if(vm.loadFlag){
              if(vm.tabid==1){
                this.mPgNum++;
                vm.getMonthlist();
                console.log("月榜上拉加载："+this.mPgNum);
              }
              else if(vm.tabid==2){
                this.yPgNum++;
                console.log("总榜上拉加载："+this.yPgNum);
                vm.getYearlist();
              }
            }


          },
          //月榜
          getMonthlist(){
            var vm=this;
            Indicator.open({
              text:"加载中...",
              spinnerType:'fading-circle'
            });
            let params={
              api:"/yg-voucher-service/voucher/listMonthDBNumberRankViewVO.apec",
              data:{
                pageNumber:vm.mPgNum,
                pageSize:vm.mPgSize
              }
            };
            vm.post(params,vm.monthListCb);
          },
          monthListCb(data){
            var vm=this;
            Indicator.close();
            if(data.succeed){
              if(vm.loadMopen){
                if(data.data.rows.length< vm.mPgSize){
                  console.log("月榜没数据了");
                  vm.loadFlag=false;
                }
                else{
                  vm.loadFlag=true;
                }
              }
              vm.rateData=vm.rateData.concat(data.data.rows);
            }
            else{
              Toast(data.errorMsg);
            }
          },
          //总数
          getYearlist(){
            var vm=this;
            Indicator.open({
              text:"加载中...",
              spinnerType:"fading-circle"
            });
            let params={
              api:"/yg-voucher-service/voucher/listTotalDBNumberRankViewVO.apec",
              data:{
                pageNumber:vm.yPgNum,
                pageSize:vm.yPgSize
              }
            };
            vm.post(params,vm.yearlistCb);
          },
          yearlistCb(data){
            var vm=this;
            Indicator.close();
            if(data.succeed){
              if(vm.loadMopen){
                if(data.data.rows.length< vm.mPgSize){
                  console.log("年榜没数据了");
                  vm.loadFlag=false;
                }
                else{
                  vm.loadFlag=true;
                }
              }
              vm.rateData=vm.rateData.concat(data.data.rows);
            }
            else{
              Toast(data.errorMsg);
            }
          },
          //封装post请求
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
