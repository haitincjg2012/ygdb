/**
* Created on 2017/7/29.
* 修改供求 弹窗
*/
<template>
  <div>
      <div>
        <div class="rodal-fade-enter" v-show="show" id="buyWindow" style="animation-duration:300ms;">
          <div class="minerWindow" @click="hideWindow"></div>
          <div class="minerWinContent rodal-zoom-enter" style="animation-duration:300ms;">
            <div class="mint-msgbox-header">
              <div class="mint-msgbox-title">提示</div>
            </div>
            <div class="mint-msgbox-content">
              <div v-if="priceSwitch==='0'" class="mint-msgbox-message">请输入该供求的单价</div>
              <div v-else class="mint-msgbox-message">请输入该求购的价格范围</div>

              <div v-if="priceSwitch==='0'" class="mint-msgbox-input">
                <span style="font-size: 15px;margin-right: 5px">单价</span><input v-model="num" placeholder="请输入单价" type="number" @input="wgt">
                <span style="font-size: 15px;margin-left: 5px">{{unit}}</span>
                <div class="mint-msgbox-errormsg" v-show="errorMsg">您输入的价格不准确哦~，请重新输入</div>
              </div>
              <div v-else class="mint-msgbox-input">
                <span style="font-size: 15px;margin-right: 5px">价格</span><input class="qgip" v-model="startNum" placeholder="请输入起价" type="number" @input="wgtF">
                <span>--</span>
                <input class="qgip" v-model="endNum" placeholder="请输入尾价" type="number" @input="wgtS">
                <span style="font-size: 15px;margin-left: 5px">{{unit}}</span>
                <div class="mint-msgbox-errormsg" v-show="errorMsg">您输入的价格范围不准确哦~（提示：起价和尾价不能为空，起价不能大于尾价）</div>
              </div>
            </div>
            <div class="mint-msgbox-btns">
              <button @click.stop="cancleBtn" style="width: 50%;" class="mint-msgbox-btn msgbox-cancel ">取消</button>
              <button @click.stop="confirmBtn" class="mint-msgbox-btn mint-msgbox-confirm ">确定</button>
            </div>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
  import API from '../../../api/api'
  const api = new API();
  import {MessageBox, Indicator,Toast} from 'mint-ui';
  export default{
    created(){
      const self = this;
      this.$root.eventHub.$on('puw.Event', function (item) {
        self.priceSwitch=item.index;
        self.unit=item.priceUnit;
        self.num='';
        self.startNum='';
        self.endNum='';
        self.errorMsg=false;
        self.show = true;
      });
    },
    mounted(){
    },
    data(){
      return {
        show: false,//显示/隐藏标志
        num:'',
        startNum:'',
        endNum:'',
        priceSwitch:'0',//标识属于供求窗口还是求购窗口
        errorMsg:false,
        unit:''
      }
    },
    mounted(){
    },
    deactivated(){
    },
    computed: {},
    methods: {
      wgt(e){
        var e = e || window.event;
        var target = e.toElement || e.srcElement;
        var value = target.value.toString();
        var len = value.length;
        if(len > 4){
          this.num = value.slice(0, 4);
        }
      },
      wgtF(e){
        var e = e || window.event;
        var target = e.toElement || e.srcElement;
        var value = target.value.toString();
        var len = value.length;
        if(len > 4){
          this.startNum = value.slice(0, 4);
        }
      },
      wgtS(e){
        var e = e || window.event;
        var target = e.toElement || e.srcElement;
        var value = target.value.toString();
        var len = value.length;
        if(len > 4){
          this.endNum = value.slice(0, 4);
        }
      },
      hideWindow(){
        this.show = false;
        this.num='';
        this.startNum='';
        this.endNum='';
        this.priceSwitch='0';//标识属于供求窗口还是求购窗口
        this.errorMsg=false;
      },
      confirmBtn(){
        if (this.priceSwitch==='0' && !this.num)
        {
          this.errorMsg = true;
          return;
        };
        if (this.priceSwitch==='1' && (!this.startNum || !this.endNum))
        {
          this.errorMsg = true;
          return;
        };
        if (this.priceSwitch==='1' && ((this.startNum-0)>=(this.endNum-0)))
        {
          this.errorMsg = true;
          return;
        };
        this.show = false;
        this.$root.eventHub.$emit('puwUpdate.confirm', {'priceSwitch':this.priceSwitch,'num':this.num,'startNum':this.startNum,'endNum':this.endNum});
      },
      cancleBtn(){
        this.show = false;
        this.num='';
        this.startNum='';
        this.endNum='';
        this.priceSwitch='0';//标识属于供求窗口还是求购窗口
        this.errorMsg=false;
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .minerWindow
    z-index: 99;
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    right: 0;
    background-color: #000000;
    width: 100%;
    height: 100%;
    opacity: 0.5;
  .minerWinContent
    text-align: center;
    z-index: 999;
    background-color: #fff;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    border-radius: 10px;
    width: (320/_rem);
    .mint-msgbox-header
      padding: (15/_rem) 0 0
      .mint-msgbox-title
        text-align: center;
        padding-left: 0;
        margin-bottom: 0;
        font-size: (16/_rem);
        font-weight: 700;
        color: #333;
    .mint-msgbox-content
      padding: (10/_rem) (10/_rem) (15/_rem);
      border-bottom: 1px solid #ddd;
      min-height: (36/_rem);
      position: relative;
      .mint-msgbox-message
        color: #333;
        margin: 0;
        text-align: center;
        line-height: (36/_rem);
      .mint-msgbox-input
        padding-top: 0;
        line-height: (40/_rem);
        .mint-msgbox-errormsg
          color: red;
          font-size: (12/_rem);
          min-height: (18/_rem);
          margin-top: 2px;
        input
          border: 1px solid #dedede;
          border-radius: 5px;
          padding: 4px 5px;
          width: 60%;
          -webkit-appearance: none;
          -moz-appearance: none;
          appearance: none;
          outline: none;
        .qgip
          border: 1px solid #dedede;
          border-radius: 5px;
          padding: 4px 5px;
          width: 25%;
          -webkit-appearance: none;
          -moz-appearance: none;
          appearance: none;
          outline: none;
    .mint-msgbox-btns
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
      height: (40/_rem);
      line-height: (40/_rem);
      .mint-msgbox-btn
        line-height: (35/_rem);
        display: block;
        background-color: #fff;
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
        margin: 0;
        border: 0;
      .mint-msgbox-confirm
        color: #26a2ff;
        width: 50%;
      .msgbox-cancel
        border-right: 1px solid #ddd!important;
</style>
