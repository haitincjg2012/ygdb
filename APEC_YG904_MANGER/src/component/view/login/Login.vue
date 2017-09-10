<template>
    <el-row class="login-wrapper">
        <!--左侧logo-->
        <el-col :span="12">
            <div class="img-wrapper">
                <img src="../../img/logo.png" class="login-img"/>
            </div>
        </el-col>
        <!--右侧登录框-->
        <el-col :span="12">
            <div class="form-wrapper">
                <div class="title">易果管理后台</div>
                <el-form :inline="true" class="login-form">
                    <el-form-item label="账号：">
                        <!--elementUI之坑，keyup.enter无效，需加上.native响应原生事件才行-->
                        <el-input v-model="account" @keyup.enter.native="login"></el-input>
                    </el-form-item>
                    <el-form-item label="密码：">
                        <el-input type="password" v-model="password" @keyup.enter.native="login"></el-input>
                    </el-form-item>
                    <el-button class="login-btn" type="primary" @click="login">登录</el-button>
                </el-form>
            </div>
        </el-col>
    </el-row>
</template>

<script>
    import axiosPost from '../../js/axiosPost'

    export default {
        data () {
            return {
                account: '',
                password: '',
            }
        },
        methods: {
            login: function () {
	                let vm = this;
	            let url = '/enquiry/manager/login';
	            let param = {
//                    for 正式
                    managerPhone: vm.account,
                    managerPassword: vm.password
//                    for 调试
//                    managerPhone: 13560788956,
//                    managerPassword: 123456

                };
                let callback = function (res) {
                    let name = res.data.data.managerName;
                    setTimeout(function() {
//                        携带着用户名发射事件
                        vm.$router.push({
//                            本应跳至framework，但因其拥有默认子路由，无name，所以直接调用子路由name
                            name: 'home',
                            params: {
                                name: name
                            }
                        });
                    }, 500);
                };
                axiosPost(url, param, callback);
            }
        }
    }
</script>

<style scoped>
    .login-wrapper {
        position: absolute;
        width: 100%;
        top: 25%;
    }
    .img-wrapper {
        text-align: center;
    }
    .title {
        font-size: 40px;
        margin-bottom: 30px;
    }
    .form-wrapper {
        width: 100%;
        text-align: center;
    }
    .login-form {
        width: 400px;
	    border-radius: 5px;
	    box-shadow: 0 0 80px #DDD;
        padding: 10px;
        margin: 0 auto;
        text-align: center;
    }
    .el-form-item {
        margin: 20px 0;
    }
    .el-input {
        width: 300px;
    }
    .login-btn {
        margin: 10px auto;
	    width: 60%;
	    background-color: #F8B62D;
	    border-color: #F8B62D;
    }
    .login-btn:hover {
	    background-color: #F8B62D;
    }
    img {
        width: 350px;
        height: 350px;
    }
</style>