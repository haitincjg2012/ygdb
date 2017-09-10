<template>
    <div>
        <el-row class="z-top">
            <el-form :inline="true">
                <el-col :span="6">
                    <el-form-item label="姓名:">
                        <el-input v-model="name" placeholder="请输入内容"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="2" :offset="12">
                    <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                </el-col>
           </el-form>
        </el-row>
        <el-row class="z-top">
             <el-col :span="16">
                 <el-table
                         :data="tableData"
                         border
                         style="width: 100%"
                          class="z-top">
                     <el-table-column
                             prop="name"
                            label="客户姓名"
                            width="180">
                     </el-table-column>
                     <el-table-column
                            prop="phone"
                            label="手机号码"
                            width="180">
                     </el-table-column>
                     <el-table-column
                            prop="ID"
                            label="身份证"
                             width="180">
                     </el-table-column>
                     <el-table-column
                             prop="pic"
                             label="照片"
                             width="180">
                     </el-table-column>
                     <el-table-column
                             prop="state"
                             label="状态"
                             width="180">
                     </el-table-column>
                     <el-table-column
                             label="操作">
                         <template scope="scope">
                             <el-button type="text" size="small" @click="">通过</el-button>
                             <el-button type="text" size="small"
                                        @click="popup"
                                        >驳回</el-button>
                         </template>
                     </el-table-column>
                 </el-table>
             </el-col>
        </el-row>
        <div>
            <el-dialog title="驳回" :visible.sync="dialogFormVisible" modal="false">
                <el-form :model="form">
                    <el-form-item label="驳回理由" :label-width="formLabelWidth">
                        <el-input v-model="form.name" type="textarea" :rows="4"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
                </div>
            </el-dialog>
        </div>

    </div>
</template>
<script>
    import axiosPost from '../../js/axiosPost'

    var data = [{
        name:'znw',
        phone:18986008673,
        ID:123454546577868979789,
        pic:"",
        state:'通过'
    }];
    export default{
        data(){
             return {
                name:'',
                 dialogFormVisible:true,
                 tableData:data,
                 form:{
                    name:''
                 },
                 formLabelWidth:"120px"
             }
         },
        methods:{
            retrieve(){
                let vm = this;
//                正文
                let url = '/enquiry/manager/getCheckedBanker';
                let param = {
                    currentNo: this.currentNo,
                    pageSize: 15,
                    bankName: vm.organization,
                    bankerName: vm.agent
                };
                let callback = function (res) {
                    if (res.data.data == null) {
                        console.log('data = null');
                        vm.registeredList = [];
                    }
                    vm.registeredList = res.data.data.rows;
                    vm.pageCount = res.data.data.pageCount;
                };
                axiosPost(url, param, callback);
            },
            popup(){
                alert(123);
            }
        }

    }
</script>