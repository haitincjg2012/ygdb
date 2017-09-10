/**
* Created by gsy on 2017/08/21.
* 主内容头部（标题+面包屑）
<!--<my-header :headTitle="title"></my-header>-->
*/
<template>
    <div class="myHead">
        <span class="title">{{headTitle}}</span>
            <span class="bread">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item :to="{path:'/home'}"><i class="el-icon-menu breadIcon"></i>易果代办管理系统</el-breadcrumb-item>
                    <el-breadcrumb-item>{{headTitle}}</el-breadcrumb-item>
                </el-breadcrumb>
            </span>
    </div>
</template>

<script>
    export default{
        props:{
          headTitle:{
              type:String
          }
        },
        data(){
            return {}
        },
        methods: {},
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
    @import './header.css';
</style>
