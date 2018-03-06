/**
* Created by gsy on 2017/08/21.
* 左侧导航栏
*/
<template>
    <el-menu class="leftNav" :router="true" background-color="#eef1f6" text-color="#000"
      active-text-color="#20a0ff" >
       <!-- <el-submenu index="1">
            <template slot="title" class="subTitle"><i class="el-icon-menu"></i>商品</template>
            <el-menu-item-group>
                <el-menu-item index="fx">品类</el-menu-item>
                <el-menu-item index="th">商品</el-menu-item>
            </el-menu-item-group>
        </el-submenu>-->
        <el-submenu index="2">
            <template slot="title" class="subTitle"><i class="el-icon-menu"></i>客户</template>
            <el-menu-item-group>
                <el-menu-item index="member">客户列表</el-menu-item>
                <el-menu-item index="certificate">实名认证审核</el-menu-item>
                <el-menu-item index="company">企业管理</el-menu-item>
                <!--<el-menu-item index="th">客户举报</el-menu-item>-->
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="3">
            <template slot="title" class="subTitle"><i class="el-icon-sort"></i>交易</template>
            <el-menu-item-group id="transactionNav">
                <el-menu-item index="supplyDemand">供求管理</el-menu-item>
                <!--<el-menu-item index="qg">求购管理</el-menu-item>-->
                <el-menu-item index="settlement">交收单管理</el-menu-item>
                <el-menu-item index="newsIssue">行情发布</el-menu-item>
                <el-menu-item index="whistleBlowing">举报</el-menu-item>
                <el-menu-item index="moments">果友圈动态</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="4">
            <template slot="title" class="subTitle"><i class="el-icon-star-on"></i>积分</template>
            <el-menu-item-group>
                <el-menu-item index="customerIntegral">客户积分</el-menu-item>
                <el-menu-item index="level">等级设置</el-menu-item>
                <el-menu-item index="rules">积分规则</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="5">
            <template slot="title" class="subTitle"><i class="el-icon-picture"></i>广告</template>
            <el-menu-item-group>
            	<el-menu-item index="advertising">广告管理</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="6">
            <template slot="title" class="subTitle"><i class="el-icon-phone"></i>运营</template>
            <el-menu-item-group>
            	<el-menu-item index="lottery">抽奖活动</el-menu-item>
            	<el-menu-item index="guessing">价格竞猜</el-menu-item>
                <el-menu-item index="rank">排行榜</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <el-submenu index="7">
            <template slot="title" class="subTitle"><i class="el-icon-goods"></i>商品</template>
            <el-menu-item-group>
            	<el-menu-item index="category">商品品类</el-menu-item>
            	<el-menu-item index="attribute">商品属性</el-menu-item>
            </el-menu-item-group>
        </el-submenu>
        <!--<el-submenu index="4">
            <template slot="title" class="subTitle"><i class="el-icon-menu"></i>数据分析</template>
            <el-menu-item-group>
                <el-menu-item index="th">调货明细</el-menu-item>
                <el-menu-item index="db">代办调货分析</el-menu-item>
                <el-menu-item index="cd">菜单分析</el-menu-item>
                <el-menu-item index="cd">客户积分排行及积分明细</el-menu-item>
            </el-menu-item-group>
        </el-submenu>-->
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
