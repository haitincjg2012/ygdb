/**
* Created by gsy on 2017/9/14.
* 新闻列表
*/
<template>
  <ul class="newsList">
    <li v-for="item in listFilter" :key="item.id" @click="goDetail(item.id)">
      <div class="newsItem">
        <div class="newslistR" v-if="item.url">
        <!--<div class="newslistR">-->
          <img :src="item.url+'?x-oss-process=style/_list_hq'"/>
        </div>
        <div class="newslistL">
          <h2>{{item.title}}</h2>
          <p>{{item.author}}<span>{{item.createDate|ymdFilter}}</span></p>
        </div>
      </div>
    </li>
    <li v-if="!loadMflag" class="liLast">
      <div class="loading-wrapper">没有更多信息了</div>
    </li>
  </ul>
</template>

<script>
    export default{
        data(){
            return {}
        },
        props:['listFilter','loadMflag'],
        methods: {
          //去新闻页详情
          goDetail(id){
            var vm=this;
            vm.$router.push({name:"newsDetail",query:{id:id}});
            vm.$emit("changeReflag");
          }
        },
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">

</style>
