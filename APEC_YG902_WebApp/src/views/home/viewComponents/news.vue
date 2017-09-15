/**
* Created by gsy on 2017/9/11.
* 市场行情
*/
<template>
  <div class="newsContainer">
    <div class="myheader">
      <my-header :headTitle="title"></my-header>
    </div>
    <my-scroll class="newsScroll" :data="newsData" :pullup="pullup" @scrollToEnd="loadMore">
        <div class="newsContent">
          <!--轮播-->
          <div class="carousel">
            <mt-swipe :auto="4000">
              <template v-for="item in carouselFilter" >
                <mt-swipe-item :key="item.id" >
                  <img :src="item.url" @click.stop="goDetail(item.id)" /><!--
                --><span class="wordWrapper"></span><!--
                --><h1>{{item.title}}</h1>
                </mt-swipe-item>
              </template>

            </mt-swipe>
          </div>
          <!--列表-->
          <my-newslist :listFilter="listFilter" :loadMflag="loadMflag"></my-newslist>
          <!--<ul class="newsList">
            <li v-for="item in listFilter" :key="item.id" @click="goDetail(item.id)">
              <div class="newsItem">
                <div class="newslistR">
                  <img :src="item.url"/>
                </div>
                <div class="newslistL">
                  <h2>{{item.title}}</h2>
                  <p>{{item.author}}<span>{{item.createDate|ymdFilter}}</span></p>
                </div>
              </div>
            </li>
            <li>
              <div class="loading-wrapper" v-if="!loadMflag">没有更多信息了</div>
            </li>
          </ul>-->
        </div>
      </my-scroll>

  </div>

</template>

<script>
    import {Swipe,SwipeItem,Indicator,Toast} from 'mint-ui'
    import header from '@/components/header/header'
    import newslist from './newslist'
    import scroll from '@/components/scroll/scroll'
    import imgsrc1 from '@/assets/img/xqimg1.png'//测试图片1
    import imgsrc2 from '@/assets/img/xqimg2.png'//测试图片2
    import imgsrc3 from '@/assets/img/xqimg3.png'//测试图片3
    import API from '../../../api/api'
    const api = new API();
    export default{
        data(){
            return {
              title:"市场行情",
              newsData:[],
               //新闻列表-测试数据
             /* newsData:[
                {id:1,title:"2018年红富士苹果走货令人担忧",imgsrc:imgsrc1,websit:"海外网",peroid:"1小时前"},
                {id:2,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc2,websit:"海外网",peroid:"2小时前"},
                {id:3,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc3,websit:"海外网",peroid:"3小时前"},
                {id:4,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc1,websit:"百度网",peroid:"4小时前"},
                {id:5,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc2,websit:"网易网",peroid:"5小时前"},
                {id:6,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc3,websit:"国内网",peroid:"6小时前"},
                {id:7,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc1,websit:"海外网",peroid:"7小时前"},
                {id:8,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc2,websit:"海外网",peroid:"8小时前"},
                {id:9,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc3,websit:"海外网",peroid:"9小时前"},
                {id:10,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc1,websit:"百度网",peroid:"10小时前"},
                {id:11,title:"2017年红富士苹果走货令人担忧",imgsrc:imgsrc2,websit:"网易网",peroid:"11小时前"},
                {id:12,title:"122017年红富士苹果走货令人担忧",imgsrc:imgsrc3,websit:"国内网",peroid:"12小时前"}
              ],*/
              /*分页参数--上拉加载更多*/
              pullup:true,
              pulldown:true,
              loadMflag:true,
              loadMopen:false,
              pageNum:1,//页码
              pageSize:10
            }
        },
        created(){
          this.loadMopen=false; //上拉加载标志
        },
        mounted(){
          var vm=this;
          vm.newslist();
        },
        methods: {
          //获取新闻列表
          newslist(){
            var vm=this;
            Indicator.open({
              text:'加载中...',
              spinnerType:'fading-circle'
            });
            let params={
              api:"/yg-cms-service/cms/newsList.apec",
              data:{
                //固参
                channelCode:"NEWS",
                category:"NEWS",
                //分页信息
                pageNumber:vm.pageNum,//页码
                pageSize:vm.pageSize//页容量
              }
            };
            vm.post(params,vm.newslistCb);
          },
          newslistCb(data){
            var vm=this;
            Indicator.close();
            if(data.succeed){
              if(vm.loadMopen){
                if(data.data.rows.length<vm.pageSize){
                  console.log("没数据了");
                  this.loadMflag=false;
                }
                else{
                  this.loadMflag=true;
                }
              }
              vm.newsData=vm.newsData.concat(data.data.rows);
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
          },
          //去新闻详情页
          goDetail(id){
            var vm=this;
            vm.$router.push({name:"newsDetail",query:{id:id}});
          },
          //上拉加载更多
          loadMore(){
            this.loadMopen=true;
            if(this.loadMflag){
              this.pageNum++;
              this.newslist();
              console.log("上拉加载更多："+this.pageNum);
            }
          }
        },
        computed:{
          carouselFilter(){
            return this.newsData.slice(0,3);
          },
          listFilter(){
            console.log("长度："+this.newsData.slice(3).length);
            return this.newsData.slice(3);
          }
        },
        filters:{

        },
        components: {
          'my-header':header,
          'my-scroll':scroll,
          'my-newslist':newslist
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
 @import '../../../assets/css/news.css';
</style>
