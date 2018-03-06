/**
* Created by name on 2017/09/11.
* 我的关注
*/
<template>
  <div class="sy_careBox">
    <div class="myheader">
      <my-header :headTitle="title" ></my-header>
      <!--<my-header :headTitle="title" v-on:initPage="initPage"></my-header>-->
      <div class="mycare">
        <!--筛选-->
        <div class="z-ag-select">
          <div class="z-ag-a" @click="selChoose('identify')">
            <span class="sp-text-com">身份</span>
            <!--<span class="sp-text-com">综合排序</span>-->
            <span class="triangle"></span>
            <span class="z-vertical-line"></span>
          </div>
          <div class="z-ag-s" @click="selChoose('area')">
            <span class="sp-text-com">区域</span>
            <span class="triangle"></span>
          </div>
        </div>
      </div>
    </div>
    <div>
      <my-scroll class="careScroll" :data="mainlistData" :pullup="pullup" @scrollToEnd="loadMore">
        <div class="z-ag-frame">
          <!--列表数据-->
          <ul class="z-ag-list clearfix" v-show="!blankFlag">
            <li class="gsy-agencylist" v-for="item in mainlistData" :key="item.id" @click="goDetail(item)">
              <div class="z-com-f">
                <div class="primaryMain clearfix" >
                  <div class="pic-com">
                    <img :src ="item.orgFirstBannerUrl+ '?x-oss-process=style/_list'" v-if="item.orgFirstBannerUrl"/>
                    <img src="../../../assets/img/coldDefault.png" v-if="item.userType=='LK' && !item.orgFirstBannerUrl"/>
                    <img src="../../../assets/img/DBKS.png" v-if="((item.userType=='DB')||(item.userType=='KS')||(item.userType=='ZZH')||(item.userType=='HZS')) && !item.orgFirstBannerUrl"/>
                  </div>
                  <div class="z-ag-info clearfix" >
                    <div class="z-ag-real-info">
                      <div class="z-ag-r-f-partO">
                        <span class="z-ag-r-name">{{item.orgName}}</span>
                        <!--<img :src="item.imgLevel" class="z-ag-level">-->
                        <!--<img :src="item.userLevelName" class="z-ag-level">-->
                        <span class="z-ag-r" v-if="item.userRealAuth=='NORMAL'">实名认证</span>
                      </div>
                      <!--<div class="z-ag-r-f-partT">
                        <img src="../../../assets/img/fireIcon.png" class="z-ag-r-img">
                        <span class="z-ag-r-text">{{item.viewNum}}</span>
                      </div>-->
                    </div>
                    <div class="z-ag-result">
                    <span class="z-ag-res">身份：</span>
                    <span class="z-ag-r-num"></span>
                    <span class="z-ag-res">{{item.userTypeKey}}</span>
                  </div>
                    <div class="z-ag-main clearfix">
                      <span class="z-ag-main-com-t"></span>
                      <span class="z-ag-main-com-t"></span>
                    </div>
                    <div class="z-ag-address">
                      <img class="pos-pic" src="../../../assets/img/placeIcon.png">
                      <span class="z-ag-addr">{{item.address}}</span>
                    </div>
                  </div>
                </div>
              </div>
            </li>
            <!--
            <li  class="loading-wrapper">
              <div>
                <span v-if="!loadMflag">没有更多信息了</span>
                <span v-if="nodata">暂无数据</span>
              </div>
            </li>
          -->
          </ul>

          <my-scrolltip ref="loadMoreTip"></my-scrolltip>

          <!--查询遮罩层-->
           <div class="z-ag-shadow" v-if="shadowF">
             <!--身份-->
             <ul class="z-ag-listS clearfix" v-if="searType=='identify'">
               <li v-for="identy in usertypeData">
                 <div @click="search(identy,'identify')"  class='pitch' :class="{on:identy.flag}" >
                   <img src="../../../assets/img/hack.png">
                   <span>{{identy.name}}</span>
                 </div>
               </li>
             </ul>
             <!--区域-->
             <ul class="z-ag-listS clearfix" v-if="searType=='area'">
               <li v-for="area in parentA">
                 <div @click="search(area,'area')" class='pitch' :class="{on:area.flag}">
                   <img src="../../../assets/img/hack.png">
                   <span>{{area.keyword}}</span>
                 </div>
               </li>
               <li v-for="city in childA['3710']" v-if="searArea=='3710'">
                 <div @click="search(city,'firstCity')" class='pitch secondRate' :class="{on:city.flag}">
                   <img src="../../../assets/img/hack.png">
                   <span>{{city.keyword}}</span>
                 </div>
               </li>
               <li v-for="city in childA['3706']" v-if="searArea=='3706'">
                 <div @click="search(city,'secondCity')" class='pitch secondRate' :class="{on:city.flag}">
                   <img src="../../../assets/img/hack.png">
                   <span>{{city.keyword}}</span>
                 </div>
               </li>
             </ul>
             <div class="shadow" @click="hideSearch"></div>
           </div>
        </div>
      </my-scroll>
    </div>
    <my-blankimg :isShow="blankFlag"></my-blankimg>
  </div>
</template>
<style>
  @import "../../../assets/css/myAttention.css";
  @import "../../../assets/css/myAttentionlist.css";
</style>
<script>
  import c_js from '../../../assets/js/common'
  import {Toast,Indicator,MessageBox} from 'mint-ui'
  import API from '../../../api/api'
  import header from '@/components/header/header.vue'
  import scroll from '@/components/scroll/scroll'
  import scrollTip from '@/components/downLoadAnimal'
  import blankimg from '@/components/blank' //默认图
  const api = new API();
  export default{
    data(){
      return{
        title:"我的关注",
        shadowF:false,
        mainlistData:[],//主列表数据
        //分页参数
        pullup:true,
        loadMflag:true,//"没有更多信息了"文字
        // loadMopen:false,//上拉加载标识
        blankFlag:null,//无数据显示默认图
        // nodata:false,//"暂无数据"标识
        pageNum:1,//页码
        pageSize:10,
        // scrolltipFlag:false,//控制加载tip是否显示
        //查询参数
        sear_idCode:null,
        sear_addrname:"",
        searType:"",//点击 搜索的类型 身份/区域 identify/area
        //区域
        searArea:"",//选中的一级市code  威海/烟台
        addrFirst:"",//一级区域
        addrSecond:"",//二级区域
        //身份
        usertypeData:[
          {code:null,name:"全部","flag":false},
          {code:"DB",name:"代办","flag":false},
          {code:"KS",name:"客商","flag":false},
          {code:"LK",name:"冷库","flag":false},
          {code:"ZZH",name:"果农","flag":false},
          {code:"HZS",name:"合作社","flag":false}
        ],
        //区域
       /* areaData:{

        },*/
        parentA:[{"keyword":"一级全部","code":1000,"flag":false},{"keyword":"威海市","code":3710,"flag":false},{"keyword":"烟台市", "code":3706,"flag":false}],
        childA:{
          "3710":[{"keyword":"二级全部","flag":false},{"keyword": "桥头镇","flag":false},{"keyword": "文登市","flag":false},{"keyword": "荣成市","flag":false},{"keyword": "乳山市","flag":false}],
          "3706":[{"keyword":"二级全部","flag":false},{"keyword": "牟平区","flag":false},{"keyword": "龙口市","flag":false},{"keyword": "莱阳市","flag":false},{"keyword": "莱州市","flag":false},{"keyword": "蓬莱市","flag":false},{"keyword": "招远市","flag":false},{"keyword": "栖霞市","flag":false},{"keyword": "海阳市","flag":false}]
        }

      }
    },
    activated(){
      var vm=this;
      vm.initPage();
      vm.getMainlist();
      /*
      vm.$nextTick(function(){
        vm.$refs.loadMoreTip.hide();
      });
      */
    },

    created(){
    },
    methods:{
      //点击“身份/区域”  identify/area
      selChoose(type){
        var vm=this;
        vm.shadowF=true;
        vm.searType=type;
        console.log("点击类型："+vm.searType);
      },
      //搜索
      search(item,type){   //身份/区域 identify/area
        var vm=this;

        var selCode=item.code;
        if(type=='identify'){//身份
          for(var i in vm.usertypeData){
            if(selCode == vm.usertypeData[i].code){
              vm.usertypeData[i].flag=true;
              vm.sear_idCode=vm.usertypeData[i].code;
              vm.shadowF=false;
              console.log("当前选中的是:"+vm.sear_idCode+" "+vm.usertypeData[i].name);
              vm.mainlistData=[];

              vm.$refs.loadMoreTip.hide();//隐藏“没有更多”文字提示
              vm.getMainlist();
            }
            else{

              vm.usertypeData[i].flag=false;
            }
          }
        }
        else if(type=='area') {//区域
          /*//初始化一级/二级选择的地址
          vm.initSelarea();*/
//          var parentData = vm.parentA;
          //遍历一级市
          for (var i in vm.parentA) {
            if (selCode == vm.parentA[i].code) {
              vm.parentA[i].flag = true;
              var addrWordSecond=vm.parentA[i].keyword == '一级全部' ? "" : vm.parentA[i].keyword;
              vm.addrFirst = addrWordSecond;
              vm.searArea = vm.parentA[i].code;
              if(vm.addrFirst==""){//如果选择‘一级全部’
                vm.sear_addrname="";
//                console.log("您请选择了全部");
                vm.initAreaflag(vm.parentA);
                vm.initAreaflag(vm.childA[3710]);
                vm.initAreaflag(vm.childA[3706]);
                vm.mainlistData=[];
                vm.$refs.loadMoreTip.hide();//隐藏“没有更多”文字提示
                vm.getMainlist();
                vm.shadowF=false;
              }
            }
            else {
              vm.parentA[i].flag = false;
            }
//            console.log("区域:" + vm.addrFirst + " selected code:" + vm.searArea);
          }
        }
        else if(type=='firstCity'||type=='secondCity'){
          //遍历二级市
          if(vm.searArea!='1000'){//一级除选择‘全部’外
            var childData=vm.childA[vm.searArea];
            var selKey=item.keyword;
            for(var i in childData){
              if(selKey == childData[i].keyword){
                childData[i].flag=true;
                var addrWordFirst=childData[i].keyword=='二级全部'?"":childData[i].keyword;
                vm.addrSecond=addrWordFirst;
                if(vm.addrSecond==""){//如果选择‘二级全部’，重置除“全部”的flag
                  vm.initAreaflag(childData);

                  console.log("二级区域:"+vm.addrSecond);
                  vm.sear_addrname=vm.addrFirst+vm.addrSecond;
                  vm.mainlistData=[];
                  vm.$refs.loadMoreTip.hide();//隐藏“没有更多”文字提示
                  vm.getMainlist();
                  vm.shadowF=false;
                  console.log("搜索地址："+vm.sear_addrname);
                  return false;

                }
              }
              else{
                childData[i].flag=false;
              }


            }
          }
          vm.sear_addrname=vm.addrFirst+vm.addrSecond;
          vm.mainlistData=[];
          vm.$refs.loadMoreTip.hide();//隐藏“没有更多”文字提示
          vm.getMainlist();
          vm.shadowF=false;
          console.log("搜索地址："+vm.sear_addrname);
        }

      },
      //初始化区域选择
      /*initSelarea(){
        var vm=this;
        vm.addrFirst="";
        vm.addrSecond="";
        //初始化一级选择flag
        vm.initAreaflag(vm.parentA);
      },*/
      //初始化区域（除“全部”外，其他flag置为false）
      initAreaflag(array){
        // console.log("初始化区域");
        var vm=this;
        for(var i in array){
          if(array[i].name=="全部" || array[i].keyword=="全部" || array[i].keyword=="一级全部" || array[i].keyword=="二级全部"){
            array[i].flag=true;
//            vm.set(array,flag,false);
          }
          else{
            array[i].flag=false;
          }
        }
      },
      //隐藏搜索框
      hideSearch(){
        var vm=this;
        vm.shadowF=false;

       /* //遍历获取值
        for(var i in vm.parentA){

        }
        for(var i in vm.childA['3710']){
          vm.search(vm.childA['3710'][i],'firstCity');
        }
        for(var i in vm.childA['3706']){
          vm.search(vm.childA['3706'][i],'secondCity');
        }


        vm.mainlistData=[];
        vm.getMainlist();
        */
      },
      //去详情页
      goDetail(item){
        var vm=this;
        let id=item.id;
        let orgId=item.userOrgId;
        let userId="";
        var flag="";
        let userType=item.userType;
        console.log("userType:"+userType);
        let routeName="";
        //代办(ag)，客商(trader)，冷库(cold)跳转到组织的详情(useOrgId) name:"xqframe"
        //果农.合作社跳转到被浏览者的个人页面（id）name:"personXq"
        switch(userType){
          case 'DB':
              flag="ag";
              routeName="xqframe";
              break;
          case 'KS':
              flag="trader";
              routeName="xqframe";
              break;
          case 'LK':
              flag="cold";
              routeName="xqframe";
              id = orgId;
              break;
          case 'ZZH':
              flag="ZZH";
              routeName="personXq";
              userId=id;
              id="";

              break;
          case 'HZS':
              flag="HZS";
              userId = item.id;
              routeName="personXq";
              break;
        }
        vm.$router.push({name:routeName,query:{flag:flag,id:id,orgId:orgId,userId:userId}});
      },
      //初始化页面
      initPage(){
        var vm=this;
        vm.mainlistData=[];
        vm.pageNum=1;
        vm.loadMflag=true;//上拉加载标识 "没有更多信息了"文字
        // vm.loadMopen=false;//上拉加载标识
        vm.scrolltipFlag=false;
        // vm.nodata=false;
        vm.shadowF=false;
        //初始化“身份/区域”
        vm.initAreaflag(vm.usertypeData);
        vm.initAreaflag(vm.parentA);
        vm.initAreaflag(vm.childA["3710"]);
        vm.initAreaflag(vm.childA["3706"]);



        /*for(var i in vm.parentA){
          console.log("vm.parentA[i].flag:"+vm.parentA[i].flag)
        }*/


        vm.sear_idCode=null;
        vm.sear_addrname="";

      },
      //上拉加载更多
      loadMore(){
        var vm=this;

        if(vm.loadMflag){
          // vm.loadMopen=true;
          vm.pageNum++;
          vm.$nextTick(function(){
            vm.$refs.loadMoreTip.loading();
          });
          vm.getMainlist();
          // console.log("上拉加载更多："+vm.pageNum);
        }
      },
      /*获取主列表数据*/
      getMainlist(){
        var vm=this;
        /*
        Indicator.open({
          text:"加载中...",
          spinnerType:'fading-circle'
        });
        */
        let params={
          api:"/yg-user-service/user/findUserFocusOrg.apec",
          data:{
            userType:vm.sear_idCode,//用户身份
            address:vm.sear_addrname, //所在地区
            //分页信息
            pageNumber:vm.pageNum,//页码
            pageSize:vm.pageSize//页容量
          }
        };
        vm.post(params,vm.mainlistCb);

      },
      mainlistCb(data){
        var vm=this;
        Indicator.close();
        if(data.succeed){
          //当只有一页时，触发上拉加载不请求
         /* if(data.data.rows.length<vm.pageSize){
            console.log("没数据了");
            vm.loadMflag=false;
          }*/
          //用户触发上拉加载时

            if(data.data.totalElements == 0){//无数据
              // vm.nodata=true;
              vm.blankFlag=true;
              vm.loadMflag=false;
              vm.$refs.loadMoreTip.hide()
            }
            else if(data.data.totalElements && data.data.currentNo==data.data.pageCount){//没有分页了
            // else if(data.data.rows.length < vm.pageSize || data.data.currentNo == data.data.pageCount){//没有分页了
              console.log("没数据了");
              vm.blankFlag=false;
              vm.loadMflag=false;
              // if(vm.loadMopen){
              vm.$refs.loadMoreTip.end("没有更多数据了");
            // }
            }
            else{//还有分页

              vm.blankFlag=false;
              vm.loadMflag=true;
              // vm.$refs.loadMoreTip.hide();
            }
          // }
            vm.mainlistData=vm.mainlistData.concat(data.data.rows);
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
    components:{
      'my-header':header,
      'my-scroll':scroll,
      'my-scrolltip':scrollTip,
      'my-blankimg':blankimg
    }
  }
</script>
