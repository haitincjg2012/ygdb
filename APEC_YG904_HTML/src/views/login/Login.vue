/**
* Created by gsy on 2017/8/18.
* 登录页
*/
<template>
    <div class="loginBox">
        <el-row class="loginContent">
            <el-col :span="12">
                <div class="logo">
                    <img src="../../assets/images/logo.png" />
                </div>
            </el-col>
            <el-col :span="12">
                <div class="formBox">
                    <div class="title">
                    	<span class="titName">易果代办</span>
                    	<span>管理系统</span>
                    </div>
                    <el-form class="formContent" label-width="60px" :model="loginForm" :rules="loginRules" ref="loginForm">
                        <el-form-item label="账号：" prop="username">
                            <el-input v-model="loginForm.username" auto-complete="off" placeholder="请输入用户名"></el-input>
                        </el-form-item>
                        <el-form-item label="密码：" prop="password">
                            <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="请输入密码"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button class="login" type="primary" @click="loginBtn('loginForm')">登录</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>

    export default {
        data () {
            return {
                loginForm:{
                    username:"",
                    password:""
                },
                userInfo:[],
                loginRules:{
                    username:[{required:true,message:'请输入用户名',trigger:'blur'}],
                    password:[{required:true,message:'请输入密码',trigger:'blur'}]
                }
            }
        },
        methods: {
            loginBtn:function(formName){
                var vm = this;
                var staticFlag=vm.$store.state.staticFlag;
//                console.log("staticFlag:"+staticFlag);
                if(!staticFlag){
                    //请求后台方法
                     vm.$refs[formName].validate((valid)=>{
                     if(valid){
                     //console.log("url:"+vm.apiUrl.loginUrl);
                     let params = {
                     url: vm.apiUrl.loginUrl,
                     data: {
                     userName:vm.loginForm.username,
                     password:vm.loginForm.password
                     }
                     }
                     vm.ax.post(params,vm.sucCb);
                     }else{
                     console.log("验证未通过");
                     }
                     });
                }
                else{
                    //引入静态数据
                    vm.$refs[formName].validate((valid)=>{
                        if(valid){
                            let params = {
                            url:vm.staticUrl.loginUrl,
                                data: {
                                    userName:vm.loginForm.username,
                                    password:vm.loginForm.password
                                }
                            }
                            vm.ax.get(params,vm.sucCb);
                        }else{
                            console.log("验证未通过");
                }
                });
                }


            },
            sucCb(data){
                //暂时备注防止跳转
                var vm=this;
                vm.userInfo=data.data;
//                console.log("登录成功!用户名："+vm.userInfo.name);
                //获取token
                vm.commonJs.setValue('authToken',data.data.token);
                vm.$store.commit('changeAuthToken',vm.userInfo);
                //记住登录名
                vm.commonJs.setValue('phone',data.data.phone);
                //获取用户名
                vm.commonJs.setValue('userName',data.data.name);
                vm.$store.commit('changeUserName',vm.userInfo);
                //获取用户对象
                vm.commonJs.setValue('userNo',data.data.userNo);
                vm.$store.commit('changeUserInfo',vm.userInfo);

//                console.log("name："+vm.$store.state.userName+" token："+vm.$store.state.authToken+" phone："+data.data.phone+" userid:"+vm.$store.state.userNo);
                //跳转到“历史记录路径”或者“客户认证页”（默认）
                var redirect=decodeURIComponent(this.$route.query.redirect||'/certificate');
                vm.$router.push({path:redirect});
                //加载菜单(暂无)
//              vm.$root.eventHub.$emit('initMenu',"");
            }
        },
        created() {

        },
        mounted(){
            var vm=this;
            //登录成功记住用户名
            var phone=vm.commonJs.getValue('phone');
            if(phone){
                vm.loginForm.username=vm.commonJs.getValue('phone');
            }
        }

    }
</script>
<style scoped>
    @import '../../assets/css/login.css';

</style>