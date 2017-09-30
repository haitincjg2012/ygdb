/**
* Created by gsy on 2017/08/21.
* 主内容头部（标题+面包屑）
* 首页/一级模块名/二级模块名/详情、编辑、新增
* 首页/firsTit/secondTit/thirdTit
<!--<my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>-->
*/
<template>
    <div class="myHead">
        <span class="title" v-if="!breadFlag">{{secondTitle}}</span>
        <span class="title" v-if="breadFlag">{{thirdTitle}}</span>
            <span class="bread">
                <el-breadcrumb separator="/">
                    <!--<el-breadcrumb-item :to="{path:'/home'}"><i class="el-icon-menu breadIcon"></i>首页</el-breadcrumb-item>-->
                    <el-breadcrumb-item><i class="el-icon-menu breadIcon"></i>首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="greyTit">{{firstTitle}}</el-breadcrumb-item>
                    <el-breadcrumb-item><span @click="initialPage">{{secondTitle}}</span></el-breadcrumb-item>
                    <el-breadcrumb-item v-if="breadFlag">{{thirdTitle}}</el-breadcrumb-item>
                    <!--<el-breadcrumb-item v-if="viewFlag">详情</el-breadcrumb-item>
                    <el-breadcrumb-item v-if="editFlag">编辑</el-breadcrumb-item>
                    <el-breadcrumb-item v-if="addFlag">新增</el-breadcrumb-item>-->
                </el-breadcrumb>
            </span>
    </div>
</template>

<script>
    export default{
        props:{
            firstTitle:{//一级名称
              type:String
          },
            secondTitle:{//二级名称
              type:String
          },
            thirdTitle:{//三级名称
              type:String
          },
            breadFlag:{//查看、详情
                type:Boolean,
                default:false
          }
         /* viewFlag:{//查看、详情
              type:Boolean,
              default:false
          },
          editFlag:{//编辑
              type:Boolean,
              default:false
          },
          addFlag:{//新增
              type:Boolean,
              default:false
          }*/

        },
        data(){
            return {}
        },
        methods: {
            initialPage:function(){
                var vm=this;
//              console.log("initialPage");
                vm.$emit("initialPage");
            }
        },
        components: {}
    }
</script>

<style lang="stylus" rel="stylesheet/stylus">
    @import './header.css';
</style>
