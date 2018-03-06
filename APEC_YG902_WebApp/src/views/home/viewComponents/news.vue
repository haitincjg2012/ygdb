/**
* Created by gsy on 2017/9/11.
* 市场行情
*/
<template>
  <div class="newsContainer">
    <div class="myheader">
      <my-header :headTitle="title" v-on:initPage="initpage"></my-header>
    </div>
    <div class="publicate" @click="goIsuueNews">
        <span>发行情</span>
    </div>
    <!--<div v-show="!blankFlag">-->
      <my-scroll class="newsScroll" :data="newsData" :pullup="pullup" @scrollToEnd="loadMore" :pulldown="pulldown" @pulldown="downRefresh" ref="wrapper">
        <div class="newsContent">
          <div class="downRefresh" v-if="dnfreshFlag">
            <my-scrolltip ref="refreshTip"></my-scrolltip>
          </div>
          <!--轮播-->
          <div class="carousel">
            <mt-swipe :auto="4000">
              <template v-for="item in carouselData" >
                <mt-swipe-item :key="item.id" >
                  <img :src="item.url+'?x-oss-process=style/_detail'" @click.stop="goDetail(item.id)" /><!--
                --><span class="wordWrapper"></span><!--
                --><h1>{{item.title}}</h1>
                </mt-swipe-item>
              </template>

            </mt-swipe>
          </div>
          <!--列表-->
          <my-newslist :listFilter="newsData" v-on:changeReflag="changereflag"></my-newslist>
          <!-- <my-newslist :listFilter="newsData" :loadMflag="loadMflag" v-on:changeReflag="changereflag"></my-newslist> -->
          <my-scrolltip ref="loadmoreTip" ></my-scrolltip>
        </div>
      </my-scroll>
    <!--</div>-->
    <my-blankimg :isShow="blankFlag"></my-blankimg>

  </div>

</template>

<script>
    import {Swipe,SwipeItem,Indicator,Toast} from 'mint-ui'
    import header from '@/components/header/header'
    import newslist from './newslist'
//    import scroll from '@/components/scroll/scroll'//分页
    import scroll from '@/components/scroll/scrollbg'//分页

    import imgsrc1 from '@/assets/img/xqimg1.png'//测试图片1
    import imgsrc2 from '@/assets/img/xqimg2.png'//测试图片2
    import imgsrc3 from '@/assets/img/xqimg3.png'//测试图片3
    import scrollTip from '@/components/downLoadAnimal' //滚动提示
    import blankimg from '@/components/blank' //默认图
    import API from '../../../api/api'
    import WX from '../../../components/wx.vue'
    const api = new API();
    export default{
        data(){
            return {
              title:"市场行情",
              carouselData:null,//轮播数据
              newsData:null,//列表数据
              refershFlag:true,//通过返回icon离开页面再进入页面需要刷新，通过点击进入详情再进入页面不需要刷新
              blankFlag:null,
              /*分页参数--上拉加载更多*/
              pullup:true,
              pulldown:true,
              loadMflag:true,
              // loadMopen:false,
              pageNum:1,//页码
              pageSize:10,
              dnfreshFlag:false //下拉刷新标志
            }
        },
        created(){
          // this.loadMopen=false; //上拉加载标志
        },
        mounted(){
         /* var vm=this;
          vm.newslist();*/
        },
        activated(){

          var vm=this;
          vm.$refs.wrapper.init();
          vm.carouselist();
          window.postId = "";
          vm.dnfreshFlag=false;
          // var qyrefershflag=vm.$route.query.refershflag;
          /*
          vm.$nextTick(function(){
           vm.$refs.loadMoreTip.hide();
          });
          */
          if(vm.refershFlag){
            vm.newslist();
          }
            WX.wx();

        },
        methods: {
          //下拉刷新
          downRefresh(){
            var vm=this;
            // console.log("上拉刷新");
            vm.dnfreshFlag=true;
            vm.$nextTick(function(){
              // console.log(vm.$refs.refreshTip, 8888);
              vm.$refs.refreshTip.refresh("刷新中...");
            });

            vm.pageNum=1;
            vm.newsData=null;
            vm.newslist();
          },
          //去发布新闻
          goIsuueNews(){
            var vm=this;
            vm.$router.push({
              name:"newsPublish"
            });
          },
          //初始化页面数据
          initpage(){

            var vm=this;
            vm.carouselData=null;
            vm.newsData=null;
            vm.refershFlag=true;
            vm.pageNum=1;
//            vm.$router.replace({ path: '/home' })
          },
          //改变refershFlag状态
          changereflag(){
            var vm=this;
            vm.refershFlag=false;
          },
          //获取轮播数据
          carouselist(){
            var vm=this;
            let params={
              api:"/yg-society-service/societyPost/newsBannerList.apec",
              data:{
                realm:"ARTICLE",
                pageSize:3
              }
            };
            vm.post(params,vm.carouselistCb,false);
          },
          carouselistCb(data){
            var vm=this;
            if(data.succeed){
              vm.carouselData=data.data.rows?data.data.rows:[];
              // console.log("轮播图长度："+data.data.rows.length);
            }
          },
          /*
          componentInit(){
            //组件1
            //组件2
            //组件3
            //组件4
            //组件5
            // .....
            //组件n
            // vm.$refs.forEach(function(current){
            // if(params){
           //}
            // current.init();
          // });
          },
          */
          //获取新闻列表
          newslist(){
            var vm=this;
            let params={
              api:"/yg-society-service/societyPost/societyPostPage.apec",
              data:{
                //固参
                realm:"ARTICLE",
                auditState:'Y',
                //分页信息
                pageNumber:vm.pageNum,//页码
                pageSize:vm.pageSize//页容量
              }
            };
            var failcb=function(vm){
              vm.$refs.loadmoreTip.end("网络异常...");
            }
            vm.post(params,vm.newslistCb,true,failcb.bind(this));
          },

          newslistCb(data){
            var vm=this;
            if(data.succeed){
              // if(vm.loadMopen){//上拉刷新
                if(data.data.totalElements == 0){
                  vm.blankFlag=true;
                  vm.loadMflag=false;
                }
                else if(data.data.totalElements && data.data.currentNo == data.data.pageCount){//没有分页了
                    vm.blankFlag=false;
                    vm.loadMflag=false;
                    vm.$refs.loadmoreTip.end("没有更多数据了");
                }
                else{//仍有分页
                    vm.blankFlag=false;
                    vm.loadMflag=true;
                    // vm.$refs.loadmoreTip.hide();
                }
              // }
              if(vm.dnfreshFlag){//下拉加载
                setTimeout(function(){
                  // vm.$refs.refreshTip.end("刷新完成");
                  vm.dnfreshFlag=false;

                },1000);

              }
//              vm.newsData = data.data.rows;
              vm.newsData=vm.newsData?vm.newsData.concat(data.data.rows):[].concat(data.data.rows);

            }
            else{
              Toast(data.errorMsg);
            }
          },
          //封装post请求
          post(params, fn, flag, failcb){
           var vm=this;
           try {
             api.post(params).then((res) => {
               var data = res.data;
               fn(data);
             }).catch((error) => {
               console.log(error);
               Toast(error);
               if(flag){
                 failcb();
               }
             })
           } catch (error) {
             console.log(error);
             Toast(error);
             if(flag){
               failcb();
             }
        }
      },
          //去新闻详情页
          goDetail(id){
            var vm=this;
            vm.refershFlag=false;
            vm.$router.push({name:"newsDetail",query:{id:id}});
          },
          //上拉加载更多
          loadMore(){
            var vm=this;
            // vm.loadMopen=true;
            if(vm.loadMflag){
              vm.$nextTick(function(){
                vm.$refs.loadmoreTip.loading();
              });
              vm.pageNum++;
              vm.newslist();
              // console.log("上拉加载更多："+this.pageNum);
            }
          }
        },
        computed:{
          /** 一个接口时拆分数组
          carouselFilter(){
            return this.newsData.slice(0,3);
          },
          listFilter(){
//          console.log("长度："+this.newsData.slice(3).length);
            return this.newsData.slice(3);
          }
          **/
        },
        components: {
          'my-header':header,
          'my-scroll':scroll,
          'my-newslist':newslist,
          'my-scrolltip':scrollTip,
          'my-blankimg':blankimg
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
 @import '../../../assets/css/news.css';
</style>
