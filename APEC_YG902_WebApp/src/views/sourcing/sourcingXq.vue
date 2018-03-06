<template>
  <div class="c-sourcing-xq">
    <my-header class="c-sXq-header">
       <h1 slot="pack" class="c-sourcing-header">货源详情</h1>
    </my-header>
    <div class="c-pieces-one">
      <one-pieces :warehouse="person.org.orgName" :author="person.name" :address="person.org.address"></one-pieces>
    </div>
    <div class="c-sxq-blank"></div>
    <h3 class="c-Secondary-title">存货信息</h3>
    <my-scroll :data="goodsArr || []" :pullup="pullup" @scrollToEnd="loadMore" ref="gmcWrapper" class="c-pieces-two">
      <div>
        <div  :is = "item.ss"
               v-for="item in goodsArr"
              :item = "item"
        ></div>
      </div>
    </my-scroll>
    <my-phone mark="2" class="c-sXq-phone" @phone="phoneClick" @attention="attention" :attentionFlag="testFlag" :mobile="person.mobile"></my-phone>
  </div>
</template>
<style>
  @import "../../assets/css/sourcingXq.css";
</style>
<script>
  import header from "../../components/header/headerTwo.vue" //头部组件
  import onePiece from './sXqHeader.vue'//仓库的介绍
  import goods from './sXqContentOne.vue'//仓库的物品介绍
  import scroll from '../../components/scroll/scrollbg.vue'//滚动组件
  import phone from '../businessV/phone.vue' //关注组件

  import API from '../../api/api'
  const api = new API();

  var enumData = {
      0:"100 桶以下",
      100:"100-500 桶",
      500:"500-1000 桶",
      1000:"1000-2000桶",
      2000:"≥2000桶",
  }
  export default{
      data(){
        return {
            goodsArr:null,
//            goodsArr:[{ss:goods}],

          person:{
              name:"",
              mobile:"",
              org:{
                orgName:"",
                address:""
              },
          },//个人的所有属性

          pullup:true,
          pageNumber:1,
          pageCount:1000,
          pageSize:10,

          dataId:"",//详情id
          orgId:"",//组织id

          testFlag:false,//测试用法
        }
      },
      mounted(){
          this.$refs.gmcWrapper.init();
      },
      methods:{
          phoneClick(){
              var self = this;
            let params = {
              api:"/_node_product/_view_product.apno",
              data:{
                elasticId:self.dataId,
                vieType:"PHONENUM"
              }
            }
            var that = this;
            this.post(params, fn.phone.bind(that));
          },
          attention(flag){
            var self = this;
            let params = {
              api:"/_node_user/_save_user_org.apno",
              data:{
                "orgId":self.orgId,
                saveFlag:flag
              }
            }

            this.post(params,function (data) {
            });
          },
        loadMore(){
          //加载内容
          if(this.pageNumber < this.pageCount){
            this.pageNumber ++;
            this.list();
          }
        },
        list(){
          var self = this;
//          var type = this.type;
          var params = {
            api:"/yg-goodssource-service/goodssource/listByPage.apec",
            data:{
              userId:"",
              useOrgId:self.orgId,
              address:"",
              category:"",
              spec:"",
              sortType:"",
              //必须填写的参数
              pageNumber:self.pageNumber,
              pageSize:self.pageSize
            }
          }
          this.post(params, function (data) {
            if(data.succeed){
              var dt = data.data;
              self.pageCount = dt.pageCount;
              var rows = dt.rows;
              rows.forEach(function (current) {
                  current.ss = goods;
                  for(var key in enumData){

                      if(current.weight >= key){
                         current.number = enumData[key];
                      }
                  }
              });

              self.goodsArr = rows;
            }
          });
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
        getInfo(id){
            //获取该冷库的信息
          var that= this ;
          let params = {
            api:"/yg-user-service/user/findUserInfo.apec",
            data:{
              userOrgId:id
            }
          };
          this.post(params, function (data) {
              if(data.succeed){
                var dt = data.data;
                that.person.name= dt.name;//名字
                that.person.mobile= dt.mobile;//电话
                that.person.org.orgName= dt.userOrgClientVO.orgStockCap;//组织的名字
                that.person.org.address= dt.userOrgClientVO.address + dt.userOrgClientVO.addressDetail;//组织的地址
              }
          });
        }
      },
      activated(){
        this.dataId = this.$route.query.id;
        this.orgId = this.$route.query.orgId;
        this.getInfo(this.orgId);
        this.list();
//        this.$refs.gmcWrapper.init();
      },
      components:{
          "my-header":header,
          "one-pieces":onePiece,
          "my-scroll":scroll,
          "my-phone":phone,

//          "two-pieces":twoPiece,
      }
  }
</script>
