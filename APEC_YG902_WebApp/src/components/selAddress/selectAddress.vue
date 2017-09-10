<template>
  <div>

    <mt-field label="省市区" :value="selValue" placeholder="请选择省市区" :readonly="true" @click.native="openArea"></mt-field>
    <mt-field label="街道" :value="selStreet" placeholder="请选择街道" :readonly="true" @click.native="openStreet"></mt-field>

    <vue-area ref="dialogArea" :props-show="showDialog" :props-result="result" v-on:result="areaResult"></vue-area>
    <vue-street ref="dialogStreet" :props-show="showDialogStreet" :props-result="resultStreet"
                v-on:result="streetResult"></vue-street>
  </div>
</template>

<script>
  import {addressStreet} from "./addressStreet"
  import userStore from "../../store/userStore"
  import vueArea from "./selArea"
  import vueStreet from "./selStreet"

  import { Toast } from 'mint-ui';

  export default {

    data() {
      return {
        selectPro: "",
        selectCity: "",
        selectArea: "",
        selectStreet: "",
        result: {
          province: {
            code: 1,
            name: "广东省",
            parentId: 0
          },
          city: {
            code: 100,
            name: "深圳市",
            parentId: 1
          },
          area: {
            code: 1001,
            name: "福田区",
            parentId: 100
          }
        },
        resultStreet: null,
        streetCols: [],
        showDialog: false,
        showDialogStreet: false
      }
    },

    methods: {
      openArea() {
        this.showDialog = true
        this.$refs.dialogArea.initData();
      },

      openStreet() {
        if (userStore.state.proName === "") {
          return
        }
        var hasData = false;
        addressStreet.forEach(area => {
          if (area.id == userStore.state.area) {
            if (area.sub.length > 0) {
              hasData = true
            }
          }
        })

        if (hasData) {
          this.showDialogStreet = true
          this.$refs.dialogStreet.initData();
        } else {
          Toast("没有街道");
        }
      },

      areaResult: function (show, result) {
        this.showDialog = show
        this.result = result

        if (result.area.code.toString() !== userStore.state.area) {
          userStore.commit('setStreet', "")
          userStore.commit('setStreetName', "")
        }

        userStore.commit('setPro', result.province.code.toString())
        userStore.commit('setProName', result.province.name)
        userStore.commit('setCity', result.city.code.toString())
        userStore.commit('setCityName', result.city.name)
        userStore.commit('setArea', result.area.code.toString())
        userStore.commit('setAreaName', result.area.name)


        var hasData = false;
        addressStreet.forEach(area => {
          if (area.id == userStore.state.area) {
            if (area.sub.length > 0) {
              hasData = true
              userStore.commit('setStreet', area.sub[0].id)
              userStore.commit('setStreetName', area.sub[0].name)
            }
          }
        })
        if (!hasData) {
          userStore.commit('setStreetName', "-没有街道-")
        }
      },

      streetResult: function (show, result) {
        this.showDialogStreet = show
        this.result = result

        userStore.commit('setStreet', result.street.id)
        userStore.commit('setStreetName', result.street.name)
      }
    },

    computed: {
      selPro() {
        return userStore.state.proName
      },
      selCity() {
        return userStore.state.cityName
      },
      selArea() {
        return userStore.state.areaName
      },
      selStreet() {
        return userStore.state.streetName
      },
      selValue() {
        if (this.selPro.length === 0) {
          return ""
        }
        return this.selPro + ' ' + this.selCity + ' ' + this.selArea;
      }
    },

    components: {
      'vue-area': vueArea,
      'vue-street': vueStreet
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .span_address
    display inline-block
    width 100%
    height 2.2em
    line-height 2.2em
    padding-left 5px

</style>
