<template>
	<mt-popup v-model="popupVisible" position="bottom" style="width: 100%;" popup-transition="popup-fade">
  	<div class="input-container">
      <div class="picker-toolbar">
        <span @click.stop="canleBtn" class="pick-cancle">
          取消
        </span>
        <span @click.stop="comfirmBtn" class="pick-confirm">
          确认
        </span>
      </div>
  		<div class="input-area-aur" style="color:#4d555f">
        <div style="text-align: center">
          <span>请选择您的身份</span>
        </div>
        <div class="aur-line">
          <div @click.stop="AurSelect($event,item)" v-for="item in dataAur" :class="item.class"><span>{{item.name}}</span></div>
        </div>
  		</div>
  	</div>
  </mt-popup>
</template>

<script>
import {Popup} from 'mint-ui'

export default{
	components: {
    Popup
 	},
 	created() {
    this.$root.eventHub.$on('aurUpdate.open', this.openModal)
  },
 	data () {
    return {
      popupVisible: false,
      curAur:'',//key
      curAurName:'',
      dataAur: [
        {
          aur: 'DB',
          name: '代办',
          class: 's-a-box-db'
        },
        {
          aur: 'KS',
          name: '客商',
          class: 's-a-box-db'
        },
        {
          aur: 'LK',
          name: '冷库',
          class: 's-a-box-db'
        },
        {
          aur: 'ZZH',
          name: '果农',
          class: 's-a-box-db'
        },
        {
          aur: 'HZS',
          name: '合作社',
          class: 's-a-box-db'
        }
      ]
    }
  },
 	methods: {
    openModal(e){
      const self = this;
      self.popupVisible= true;
      self.curAur = e;
      self.dataAur.forEach((i) => {
        if (e === i.aur) {
          i.class = 's-a-box-db visited'
          self.curAurName = i.name;
        }
        else
          i.class = 's-a-box-db'
      })


    },
    canleBtn(){
      this.popupVisible= false;
    },
    comfirmBtn(){
      this.popupVisible= false;
      this.$root.eventHub.$emit('aurUpdate.confirm', {aurKey:this.curAur,aurName:this.curAurName});
    },
    AurSelect(event, item){
      const self = this;
      self.dataAur.forEach((i) => {
        if (item.aur === i.aur) {
          i.class = 's-a-box-db visited';
          self.curAur = item.aur;
          self.curAurName = item.name;
        }
        else
          i.class = 's-a-box-db'
      })
    },
 	}
}
</script>

<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .input-container
    width: 100%;
    position: absolute;
    bottom: 0;
    background-color: #fff;
    .picker-toolbar
      border-bottom: 1px solid #eaeaea;
      height (40/_rem)
      .pick-cancle
        float left
        display: inline-block;
        width: 50%;
        text-align: center;
        line-height: (40/_rem)
        font-size: (16/_rem);
        color: #26a2ff;
      .pick-confirm
        float right
        display: inline-block;
        width: 50%;
        text-align: center;
        line-height: (40/_rem)
        font-size: (16/_rem);
        color: #26a2ff;
    .input-area-aur
      position: relative;
      text-align: left;
      font-size: (16/_rem);
      padding: (10/_rem);
      color: #ccc;
      height (120/_rem)
      .aur-line
        text-align center
        margin-top (30/_rem)
        .s-a-box-db
          width (60 /_rem)
          border 1px solid #CCD0D1
          border-radius 4px
          text-align center
          display inline-block
          cursor pointer
          height (20 /_rem)
          line-height (20 /_rem)
          font-size 0
          color #999999
        .visited
          background-color #28CBA7
          color #ffffff !important
        span
          font-size (16/_rem)
        .s-a-box-db:not(:first-child)
          margin-left (10 /_rem)
  </style>
