<style lang="stylus" rel="stylesheet/stylus">
  _rem = 20rem;
  .tab-item
    font-size 0
    .fa-home,.fa-user
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
      display: block;

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
    <img :src="srcClass" :class="iconClass">
    <span class="tab-label" v-text="label"></span>
  </router-link>
</template>

<script>
  import $ from 'zepto'
  import homeIcon from '../assets/img/home.png'
  import homeIconR from '../assets/img/homeR.png'
  import My from '../assets/img/My.png';
  import MyR from '../assets/img/MyR.png';
  import add from '../assets/img/add.png';
  import sub from '../assets/img/sub.png'

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
        srcIcon = this.linkClass(icon, flag);
        return srcIcon
      },
      iconClass () {
        return `fa-${this.icon}`
      },
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
      linkClass(name, flag){
        var srcIcon = '';
        switch(name){
          case 'home':
              if(!flag){
                srcIcon =homeIconR;
              }
            else if(flag == 0)
              srcIcon = homeIconR;
            else
                srcIcon =homeIcon;
            break;
          case 'user':
            if(flag == 2)
              srcIcon =MyR;
            else
              srcIcon =My;
            break;
          case 'add':
            if(!flag){
              srcIcon =sub;
            }
            else if(flag == 1){
              srcIcon =add;
            }else{
              srcIcon = sub;
            }
            break;
          default:break;
        }

        return srcIcon;
//        if(show[name]){
//          return "active"
//        }else {
//          return ""
//        }
//        if(this.name===this.$route.name){
//          return "active"
//        }else{
//          return ""
//        }
      },
    }
  }
</script>
