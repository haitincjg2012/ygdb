<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .tab-item
    font-size 0
    .fa-home,.fa-user,.fa-findShome,.fa-fruitCircle
      width (24/_rem)
      height (24/_rem)
    .fa-add
      width (40/_rem)
      height (40/_rem)
    i
      position: relative;
      font-size:(15/_rem);
      span
        position: absolute;
        display: inline-block;
        top: -2px;
        left: 56%;
        min-width: 18px;
        text-align: center;
        font-size: 1.2rem;
        color: #fff;
        padding: 3px 4px;
        background-color: #ff4e4e;
        border-radius: 10px;
        white-space: nowrap;
    .tab-label
      font-size: (12/_rem);
      color:#646464;
      display: block;
    .active
      color #28CBA7
  //.tab-item:hover {
  //   -webkit-animation: icon-bounce 0.5s alternate;
  //   -moz-animation: icon-bounce 0.5s alternate;
  //   -o-animation: icon-bounce 0.5s alternate;
  //  animation: icon-bounce 0.5s alternate;
  //}
  /* animation */
  @keyframes icon-bounce {
    0%, 100% {
      -moz-transform: rotate(0deg);
      -ms-transform: rotate(0deg);
      -webkit-transform: rotate(0deg);
      transform: rotate(0deg);
    }

    25% {
      -moz-transform: rotate(15deg);
      -ms-transform: rotate(15deg);
      -webkit-transform: rotate(15deg);
      transform: rotate(15deg);
    }

    50% {
      -moz-transform: rotate(-15deg);
      -ms-transform: rotate(-15deg);
      -webkit-transform: rotate(-15deg);
      transform: rotate(-15deg);
    }

    75% {
      -moz-transform: rotate(5deg);
      -ms-transform: rotate(5deg);
      -webkit-transform: rotate(5deg);
      transform: rotate(5deg);
    }

    85% {
      -moz-transform: rotate(-5deg);
      -ms-transform: rotate(-5deg);
      -webkit-transform: rotate(-5deg);
      transform: rotate(-5deg);
    }
  }
  @-webkit-keyframes icon-bounce {
    0%, 100% {
      -moz-transform: rotate(0deg);
      -ms-transform: rotate(0deg);
      -webkit-transform: rotate(0deg);
      transform: rotate(0deg);
    }

    25% {
      -moz-transform: rotate(15deg);
      -ms-transform: rotate(15deg);
      -webkit-transform: rotate(15deg);
      transform: rotate(15deg);
    }

    50% {
      -moz-transform: rotate(-15deg);
      -ms-transform: rotate(-15deg);
      -webkit-transform: rotate(-15deg);
      transform: rotate(-15deg);
    }

    75% {
      -moz-transform: rotate(5deg);
      -ms-transform: rotate(5deg);
      -webkit-transform: rotate(5deg);
      transform: rotate(5deg);
    }

    85% {
      -moz-transform: rotate(-5deg);
      -ms-transform: rotate(-5deg);
      -webkit-transform: rotate(-5deg);
      transform: rotate(-5deg);
    }
  }
  @-moz-keyframes icon-bounce {
    0%, 100% {
      -moz-transform: rotate(0deg);
      -ms-transform: rotate(0deg);
      -webkit-transform: rotate(0deg);
      transform: rotate(0deg);
    }

    25% {
      -moz-transform: rotate(15deg);
      -ms-transform: rotate(15deg);
      -webkit-transform: rotate(15deg);
      transform: rotate(15deg);
    }

    50% {
      -moz-transform: rotate(-15deg);
      -ms-transform: rotate(-15deg);
      -webkit-transform: rotate(-15deg);
      transform: rotate(-15deg);
    }

    75% {
      -moz-transform: rotate(5deg);
      -ms-transform: rotate(5deg);
      -webkit-transform: rotate(5deg);
      transform: rotate(5deg);
    }

    85% {
      -moz-transform: rotate(-5deg);
      -ms-transform: rotate(-5deg);
      -webkit-transform: rotate(-5deg);
      transform: rotate(-5deg);
    }
  }
  @-o-keyframes icon-bounce {
    0%, 100% {
      -moz-transform: rotate(0deg);
      -ms-transform: rotate(0deg);
      -webkit-transform: rotate(0deg);
      transform: rotate(0deg);
    }

    25% {
      -moz-transform: rotate(15deg);
      -ms-transform: rotate(15deg);
      -webkit-transform: rotate(15deg);
      transform: rotate(15deg);
    }

    50% {
      -moz-transform: rotate(-15deg);
      -ms-transform: rotate(-15deg);
      -webkit-transform: rotate(-15deg);
      transform: rotate(-15deg);
    }

    75% {
      -moz-transform: rotate(5deg);
      -ms-transform: rotate(5deg);
      -webkit-transform: rotate(5deg);
      transform: rotate(5deg);
    }

    85% {
      -moz-transform: rotate(-5deg);
      -ms-transform: rotate(-5deg);
      -webkit-transform: rotate(-5deg);
      transform: rotate(-5deg);
    }
  }
</style>

<template>
  <router-link :to="path" class="tab-item">
  <!--<router-link class="tab-item">-->
    <img :src="srcClass" :class="iconClass">
    <span class="tab-label" :class="{active:labelClass}" v-text="label"></span>
  </router-link>
</template>

<script>
  import $ from 'zepto'
  import homeIcon from '../assets/img/home.png'
  import homeIconR from '../assets/img/homeR.png'
  import My from '../assets/img/My.png';
  import MyR from '../assets/img/MyR.png';
  import add from '../assets/img/add.png';
  import sub from '../assets/img/sub.png';
  import goodsD from '../assets/img/goodsD.png'//默认果圈
  import goods from '../assets/img/goods.png'//果圈
  import fruitCircleD from '../assets/img/fruitCircleD.png'//默认果圈
  import fruitCircle from '../assets/img/fruitCircle.png'//果圈

  export default {
    props: {
      path: '',
      icon: '',
      name:'',
      label: '',
      activeClass:'',
      addImgClass:''
    },
    computed: {
      srcClass(){
        var srcIcon = '';
        var icon = this.icon;
        var flag = this.$route.query.flag;
        var name = this.$route.name;
        srcIcon = this.linkClass(icon, flag,name);
        return srcIcon
      },
      iconClass () {
        return `fa-${this.icon}`
      },
      labelClass(){
          var rname = this.$route.name;
          var name = this.name;

          if(rname == name){
             return true;
          }else{
              return false;
          }
      }
    },
    data() {
      return {
        homeS:homeIcon,
        homeSR:homeIconR,
        myS:My,
        mySR:MyR,
        add:add

      }
    },
    created() {
    },
    methods:{
      linkClass(icon, flag,name){
        var srcIcon = '';
        switch(icon){
          case 'home':
           if(flag == 0)
              srcIcon = homeIconR;
           else if(name == "home"){
             srcIcon = homeIconR;
           }else
                srcIcon =homeIcon;
            break;
          case 'user':
            if(flag == 3)
              srcIcon =MyR;
            else if(name == "pc"){
              srcIcon =MyR;
            } else
              srcIcon =My;
            break;
          case 'add':
            srcIcon = sub;
            break;
          case 'findShome':
              if(flag == 1){
                srcIcon = goods;
              }
              else if(name == "findShome"){
                srcIcon =goods;
              }else{
                srcIcon = goodsD;
              }
              break;
          case 'fruitCircle':
            if(flag == 2){
              srcIcon = fruitCircle;
            }else if(name == "fruitCircle"){
              srcIcon = fruitCircle;
            }else{
              srcIcon = fruitCircleD;
            }
              break;
          default:break;
        }

        return srcIcon;
      },
    }
  }
</script>
