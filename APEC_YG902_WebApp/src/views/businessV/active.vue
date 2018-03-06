<template>
  <div class="c-xmove"  v-show="homeActiveF">
    <div class="c-activeP">
      <div v-for="item in arr" class="c-activePerson" :data-id="item.userId" :data-orgid = "item.userOrgId">
        <div class="c-active-title" :data-id="item.userId" :data-orgid = "item.userOrgId">
          <img :src="item.symbolPic" class="c-active-title-img" :data-id="item.userId" :data-orgid = "item.userOrgId">
        </div>
        <div class="c-active-content" :data-id="item.userId" :data-orgid = "item.userOrgId">
          <div class="c-active-c-head" :data-id="item.userId" :data-orgid = "item.userOrgId">
            <img :src="item.imageUrl" class="c-active-c-h-pic" :data-id="item.userId" :data-orgid = "item.userOrgId"/>
          </div>
          <div class="c-active-c-Introduction" :data-id="item.userId" :data-orgid = "item.userOrgId">
            <div class="c-active-c-I-p" :data-id="item.userId" :data-orgid = "item.userOrgId">
              <div class="c-active-c-I-p-styleR" :class="{twoText:item.userType.length == 2}" :data-id="item.userId" :data-orgid = "item.userOrgId">
                <span class="c-active-c-I-p-identify" :data-id="item.userId" :data-orgid = "item.userOrgId">{{item.userType}}</span>
              </div>
              <div class="c-active-c-I-p-styleL" :class="{twoR:item.userType.length == 2}" :data-id="item.userId" :data-orgid = "item.userOrgId">
                <span class="c-active-c-I-p-name" :data-id="item.userId" :data-orgid = "item.userOrgId">{{item.name}}</span>
              </div>
            </div>
            <div class="c-active-c-signup" :data-id="item.userId" :data-orgid = "item.userOrgId">{{item.text}}</div>
          </div>
        </div>
      </div>
    </div>
    <slot name="blank"></slot>
  </div>
</template>
<style>
  @import "../../assets/css/active.css";
</style>
<script>
  import DBKS from '../../assets/img/DBKS.png'
  import active from '../../assets/img/active.png'//活跃达人
  import tune from '../../assets/img/tune.png' //调果达人
  import API from '../../api/api'
  const api = new API();
  export default{
      data(){
          return{
            arr:null,
            fingerSPos:0,//手指初始的位置
            fingerNextP:0,//临时的位置记录
            moveEl:null,//移动的元素
            boundary:0,//边界值
            movedis:0,//默认为0，从左到右
            timeStart:0,//滑动开始的时间
            timeEnd:0,//滑动结束的时间
            timeStartV:0,
            isBackFlag:false,//判断是否跳转到个人详情
            stopInertiaMove:false,//
            af:false,//活跃达人
            tf:false,//调果达人
            homeActiveF:false,

            currentY:0,//Y轴的当前位置
            preY:0,//Y轴的上一个位置

          }
      },
    methods:{
      bs(property){
        var _elementstyle = document.createElement("div").style;
        var vendors = ["","webkit","ms","O","Moz"],
          transform,
          i = 0,
          l = vendors.length;

        for(; i < l; i ++){
          transform = vendors[i] + property;
          if(transform in _elementstyle){
            return vendors[i];
          }
        }

        return false;
      },
      translate(el, x,y) {
        var property = this.bs("Transform");
        if(property == false){
          alert("浏览器不存在此属性");
        }else{
          var adaptation = property+"Transform";
          el.style[adaptation] ='translate3d('+x +'px,'+ y +'px, 0)';
        }
      },
      personXq(id, orgId){
        if(!id)
          return;
//        var id = item.userId;
//        var orgId = item.userOrgId;
        this.$router.push({name:"personXq",query:{userId:id,orgId:orgId}});
      },
      setWidth(){
        var self = this;
        var imgArr = this.arr;


        var parent = this.moveEl,
            children = parent.children;

        var timeId = setInterval(function () {
           if(children.length > 0){
             clearInterval(timeId);
             var len = imgArr.length;
             var rect = children[0].getBoundingClientRect(),
               left = rect.left,
               top = rect.top,
               width = Math.abs(rect.right - rect.left);
               var wrapWidth = width * len + left * (len + 1);
               parent.style.width = wrapWidth + "px";

             self.boundary =wrapWidth - parent.parentElement.offsetWidth;

             for (var key = 0; key < len; key++) {
               children[key].style.left = key * width + (key + 1) * left + "px";
             }

             if(self.boundary > 0){
               var el = document.querySelector(".c-xmove");
               el.addEventListener("touchstart",self.start,false);
               el.addEventListener("touchmove",self.move,false);
               el.addEventListener("touchend",self.end,false);
             }
           }
        }, 1000 / 60)
      },
      start(e){
        var e = window.event || e;
        var touch = e.targetTouches[0];
        this.timeStart = e.timeStamp;
        this.timeStartV = new Date().getTime();

        this.fingerNextP = this.fingerSPos = touch.pageX;
        this.currentY = this.preY = touch.pageY;
        this.isBackFlag = true;//点击的时候都是跳转
        this.stopInertiaMove = true;
//        e.preventDefault();
      },
      move(e){
          var self = this,
             el = this.moveEl,
             parent = el.parentElement,
             pWidth = parent.style.width.split("px")[0],
             width = el.style.width.split("px")[0];
        if(pWidth > width){
            return;
        }
          var e = window.event || e;
          var touch = e.targetTouches[0];
          var target = e.target;
          var pos = touch.pageX;
           this.currentY = touch.pageY;
          var delay = pos - this.fingerNextP;
//          console.log(pos);
          //通过滑动的距离来判断两边的边界条件来判断
          var dir = self.direction("h",delay);
          this.directionXY(this.moveEl,dir,delay);
        //时间差小于300

        this.fingerNextP = pos;
        this.isBackFlag = false;//判断是否跳转
        if(this.currentY == this.preY){
          e.preventDefault();
        }else{
          this.preY = this.currentY;
        }

      },
      end(e){
        var e = window.event || e;

         if(this.isBackFlag){
             var id = e.target.dataset.id;
             var orgid = e.target.dataset.orgid;

             this.personXq(id, orgid);
            return;
         }
         var self = this;

        var el = e.srcElement || e.toElement;

        var touch = e.changedTouches[0];
        var pos = touch.pageX;
        var startTime = this.timeStart;
        var startTimeV = this.timeStartV;
        var timeEnd  = this.timeEnd = e.timeStamp,
            timeInterval = timeEnd - startTime;

        if(timeInterval < 300){
            var el = document.querySelector(".c-activeP");

            self.stopInertiaMove = false;
            var v = (pos - this.fingerSPos)/timeInterval;

            var dir = v > 0 ? -1: 1;//加速度的方向
            var el = this.moveEl;
            var deceleration = dir*0.0006;//减速// [ˌdi:selə'reɪʃn]
           inertiaMove();
        }else{
          e.stopPropagation();
        }

        function inertiaMove() {

          if(self.stopInertiaMove){
            return;
          }



          var nowTime = new Date().getTime();
          var t = nowTime - startTimeV;

          var nowV = v + t*deceleration;

          if((-dir) * nowV < 0){
            return;
          }
          v = nowV;
          var moveX = (v + nowV)*t/2;//移动的距离

          if(dir == -1){
            self.directionXY(el,"R",moveX);
          }else{
            self.directionXY(el,"L", moveX);
          }
          startTimeV = nowTime;
          setTimeout(inertiaMove, 10);
        }
      },
      direction(dir, param){
          //x轴方向
        switch (dir){
          case "h":
              if(param > 0){
                  return "R";//向右
              }else{
                return "L";//向左
              }
              break;
          case "v":
              break;
        }
      },
      directionXY(el,dir,delay){
          //XY的轴
        var self = this;

        switch (dir){
          case "R":
            if(Math.abs(delay) > Math.abs(self.movedis)){
              self.stopInertiaMove = true;
              self.movedis = 0
              this.translate(el, 0, 0);
            }else{
              var x = delay + self.movedis;
              self.movedis = x;
              this.translate(el, x, 0);
            }
            break;
          case "L":
            if(Math.abs(self.boundary) > Math.abs(self.movedis)){
                var x = delay + self.movedis;
              self.movedis = x;
              this.translate(el, x, 0);
            }else{
              self.stopInertiaMove = true;
              var x = - self.boundary;
              self.movedis = x;
              this.translate(el, x, 0);
            }
            break;
          //向上向下
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
      //调果达人
      getFruit(){

        var self = this;
        var params = {
//          api:"yg-voucher-service/voucher/maxVoucherOfUser.apec",
          api:"yg-voucher-service/ranking/queryRankingMan.apec",
          data:{
            "rankingType":"FRUITING_PEOPLE"
          }
        }

        this.post(params, function (data) {

            if(data.succeed){
              var rows = data.data;
              if(rows){
                rows.forEach(function (current) {
                  current.symbol = 2;
                  current.symbolPic = tune;
                  current.imageUrl = current.imageUrl == ""?DBKS:current.imageUrl + "?x-oss-process=style/_head";
                  current.text = "累计调果" + current.sumWeight +"斤";
                });

                if(rows.length > 0){
                  self.arr = self.arr?self.arr.concat(rows):[].concat(rows);
                }
                self.tf = true;
              }
            }

          if(self.af || self.tf){
            self.homeActiveF = true;
            self.setWidth();
          }


        });
      },
      //供求达人
      getSupply(){
          var self = this;
        var params = {
//          api:"yg-product-service/product/maxProduct.apec",
          api:"yg-voucher-service/ranking/queryRankingMan.apec",
          data:{
            "rankingType":"ACTIVE_MAN"
          }
        }

        this.post(params, function (data) {
          if(data.succeed){
             var rows = data.data;
             if(rows){
               rows.forEach(function (current) {
                 current.symbol = 1;
                 current.symbolPic = active;
                 current.imageUrl = current.imageUrl == ""?DBKS:current.imageUrl + "?x-oss-process=style/_head";
                 current.text = "累计发布供求" + current.time +"条";
               });

               if(rows.length > 0){
                 self.arr = self.arr?self.arr.concat(rows):[].concat(rows);
               }

               self.af = true;
             }

          }

          if(self.af || self.tf){
            self.homeActiveF = true;
            self.setWidth();
          }


        });
      }
    },
    mounted(){
      var self = this;
      self.homeActiveF = false;
      self.moveEl = document.querySelector(".c-activeP");
      self.getFruit();
      self.getSupply();
      self.tf = false;
      self.af = false;

    },
  }
</script>
