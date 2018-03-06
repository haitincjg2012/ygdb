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
          <!--<div class="z-canmer" v-if="person.poritable">-->
            <!--<input type="file" class="z-imgInput" @change="modify">-->
          <!--</div>-->
         <label for="personInput" class="z-canmer" v-if="person.poritable"></label>
         <input type="file" class="z-imgInput" @change="modify" id="personInput">
          <div class="z-h-t">
             <img :src="person.portrait">
          </div>
      </div>
      <div class="z-p-info">
         <span class="z-p-name">{{person.name}}</span>
         <img :src="person.levelSrc" class="z-p-level" >
      </div>
      <my-introduction :notice="person.notice" :bs="person.bs" :concat="person.concat" :identityHtml="person.identityHtml" :remark="person.remark"></my-introduction>
      <div class="c-z-kg"></div>
      <div class="z-p-manage-info" v-html="business">
      </div>
      <div class="z-description">
        <div class="z-p-edit-save">
          <!--<img src="../../../assets/img/sl.png" class="img-com">-->
          <span class="z-p-text">实力描述:</span>
        </div>
         <p class="z-edit-p">{{person.des == ""?"赶快填写吧，让更多用户关注你":person.des}}</p>
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
        <div id="mainST" class="z-p-main">
          <div v-if="fruitDataFlag" class="fruitNoData">
            <img src="../../../assets/img/noData.png" class="c-z-nodata-img">
            <p class="c-z-nodata-text">暂无数据</p>
          </div>
        </div>
      </div>
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
  import ALIYUN from "../../../components/aliyun.vue"
  import idIntroduction from "../../businessV/personIdIntroduction.vue" //个人介绍组件

  import personHot from '../../businessV/personalHot.vue'//个人热点组件

  const api = new API();
  var ec = require("../../../assets/js/echarts.min");

  var fn = {
      img:[],
      bannerImg:function (url) {
        this.person.bannerImgUrl = url + "?x-oss-process=style/_detail";
        var params = {
            api:"/yg-user-service/user/uploadBanner.apec",
            data:{
              imgUrl:url,
            }
        }

        this.post(params, function () {
        });
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

          if(dt.imgUrl == "" || !dt.imgUrl){
             this.person.portrait = P;
          }else{
            this.person.portrait = dt.imgUrl + "?x-oss-process=style/_head";

          }
          if(!dt.userOrgClientVO){
            this.person.bannerImgUrl = DBanner + "?x-oss-process=style/_detail";
          }else{
              if(dt.userOrgClientVO.orgBannerUrl == ""){
                this.person.bannerImgUrl = DBanner + "?x-oss-process=style/_detail";
              }else{
                this.person.bannerImgUrl = dt.userOrgClientVO.orgBannerUrl + "?x-oss-process=style/_detail";
              }

          }

          this.person.name = dt.name;
          this.person.levelSrc =IMG.methods.userLevel(dt.userPoint.userLevel);
          this.person.useType = dt.userTypeKey;
        //身份的html
        var html = "<span class='z-p-name'>" + dt.userTypeKey +"</span>";
        if(dt.userType == "LK"){
          //供应链金融,企业认证
          var arrT = dt.userOrgClientVO.userTagsVOS;
          var self = this;
          arrT.forEach(function (current) {
            var className = current.className;
            if(className == "QYRZ"){
              html += " <span class='z-p-authentication'>"+ current.tagName + "</span>";
              self.person.coldF1 = true;
            }
            if(className == "GYLJRHZK"){
              html += " <span class='z-p-coor'>"+ current.tagName + "</span>";
              self.person.coldS = true;
            }
          })


        }else{
          html += (dt.userStatus == "UNREALAUTH")? "":"实名认证";
        }
        this.person.identityHtml = html;

          this.$store.state.userId = dt.id;//用户
          this.person.real = dt.userStatus =="UNREALAUTH"?false:true;

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

        //
        var business = "";
        switch (role){
          case "LK":
            this.role.agencyF = false;
            this.role.coldF = true;
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>冷库名称</span></div><div class="c-p-m-contentCom"><p>'+this.person.lk +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>库容量</span></div><div class="c-p-m-contentCom"><p>'+(this.person.kr == "" || !this.person.kr?"暂无数据":this.person.kr)+'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
            this.business = business;
            break;
          case "DB":
            var params = {
              api:"/yg-voucher-service/voucher/getSelfNumberRankViewVO.apec",
              data:{
              }
            }
            this.post(params, this.DBRet);
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>客户市场</span></div><div class="c-p-m-contentCom"><p>'+(this.person.xsq == "" || !this.person.xsq?"暂无数据":this.person.xsq) +'</p></div></div>';
            this.business = business;
            break;
          case "KS":
            this.role.agencyF = false;
            this.role.traderF = true;
            var params = {
              api:"/yg-voucher-service/voucher/getSelfNumberRankViewVO.apec",
              data:{
              }
            }
            this.post(params, this.DBRet);
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>销售区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.xsq == "" || !this.person.xsq?"暂无数据":this.person.xsq) +'</p></div></div>';
            this.business = business;
            break;
          case "HZS":
            this.role.agencyF = false;
            this.role.cooperativeF = true;
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>合作社</span></div><div class="c-p-m-contentCom"><p>'+this.person.lk +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
            this.business = business;
            break;
          case "ZZH":
            this.role.agencyF = false;
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+ (this.person.address == "" || !this.person.address?"暂无数据":this.person.address)+'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
            business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>客户市场</span></div><div class="c-p-m-contentCom"><p>'+(this.person.xsq == "" || !this.person.xsq?"暂无数据":this.person.xsq) +'</p></div></div>';
            this.business = business;
            break;
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
            business:"",
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
            aliyunO:null,//阿里云调用函数
            fruitDataFlag:true,//默认情况下,都认为有数据存在
          }
      },
      methods:{
          reset(){
            this.fruitDataFlag = true;//默认情况下,都认为有数据存在
              this.role.agencyF = true;
            this.role.coldF = false;
            this.role.traderF = false;
            this.role.cooperativeF = false;
          },
        modify(e){
              var e = e || window.event;
              var target = e.toElement || e.srcElement;
              var files = target.files;
              var file = files[0];
              var type = files[0].type;

              var self = this;
              this.aliyunO(self.$store.state.userId,fn.bannerImg.bind(this) , file);
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
            this.fruitDataFlag = false;
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
      this.aliyunO = ALIYUN.aliyun();
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
    },
    components:{
      "my-introduction":idIntroduction,
    }
  }
</script>
