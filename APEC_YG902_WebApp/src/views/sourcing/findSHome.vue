<template>
  <div class="c-findSouce">
    <div class="c-findSource-header">
       <div class="c-fdS-position" @click="localtion">
         {{city}}
       </div>
       <div class="c-fdS-p-iconFrame"><img src="../../assets/img/city.png" class="c-fdS-p-icon"></div>
       <div class="c-fdS-Headlines">
            <h1>货源广场</h1>
       </div>
    </div>
    <!--tab切换-->
    <my-tab class="c-fdS-tab" @receive="checkedTab"></my-tab>
    <!--tab切换的内容-->
    <my-tabContent class="c-fdS-goods" :type="type" :city="city" :class="{'c-fdS-goods-index':tabContentFlag}"  :showHide.sync='tabContentFlag' @receive="listParams"></my-tabContent>
    <!--物品的展示-->
    <my-scroll class="c-fdS-scroll" :data="goodsArr|| []" :pullup="pullup" @scrollToEnd="loadMore" ref="listWrapper">
      <div>
        <div v-for="item in goodsArr"
             :is="item.ss"
             :item = "item"
             @receive="process"
            >
        </div>
      </div>
    </my-scroll>

    <!--<div class="c-fS-img">-->
        <!--<img src="../../assets/img/ing.png"/>-->
    <!--</div>-->
  </div>
</template>
<style>
  @import "../../assets/css/findSHome.css";
</style>
<script>
  import scroll from "../../components/scroll/scrollbg.vue"
  import tab from "./tab.vue"
  import tabContent from './tabContent.vue'
  import goods from "./goodlist.vue"

  import API from '../../api/api'
  const api = new API();

  var fn = {
      weightStard:{
          0:"小于100",
          100:"大于100",
          500:"大于500",
          1000:"大于1000",
          2000:"大于2000",
      },
      processData(type,data){
          var dt = data.data;
          if(data.succeed){
              var self = this;
              if(self.type != type){
                 return ;
              }

              var rows = dt.rows;
              self.pageCount = rows.pageCount;
              rows.forEach(function (current) {
                current.skuHtml = "<div class='c-goods-sku'>"+ current.skuName + '</div><div class="c-goods-space"></div>'
                    +'<div class="c-gmc-goods-source"><span>果满仓</span></div><div class="c-gmc-goods-pz"><span>果农货</span></div>';
                var weightG;
                for(var key in fn.weightStard){
                  if(key < current.weight){
                    weightG = fn.weightStard[key];
                  }
                }

                current.weightHtml = '<span class="c-goods-w-num">'+ weightG + '</span><span class="c-goods-w-unit">'+ current.weightUnit+'</span>';
                if(current.amount > 0){
                  current.priceText = '参考价:'+ current.amount + "元/" + current.priceUnit;
                }else{
                  current.priceText = '参考价: 暂无价格';
                }

                current.coldName = current.orgName;
                current.ss = goods;
              });

             self.goodsArr = (self.goodsArr || []).concat(rows);
          }
      }
  }
  export default{
      data(){
          return {
            city:"烟台市",//定位
            type:"",//品种 规格 区域
            goodsItem:null,//tab切换的具体内容
//            goodsItem:[],//tab切换的具体内容
            goodsArr:null,//物品数组
//            goodsArr:[{ss:goods}],//物品数组

            tabContentFlag:false,//tab切换的内容是否展示

            //翻页
            pullup:true,
            pageNumber:1,
            pageCount:1000,
            pageSize:10,
            userId:"",//用户ID
            useOrgId:"",//用户组织ID
            address:"",//地址/区域
            category:"",//品种
            spec:"",//规格
            sortType:"ASC",//排序

            sortMark:false,//默认标志位是升序

            recordText:"znw",//记录要查询的条件,默认条件是znw
          }
      },
      methods:{
          localtion(){
              //定位功能
            var self = this;
            this.tabContentFlag = false;
            this.$router.push({name:"location",query:{city:self.city}})
          },
          checkedTab(data){
              //选项卡的选择
            this.type = data;
            //ranking除外
            if(data == "ranking"){
              this.tabContentFlag = false;
              //升序查询
              this.resetPagination();
              if(this.sortMark){
                  this.sortMark = false;
                  this.sortType = "DESC";

                  this.recordText = "DESC";
              }else{
                 this.sortMark = true;
                 this.sortType = "ASC";

                this.recordText = "ASC";
              }
              this.goodsArr = null;
              this.list();
            }else{
              this.tabContentFlag = true;
            }

          },
          process(){
              //详情的调整
          },
          list(){
              var self = this;
              var type = this.type;
              var params = {
                  api:"/yg-goodssource-service/goodssource/listByPage.apec",
                  data:{
                    userId:self.userId,
                    useOrgId:self.useOrgId,
                    address:self.address,
                    category:self.category,
                    spec:self.spec,
                    sortType:self.sortType,
                    //必须填写的参数
                    pageNumber:self.pageNumber,
                    pageSize:self.pageSize
                  }
              }
              this.post(params, fn.processData.bind(this,type));
          },
          loadMore(){
              //加载内容
              if(this.pageNumber < this.pageCount){
                  this.pageNumber ++;
                  this.list();
              }
          },
          listParams(data){
              //接受tab切换内容过来的参数 区域 品种 规格（排序的以后扩展中）
              //type 区域 品种 规格
              //data 参数内容
              this.resetPagination();
              this.clearList();
              switch (data.type){
                case "pz":
                    this.category = data.pz.toString();//品种查询
                    break;
                case "specifications":
                  this.spec = data.specifications.toString();//规格查询
                  break;
                case "area":
                  this.address = data.area;//区域查询
                  break;
              }
              this.goodsArr = null;
              this.list();
          },
          clearList(){
              //清空列表内容
              this.goodsItem = null;
          },
          resetPagination(){
             //重置分页的参数
              this.userId ="",
              this.useOrgId ="",
              this.address ="",
              this.category ="",
              this.spec ="",
              this.sortType ="",
              //必须填写的参数
              this.pageNumber =1,
              this.pageSize =1000;
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
      },
      activated(){
          this.$refs.listWrapper.init();
          var self = this;
          if(this.$store.state.cityName){
              this.city = this.$store.state.cityName
          }

          this.list();
      },
      components:{
        "my-tab":tab,
        "my-tabContent":tabContent,
        "my-scroll":scroll,
      }

  }
</script>
