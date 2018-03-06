<template>
  <div ref="horse">
      <div class="rotate" ref="rotateA">
        <div v-for="item in arr" @click="jumpNews(item)" class="c-r-com">
          <div class="c-r-com-head">
            <span class="c-r-com-head-sp">推荐</span>
          </div>
          <div class="c-r-com-content">
            <p class="c-r-text">{{item.title}}</p>
          </div>
        </div>
      </div>
      <div class="rotate" ref="rotateF">
        <div v-for="item in arr" @click="jumpNews(item)" class="c-r-com">
          <div class="c-r-com-head">
            <span class="c-r-com-head-sp">推荐</span>
          </div>
          <div class="c-r-com-content">
            <p class="c-r-text">{{item.title}}</p>
          </div>
        </div>
      </div>
  </div>
</template>
<style>
@import "../../../assets/css/newsRotate.css";
</style>
<script>
  import API from '../../../api/api'
  const api = new API();
  import {Toast} from 'mint-ui'

  export default{
      data(){
          return {
//            arr:['【重磅新闻】烟威地区8月19日苹果价格行情 的汇总','【重磅新闻】hello world '],
            arr:null,
            h:0,
            speed:100,//滚动速度
            timeId:0,//定时器的id
            pageNum:1,//新闻页码
            pageSize:3,//新闻页容量

            horse:null,
            rotateA:null,
            rotateAH:null,
            childH:null,

          }
      },
      methods:{
        jumpNews(data){
          var vm=this;
          vm.$router.push({name:"newsDetail",query:{id:data.id}});

        },
        init(){
            var el = this.$refs.rotate;
            this.h = el.offsetHeight;
        },
        step(){
            var that = this;
            var recordNum = 0,timeId;
          test();
          function test() {
            setTimeout(function () {
              if(recordNum >= that.childH){
                recordNum = 0;
                that.pause();
              }else{
                var  scrollTop = that.horse.scrollTop;
                if (scrollTop >= that.rotateAH) {
                  that.horse.scrollTop = 0;
                } else {
                  that.horse.scrollTop = scrollTop + 2;
                }
                recordNum  = recordNum + 2;
                test();
              }
            }, 100)
          }

        },
        pause(){
          var that = this;
            setTimeout(function () {
              that.step();
            }, 2000)
        },
        newslist(){
          var vm=this;

          let params={
            api:"/yg-society-service/societyPost/societyPostPage.apec",
            data:{
              realm:"ARTICLE",
              auditState:'Y',
              //分页信息
              pageNumber:vm.pageNum,//页码
              pageSize:vm.pageSize//页容量
            }
          };
          vm.post(params,vm.newslistCb,true,'newsTip');
        },
        newslistCb(data){
          var vm=this;

          if(data.succeed){
              var rows = data.data.rows;
              var arrT = null;
              if(rows.length > 0){
                arrT = [].concat(rows);
              }
              vm.arr = arrT;
          }
          else{
            Toast(data.errorMsg);
          }
        },
        post(params, fn, flag, scrollTip){
          var vm=this;
          try {
            api.post(params).then((res) => {
              var data = res.data;
              fn(data);
            }).catch((error) => {
              console.log(error)
              if(flag){
                vm.newsShowFlag=true;
                Toast(error);
                // vm.$refs.newsTip.end("没有更多数据了");
                vm.$refs[scrollTip].end("网络异常了...");
              }
            })
          } catch (error) {
            console.log(error)
            if(flag){
              vm.newsShowFlag=true;
              Toast(error);
              vm.$refs[scrollTip].end("网络异常了...");
            }

          }
        },

      },
      activated(){
        var self = this;
        clearInterval(self.timeId);
        self.newslist();

//        self.timeId = setInterval(self.step, 2000);

      },
      mounted(){
        var self = this;
        self.horse = self.$refs.horse,
        setTimeout(function () {
          self.rotateA = self.$refs.rotateA,
          self.rotateAH = parseFloat(getComputedStyle(self.rotateA, null).height),
          self.childH = parseFloat(getComputedStyle(self.rotateA.children[0], null).height);
          self.step();

          }, 500)
        self.horse.addEventListener("touchstart",function () {

        });
        self.horse.addEventListener("touchend",function () {
        });
      }
  }
</script>
