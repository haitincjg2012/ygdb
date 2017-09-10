/**
* Created by gsy on 2017/08/21.
* 上部导航栏
*/
<template>
<el-menu theme="dark" class="topNav" mode="horizontal" :router="true">
    <el-menu-item class="title" index="">易果代办管理系统</el-menu-item>
    <!--<h1 class="title">易果代办管理系统</h1>-->
    <el-submenu index="">
        <template slot="title">我的工作台</template>
        <el-menu-item index="login">退出</el-menu-item>
    </el-submenu>
    <el-menu-item class="loginInfo" index="">欢迎您：{{username}}</el-menu-item>
</el-menu>
</template>

<script>
    export default{
        data(){
            return {
                username:""
            }
        },
        methods: {

        },
        mounted(){
            var vm=this;
            vm.username=vm.$store.state.userName;
            console.log("username:"+vm.$store.state.userName);
        },
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
    @import './nav.css';
</style>
