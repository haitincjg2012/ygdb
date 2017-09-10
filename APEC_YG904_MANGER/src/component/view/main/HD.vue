<template>
    <el-row type="flex" class="main-header">
        <el-col :span="12">
            <span class="logo-lg">
                <router-link to="/" class="color-white">易果</router-link>
            </span>
        </el-col>
        <el-col :span="12">
            <p class="right">
                <span>欢迎您：</span>
                <span>{{userName}}</span>
            </p>
        </el-col>
    </el-row>
</template>
<script>
    export default {
        props: ['userName'],
        data() {
            return {
//                dropdown: false,
            }
        },
        methods: {
//            用户名下拉框
//            drop() {
//                this.dropdown = !this.dropdown;
//            },
//            close() {
//                this.dropdown = false;
//            }
        },
    }
</script>
<style>
   @import "../../../../style/common/css/global.css";
   .main-header {
       position: fixed;
       left: 0;
       top: 0;
       padding: 10px;
       width: 100%;
       height: 60px;
       background-color: #497da4;
       line-height: 40px;
   }
   .logo-lg {
       font-size: 20px;
       margin-left: 20px;
   }

   .logo-lg a{
       color: #fff;
   }
   .right {
       margin: 0 20px 0 0;
       text-align: right;
       color: #fff;
   }
</style>