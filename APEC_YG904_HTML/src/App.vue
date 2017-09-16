<template>
  <div id="app">
    <transition>
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
    </transition>
  </div>
</template>

<script>
  export default {
    data () {
      return {

      }
    },

  methods: {

  }

}
</script>
<style>
  @import './assets/css/reset.css';
  @import './assets/css/common.css';

</style>
