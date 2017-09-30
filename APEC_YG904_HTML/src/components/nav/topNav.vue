/**
* Created by gsy on 2017/08/21.
* 上部导航栏
*/
<template>
<el-menu theme="dark" class="topNav" mode="horizontal" :router="true">
    <el-menu-item class="title" index="">易果代办管理系统</el-menu-item>
   <!-- <el-submenu index="">
        <template slot="title">我的工作台</template>
        <el-menu-item index="login">退出</el-menu-item>
    </el-submenu>-->
    <el-submenu class="loginInfo" index="">
        <template slot="title">欢迎您：{{username}}</template>
        <el-menu-item index="login">退出</el-menu-item>
    </el-submenu>
</el-menu>
</template>

<script>
    export default{
        data(){
            return {

            }
        },
        methods: {

        },
        computed:{
            username(){
                var vm=this;
                var name;
                name=vm.$store.state.userName;
                return name;
            }
        },
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
    @import './nav.css';
</style>
