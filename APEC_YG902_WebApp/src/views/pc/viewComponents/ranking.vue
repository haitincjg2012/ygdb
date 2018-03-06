/**
* Created by gsy on 2017/9/8.
*增加局部隔离系统
* 积分排行榜
*/
<template>
  <div id="rankBox">
    <my-scroll class="scrollWrapper" :data="rateData" :pullup="pullup" @scrollToEnd="loadMore">
    <div>
      <!--<div class="rankFix">-->
      <div class="rankBanner">
        <span @click="back" >
          <img src="../../../assets/img/backN.png"/>
        </span>
        <!--<h1>龙虎榜</h1>-->
        <!--<h2>最具实力调果排行榜</h2>-->
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
        <dd v-for="(item,index) in rateD">
        <span class="orderNum">
          <img :src="index|imgFilter" v-if="index<3"/>
          <i v-else>{{index+1}}</i>
        </span>
          <div class="rContent">
          <span class="headPhoto" @click="to(item)">
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
      <!-- <my-scrolltip ref="loadmoreTip"></my-scrolltip> -->

    </div>
  </my-scroll>
    <div class="c-upload-menu" @click="uploadMenu">
      <!--上传单据-->
    </div>
  </div>


</template>

<script>
    import c_js from '../../../assets/js/common'
    import {Toast,Indicator,MessageBox} from 'mint-ui'
    import API from '../../../api/api'
    import scroll from '@/components/scroll/scroll'
    import scrollTip from '@/components/downLoadAnimal'
    //徽章图片
    import rateFirst from '../../../assets/img/rateFirst.png'
    import rateSed from '../../../assets/img/rateSed.png'
    import rateThird from '../../../assets/img/rateThird.png'
    const api=new API();

    var buffer = {
        _data:{
        },
      storage:function (type,data) {
           buffer._data[type] = data;
        },
        clear:function (type) {
          delete buffer._data[type];
        },
        getData:function (type) {
          if(buffer._data[type]){
              return buffer._data[type];
          }else{
              return false;
          }
        }
      };
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
              rateData:[],
              rateD:null,

            }
        },
        activated(){
          var vm=this;
          vm.rateData=[];
          vm.rateD = null;
          vm.initTab();
          vm.initMthlist();
        },
        methods: {
            to(item){
              //跳转到个人中心
              var id = item.userId,
                   orgId = item.orgId || item.userId;
              this.$router.push({name:"personXq",query:{userId:id,orgId:orgId}});
            },
            //添加上传单据功能
          uploadMenu(){
            const self = this;
            let params = {
              api: "/_node_user/_check_pronum.apno"
            };
            try {
              api.post(params).then((res)=>{
                Indicator.close();
                var item = res.data;
                if (item.succeed){
                  var checkStatus = item.data.checkStatus;
                  var realAuth = item.data.realAuth;//是否实名验证
                  var realInfo = item.data.realInfo;//是否填写资料
                  if(!checkStatus && !realInfo){
                    //self.$store.commit("incrementCheck",{'check':'1'});
                    self.$store.state.check = "1";
                    self.$router.push({name:'validate'});
                  }
                  else
                    self.$router.push({name: 'uploadList'});
                } else {
                }
              }).catch((error)=> {

                }
              )
            } catch (error) {
              console.log(error)
            }
          },
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
            vm.rateD = null;
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

            // vm.$refs.loadmoreTip.loading();
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
                  // console.log("月榜没数据了");
                  vm.loadFlag=false;
                  // vm.$refs.loadmoreTip.end("没有更多数据了");
                }
                else{
                  vm.loadFlag=true;
                }
              }
//              rateD
                var key = "month" + data.data.currentNo;
                buffer.storage(key, data.data.rows);
//                console.log(buffer.getData(key), 8989);
              data.data.rows.forEach(function (current) {
                current.totalNumber = (current.totalNumber*1).toFixed(2);
              });
              if(data.data.rows.length > 0){
                vm.rateD=vm.rateD?vm.rateD.concat(data.data.rows):[].concat(data.data.rows);
                vm.rateData=vm.rateD;
              }

            }
            else{
              Toast(data.errorMsg);
            }
          },
          //总数
          getYearlist(){
            var vm=this;
            // vm.$refs.loadmoreTip.loading();

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
                  // console.log("年榜没数据了");
                  vm.loadFlag=false;
                  // vm.$refs.loadmoreTip.end("没有更多数据了");
                }
                else{
                  vm.loadFlag=true;
                }
              }

              data.data.rows.forEach(function (current) {
                current.totalNumber = (current.totalNumber*1).toFixed(2);
              });
              if(data.data.rows.length > 0){
                vm.rateD=vm.rateD?vm.rateD.concat(data.data.rows):[].concat(data.data.rows);
                vm.rateData=vm.rateD;
              }
//              vm.rateData=vm.rateData.concat(data.data.rows);
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
          'my-scroll':scroll,
          'my-scrolltip':scrollTip
        }
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  @import '../../../assets/css/ranking.css';
</style>
