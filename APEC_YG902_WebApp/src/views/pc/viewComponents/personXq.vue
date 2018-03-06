<template>
    <div class="c-p-xq">
      <my-scroll class="scrollWrapper" :data="dataArr" :pullup="pullup" @scrollToEnd="loadMore" ref="personWrapper">
        <div>
      <div class="z-p-banner">
        <div class="z-header-person-h">
          <img src="../../../assets/img/ret.png" @click="back">
        </div>
        <my-banner :bannerImgUrl="person.bannerImgUrl" :portrait ="person.portrait" :name="person.name" :portraitSrc="person.portraitSrc"></my-banner>
      </div>
      <div class="c-person-hot">
           <my-personHot :personHot = "person"></my-personHot>
      </div>
      <my-personInfo :identityHtml = "person.identityHtml" :signatureHtml="person.signatureHtml" :picture ="person.levelSrc"></my-personInfo>
      <div class="c-space-bg"></div>
      <div class="z-p-manage-info" v-html="person.business"></div>
      <div class="c-space-bg"></div>
      <div class="z-transship-ret" v-html="battlefieldHtml"></div>
      <div class="z-num-main" :class="{show:!role.agencyF}">
         <h4 class="z-num-main-t">调果数据</h4>
        <div class="z-space"></div>
        <!--<div id="mainS" class="ZPmain" :class="{ZPShow:AgChart}" ></div>-->
        <div id="mainS" class="ZPmain"></div>
        <!--如果没有数据就不要显示-->
        <!--<div class="c-z-ondata" v-if="AgChart">-->
          <!--<img src="../../../assets/img/noData.png" class="c-z-nodata-img">-->
          <!--<p class="c-z-nodata-text">暂无数据</p>-->
        <!--</div>-->
        <div class="c-space-bg"></div>
      </div>
      <div class="z-p-gy-T">
        <h4 class="c-p-gy-x">供求</h4>
        <div class="c-gy-v"></div>
        <div>
          <ul class="c-gq-list">
            <li
              :is="item.ss"
              :item="item"
              v-for="item in itemGQ" v-on:xq="initD">
            </li>
            <li class="li-space" v-if="!loadFlag">没有更多数据</li>
          </ul>
        </div>
      </div></div>
      </my-scroll>
      <my-phone class="footer" :mobile="person.mobile" :attentionFlag.sync="attentionFlag" @phone="phoneClick" @attention="notice" mark="2"></my-phone>
    </div>
</template>
<style scoped>
 @import "../../../assets/css/personXq.css";
</style>
<script>
  import API from '../../../api/api'
  import DEL from '../../../components/del.vue'
  import IMG from '../../../components/gqimg.vue'
  import T from "../../../assets/img/t.png"
  import DBanner from "../../../assets/img/defaultBg.png"
  import scroll from '@/components/scroll/scrollbg'
  import defaultIcon from "../../../assets/img/defaultForm.png"

  import {Swipe, SwipeItem, Indicator,MessageBox, Toast} from 'mint-ui'//弹框组件
  import personHot from '../../businessV/personalHot.vue'//个人热点组件
  import personInfo from '../../businessV/personalInfo.vue'//个人简介组件
  import Banner from '../../businessV/personalBanner.vue'//个人banner图组件

  import phoneC from '../../businessV/phone.vue' //打电话组件
//  import MYGQList from "../../home/viewComponents/mygqlist.vue"
  import MYGQList from "../../home/viewComponents/goodlist.vue"

  const api = new API();
  var ec = require("../../../assets/js/echarts.min");
//  var picturArr = {'QT':'src="../../assets/img/t.png"',//铜牌
//                    'BY':'src="../../../assets/img/y.png"',//银牌
//                    'HJ':'src="../../../assets/img/j.png"',//金牌
//                    'BJ':'src="../../../assets/img/bj-1.png"',//铂金
//                    'ZS':'src="../../../assets/img/zs.png',//钻石
//                       };
  var fn = {
      img:[],
      bannerImg:function () {
      },
      init:function (data) {
          if(!data.succeed){
            Toast(data.errorMsg);
            return;
          }
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
             this.reset();
             this.role.agencyF = false;
             this.role.coldF = true;
             break;
          case "DB":
            this.reset();
            var browserId = this.browserId;
              var params = {
                  api:"/yg-voucher-service/voucher/getNumberRankViewVO.apec",
                  data:{
                      userId:browserId
                  }
              }
              this.post(params, this.DBRet);
              break;
          case "KS":
            this.reset();
            this.role.agencyF = false;
            this.role.traderF = true;
            var browserId = this.browserId;
            var params = {
              api:"/yg-voucher-service/voucher/getNumberRankViewVO.apec",
              data:{
                userId:browserId
              }
            }
            this.post(params, this.DBRet);
            break;
          case "HZS":
            this.reset();
             this.role.agencyF = false;
             this.role.cooperativeF = true;
            break;
          case "ZZH":
            this.reset();
            this.role.agencyF = false;
            break;

        }

          if(dt.imgUrl == ""){
             this.person.portrait = defaultIcon + "?x-oss-process=style/_head";
             this.person.portraitSrc = "";
          }else{
            this.person.portrait = dt.imgUrl + "?x-oss-process=style/_head";
            this.person.portraitSrc = dt.imgUrl;
          }

          if(!dt.userOrgClientVO){
            this.person.bannerImgUrl = DBanner + "?x-oss-process=style/_head";
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
          this.person.mobile = dt.mobile;
          this.person.real = dt.userStatus =="UNREALAUTH"?false:true;

          if(dt.userOrgClientVO){
            this.person.lk =dt.userOrgClientVO.orgName ;//冷库名称
            this.person.kr =dt.userOrgClientVO.orgStockCap ;//冷库的库容量
            this.person.hzs =dt.userOrgClientVO.orgName ;//合作社的名称
            this.person.address =dt.userOrgClientVO.address ;//所在区域
            this.person.pz = dt.userOrgClientVO.mainOperating;//主营品种
            this.person.xsq = dt.userOrgClientVO.saleAddress;//销售区域和客户市场
            this.person.des = dt.userOrgClientVO.remark;//实力描述
          }

        //身份

        var html = "<span class='c-pI-levelText'>" + this.person.useType + "</span>";
        if (this.person.real) {
          html += "<span class='c-pI-real'>实名认证</span>";
        }

        this.person.identityHtml = html;
        //签名
        var signature = "";
        if(this.person.des == "" || !this.person.des){
          signature += '<span class="signDefault">急卖货，急找货，果来乐都能做</span>'
        }else{
          signature += '<span class="c-pI-sign-text">' + dt.userOrgClientVO.remark + '</span>';
        }
        this.person.signatureHtml = signature;

        //个人业务
        var business = "";
        if(this.person.useType == "冷库"){
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>冷库名称</span></div><div class="c-p-m-contentCom"><p>'+this.person.lk +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>库容量</span></div><div class="c-p-m-contentCom"><p>'+this.person.kr +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
        }else if(this.person.useType == "合作社"){
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>合作社</span></div><div class="c-p-m-contentCom"><p>'+this.person.lk +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
        }else if(this.person.useType == "代办"){
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>客户市场</span></div><div class="c-p-m-contentCom"><p>'+(this.person.xsq == "" || !this.person.xsq?"暂无数据":this.person.xsq) +'</p></div></div>';
        }else if(this.person.useType == "客商"){
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.address == "" || !this.person.address?"暂无数据":this.person.address) +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>销售区域</span></div><div class="c-p-m-contentCom"><p>'+(this.person.xsq == "" || !this.person.xsq?"暂无数据":this.person.xsq) +'</p></div></div>';
        }else if(this.person.useType == "果农"){
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>所在区域</span></div><div class="c-p-m-contentCom"><p>'+ (this.person.address == "" || !this.person.address?"暂无数据":this.person.address)+'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>主营品种</span></div><div class="c-p-m-contentCom"><p>'+(this.person.pz == "" || !this.person.pz?"暂无数据":this.person.pz) +'</p></div></div>';
          business += '<div class="c-p-manage-com"><div class="c-p-m-nameCom"><span>客户市场</span></div><div class="c-p-m-contentCom"><p>'+(this.person.xsq == "" || !this.person.xsq?"暂无数据":this.person.xsq) +'</p></div></div>';
        }
        this.person.business = business;
        let params3 = {
          api:"/_node_user_org/_view_org_info.apno",
          data:{
            orgId:dt.userOrgClientVO.id//组织id和userOrgId和orgId
          }
        }

        this.post(params3,this.bs);
      },
      addImage:function (data){
        var dt = data.data;

      },
    initChat:function (data, weight) {

      var el = document.getElementById("mainS");
      var myChart = ec.echarts.init(el);
      var xData = [];
      var yData = [];
      if(data){
          var dt = JSON.parse(data);
      }
      for(var key in dt){
        xData.push(key);
        yData.push(dt[key]);
      }
      var nameData = ["70#一二","75#一二","80#一二","85#一二",];
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
          name:"重量("+weight+")",
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
    },
    initgq(data){
      if(!data.succeed){
        return;
      }
      var that = this;
      var rows = data.data.rows;
      if(rows.length > 0){
        var arr = [];
        if(this.pageNumber >= data.data.pageCount){
          this.loadFlag = false;
        }

        this.pageCount = data.data.pageCount;
        rows.forEach(function (current, index) {
          var obj = {};
          var len = 0;
          obj.ss = MYGQList;
          obj.skuName = current.skuName;
          obj.showCredateTime = current.showCredateTime;
          var id = current.id;
          obj.id = id;
          obj.levelImg =IMG.methods.userLevel(current.userLevelName);
          obj.img = current.firstImageUrl+ "?x-oss-process=style/_list";

          obj.name = current.showUserName;
          obj.Flag = that.name;
          obj.userId = current.userId;//userId和orgId是相同的
          obj.orgId = current.userId;
          obj.priceUnit = current.priceUnit;
          obj.agency = current.userTypeName;
          obj.number = current.viewNum;
//          obj.star = current.saveNum;
//          obj.starFlag = current.saveFlag;
          //地址的截取
          var pos = current.address.indexOf("镇");
          var posL = current.address.indexOf("区");

          if(pos > -1){
            obj.local = current.address.substring(0, pos + 1);
          }else if(posL > -1){
            obj.local = current.address.substring(0, posL + 1);
          }else
            obj.local = current.address;

          var wU = current.weightUnit;
          var t = "";
          if(wU){
            t = wU;
          }
          obj.weight = current.weight +" " + t;
          len += obj.weight.length;
          obj.path = index;
          //品种的种类
          var html = "";
          current.showSecondInfo.forEach(function (fruitC) {
            html +="<span class='g-sp-com g-first ' data-id="+current.id+">"+fruitC + "</span>";
          });

          obj.fruitdate = html;

          var Identification = current.productTypeName;
          obj.gq = current.productTypeName;
          //单一的排序
          //只有求购和供应
          //求购和供应的价格
          var hprice = "";
          if(Identification == "求购"){
            hprice = "<span data-id=" + current.id +" class='g-price-com-f gy'>&yen;" + current.startAmount + "~" +current.endAmount + "</span><span class='g-unit'  data-id=" + current.id + ">" + current.priceUnit +"</span>";
            obj.price = hprice;

            obj.bg = true;
//            obj.indentification = 0;
//            obj.endAmount = current.endAmount.toString();
//            len += obj.endAmount.length;
//            obj.startAmount = current.startAmount.toString();
//            len += obj.startAmount.length;
          }else{
            hprice = "<span data-id=" + current.id +" class='g-price-com-f gy'>&yen;" + current.amount + "</span><span class='g-unit'  data-id=" + current.id + ">" + current.priceUnit +"</span>";
            obj.price = hprice;
            obj.bg = false;
          }

          //重量
          obj.weight = "<span class='g-unit'>" + current.weight + "&nbsp;" + current.weightUnit + "</span>";

          var n = (250 - (len + 3 + 3 + 3) * 10)/20;
          obj.wh = n;

          var pattern =/[0-9]*/g;
          var pt = /([a-z]*|[A-Z]*|(\s*))/g;
          var st = obj.name +"";
          var arrttt =st.match(pattern);
          var arrtttt = st.match(pt);
          var lF = 0,lt = 0;

          arrttt.forEach(function (current) {
            lF += current.length;
          })

          arrtttt.forEach(function (current) {
            lt += current.length;
          })
          obj.addreeWh = (185 - (obj.name.toString().length + obj.agency.length) * 12 + lF * 6 + lt*8)/20;;
          obj.productTypeName = IMG.methods.img(current.productTypeName);
          if(that.del.hasOwnProperty(id)){
            return;
          }
          that.del[id] = 0;
          arr.push(obj);
        });
        that.itemGQ = that.itemGQ?that.itemGQ.concat(arr):[].concat(arr);
        that.dataArr =that.dataArr.concat(arr);
      }

    },
    GZ(data){
      if(!data.succeed){
        return;
      }
      this.attentionFlag = data.data.attenFlag;
    },
    phone:function (data) {
      if(data.succeed){
        this.person.concat++;
      }

    },
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
                  real:"实名认证",
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
              portraitSrc:null,//默认图片的路径
              mobile:0,//个人电话
            },
            battlefieldHtml:"",//调果战绩
            battlefield:{
                number:"0",
                rank:"1"
            },
            role:{
                agencyF:true,//代办
                coldF:false,//冷库
                traderF:false,//客商
                cooperativeF:false,//合作社
            },
            dataArr:[],
            itemGQ:null,//供求信息
            del:{},//记录数据条数,
            browserId:"",//被浏览人id
            borgId:"",

            //上拉加载更多
            pullup:true,
            loadFlag:true,//"没有更多信息了"文字
            loadMopen:false,//上拉加载标识
            pageCount:10000,//供求的总数
            pageNumber:1,//供求信息的页码
            AgChart:false,//默认情况是有数据
            attentionFlag:false,//是否关注
          }
      },
      methods:{
          reset(){

              this.role.agencyF = true;
              this.role.coldF = false;
              this.role.traderF = false;
              this.role.cooperativeF = false;
              this.AgChart = false;

          },
        initD(data){
        },
        GZ(){
          // 是否关注的方法
          var self = this;
          var params = {
            api:"/_node_user/_org_atten_flag.apno",
            data:{
              "orgId":self.borgId
            }
          }
          self.post(params,fn.GZ.bind(this));
        },
        phoneClick(){
          var self = this;
          let params = {
            api:"/_node_user_org/_view_org.apno",
            data:{
              orgId:self.borgId,
              vieType:"PHONENUM"
            }
          }

          this.post(params,this.phoneViewRet);
        },
        phoneViewRet(){
        },
        modify(e){
              var e = e || window.event;
              var target = e.toElement || e.srcElement;
              var files = target.files;
              var type = files[0].type;
              var fd = new FormData();
             fd.append("file", files[0]);
            let params = {
              api:"/yg-user-service/user/uploadBanner/uploadFile.apec",
              data: fd
            }

            this.postImg(params, fn.bannerImg.bind(this))
          },
        back(){
            this.reset();
            this.loadFlag = true;
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

          if(data.data){
            var dt = data.data;
            var weight = dt.weight;
            var number = dt.totalNumber.toFixed(1);
            var numberW = number + weight;
            var rank = dt.rankNo;
            if(number > 0){
              this.battlefieldHtml = "<h4 class='z-transship-title'>平台战绩</h4><div class='z-space'></div><div class='z-transship-num'><div class='z-r-totalW'>"
                +"<p class='z-r-s-com'>" + numberW + "</p><p>累计调货</p></div><div class='z-vertical-line'><div class='z-v-line'></div></div>"
                +"<div class='z-r-rank'><p class='z-r-s-com'>"+ rank + "</p><p>平台排名</p></div></div><div class='c-space-bg'></div>";
            }else{
                this.role.agencyF = false;
                return;
            }


            if(dt.attrNumberMap){
              this.$nextTick(function () {
                fn.initChat(dt.attrNumberMap, weight);
              })
            }
          }

        },
        bs(data){
          var dt = data.data;
          this.person.notice = dt.attenNum;
          this.person.bs = dt.viewNum;
          this.person.concat = dt.phoneNum;
        },
        gqlist(){
          var pg = arguments[0] || this.pageNumber;
          var that = this;
          let params = {
            api:"/_node_user/_product_list.apno",
            data:{
              orgId:that.borgId,
              pageNumber: "1",
            }
          }
          this.post(params,fn.initgq.bind(this));
        },
        notice(flag){
          var fn;
          if(flag){
            this.person.notice =this.person.notice*1 + 1;
//            this.person.sh = true;
            fn = true;
          }else{
            this.person.notice =this.person.notice - 1;
//            this.person.sh = false;
            fn = false;
          }

          var self = this;
          let params = {
            api:"/_node_user/_save_user_org.apno",
            data:{
              "orgId":self.borgId,
              saveFlag:fn
            }
          }

          this.post(params,this.phoneViewRet);
        },
        loadMore(){
            var self = this;
            if(this.pageCount > this.pageNumber){
                this.pageNumber ++;
                this.gqlist();
            }
        }
      },
    activated(){

      this.del ={};
      this.dataArr = [];
      this.itemGQ = null;
      this.battlefieldHtml = "";//调果战绩的初始化
      var browserId = this.$route.query.userId;
      var borgId = this.$route.query.orgId;
      this.browserId = browserId;
      this.borgId = borgId;
      this.AgChart = false;

      //获取个人信息
      let params = {
        api:"/yg-user-service/user/findUserInfo.apec",
        data:{
            id:browserId
        }
      }
      this.post(params, fn.init.bind(this));
      this.gqlist(1);


      let paramsT = {
        api:"/_node_user_org/_view_org.apno",
        data:{
          orgId:borgId,
          vieType:"VIEWNUM"
        }
      }

      this.post(paramsT,this.phoneViewRet);

      this.GZ();
      var self = this;
      this.$nextTick(function () {
        self.$refs.personWrapper.init();
      });
    },
    components: {
      'my-scroll':scroll,
      'my-personHot':personHot,//个人的热点
      'my-personInfo':personInfo,//个人简介组件
      'my-banner':Banner,//个人banner图组件
      'my-phone':phoneC,//打电话组件
    }
  }
</script>
