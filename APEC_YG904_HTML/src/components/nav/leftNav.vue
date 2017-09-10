/**
* Created by gsy on 2017/08/21.
* 左侧导航栏
*/
<template>
    <el-menu class="leftNav" :router="true">
        <el-submenu index="1">
            <template slot="title" class="subTitle"><i class="el-icon-menu"></i>商品</template>
            <el-menu-item-group>
                <el-menu-item index="fx">品类</el-menu-item>
                <el-menu-item index="th">商品</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="2">
            <template slot="title" class="subTitle"><i class="el-icon-menu"></i>客户</template>
            <el-menu-item-group>
                <el-menu-item index="member">客户列表</el-menu-item>
                <el-menu-item index="certificate">实名认证审核</el-menu-item>
                <el-menu-item index="th">客户举报</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="3">
            <template slot="title" class="subTitle"><i class="el-icon-menu"></i>交易</template>
            <el-menu-item-group>
                <el-menu-item index="gq">供求管理</el-menu-item>
                <el-menu-item index="qg">求购管理</el-menu-item>
                <el-menu-item index="jsd">交收单管理</el-menu-item>
                <el-menu-item index="hq">行情发布</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="4">
            <template slot="title" class="subTitle"><i class="el-icon-menu"></i>数据分析</template>
            <el-menu-item-group>
                <el-menu-item index="th">调货明细</el-menu-item>
                <el-menu-item index="db">代办调货分析</el-menu-item>
                <el-menu-item index="cd">菜单分析</el-menu-item>
                <el-menu-item index="cd">客户积分排行及积分明细</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
    </el-menu>
</template>

<script>
    export default{
        data(){
            return {}
        },
        methods: {},
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
    @import './nav.css';
</style>
