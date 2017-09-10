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
              <div class="mint-msgbox-message">请输入修改数量</div>

              <div class="mint-msgbox-input">
                <span style="font-size: 15px;margin-right: 5px">数量</span><input v-model="weight" placeholder="请输入数量" type="number" @input="wgt">
                <span style="font-size: 15px;margin-left: 5px">{{unit}}</span>
                <div class="mint-msgbox-errormsg" style="visibility: hidden;"></div>
              </div>
            </div>
            <div class="mint-msgbox-btns">
              <button @click.stop="cancleBtn" style="border-right: 1px solid #ddd;width: 50%;" class="mint-msgbox-btn mint-msgbox-cancel ">取消</button>
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
      this.$root.eventHub.$on('wuw.Event', function (item) {
        self.show = true;
        self.unit = item.weightUnit;
        self.weight = ''
      });
    },
    mounted(){
    },
    data(){
      return {
        show: false,//显示/隐藏标志
        weight:'',
        unit:''
      }
    },
    mounted(){
    },
    computed: {},
    methods: {
      wgt(e){
          var e = e || window.event;
          var target = e.toElement || e.srcElement;
          var value = target.value.toString();
          var len = value.length;
          if(len > 4){
              this.weight = value.slice(0, 4);
          }
      },
      hideWindow(){
        this.show = false;
      },
      confirmBtn(){

        if (!this.weight)
        {
          Toast('请输入正确的数量');
          return;
        }
        this.show = false;
        this.$root.eventHub.$emit('wuwUpdate.confirm', this.weight);
      },
      cancleBtn(){
        this.show = false;
      }
    }
  }
</script>
