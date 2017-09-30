<template>
    <div class="z-p-home">
      <div class="z-p-banner">
        <div class="z-header-person-h">
          <img src="../../../assets/img/ret.png" @click="back">
        </div>
         <div class="z-banner-img">
            <img :src="person.bannerImgUrl">
           <!--<img src="../../../assets/img/xqimg1.png">-->
         </div>
          <div class="z-canmer" v-if="person.poritable">
            <input type="file" class="z-imgInput" multiple @change="modify">
          </div>
          <div class="z-h-t">
             <img :src="person.portrait">
          </div>
      </div>
      <div class="z-p-info">
         <span class="z-p-name">{{person.name}}</span>
        <img :src="person.levelSrc" class="z-p-level" >
         <span class="z-p-agency" >{{person.useType}}</span>
         <span class="z-p-real" v-if="person.real">实名认证</span>
        <!--<img src="../../../assets/img/Diamonds.png" class="z-p-level">-->
      </div>
      <div class="z-p-concat">
         <div class="z-p-notice">
            <p class="z-common-t">{{person.notice}}</p>
            <p class="z-common-t2">关注</p>
         </div>
        <div class="z-p-browse">
          <p class="z-common-t">{{person.bs}}</p>
          <p class="z-common-t2">浏览</p>
        </div>
        <div class="z-p-c">
          <p class="z-common-t">{{person.concat}}</p>
          <p class="z-common-t2">联系</p>
        </div>
        <!--<div class="z-p-economy">-->
           <!--<span class="z-p-economy-text">供应链金融合作库</span>-->
        <!--</div>-->
      </div>
      <!--<div class="z-space"></div>-->
      <div class="c-z-kg"></div>
      <div class="z-p-manage-info">
        <div class="z-p-cold" v-if="role.coldF">
          <img src="../../../assets/img/lk.png" class="img-com">
          <span>冷库名称</span>
          <input type="text" disabled v-model="person.lk">
        </div>
        <div class="z-p-c-storage" v-if="role.coldF">
          <img src="../../../assets/img/kr.png"  class="img-com">
          <span>库容量 &nbsp;&nbsp;</span>
          <input type="text" disabled v-model="person.kr">
        </div>
        <div class="z-p-c-cooperative" v-if="role.cooperativeF">
          <img src="../../../assets/img/lk.png"  class="img-com">
          <span>合作社 &nbsp;&nbsp;</span>
          <input type="text" disabled v-model="person.hzs">
        </div>
        <div class="z-p-area">
            <img src="../../../assets/img/qy.png"  class="img-com">
            <span>所在区域</span>
            <input type="text" disabled v-model="person.address">
        </div>
        <div class="z-p-pz" >
          <img src="../../../assets/img/zypz.png"  class="img-com">
          <span>主营品种</span>
          <input type="text" disabled v-model="person.pz">
        </div>
        <div class="z-p-cmarket" v-if="role.agencyF">
          <img src="../../../assets/img/kh.png"  class="img-com">
          <span>客户市场</span>
          <input type="text" disabled v-model="person.xsq">
        </div>
        <!--<div class="z-p-transship" v-if="role.traderF">-->
          <!--<img src="../../../assets/img/dhqy.png" class="img-com">-->
          <!--<span>调货区域</span>-->
          <!--<input type="text" readonly v-model="person.dhq">-->
        <!--</div>-->
        <div class="z-p-sale"  v-if="role.traderF">
          <img src="../../../assets/img/xsqy.png" class="img-com">
          <span>销售区域</span>
          <input type="text" disabled v-model="person.xsq">
        </div>
      </div>
      <div class="z-description">
        <div class="z-p-edit-save">
          <img src="../../../assets/img/sl.png" class="img-com">
          <span class="z-p-text">实力描述:</span>
          <!--<div class="z-save" v-if="saveFlag" @click="save">保存</div>-->
        </div>
         <!--<textarea placeholder="赶快填写吧，让更多用户关注你" ref="edit" disabled></textarea>-->
         <p class="z-edit-p">{{person.des == ""?"赶快填写吧，让更多用户关注你":person.des}}</p>
         <!--<div class="z-icon" @click="edit">-->
         <!--</div>-->
         <ul>
             <li v-for="item in person.itemRImg"
                >
                 <img :src="item">
             </li>
         </ul>
      </div>
      <div class="z-transship-ret" v-if="role.agencyF || role.traderF">
          <div class="c-z-kg"></div>
          <h4 class="z-transship-title">平台战绩</h4>
          <div class="z-space"></div>
          <div class="z-transship-num">
             <div class="z-r-totalW">
                  <p class="z-r-s-com">{{battlefield.number}}</p>
                  <p>累计调货</p>
             </div>
             <div class="z-vertical-line">
                <div class="z-v-line">
                </div>
             </div>
             <div class="z-r-rank">
               <p class="z-r-s-com">{{battlefield.rank}}</p>
               <p>平台排名</p>
             </div>
          </div>
      </div>
      <div class="z-num-main"  v-if="role.agencyF">
          <div class="c-z-kg"></div>
         <h4 class="z-num-main-t">调果数据</h4>
        <div class="z-space"></div>
        <div id="mainST" class="z-p-main"></div>
      </div>
      <!--<div class="c-test">-->
         <!--<div class="c-t-com" @click="test1">代办</div>-->
        <!--<div class="c-t-com" @click="test2">客商</div>-->
        <!--<div class="c-t-com" @click="test3">种植户</div>-->
        <!--<div class="c-t-com" @click="test4">冷库</div>-->
        <!--<div class="c-t-com" @click="test5">合作社</div>-->
      <!--</div>-->
    </div>
</template>
<style scoped>
 @import "../../../assets/css/personH.css";
</style>
<script>
  import API from '../../../api/api'
  import DEL from '../../../components/del.vue'
  import IMG from '../../../components/gqimg.vue'
  import T from "../../../assets/img/t.png"
  import DBanner from "../../../assets/img/defaultBg.png"
  import P from "../../../assets/img/icon.png"
  import {MessageBox, Indicator, Toast} from 'mint-ui';
  const api = new API();
  var ec = require("../../../assets/js/echarts.min");

  var fn = {
      img:[],
      bannerImg:function (data) {
          if(!data.succeed){
          Toast(data.errorMsg);
          return;
          }
        this.person.bannerImgUrl = data.data.orgBannerURL;
      },
      init:function (data) {

        var isobj = data.data;
        var dt;
        if(typeof isobj == "string"){
            dt = JSON.parse(isobj);
        }else{
            dt = isobj
        }

        var role = dt.userType;
        switch (role){
          case "LK":
             this.role.agencyF = false;
             this.role.coldF = true;
              break;
          case "DB":
//              var userId = window.localStorage.userId;
              var params = {
                  api:"/yg-voucher-service/voucher/getSelfNumberRankViewVO.apec",
                  data:{
//                      userId:userId
                  }
              }
              this.post(params, this.DBRet);
              break;
          case "KS":
            this.role.agencyF = false;
            this.role.traderF = true;
//            var userId = window.localStorage.userId;
            var params = {
              api:"/yg-voucher-service/voucher/getSelfNumberRankViewVO.apec",
              data:{
//                userId:userId
              }
            }
            this.post(params, this.DBRet);
            break;
          case "HZS":
             this.role.agencyF = false;
             this.role.cooperativeF = true;
            break;
          case "ZZH":
            this.role.agencyF = false;
            break;

        }

          if(dt.imgUrl == "" || !dt.imgUrl){
             this.person.portrait = P;
          }else{
            this.person.portrait = dt.imgUrl;

          }
          if(!dt.userOrgClientVO){
            this.person.bannerImgUrl = DBanner;
          }else{
              if(dt.userOrgClientVO.orgBannerUrl == ""){
                this.person.bannerImgUrl = DBanner;
              }else{
                this.person.bannerImgUrl = dt.userOrgClientVO.orgBannerUrl;
              }

          }

          this.person.name = dt.name;
          this.person.levelSrc =IMG.methods.userLevel(dt.userPoint.userLevel);
          this.person.useType = dt.userTypeKey;
           console.log(dt.userTypeKey);
          this.person.real = dt.userRealAuth =="UNREALAUTH"?false:true;
          if(dt.userOrgClientVO){
            this.person.cold =dt.userOrgClientVO.orgName ;
            this.person.lk = dt.userOrgClientVO.orgName;
            this.person.kr =dt.userOrgClientVO.orgStockCap ;
            this.person.hzs =dt.userOrgClientVO.orgName ;
            this.person.address =dt.userOrgClientVO.address ;
            this.person.pz = dt.userOrgClientVO.mainOperating;
            this.person.xsq = dt.userOrgClientVO.saleAddress;
            this.person.des = dt.userOrgClientVO.remark;
//            this.person.itemRImg = dt.userOrgClientVO
          }

          if(dt.userDetailType == "LK_BG"){
               this.person.poritable = false;
          }else{
              this.person.poritable = true;
          }


      },
      addImage:function (data){
        var dt = data.data;

      },
    initChat:function (data, weight) {
      var el = document.getElementById("mainST");
      var myChart = ec.echarts.init(el);
      var xData = [];
      var yData = [];
      var dt;
      if(data){
          dt = JSON.parse(data)
      }
      for(var key in dt){
        xData.push(key);
        yData.push(dt[key]);
      }
      var tw;
      if(weight){
        tw ="重量("+weight+")";
      }else{
        tw ="重量";
      }
      var nameData = ["70#一二","75#一二","80#一二","85#一二","95#一二","89#一二"];
      var option = {
        color: ['#27cba8'],
        title: {
          text: '',
        },
        grid: {
          left: '3%',
          right: '13%',
          bottom: '15%',
          containLabel: true
        },
        tooltip: {
          axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        legend: {
          data:['数量'],
          selectedMode:false,
        },
        xAxis: {
          data: xData,
          name:"种类",
//            max:5,
//            splitNumber:10,
          boundaryGap: ['120%', '120%'],

          axisTick:{
            interval:0,
//                length:10
          },
          axisLabel:{
            interval:0 ,
            rotate:45
//            formatter:function(val){
//              return val.split("").join("\n");
//            }
          }
        },
        yAxis: {
          name:tw,
          splitLine:{
            show:false
          }
        },
        series: [{
          name: '数量' ,
          type: 'bar',
          barWidth:"30%",
//          data: [5, 20, 36, 20, 36, 10]
          data:yData
        }]
      };

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }
  }
  export default{
      data(){
          return{
            saveFlag:false,
            editFlag:true,
            items:null,
            person:{
                  name:'',
              lk:"",
              kr:"",
              hzs:"",
                  address:'',
                  useType:'',
                  real:"",
              levelSrc:T,
              address:"",
              dhq:"",
              pz:'',
              bannerImgUrl:"#",
              market:"",
              xsq:"",
              des:"",
              notice:0,
              bs:0,
              concat:0,
              portrait:null,
              itemRImg:[],//实力描述图片
            },
            battlefield:{
                number:"0",
                rank:"1"
            },
            role:{
                agencyF:true,
                coldF:false,
                traderF:false,
              cooperativeF:false,
            },

          }
      },
      methods:{
          reset(){
              this.role.agencyF = true;
            this.role.coldF = false;
            this.role.traderF = false;
            this.role.cooperativeF = false;
          },
        modify(e){
              var e = e || window.event;
              var target = e.toElement || e.srcElement;
              var files = target.files;
              var type = files[0].type;
              var fd = new FormData();
             fd.append("whetherCompression",1);
             fd.append("file", files[0]);
            let params = {
              api:"/yg-user-service/user/uploadBanner/uploadFile.apec",
              data: fd
            }

            this.postImg(params, fn.bannerImg.bind(this))
          },
        back(){
            this.reset();
              this.$router.go(-1);
          },
        postImg(params, fn){
          try {
            api.postImg(params).then((res) => {
              var data = res.data;
              fn(data);
            }).catch((error) => {
              console.log(error)
            })
          } catch (error) {
            console.log(error)
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
        edit(){
            this.editFlag = false;
            this.saveFlag = true;
            var el = this.$refs.edit;
            this.$router.push({name: 'pcInfo'});
        },
        save(){
          this.editFlag = true;
          this.saveFlag = false;
          var el = this.$refs.edit;
          el.readOnly= true;
        },
        addImage(e){
            var e = e || window.event;
            var target = e.toElement || e.srcElement;
            var files = target.files;
            var fd = new FormData();
            fd.append("file",files[0]);
            let params = {
              api:"/yg-user-service/user/uploadBanner/uploadFile.apec",
              data: fd
            }

          this.postImg(params, fn.addImage.bind(this))
        },
        DBRet(data){
          var dt = data.data;
          var weight = dt.weight;
          this.battlefield.number = dt.totalNumber + weight;
          this.battlefield.rank = dt.rankNo;
          if(dt.attrNumberMap){
            fn.initChat(dt.attrNumberMap, weight);
          }else{
            fn.initChat(dt.attrNumberMap, weight);
          }
        },
        bs(data){
          var dt = data.data;

          this.person.notice = dt.attenNum;
          this.person.bs = dt.viewNum;
          this.person.concat = dt.phoneNum;
        },
      },
    activated(){
      var useId = window.localStorage.useId;
      let params3 = {
        api:"/_node_user_org/_view_org_info.apno",
        data:{
          orgId:useId
        }
      }

      this.post(params3,this.bs);
      let params = {
        api:"/yg-user-service/user/findSelfInfo.apec",
      }
      this.post(params, fn.init.bind(this));

    }
  }
</script>
