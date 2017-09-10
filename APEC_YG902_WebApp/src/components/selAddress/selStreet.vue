<template>
  <div class="ProvCityBoxWarp">
    <transition name="fade" mode="out-in">
      <div class="ProvCityBoxBg" v-show="show" @click="show = false" @touchmove="_stopDef" @mousewheel="_stopDef"></div>
    </transition>
    <transition name="select">
      <div class="ProvCityBox" v-show="show" @mousewheel="_stopDef">
        <div class="ProvCityHeader">
          <div class="ProvCityHeaderCancle" @click="show = false">{{cancel}}</div>
          {{title}}
          <div class="ProvCityHeaderConfirm" @click="submit">{{confirm}}</div>
        </div>
        <div class="ProvCityContent">
          <div class="ProvCityContentList" style="width: 100%">
            <ul ref="province-list"
                style="padding: 0"
                :class="{'province_dragging': provinceState.dragging}"
                @touchstart="_onTouchStart('province', $event)"
                @mousedown="_onTouchStart('province', $event)"
                :style="{'transform' : 'translate3d(0,' + provinceState.translateY + 'px, 0)'}">
              <li></li>
              <li></li>
              <li></li>
              <li v-for="(item, index) in provinceState.data"
                  :key="index"
                  :class="{
                                            'current': item.id == provinceState.selectedId,
                                            'node1':  Math.abs(index - provinceState.index) == 1,
                                            'node2':  Math.abs(index - provinceState.index) == 2,
                                            'node3':  Math.abs(index - provinceState.index) >= 3
                                        }"
              >{{item.name}}
              </li>
              <li></li>
              <li></li>
              <li></li>
            </ul>
          </div>
        </div>
        <hr class="ProvCitySelectedTop">
        <hr class="ProvCitySelectedBottom">
      </div>
    </transition>
  </div>
</template>
<script>
  import {addressStreet} from './addressStreet'
  import userStore from "../../store/userStore"
  import './style.css'

  export default {
    data: function () {
      return {
        show: this.propsShow,
        result: this.propsResult,
        target: '',
        provinceState: {
          data: null,
          selectedId: null,
          index: 0,
          startPos: null,
          translateY: 0,
          startTranslateY: 0,
          dragging: false
        },
        delta: 0,
        slideEls: null,

      }
    },
    mounted: function () {
      this.initData();
      this._onTouchMove = this._onTouchMove.bind(this);
      this._onTouchEnd = this._onTouchEnd.bind(this);
    },
    methods: {
      initData(){
        addressStreet.forEach(area => {
          if (area.id == userStore.state.area) {
            this.provinceState.data = area.sub
          }
        })

        if (this.provinceState.data[0].length == 0) {
          return
        }

//        this.provinceState.selectedId = userStore.state.street.length == 0 ?
//          this.provinceState.data[0].id : userStore.state.street;

        if (userStore.state.street.length == 0) {
          this.provinceState.selectedId = this.provinceState.data[0].id
          this.result = {
            'street': this.provinceState.data[0]
          };
        } else {
          this.provinceState.data.forEach(street => {
            if (street.id == userStore.state.street) {
              this.provinceState.selectedId = street.id
              this.result = {
                'street': street
              };
            }
          })
        }


//        if (this.result == null) {
//          this.result = {
//            'street': this.provinceState.data[0]
//          };
//        }
      },
      submit(){
        this.result = {
          'street': this.provinceState.data[this.provinceState.index]
        };
        this.show = false;
      },
      getSelectedData(index){
        let target = this.target;
        let thisData = this[target + 'State'];
        thisData.selectedId = thisData.data[index].id;
      },
      setPage() {
        let target = this.target;
        let thisData = this[target + 'State'];
        let clientHeight = this.slideEls[0]['clientHeight'];
        let total = thisData.data.length;
        let goPage = Math.round((thisData.translateY / clientHeight).toFixed(1));
        if (goPage > 0) {
          goPage = 0;
        }
        goPage = total - 1 >= Math.abs(goPage) ? goPage : -(total - 1);
        let index = Math.abs(goPage);
        thisData.index = index;
        this.getSelectedData(index);
        thisData.translateY = goPage * clientHeight;
      },
      _getTouchPos(e) {
        return e.changedTouches ? e.changedTouches[0]['pageY'] : e['pageY'];
      },
      _getElem(e){
        return Array.from(e.currentTarget.children).slice(3, -3);
      },
      _onTouchStart(target, e){
        let thisData = this[target + 'State'];
        this.target = target;
        this.slideEls = this._getElem(e);
        this.delta = 0;
        thisData.startPos = this._getTouchPos(e);
        thisData.startTranslateY = thisData.translateY;
        thisData.dragging = true;
        document.addEventListener('touchmove', this._onTouchMove, false);
        document.addEventListener('touchend', this._onTouchEnd, false);
        document.addEventListener('mousemove', this._onTouchMove, false);
        document.addEventListener('mouseup', this._onTouchEnd, false);
      },
      _onTouchMove(e) {
        let target = this.target;
        let thisData = this[target + 'State'];
        this.delta = this._getTouchPos(e) - thisData.startPos;
        thisData.translateY = thisData.startTranslateY + this.delta;
        if (Math.abs(this.delta) > 0) {
          e.preventDefault();
        }
      },
      _onTouchEnd(e) {
        let target = this.target;
        let thisData = this[target + 'State'];
        thisData.dragging = false;
        this.setPage();
        document.removeEventListener('touchmove', this._onTouchMove);
        document.removeEventListener('touchend', this._onTouchEnd);
        document.removeEventListener('mousemove', this._onTouchMove);
        document.removeEventListener('mouseup', this._onTouchEnd);
      },
      _stopDef(e){
        e.preventDefault()
      }
    },
    watch: {
      propsShow: function (newVal) {
        this.show = newVal
      },
      show: function (newVal) {
        this.$emit('result', newVal, this.result)
      }
    },
    props: {
      'propsResult': {
        type: Object,
        default: null
      },
      'propsShow': {
        type: Boolean,
        default: false
      },
      'title': {
        type: String,
        default: '请选择'
      },
      'confirm': {
        type: String,
        default: '确定'
      },
      'cancel': {
        type: String,
        default: '取消'
      }
    }
  }
</script>
