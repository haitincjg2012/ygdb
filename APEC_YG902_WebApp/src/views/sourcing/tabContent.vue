<!--品种 规格 区域-->
<template>
  <div>
    <div class="section-first">
      <!--区域-->
      <div v-if="type == 'area'" class="c-tab-area">
        <div class="c-tab-area-title">
          <div class="c-tab-area-text">区域</div>
        </div>
        <div class="c-tab-area-content c-tab-area-county">
          <my-scroll :data="countyArr|| []" ref="countyWrapper" class="c-tab-county-scroll">
            <div>
              <div class="c-tab-area-text c-tab-area-sty" @click="county('all')">不限</div>
              <div v-for="item in countyArr" class="c-tab-area-text c-tab-area-sty" :class="{'c-tab-county-activate':item.flag}" @click="county(item)">
                {{item.keyWord}}
              </div>
            </div>
          </my-scroll>
        </div>
        <div class="c-tab-area-content c-tab-area-town">
          <my-scroll :data="townArr || []" ref="townWrapper" class="c-tab-county-scroll">
             <div>
               <div class="c-tab-area-text c-tab-area-sty" @click="town('all')">不限</div>
               <div v-for="item in townArr" class="c-tab-area-text c-tab-area-sty" :class="{'c-tab-town-activate':item.flag}" @click="town(item)">
                 {{item.keyWord}}
               </div>
             </div>
          </my-scroll>
        </div>
      </div>
      <!--品种-->
      <div v-else-if="type == 'pz'" class="c-tab-pz" ref="pzFrame">
        <my-scroll :data="pzArr || []" class="c-tab-pz-scroll" ref="pzWrapper">
          <!--<div class="c-tab-pz-ul">-->
          <div>
            <ul class="clearfix c-tab-pz-ul" ref="pzUl">
              <li v-for="(item,index) in (pzArr.length > 0?pzArr:null)" @click="pzChecked(item)" class="c-tab-pz-li" :class="{'c-tab-pz-activate':item.flag}">{{item.keyword}}</li>
            </ul>
          </div>
        </my-scroll>
        <div class="c-tab-btn" ref="pzBtn">
          <div class="c-tab-btn-reset" @click="reset('pz')">重置</div>
          <div class="c-tab-btn-ok" @click="ok('pz')">确定</div>
        </div>
      </div>
      <!--规格-->
      <div v-else-if="type == 'specifications'" class="c-tab-pz" ref="specifyFrame">
        <my-scroll :data="specifyArr || []" class="c-tab-specify-scroll" ref="specifyWrapper">
          <div>
            <ul class="clearfix c-tap-specification" ref="specifyUl">
              <li v-for="(item,index) in  (specifyArr.length > 0?specifyArr:null)" @click="specifyChecked(item)">
                <div class="pitch" :class="{'c-tab-specify-activate':item.flag}">
                  <img src="../../assets/img/hack.png">
                  <span>{{item.keyword}}</span>
                </div>
              </li>
            </ul>
          </div>
        </my-scroll>
        <div class="c-tab-btn" ref="specifyBtn">
          <div class="c-tab-btn-reset" @click="reset('specifications')">重置</div>
          <div class="c-tab-btn-ok" @click="ok('specifications')">确定</div>
        </div>
      </div>
    </div>

    <!--flex切分上下文-->
    <div class="section-second" @click="hidePage"></div>
  </div>
</template>
<style>
  @import "../../assets/css/tabContent.css";
</style>
<script>
  import dataConfig from "../../api/data" //区域 品种 规格
//  import scroll from '../../components/scroll/scrollbg.vue'
  import scroll from '../../components/scroll/scroll.vue'
  export default{
      data(){
          return {
            pzArr:null,//品种的数据
            specifyArr:null,//规格的数据
            cityArr:null,//区域的数据

            countyArr:null,//区县的数据
            townArr:null,//乡镇的数据

            flag:false,//山东、烟台的情况

            province:"山东省",//省份
            countyName:'',//区县的名称
            townName:'',//区县的名称

            pzRecord:[],//记录品种的查询规则
            specifyRecord:[],//记录规格的查询规则
            areaRecord:[],//记录区域的查询规则
          }
      },
      props:{
        type:"",
        city:"",//定位的城市
        showHide:"",//显示tab的内容
      },
    created(){
//          this.$refs.countyWrapper.init();

    },
    methods:{
      getCounty(province, city){
        var self = this;
        var timeId = setInterval(function () {
          if(window.tree){
            clearInterval(timeId);
            self.countyArr = [];

            if(self.flag){
              self.county = window.tree[province][city][self.flag];
            }else{

              for(var key in window.tree[province][city]){
                  var obj = {};
                  obj.keyword = key;
                  obj.flag = false;
                self.countyArr.push(obj);
              }
            }
          }

        }, 100);
      },
      getTown(province, city,county){
          //获取乡镇的名称
        var self = this;
        var timeId = setInterval(function () {
          if(window.tree){
            clearInterval(timeId);

            var dt = window.tree[province][city][county]
            //类型
            var type = Object.prototype.toString.call(dt).slice(8, -1);
            if(type == "Array"){
              self.townArr = [];
               dt.forEach(function (current) {
                 var obj = {};
                 obj.keyWord = current;
                 obj.flag = false;
                 self.townArr.push(obj);
               })
            }else{
              self.townArr = null;
            }
//            self.$nextTick(function () {
//              this.$refs.townWrapper.init();//区县的滑动
//            });
          }

        }, 100);
      },
      county(item){
          //查找区县
        if(item == "all"){
            //查询市下面的所有乡镇
          this.launch("area", this.city);
        }else{
            //市下面的镇、乡数据默认是山东省
          this.getTown(this.province, this.city, item.keyWord);
          this.resetCount(item.keyWord);
        }
      },
      resetCount(keyword){
          //区县的重置
        this.countyName = keyword;
        this.countyArr.forEach(function (current) {
          if(current.keyWord == keyword){
            current.flag = true;
          }else
            current.flag = false;
        })
      },
      resetTown(keyword){
        this.townName = keyword;
        this.townArr.forEach(function (current) {
          if(current.keyWord == keyword){
            current.flag = true;
          }else
            current.flag = false;
        })
      },
      town(item){
        //查找镇
        if(item == 'all'){
          var county = this.city + this.countyName;
          this.launch("area", county);
        }else{
          this.resetTown(item.keyWord);
          var town = this.city + this.countyName + this.townName;
          this.launch("area", town);
        }
        self.townArr = null;
        this.hidePage();
      },
      pzChecked(item){
          //品种的选择
        if(item.flag){
          item.flag= false;
        }else{
          item.flag= true;
        }

      },
      specifyChecked(item){
          //规格的选择
        if(item.flag){
          item.flag= false;
        }else{
          item.flag= true;
        }
      },
      ok(type){
        //品种 规格的确定
        this.reset(type);
        //发送
        this.launch(type);
        this.hidePage();
        //记录重置
        this.pzRecord = [],//记录品种的查询规则
        this.specifyRecord =[],//记录规格的查询规则
        this.areaRecord =[];//记录区域的查询规则
      },
      launch(type,area){
          var obj = {
              type:type,
          };
          switch (type){
            case "pz":
              obj.pz = this.pzRecord;
                break;
            case "specifications":
              obj.specifications = this.specifyRecord;
              break;
            case "area":
              obj.area = area;
              break;
          }
        this.$emit("receive",obj);
      },
      reset(type){
        //品种 规格的重置
        var self = this;
        switch (type){
          case "pz":
            //重组数据
            for(var i = 0, len = this.pzArr.length; i < len ; i ++){
                if(this.pzArr[i]["flag"]){
                   this.pzRecord.push(self.pzArr[i].keyword);
                }
              this.pzArr[i]["flag"]= false;
            }
            break;
          case "specifications":
            //重组数据
            for(var i = 0, len = this.specifyArr.length; i < len ; i ++){
              if(this.specifyArr[i]["flag"]){
                this.specifyRecord.push(self.specifyArr[i].keyword);
              }
              this.specifyArr[i]["flag"]= false;
            }
            break;
        }
      },
      hidePage(){
          //隐藏tab切换页的内容
        this.$emit('update:showHide', false)
      },
      setHeight(pzFrame, elScroll, elUl, elBtn){
          //重新布局
        var styleFrame = getComputedStyle(pzFrame, null);
        var styleUl = getComputedStyle(elUl, null);
        var styleBtn = getComputedStyle(elBtn, null);

        var fMaxHeight = parseFloat(styleFrame.maxHeight);
        var ulHeight = parseFloat(elUl.offsetHeight);
        var btnHeight = parseFloat(styleBtn.height);
        var scrollHeight =fMaxHeight -btnHeight;
        if(fMaxHeight -btnHeight -ulHeight > 0){
          pzFrame.style.height = (btnHeight + ulHeight) + "px";
        }else{
          pzFrame.style.height = fMaxHeight+ "px";
          elScroll.$el.style.height = scrollHeight+ "px";
          elScroll.refresh();
        }
      },
    },
    watch:{
      type(newP, oldP){
          this.townArr = null;
          switch (newP){
            case "pz":
                //重组数据
              var dt = dataConfig["1001"];
              this.pzArr = [];
              for(var i = 0, len = dt.length; i < len ; i ++){
                  var obj = {};
                 obj.keyword= dt[i].keyword;
                 obj.flag= false;
                this.pzArr.push(obj);
              }

              this.$nextTick(function () {
                var pzFrame = this.$refs.pzFrame;
                var elScroll = this.$refs.pzWrapper;
                var elUl = this.$refs.pzUl;
                var elBtn = this.$refs.pzBtn;
                this.setHeight(pzFrame, elScroll, elUl, elBtn);
              });
                break;
            case "specifications":
              //重组数据
               var dt = dataConfig["1002"];
              this.specifyArr = [];
              for(var i = 0, len = dt.length; i < len ; i ++){
                var obj = {};
                obj.keyword= dt[i].keyword;
                obj.flag= false;
                this.specifyArr.push(obj);
              }

              this.$nextTick(function () {
                var specifyFrame = this.$refs.specifyFrame;
                var elScroll = this.$refs.specifyWrapper;
                var elUl = this.$refs.specifyUl;
                var elBtn = this.$refs.specifyBtn;
                this.setHeight(specifyFrame, elScroll, elUl, elBtn);
              });
                break;
            case "area":
                //区县的项目

                var self = this;
                var dt = dataConfig["1003"];
                dt.forEach(function (current) {
                  for(var key in current){
                    if(current.keyWord == self.city){
                      var value = current.value;
                      self.countyArr = [];

                      current[value].forEach(function (item) {
                        var obj = {};
                        obj.keyWord = item.keyWord;
                        obj.flag = false;
                        self.countyArr.push(obj);
                      });
                    }
//                    self.$nextTick(function () {
//                      this.$refs.countyWrapper.init();//区县的滑动
//                    });
                    return;
                  }
                });

//                this.getCounty(this.province,this.city);


                break;
          }

      },
      city(newV, oldV){
        var self = this;
        var dt = dataConfig["1003"];
        dt.forEach(function (current) {
          for(var key in current){
            if(current.keyWord == newV){
              var value = current.value;
              self.countyArr = [];

              current[value].forEach(function (item) {
                var obj = {};
                obj.keyWord = item.keyWord;
                obj.flag = false;
                self.countyArr.push(obj);
              });
            }
            return;
          }
        });
      }

    },
    components:{
          "my-scroll":scroll
    }
  }
</script>
