<template>
    <el-dialog :visible.sync="dialogTableVisible">
        <el-form model="ruleForm" label-width="100px" :rule="rule" ref="ruleForm">
            <el-form-item label="姓名" prop="name">
                <el-input type="text" v-model="ruleForm.name" autoComplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input type="text" v-model="ruleForm.phone" autoComplete="off"></el-input>
            </el-form-item>
            <el-form-item label="企业类型">
                <el-select v-model="ruleForm.ft" placeholder="请选择">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="主营品种" prop="type">
                <el-input type="text" v-model="ruleForm.type"></el-input>
            </el-form-item>
            <el-form-item label="区域" prop="area">
                <el-input type="text" v-model="ruleForm.area"></el-input>
            </el-form-item>
            <el-form-item label="定位" prop="pos">
                <el-input type="text" v-model="ruleForm.pos"></el-input>
            </el-form-item>
            <el-form-item label="认证标识">
                <el-input></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input type="text" v-model="ruleForm.password"></el-input>
            </el-form-item>
            <el-form-item >
                <el-button type="primary" @click="submitForm()">提交</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>
<script>
    export default{
        data(){
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm2.checkPass !== '') {
                        this.$refs.ruleForm2.validateField('checkPass');
                    }
                    callback();
                }
            };
            return {
                dialogTableVisible:false,
                ruleForm:{
                    name:'',
                    phone:'',
                    ft:'',
                    type:'',
                    area:'',
                    pos:'',
                    password:''
                },
                rule:{
                    password:[{ validator: validatePass, trigger: 'blur' }]
                }
            }
        },
        methods:{
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        alert('submit!');
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }
    }
</script>