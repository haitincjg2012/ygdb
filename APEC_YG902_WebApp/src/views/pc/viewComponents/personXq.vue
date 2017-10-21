<template>
    <div class="z-p-home c-p-xq">
      <my-scroll class="scrollWrapper" :data="itemGQ" :pullup="pullup" @scrollToEnd="loadMore">
        <div>
      <div class="z-p-banner">
        <div class="z-header-person-h">
          <img src="../../../assets/img/ret.png" @click="back">
        </div>
         <div class="z-banner-img">
            <img :src="person.bannerImgUrl">
           <!--<img src="../../../assets/img/xqimg1.png">-->
         </div>
          <!--<div class="z-canmer">-->
            <!--<input type="file" class="z-imgInput" @change="modify">-->
          <!--</div>-->
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
        <!--<div class="z-p-follow">-->
          <!--<div class="z-p-f" :class="{active:person.sh}" @click="notice">-->
            <!--{{person.sh?"已关注":"关注"}}-->
          <!--</div>-->
        <!--</div>-->
      </div>
      <div class="z-space"></div>

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
        <div class="z-p-pz" v-if="!role.coldF">
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
        <div class="z-p-sale"  v-if="role.traderF && !role.agencyF">
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
      </div>
      <div class="z-transship-ret" v-if="role.agencyF || role.traderF">
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
      <div class="z-num-main" :class="{show:!role.agencyF}">
         <h4 class="z-num-main-t">调果数据</h4>
        <div class="z-space"></div>
        <div id="mainS" class="ZPmain" :class="{ZPShow:AgChart}" ></div>
        <div class="c-z-ondata" v-if="AgChart">
          <img src="../../../assets/img/noData.png" class="c-z-nodata-img">
          <p class="c-z-nodata-text">暂无数据</p>
        </div>
        <!--<div class="z-p-main"></div>-->
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
      </div>
        </div>
      </my-scroll>
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
  import scroll from '@/components/scroll/scroll'
//  import P from "../../../assets/img/p.png"
  import defaultIcon from "../../../assets/img/defaultForm.png"

  import MYGQList from "../../home/viewComponents/mygqlist.vue"
  const api = new API();
  var ec = require("../../../assets/js/echarts.min");

  var fn = {
      img:[],
      bannerImg:function () {
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
          }else{
            this.person.portrait = dt.imgUrl + "?x-oss-process=style/_head";

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
          this.person.real = dt.userRealAuth =="UNREALAUTH"?false:true;
          if(dt.userOrgClientVO){
            this.person.lk =dt.userOrgClientVO.orgName ;//冷库名称
            this.person.kr =dt.userOrgClientVO.orgStockCap ;//冷库的库容量
            this.person.hzs =dt.userOrgClientVO.orgName ;//合作社的名称
            this.person.address =dt.userOrgClientVO.address ;//所在区域
            this.person.pz = dt.userOrgClientVO.mainOperating;//主营品种
            this.person.xsq = dt.userOrgClientVO.saleAddress;//销售区域和客户市场
            this.person.des = dt.userOrgClientVO.remark;//实力描述
          }


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
      var rows = data.data.rows;
      var arr = [];
      var that = this;

      if(this.pageNumber >= data.data.pageCount){
        this.loadFlag = false;

      }
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
        obj.local = current.address;
        obj.name = current.showUserName;
        obj.Flag = that.name;
        obj.userId = current.userId;//userId和orgId是相同的
        obj.orgId = current.userId;
        obj.priceUnit = current.priceUnit;
        obj.agency = current.userTypeName;
        obj.number = current.viewNum;
        obj.star = current.saveNum;
        obj.starFlag = current.saveFlag;
        var wU = current.weightUnit;
        var t = "";
        if(wU){
          t = wU;
        }
        obj.weight = current.weight +" " + t;
        len += obj.weight.length;
        obj.path = index;

        var goodTime = current.showSecondInfo;
        var text = goodTime.join(" ");

        obj.fruitdate = text;

        var Identification = current.productTypeName;
        obj.gq = current.productTypeName;
        //单一的排序
        //只有求购和供应
        if(Identification == "求购"){
          obj.bg = true;
          obj.indentification = 0;
          obj.endAmount = current.endAmount.toString();
          len += obj.endAmount.length;
          obj.startAmount = current.startAmount.toString();
          len += obj.startAmount.length;
        }else{
          obj.bg = false;
          obj.indentification = 1;
          obj.amount = current.amount.toString();
          len += obj.amount.length;
        }
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
      that.itemGQ = arr;
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
              portrait:null
            },
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

//              console.log(data);
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

          var dt = data.data;
          var weight = dt.weight;
          this.battlefield.number = dt.totalNumber + weight;
          this.battlefield.rank = dt.rankNo;
          if(dt.attrNumberMap){
              this.$nextTick(function () {
                fn.initChat(dt.attrNumberMap, weight);
              })

          }else{
              console.log(dt);
            this.AgChart = true;
//            fn.initChat(dt.attrNumberMap, weight);
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
        phoneViewRet(){
        },
        notice(){
          var fn;
          if(!this.person.sh){
            Toast('关注');
            this.person.notice =this.person.notice*1 + 1;
            this.person.sh = true;
            fn = true;
          }else{
            Toast('取消关注');
            this.person.notice =this.person.notice - 1;
            this.person.sh = false;
            fn = false;
          }

          var self = this;
          let params = {
            api:"/_node_user/_save_user_org.apno",
            data:{
              "orgId":self.orgId,
              saveFlag:fn
            }
          }

          this.post(params,this.phoneViewRet);
        },
        loadMore(){
            var self = this;
//          console.log(self.loadFlag);
            if(self.loadFlag){

                this.pageNumber ++;
                this.gqlist();
            }
        }
      },
    activated(){
      this.del ={};
      var browserId = this.$route.query.userId;
      var borgId = this.$route.query.orgId;
      this.browserId = browserId;
      this.borgId = borgId;
      this.AgChart = false;
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


    },
    components: {
      'my-scroll':scroll
    }
  }
</script>
