<template>
    <div>
        <el-row class="z-top">
            <el-col>
                <el-form :inline="true">
                    <el-form-item label ="区域">
                        <el-select v-model="value" placeholder="请选择">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="姓名:">
                        <input v-model="name" placeholder="请输入内容" class="el-input el-input__inner z-input"></input>
                    </el-form-item>
                    <el-form-item label="企业类型">
                        <input v-model="firm" placeholder="请输入内容" class="el-input el-input__inner z-input"></input>
                    </el-form-item>
                    <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                </el-form>
            </el-col>
        </el-row>
        <el-row>
            <el-col :offset="1">
                <el-button type="primary" @click="add">新增</el-button>
            </el-col>
        </el-row>
            <el-table
                    :data="tableData"
                    border
                    style="width: 100%" class="z-top">
                <el-table-column
                        prop="id"
                        label="微信OPENId"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="客户姓名"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        label="电话"
                        width="180">
                </el-table-column>
                <el-table-column
                          prop="area"
                          label="区域"
                           width="180">
                </el-table-column>
                <el-table-column
                            prop="type"
                            label="主营品种"
                            width="180">
                </el-table-column>
                <el-table-column
                            prop="firmtype"
                            label="企业类型"
                            width="180">
                </el-table-column>
                <el-table-column
                            prop="certificationmark"
                            label="认证标识"
                            width="180">
                </el-table-column>
                <el-table-column
                            prop="probation"
                            label="试用期"
                            width="180">
                </el-table-column>
                <el-table-column
                            label="操作"
                            >
                    <template scope="scope">
                        <el-button @click="handleClick" type="text" size="small">详情</el-button>
                        <el-button type="text" size="small">编辑</el-button>
                        <el-button
                                @click.native.prevent="deleteRow(scope.$index, tableData)"
                                type="text"
                                size="small">
                            移除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
           <el-pagination class="pagination" layout="total,prev, pager, next" :total="total" :page-count="pageCount" :currentPage="currentNo" :page-size='10' @current-change="retrieve" ></el-pagination>
        <router-view></router-view>
    </div>
</template>
<script>
    import axiosPost from '../../js/axiosPost'
    var data = [{
        id:"1wwww",
        name:"hello world1",
        phone:18986008673,
        area:"上海",
        type:"红苹果",
        firmtype:"xxx公司",
        certificationmark:'认证',
        probation:''
    }];
    export default{
        data(){
         return{
             options:[{value:'选项1',label:'北京烤鸭'}],
             value: '',
             name:'',
             firm:'',
             tableData:data,
             pageCount:50,
             currentNo:3,
             total:100
         }
        },
        methods:{
            handleClick(){
                console.log(1);
            },
            deleteRow(index, rows) {
                rows.splice(index, 1);
            },
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
            add(){
                 this.$router.push("/Addr");
            },
            handleSizeChange(){
                console.log("hello world");
            }
        }
    };
</script>
<style>
    .z-input{
        width: auto;
    }
    .z-top{
        margin: 10px;
    }

    .pagination{
        position: absolute;
        bottom: 20px;
        right: 30px;
    }

</style>