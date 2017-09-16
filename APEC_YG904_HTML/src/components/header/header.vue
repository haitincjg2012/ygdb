/**
* Created by gsy on 2017/08/21.
* 主内容头部（标题+面包屑）
<!--<my-header v-on:initialPage="initialPage" :fatherTitle="fatitle" :headTitle="title" :viewFlag="viewFlag" :editFlag="editFlag" :addFlag="addFlag"></my-header>-->
*/
<template>
    <div class="myHead">
        <span class="title">{{headTitle}}</span>
            <span class="bread">
                <el-breadcrumb separator="/">
                    <!--<el-breadcrumb-item :to="{path:'/home'}"><i class="el-icon-menu breadIcon"></i>首页</el-breadcrumb-item>-->
                    <el-breadcrumb-item><i class="el-icon-menu breadIcon"></i>首页</el-breadcrumb-item>
                    <el-breadcrumb-item><span @click="initialPage">{{fatherTitle}}</span></el-breadcrumb-item>
                    <el-breadcrumb-item v-if="viewFlag">详情</el-breadcrumb-item>
                    <el-breadcrumb-item v-if="editFlag">编辑</el-breadcrumb-item>
                    <el-breadcrumb-item v-if="addFlag">新增</el-breadcrumb-item>
                </el-breadcrumb>
            </span>
    </div>
</template>

<script>
    export default{
        props:{
          fatherTitle:{//一级名称
              type:String
          },
          headTitle:{//二级名称
              type:String
          },
          viewFlag:{//查看、详情
              type:Boolean,
              default:true
          },
          editFlag:{//编辑
              type:Boolean,
              default:true
          },
          addFlag:{//新增
              type:Boolean,
              default:true
          }

        },
        data(){
            return {}
        },
        methods: {
            initialPage:function(){
                var vm=this;
                console.log("initialPage");
                vm.$emit("initialPage");
            }
        },
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
    @import './header.css';
</style>
